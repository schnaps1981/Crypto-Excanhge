package workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorAdministration
import models.CryptoWorkMode
import repository.IOrderRepository

fun ICorChainDsl<CryptoOrderContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        orderRepo = when (workMode) {
            CryptoWorkMode.TEST -> settings.repoTest
            CryptoWorkMode.STUB -> IOrderRepository.NONE
            else -> settings.repoProd
        }
        if (workMode != CryptoWorkMode.STUB && orderRepo == IOrderRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
