package service

import biz.ExmoProcessor
import common.ExmoContext
import models.CryptoSettings

class ExmoService(settings: CryptoSettings) {
    private val processor = ExmoProcessor(settings)

    suspend fun exec(context: ExmoContext) = processor.exec(context)
}