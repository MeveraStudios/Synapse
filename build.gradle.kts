plugins {
    id("com.vanniktech.maven.publish") version "0.31.0"
}

allprojects {
    group = "studio.mevera"
    version = "1.0"

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
    apply(plugin = "com.vanniktech.maven.publish")

    mavenPublishing {
        val artifcatID = project.name.startsWith("synapse-").let { if (it) project.name else "synapse-" + project.name }
        coordinates(group.toString(), artifcatID, version.toString())
    }
}