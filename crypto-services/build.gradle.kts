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

    implementation(project(":crypto-common"))
    implementation(project(":crypto-stubs"))
    implementation(project(":crypto-transport-main-openapi-v1"))
    implementation(project(":crypto-biz"))
}
