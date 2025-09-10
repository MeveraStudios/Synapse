plugins {
    id("java")
    id("io.freefair.lombok") version "8.14"
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

tasks.shadowJar {
    archiveFileName.set("synapse-${'$'}{project.name}-${'$'}{project.version}.jar")
}
