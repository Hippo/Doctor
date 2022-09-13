plugins {
    java
    id("maven-publish")
}

group = "rip.hippo"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.inject:javax.inject:1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}