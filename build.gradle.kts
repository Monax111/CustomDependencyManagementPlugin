plugins {
    kotlin("jvm") version "1.3.71"
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.sonarqube") version "2.8"

}

repositories {
    jcenter()
    mavenCentral()
}

group = "ru.tim"


java.sourceCompatibility = JavaVersion.VERSION_1_8
java.withSourcesJar()

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
}


gradlePlugin {
    plugins {
        create("plugin") {
            id = "ru.tim.dependency-management"
            implementationClass = "ru.tim.plugin.CustomDependencyManagementPlugin"
        }
    }
}