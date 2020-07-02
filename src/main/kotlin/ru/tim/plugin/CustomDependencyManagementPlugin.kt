package ru.tim.plugin


import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeExtension
import java.io.File


val javaVersion = JavaVersion.VERSION_1_8


class CustomDependencyManagementPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("Applying Tim Plugin")

        configureSonar(project)

        project.afterEvaluate {
            configureKotlin(it)
            configureJava(it, javaVersion)
        }

        project.subprojects{
            it.plugins.apply("ru.tim.dependency-management")
        }


    }

    private fun configureKotlin(subProject: Project) {
        subProject.tasks.withType(KotlinCompile::class.java) {
            it.kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = javaVersion.toString()
            }
        }
    }

    private fun configureJava(subProject: Project, javaVersion: JavaVersion) {
        subProject.extensions.findByType(JavaPluginExtension::class.java)?.also {
            it.withSourcesJar()
            it.sourceCompatibility = javaVersion
            it.targetCompatibility = javaVersion
        }
    }

    private fun configureSonar(project: Project) {
        project.pluginManager.withPlugin("org.sonarqube") {

            val pathToReport = "${project.buildDir}/report/jacoco/xml/jacocoTestReport.xml"

            project.afterEvaluate {
                it.tasks.withType(JacocoReport::class.java).firstOrNull()?.also {
                    it.reports.also { report ->
                        report.xml.isEnabled = true
                        report.xml.destination = File(pathToReport)
                        report.html.isEnabled = false
                    }
                }
                    ?: throw RuntimeException("Not Found JacocoReport task. Maybe you forgot to apply plugin??")
                it.tasks.forEach {
                    println(it.name)
                }
                it.tasks.getByName(SonarQubeExtension.SONARQUBE_TASK_NAME).dependsOn("jacocoTestReport")

            }

            project.extensions.getByType(SonarQubeExtension::class.java).also { sonar ->
                sonar.properties {
                    it.property("sonar.projectKey", "${project.name}:${project.name}")
                    it.property("sonar.projectName", "${project.name}:${project.name}")
                    it.property("sonar.exclusions", "**/generated-sources/**,")
                    it.property("sonar.coverage.exclusions", "**/configuration/**")
                    it.property("sonar.coverage.jacoco.xmlReportPaths", pathToReport)
                }
            }

        }
    }

}

