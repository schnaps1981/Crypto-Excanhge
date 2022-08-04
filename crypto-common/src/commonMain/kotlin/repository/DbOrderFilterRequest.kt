package repository

import models.filter.ICryptoFilter

data class DbOrderFilterRequest(
    val filter: ICryptoFilter = ICryptoFilter.NONE
)
