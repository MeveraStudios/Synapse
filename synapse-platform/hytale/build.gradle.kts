plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
    maven(url = "https://maven.hytale.com/release/")
}

val hytaleVersion = "2026.02.19-1a311a592"
val imperatVersion: String by rootProject.extra
dependencies {
    api(project(":synapse-core"))
    compileOnly("com.hypixel.hytale:Server:${hytaleVersion}")
    implementation("studio.mevera:imperat-core:$imperatVersion")
    implementation("studio.mevera:imperat-hytale:$imperatVersion")
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