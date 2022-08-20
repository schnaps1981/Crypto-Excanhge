package models.filter

sealed interface ICryptoFilter {
    fun deepCopy(): ICryptoFilter

    companion object {
        val NONE = ICryptoFilterNone
    }
}

object ICryptoFilterNone : ICryptoFilter {
    override fun deepCopy() = ICryptoFilterNone
}
