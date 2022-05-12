package context

import models.*
import models.commands.CryptoTickerCommands

data class CryptoTickerContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoStubs = CryptoStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    var command: CryptoTickerCommands = CryptoTickerCommands.NONE,

    var supportedCurrenciesResponse: MutableList<String> = mutableListOf(),

    var ratesRequest: CryptoPair = CryptoPair(),
    var ratesResponse: CryptoPairRate = CryptoPairRate()

) : CryptoBaseContext
