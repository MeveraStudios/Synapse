plugins {
    id("signing")
    id("com.vanniktech.maven.publish") version "0.34.0"
}

allprojects {
    group = "studio.mevera"
    version = "1.2.0"

    val targetJavaVersion = 21
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)

        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "signing")
    apply(plugin = "com.vanniktech.maven.publish")

    signing {
        val key = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey")?.replace("\\n", "\n")
        val password = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")

        if (!key.isNullOrEmpty() && !password.isNullOrEmpty()) {
            useInMemoryPgpKeys(key, password)
            sign(publishing.publications)
        }
    }

    mavenPublishing {
        val artifcatID = project.name.startsWith("synapse-").let { if (it) project.name else "synapse-" + project.name }
        coordinates(group.toString(), artifcatID, version.toString())

        pom {
            name = "Synapse"
            description = "A high-performance, extensible placeholder translation framework for Minecraft servers."
            url = "https://github.com/MeveraStudios/Synapse/"
            licenses {
                license {
                    name.set("MIT License")
                    url = "https://opensource.org/licenses/MIT"
                    distribution = "https://mit-license.org/"
                }
            }
            developers {
                developer {
                    id = "iiahmedyt"
                    name = "iiAhmedYT"
                    url = "https://github.com/iiAhmedYT/"
                }
            }
            scm {
                url = "https://github.com/MeveraStudios/Synapse/"
                connection = "scm:git:git://github.com/MeveraStudios/Synapse.git"
                developerConnection = "scm:git:ssh://git@github.com/MeveraStudios/Synapse.git"
            }
        }

        if (!gradle.startParameter.taskNames.any { (it == "publishToMavenLocal") }) {
            publishToMavenCentral(automaticRelease = true)
        }
    }

    afterEvaluate {
        tasks.named("generateMetadataFileForMavenPublication") {
            mustRunAfter("plainJavadocJar", "sourcesJar")
        }
    }

}