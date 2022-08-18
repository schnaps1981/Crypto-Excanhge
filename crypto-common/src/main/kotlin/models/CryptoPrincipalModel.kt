package models

data class CryptoPrincipalModel(
    val id: CryptoUserId = CryptoUserId.NONE,
    val groups: Set<CryptoUserGroups> = emptySet()
) {
    companion object {
        val NONE = CryptoPrincipalModel()
    }
}
