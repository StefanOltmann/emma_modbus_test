plugins {
    kotlin("jvm") version "2.0.0"
}

group = "de.stefan-oltmann.sun2000"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.ghgande:j2mod:3.2.1")
}

kotlin {
    jvmToolchain(17)
}