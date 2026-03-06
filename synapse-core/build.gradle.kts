plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:26.0.2-1")
    annotationProcessor("org.jetbrains:annotations:26.0.2-1")
    // TODO: Remove this dependency once we have a better way to handle JSON serialization/deserialization in the library.
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}