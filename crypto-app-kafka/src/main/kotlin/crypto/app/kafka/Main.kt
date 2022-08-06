package crypto.app.kafka

import OrderService
import crypto.app.inmemory.OrderRepositoryInMemory

fun main() {
    val config = KafkaConfig(hosts = listOf("localhost:9094"), groupId = "kafkaApp")

    val repo = OrderRepositoryInMemory()
    val service = OrderService(repo)

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
