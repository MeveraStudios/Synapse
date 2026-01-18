plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven(url = "https://repo.gravemc.net/releases/")
}

dependencies {
    api(project(":synapse-core"))
    compileOnly("com.hypixel:hytale-server:1.0.0")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("manifest.json") {
        expand(props)
    }
}

tasks.jar {
    archiveBaseName.set("synapse-hytale")
    archiveVersion.set(version.toString())
}