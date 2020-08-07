pluginManagement {
    repositories {
        jcenter()
        gradlePluginPortal()
        mavenLocal()
    }
}


rootProject.name = "custom-dependency-management-gradle-plugin"

include("dependency-management-plugin")
include("target-project")