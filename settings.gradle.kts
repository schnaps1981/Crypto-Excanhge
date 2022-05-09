rootProject.name = "CryptoExchange"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("crypto-transport-main-openapi-v1")
include("crypto-common")
