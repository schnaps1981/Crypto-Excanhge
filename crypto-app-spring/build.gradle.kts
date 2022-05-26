plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = rootProject.group
version = rootProject.version
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
    implementation("org.springframework.boot:spring-boot-starter-web") // Controller, Service, etc..
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // for spring-boot app

    // transport models
    implementation(project(":crypto-common"))
    implementation(project(":crypto-mappers-v1"))
    implementation(project(":crypto-transport-main-openapi-v1"))
    implementation(project(":crypto-services"))

    // Stubs
    implementation(project(":crypto-stubs"))

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    val kotestVersion: String by project
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("com.ninja-squad:springmockk:3.1.1") // mockking beans
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
