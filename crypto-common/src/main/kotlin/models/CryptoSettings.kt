package models

import repository.IOrderRepository

data class CryptoSettings(
    val repoStub: IOrderRepository = IOrderRepository.NONE,
    val repoTest: IOrderRepository = IOrderRepository.NONE,
    val repoProd: IOrderRepository = IOrderRepository.NONE,
)
