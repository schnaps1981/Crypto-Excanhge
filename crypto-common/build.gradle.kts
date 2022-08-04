plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-common"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
}
