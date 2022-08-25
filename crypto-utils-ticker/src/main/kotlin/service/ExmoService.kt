package service

import biz.ExmoProcessor
import common.ExmoContext
import models.CryptoSettings

class ExmoService(settings: CryptoSettings, tickers: List<Pair<String, String>>, ) {
    private val processor = ExmoProcessor(settings, tickers)

    suspend fun exec(context: ExmoContext) = processor.exec(context)
}