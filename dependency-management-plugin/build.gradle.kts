import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("maven-publish")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // don't replace on `implementation`. If target project use version plugin less then in this plugin
    // gradle can't downgrade version plugin in target project
    compileOnly("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.2.1")
    compileOnly("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
}


gradlePlugin {
    plugins {
        create("dependency-management-plugin") {
            id = "tim.plugin"
            implementationClass = "ru.tim.project.CustomDependencyManagementPlugin"
        }
    }
}

publishing {
    afterEvaluate {
        publications.getByName("pluginMaven") {
            group = "ru.tim"
            version = version.toString()
        }
    }
}