package models

data class CryptoUserInfo(
    var userId: CryptoUserId = CryptoUserId.NONE,
    var balances: List<CryptoCurrency> = listOf()
)
