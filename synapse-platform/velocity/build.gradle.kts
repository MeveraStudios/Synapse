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

val imperatVersion: String by rootProject.extra
dependencies {
    api(project(":synapse-platform:adventure"))
    implementation("studio.mevera:imperat-core:$imperatVersion")
    implementation("studio.mevera:imperat-velocity:$imperatVersion")
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
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
    relocate("studio.mevera.imperat", "studio.mevera.synapse.shade.imperat")
}
