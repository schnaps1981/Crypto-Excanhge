plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":crypto-common"))
}
