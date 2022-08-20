plugins {
    kotlin("jvm")
}

tasks {
    withType<Test> {
        environment("crypto.sql_drop_db", true)
        environment("crypto.sql_fast_migration", true)
    }
}

group = rootProject.group
version = rootProject.version

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val postgresTestContainerVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":crypto-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

    testImplementation("org.testcontainers:postgresql:$postgresTestContainerVersion")
    testImplementation(project(":crypto-repo-test"))
}
