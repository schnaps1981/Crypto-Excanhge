plugins {
    kotlin("jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

group = rootProject.group
version = rootProject.version

application {
    mainClass.set("crypto.app.kafka.MainKt")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk17:alpine-jre")
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val kafkaTestContainerVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    implementation(kotlin("stdlib"))

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // transport models
    implementation(project(":crypto-common"))
    implementation(project(":crypto-transport-main-openapi-v1"))
    implementation(project(":crypto-mappers-v1"))
    // service
    implementation(project(":crypto-services"))
    // logic
    implementation(project(":crypto-biz"))
    //stubs
    implementation(project(":crypto-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation("org.testcontainers:kafka:$kafkaTestContainerVersion")
}
