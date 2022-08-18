package context

import helpers.NONE
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoTickerCommands
import stubs.CryptoTickerStubs

data class CryptoTickerContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoTickerStubs = CryptoTickerStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    override var command: CryptoTickerCommands = CryptoTickerCommands.NONE,

    override var timeStart: Instant = Instant.NONE,

    override var settings: CryptoSettings = CryptoSettings(),

    var supportedCurrenciesResponse: MutableList<String> = mutableListOf(),

    var ratesRequest: CryptoPair = CryptoPair(),
    var ratesResponse: CryptoPairRate = CryptoPairRate()

) : CryptoBaseContext<CryptoTickerStubs, CryptoTickerCommands>
