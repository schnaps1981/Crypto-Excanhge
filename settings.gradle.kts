rootProject.name = "CryptoExchange"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false

        // spring
        val springBootVersion: String by settings
        val springDependencyVersion: String by settings
        val springPluginVersion: String by settings

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyVersion
        kotlin("plugin.spring") version springPluginVersion

        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
    }
}

include("crypto-transport-main-openapi-v1")
include("crypto-common")
include("crypto-mappers-v1")
include("crypto-stubs")
include("crypto-app-spring")
include("crypto-app-ktor")
include("crypto-services")
