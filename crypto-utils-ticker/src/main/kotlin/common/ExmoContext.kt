package common

import common.models.ExmoInData
import common.models.ExmoOutData
import common.models.ExmoState
import models.CryptoSettings
import repository.IOrderRepository

class ExmoContext {
    var settings: CryptoSettings = CryptoSettings()
    var exmoRepo: IOrderRepository = IOrderRepository.NONE

    var state: ExmoState = ExmoState.NONE

    var exmoInData: ExmoInData = ExmoInData.EMPTY
    var exmoOutData: ExmoOutData = ExmoOutData.EMPTY
}