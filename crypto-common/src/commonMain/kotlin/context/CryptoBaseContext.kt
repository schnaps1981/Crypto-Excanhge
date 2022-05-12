package context

import models.*

sealed interface CryptoBaseContext {
    var state: CryptoState
    val errors: MutableList<CryptoError>

    var requestId: CryptoRequestId

    var workMode: CryptoWorkMode
    var stubCase: CryptoStubs
}
