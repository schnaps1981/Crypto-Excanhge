package permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import models.*

fun ICorChainDsl<CryptoOrderContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == CryptoState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        orderRepoRead.principalRelations = orderRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = orderRepoRead.principalRelations.asSequence().flatMap { relation ->
            chainPermissions.map { permission ->
                AccessTableConditions(
                    command = command,
                    permission = permission,
                    relation = relation,
                )
            }
        }.any {
            accessTable[it] ?: false
        }
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(CryptoError(message = "User is not allowed to this operation"))
        }
    }
}

private fun CryptoOrder.resolveRelationsTo(principal: CryptoPrincipalModel): Set<CryptoPrincipalRelations> =
    setOfNotNull(
        CryptoPrincipalRelations.NONE,
        CryptoPrincipalRelations.OWN.takeIf { principal.id == ownerId },
        CryptoPrincipalRelations.ANY.takeIf { principal.groups.contains(CryptoUserGroups.ADMIN) },
    )