import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    //if in uncommented will be error
    //id("org.sonarqube") version "2.8"
    id("io.gitlab.arturbosch.detekt")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
