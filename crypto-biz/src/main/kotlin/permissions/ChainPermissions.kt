package permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState
import models.CryptoUserGroups
import models.CryptoUserPermissions

fun ICorChainDsl<CryptoOrderContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == CryptoState.RUNNING }

    handle {
        val permAdd: Set<CryptoUserPermissions> = principal.groups.map {
            when (it) {
                CryptoUserGroups.USER -> setOf(
                    CryptoUserPermissions.CREATE_OWN,
                    CryptoUserPermissions.READ_OWN,
                    CryptoUserPermissions.DELETE_OWN,
                    CryptoUserPermissions.FILTER_OWN
                )

                CryptoUserGroups.ADMIN -> setOf(
                    CryptoUserPermissions.CREATE_OWN,
                    CryptoUserPermissions.READ_OWN,
                    CryptoUserPermissions.DELETE_OWN,
                    CryptoUserPermissions.READ_ANY,
                    CryptoUserPermissions.DELETE_ANY,
                    CryptoUserPermissions.FILTER_ANY
                )

                CryptoUserGroups.BANNED -> setOf()

            }
        }.flatten().toSet()

        val permDel: Set<CryptoUserPermissions> = principal.groups.map {
            when (it) {
                CryptoUserGroups.USER -> setOf()

                CryptoUserGroups.ADMIN -> setOf()

                CryptoUserGroups.BANNED -> setOf(
                    CryptoUserPermissions.CREATE_OWN,
                    CryptoUserPermissions.READ_OWN,
                    CryptoUserPermissions.DELETE_OWN,
                    CryptoUserPermissions.READ_ANY,
                    CryptoUserPermissions.DELETE_ANY,
                    CryptoUserPermissions.FILTER_OWN,
                    CryptoUserPermissions.FILTER_ANY
                )
            }
        }.flatten().toSet()

        chainPermissions.addAll(permAdd)
        chainPermissions.removeAll(permDel)
    }
}
