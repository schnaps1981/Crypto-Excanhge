plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":crypto-transport-main-openapi-v1"))
    implementation(project(":crypto-common"))

    testImplementation(kotlin("test-junit"))
}
