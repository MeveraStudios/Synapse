plugins {
    id("java-library")
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
    api(project(":synapse-platform:adventure"))
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
}

java {
    withSourcesJar()
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("velocity-plugin.json") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
}
