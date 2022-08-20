package common

import common.models.ExmoInData
import common.models.ExmoOutData
import common.models.ExmoState
import models.CryptoError
import models.commands.CryptoOrderCommands

class ExmoContext {
    var state: ExmoState = ExmoState.NONE

    var exmoInData: ExmoInData = ExmoInData.EMPTY
    var exmoOutData: ExmoOutData = ExmoOutData.EMPTY
}