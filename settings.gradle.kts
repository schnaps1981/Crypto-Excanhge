rootProject.name = "CryptoExchange"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("transport-main-openapi-v1")
