rootProject.version = "1.0-SNAPSHOT"
rootProject.group = "org.xiaoxigua"

task("runAllServer") {
    dependsOn(":fabric:runServer", ":velocity:runVelocity")
}

task("jar", Copy::class) {
    dependsOn(":fabric:jar", ":velocity:jar")
    from("fabric/build/devlibs/fabric-${rootProject.version}-dev.jar", "velocity/build/libs/velocity-${rootProject.version}.jar")
    into("build/")
}