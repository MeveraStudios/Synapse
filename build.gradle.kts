plugins {
    id("com.vanniktech.maven.publish") version "0.31.0"
}

allprojects {
    group = "studio.mevera"
    version = "1.0"

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

subprojects {
    apply(plugin = "com.vanniktech.maven.publish")

    mavenPublishing {
        coordinates(group.toString(), "synapse-" + project.name, version.toString())
    }
}