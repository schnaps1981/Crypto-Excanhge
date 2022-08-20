package crypto.app.ktor.helpers

import SQLDbConfig
import io.ktor.server.application.*

fun SQLDbConfig.Companion.fromEnvironment(environment: ApplicationEnvironment) = SQLDbConfig(
    url = environment.config.property("sql.url").getString(),
    user = environment.config.property("sql.user").getString(),
    password = environment.config.property("sql.pass").getString(),
    schema = environment.config.property("sql.schema").getString()
)
