import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

group = "org.dkenez"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("com.badlogicgames.gdx:gdx:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation("io.github.libktx:ktx-app:1.10.0-b1")
    implementation("io.github.libktx:ktx-graphics:1.10.0-b1")
    implementation("io.github.libktx:ktx-vis:1.10.0-b1")
    implementation("io.github.libktx:ktx-scene2d:1.10.0-b1")
    implementation("io.github.libktx:ktx-style:1.10.0-b1")
    implementation("io.github.libktx:ktx-preferences:1.10.0-b1")
    implementation("io.github.libktx:ktx-actors:1.10.0-b1")
    implementation("io.github.libktx:ktx-math:1.10.0-b1")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}