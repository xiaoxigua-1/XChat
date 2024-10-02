rootProject.version = "1.0-SNAPSHOT"
rootProject.group = "org.xiaoxigua"

plugins {
    kotlin("jvm") version "2.0.20-Beta1" apply false
    kotlin("kapt") version "2.0.20-Beta1" apply false
    kotlin("plugin.serialization") version "2.0.20" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8" apply false
}

task("runAllServer") {
    dependsOn(":fabric:runServer", ":velocity:runVelocity")
}

task("jar", Copy::class) {
    dependsOn(":fabric:jar", ":velocity:jar")
    from("fabric/build/devlibs/fabric-${rootProject.version}-dev.jar", "velocity/build/libs/velocity-${rootProject.version}.jar")
    into("build/")
}