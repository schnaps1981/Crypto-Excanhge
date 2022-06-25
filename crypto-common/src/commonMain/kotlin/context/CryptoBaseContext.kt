package context

import models.CryptoError
import models.CryptoRequestId
import models.CryptoState
import models.CryptoWorkMode

sealed interface CryptoBaseContext<STUB, COMMAND> {
    var state: CryptoState
    val errors: MutableList<CryptoError>

    var requestId: CryptoRequestId

    var workMode: CryptoWorkMode
    var stubCase: STUB

    var command: COMMAND
}

fun CryptoBaseContext<*, *>.fail(error: CryptoError) {
    addError(error)
    state = CryptoState.FAILED
}

fun CryptoBaseContext<*, *>.addError(error: CryptoError) = errors.add(error)
