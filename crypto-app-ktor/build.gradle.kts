plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

group = rootProject.group
version = rootProject.version

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("(c) Otus")
        ports.set(listOf(8080))
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
    val ktorVersion: String by project
    val logbackVersion: String by project
    val kotlinVersion: String by project

    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")

    // jackson
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")


    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // transport models
    implementation(project(":crypto-common"))
    implementation(project(":crypto-transport-main-openapi-v1"))
    implementation(project(":crypto-mappers-v1"))
    implementation(project(":crypto-services"))

    // Stubs
    implementation(project(":crypto-stubs"))

    //Repositories
    implementation(project(":crypto-repo-inmemory"))
    implementation(project(":crypto-repo-sql"))

    // Logging
    implementation(project(":crypto-logging"))
    implementation(project(":crypto-logging-mapper"))
    implementation(project(":crypto-api-logs"))

    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
}
