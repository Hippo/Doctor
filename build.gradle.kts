plugins {
    java
}

group = "rip.hippo"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.inject:javax.inject:1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}