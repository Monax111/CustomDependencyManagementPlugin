package ru.tim.project


import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.sonarqube.gradle.SonarQubeExtension

class CustomDependencyManagementPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("Applying Tim Plugin for root project")

        doSomeWithRootProject()

        project.subprojects {
                configureDetekt(it)
                configureSonar(it)
            }

    }

    private fun doSomeWithRootProject() {
        println("Root project configured")
    }

    private fun configureDetekt(project: Project) {
        project.pluginManager.withPlugin("io.gitlab.arturbosch.detekt") {
            project.extensions.getByType(DetektExtension::class.java).also {
                println("Detekt configured")
            }
        }
    }


    private fun configureSonar(project: Project) {
        project.pluginManager.withPlugin("org.sonarqube") {
            project.extensions.getByType(SonarQubeExtension::class.java).also {
                println("Sonarqube configured")
            }
        }
    }

}

