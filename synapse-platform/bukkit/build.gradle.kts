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
    maven {
        name = "PAPI"
        url = uri("https://repo.helpch.at/releases/")
        content {
            includeGroup("me.clip")
        }
    }
    maven {
        name = "libby-repo"
        url = uri("https://repo.alessiodp.com/snapshots")
        content {
            includeGroup("com.alessiodp.libby")
        }
    }
}

val hiddenShadowed: Configuration by configurations.creating
dependencies {
    api(project(":synapse-platform:adventure"))

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")

    compileOnly("com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")
    hiddenShadowed("com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")
}

java {
    withSourcesJar()
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("bukkit.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    from(hiddenShadowed.map { zipTree(it) }) // physically include it
    relocate("com.alessiodp.libby", "studio.mevera.synapse.shade.libby")
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
}