package models

data class CryptoOrder(
    var orderId: CryptoOrderId = CryptoOrderId.NONE,
    var created: Int = 0,
    var orderState: CryptoOrderState = CryptoOrderState.NONE,
    var amount: Double = 0.0,
    var quantity: Double = 0.0,
    var price: Double = 0.0,
    var orderType: CryptoOrderType = CryptoOrderType.NONE,
    var pair: CryptoPair = CryptoPair()
)
