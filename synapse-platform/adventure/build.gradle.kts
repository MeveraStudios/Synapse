plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

val adventureVersion = "4.24.0"
dependencies {
    api(project(":synapse-core"))
    compileOnlyApi("net.kyori:adventure-text-minimessage:${adventureVersion}")
}

java {
    withSourcesJar()
    withJavadocJar()
}
