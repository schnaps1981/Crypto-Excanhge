rootProject.name = "CryptoExchange"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("api-v1-jackson")
include("api-v1-moshi")
