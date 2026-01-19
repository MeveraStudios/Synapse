plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
    maven(url = "https://repo.gravemc.net/releases/")
}

val imperatVersion: String by rootProject.extra
dependencies {
    api(project(":synapse-core"))
    implementation("studio.mevera:imperat-core:$imperatVersion")
    implementation("studio.mevera:imperat-hytale:$imperatVersion")
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

tasks.shadowJar {
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
    relocate("studio.mevera.imperat", "studio.mevera.synapse.shade.imperat")
}