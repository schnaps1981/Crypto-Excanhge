package models

data class CryptoAccount(
    var id: CryptoUserId = CryptoUserId.NONE,
    var balances: MutableList<CryptoCurrency> = mutableListOf(),
    var state: CryptoAccountState = CryptoAccountState.INACTIVE
)
