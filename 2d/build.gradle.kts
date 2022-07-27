
plugins {
    kotlin("jvm") version "1.6.21"
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
    implementation("batik:batik-swing:1.6-1")
    implementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation(fileTree("lib"))
}
