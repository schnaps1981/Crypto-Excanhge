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

    implementation(kotlin("stdlib"))

    implementation(project(":crypto-api-logs"))
    implementation(project(":crypto-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    testImplementation(kotlin("test-junit"))
}
