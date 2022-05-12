package models

data class CryptoPairRate(
    var pair: CryptoPair = CryptoPair(),
    var rate: Double = 0.0
)
