plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"

    id("com.gradleup.shadow") version "9.6.0"
    application
}

group = "dev.akkih"
version = "26.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.x")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.8.x")

    implementation("net.dv8tion:JDA:6.5.0")
    implementation("club.minnced:jda-ktx:0.15.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("io.ktor:ktor-client-core:3.5.1")
    implementation("io.ktor:ktor-client-cio:3.5.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("dev.akkih.ssbot.SkinSpriteBotKt")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set("ssbot")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    register("stage") {
        dependsOn(shadowJar)
    }
}