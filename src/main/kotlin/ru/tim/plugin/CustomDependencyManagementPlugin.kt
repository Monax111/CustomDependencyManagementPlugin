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

        project.allprojects { subProject ->
            configureSonar(subProject, project)
            configureJava(subProject, javaVersion)

            subProject.afterEvaluate {
                configureKotlin(it)
            }
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

    private fun configureSonar(subProject: Project, project: Project) {
        subProject.pluginManager.withPlugin("org.sonarqube") {

            val pathToReport = "${subProject.buildDir}/report/jacoco/xml/jacocoTestReport.xml"

            subProject.afterEvaluate {
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

            subProject.extensions.getByType(SonarQubeExtension::class.java).also { sonar ->
                sonar.properties {
                    it.property("sonar.projectKey", "${project.name}:${subProject.name}")
                    it.property("sonar.projectName", "${project.name}:${subProject.name}")
                    it.property("sonar.exclusions", "**/generated-sources/**,")
                    it.property("sonar.coverage.exclusions", "**/configuration/**")
                    it.property("sonar.coverage.jacoco.xmlReportPaths", pathToReport)
                }
            }

        }
    }

}

