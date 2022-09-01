package biz.helpers

import OrderStubs.oneOrderStub
import models.CryptoPrincipalModel
import models.CryptoUserGroups
import models.CryptoUserId

fun principalUser(id: CryptoUserId = oneOrderStub.ownerId, banned: Boolean = false) = CryptoPrincipalModel(
    id = id,
    groups = setOf(
        CryptoUserGroups.USER,
        if (banned) CryptoUserGroups.BANNED else null
    ).filterNotNull().toSet()
)

fun principalAdmin(id: CryptoUserId = oneOrderStub.ownerId, banned: Boolean = false) = CryptoPrincipalModel(
    id = id,
    groups = setOf(
        CryptoUserGroups.ADMIN,
        if (banned) CryptoUserGroups.BANNED else null
    ).filterNotNull().toSet()
)
