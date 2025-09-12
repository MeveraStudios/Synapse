plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":synapse-platform:adventure"))
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.shadowJar {
    archiveFileName.set("synapse-${'$'}{project.name}-${'$'}{project.version}.jar")
}
