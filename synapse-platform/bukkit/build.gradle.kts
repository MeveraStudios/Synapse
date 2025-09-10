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

dependencies {
    implementation(project(":synapse-platform:adventure"))
    implementation("com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
}

tasks.shadowJar {
    archiveFileName.set("synapse-${project.name}-${project.version}.jar")
}