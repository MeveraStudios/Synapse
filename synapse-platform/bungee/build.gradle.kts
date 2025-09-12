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

dependencies {
    api(project(":synapse-platform:adventure"))
    implementation("com.alessiodp.libby:libby-bungee:2.0.0-SNAPSHOT")

    compileOnly("net.md-5:bungeecord-api:1.20-R0.3-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.shadowJar {
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
}
