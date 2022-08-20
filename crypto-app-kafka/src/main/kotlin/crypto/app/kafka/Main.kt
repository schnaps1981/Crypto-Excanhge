package crypto.app.kafka

import OrderRepositorySql
import OrderService
import SQLDbConfig
import crypto.app.inmemory.OrderRepositoryInMemory
import models.CryptoSettings

fun main() {
    val config = KafkaConfig(hosts = listOf("localhost:9094"), groupId = "kafkaApp")
    val dbConfig = SQLDbConfig()

    val settings = CryptoSettings(
        repoTest = OrderRepositoryInMemory(),
        repoProd = OrderRepositorySql(dbConfig)
    )
    val service = OrderService(settings)

    val processor by lazy {
        KafkaProcessor(
            config = config,
            service = service
        )
    }

    val controller by lazy {
        KafkaController(processors = setOf(processor))
    }

    controller.start()
}
