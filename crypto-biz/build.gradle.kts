plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    val corVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))


    implementation(project(":crypto-common"))
    implementation(project(":crypto-stubs"))
    implementation("com.github.crowdproj.kotlin-cor:kotlin-cor:$corVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":crypto-repo-inmemory"))
    testImplementation(project(":crypto-repo-test"))
    testImplementation(project(":crypto-stubs"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}
