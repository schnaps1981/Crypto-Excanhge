package biz.workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import common.ExmoContext
import common.models.ExmoState

fun ICorChainDsl<ExmoContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == ExmoState.NONE }
    handle { state = ExmoState.RUNNING }
}