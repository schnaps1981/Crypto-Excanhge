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
