package repository

import models.CryptoUserId
import models.filter.ICryptoFilter

data class DbOrderFilterRequest(
    val ownerId: CryptoUserId = CryptoUserId.NONE,
    val filter: ICryptoFilter = ICryptoFilter.NONE
)
