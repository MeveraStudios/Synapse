plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "minecraft"
        url = uri("https://libraries.minecraft.net/")
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

    compileOnly("net.md-5:bungeecord-api:1.21-R0.4-SNAPSHOT")
    compileOnly("com.alessiodp.libby:libby-bungee:2.0.0-SNAPSHOT")
    hiddenShadowed("com.alessiodp.libby:libby-bungee:2.0.0-SNAPSHOT")
}

java {
    withSourcesJar()
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("bungee.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    from(hiddenShadowed.map { zipTree(it) }) // physically include it
    relocate("com.alessiodp.libby", "studio.mevera.synapse.shade.libby")
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
}
