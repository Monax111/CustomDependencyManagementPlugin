plugins {
    kotlin("jvm") version "1.3.70" apply false
    id("io.gitlab.arturbosch.detekt") version "1.2.1" apply false

    //first time it must be commented, and execute pushToLicalMaven in project depebdency-mamagement-plugin
    //id("tim.plugin") version "0.0.1"
}

allprojects{
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenLocal()
    }
}
