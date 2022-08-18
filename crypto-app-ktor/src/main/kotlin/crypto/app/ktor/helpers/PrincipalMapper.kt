package crypto.app.ktor.helpers

import crypto.app.ktor.KtorAuthConfig
import io.ktor.server.auth.jwt.*
import models.CryptoPrincipalModel
import models.CryptoUserGroups
import models.CryptoUserId

fun JWTPrincipal?.toModel() = this?.run {
    CryptoPrincipalModel(
        id = payload.getClaim(KtorAuthConfig.ID_CLAIM).asString()?.let { CryptoUserId(it) } ?: CryptoUserId.NONE,
        groups = payload
            .getClaim(KtorAuthConfig.GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                try {
                    CryptoUserGroups.valueOf(it)
                } catch (t: Throwable) {
                    null
                }
            }?.toSet() ?: emptySet()
    )
} ?: CryptoPrincipalModel.NONE
