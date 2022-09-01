package models.filter

import models.CryptoFilterApplyTo

sealed interface ICryptoFilter {
    var filterPermissions: MutableSet<CryptoFilterApplyTo>

    fun deepCopy(): ICryptoFilter

    companion object {
        val NONE = ICryptoFilterNone
    }
}

object ICryptoFilterNone : ICryptoFilter {
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf(CryptoFilterApplyTo.NONE)

    override fun deepCopy() = ICryptoFilterNone
}
