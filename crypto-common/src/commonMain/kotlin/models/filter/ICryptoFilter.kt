package models.filter

sealed interface ICryptoFilter {
    companion object {
        val NONE = ICryptoFilterNone
    }
}

object ICryptoFilterNone : ICryptoFilter
