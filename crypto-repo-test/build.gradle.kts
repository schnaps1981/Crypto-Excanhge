plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":crypto-common"))

    api(kotlin("test-junit"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}
