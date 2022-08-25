package biz.workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import common.ExmoContext
import repository.IOrderRepository

fun ICorChainDsl<ExmoContext>.initRepo(title: String) = worker {
    this.title = title

    handle {
        exmoRepo = if (settings.repoProd != IOrderRepository.NONE) {
            settings.repoProd
        } else {
            settings.repoTest
        }
    }
}
