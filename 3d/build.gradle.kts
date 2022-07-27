plugins {
    kotlin("jvm") version "1.6.21"
    id("org.openjfx.javafxplugin") version "0.0.10"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.hamcrest:hamcrest:2.2")
//    implementation("org.openjfx:javafx:18.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.controls")
}
