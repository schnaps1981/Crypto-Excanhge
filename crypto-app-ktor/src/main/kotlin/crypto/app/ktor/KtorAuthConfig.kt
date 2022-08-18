package crypto.app.ktor

import io.ktor.server.application.*

data class KtorAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
) {
    constructor(environment: ApplicationEnvironment) : this(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )

    companion object {
        const val ID_CLAIM = "id"
        const val GROUPS_CLAIM = "groups"

        val TEST = KtorAuthConfig(
            secret = "secret",
            issuer = "CryptoExchange",
            audience = "users",
            realm = "exchange"
        )
    }
}
