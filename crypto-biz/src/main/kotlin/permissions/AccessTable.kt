package permissions

import models.CryptoPrincipalRelations
import models.CryptoUserPermissions
import models.commands.CryptoOrderCommands

data class AccessTableConditions(
    val command: CryptoOrderCommands,
    val permission: CryptoUserPermissions,
    val relation: CryptoPrincipalRelations
)

val accessTable = mapOf(
    // Можно создавать ордер (только себе)
    AccessTableConditions(
        command = CryptoOrderCommands.CREATE,
        permission = CryptoUserPermissions.CREATE_OWN,
        relation = CryptoPrincipalRelations.OWN
    ) to true,

    // Можно читать свой ордер
    AccessTableConditions(
        command = CryptoOrderCommands.READ,
        permission = CryptoUserPermissions.READ_OWN,
        relation = CryptoPrincipalRelations.OWN
    ) to true,

    // Можно читать любой ордер
    AccessTableConditions(
        command = CryptoOrderCommands.READ,
        permission = CryptoUserPermissions.READ_ANY,
        relation = CryptoPrincipalRelations.ANY
    ) to true,

    //Можно удалять свой ордер
    AccessTableConditions(
        command = CryptoOrderCommands.DELETE,
        permission = CryptoUserPermissions.DELETE_OWN,
        relation = CryptoPrincipalRelations.OWN
    ) to true,

    //Можно удалять любой ордер
    AccessTableConditions(
        command = CryptoOrderCommands.DELETE,
        permission = CryptoUserPermissions.DELETE_ANY,
        relation = CryptoPrincipalRelations.ANY
    ) to true
)
