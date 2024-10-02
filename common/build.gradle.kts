plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.gradle.plugin.idea-ext")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-nbt:4.17.0")
    implementation("com.akuleshov7:ktoml-core:0.5.1")
    implementation("com.akuleshov7:ktoml-file:0.5.1")
    implementation("io.netty:netty-buffer:4.2.0.Alpha4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
