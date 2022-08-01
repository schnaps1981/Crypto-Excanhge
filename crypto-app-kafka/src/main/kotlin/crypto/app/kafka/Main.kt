package crypto.app.kafka

import OrderService

fun main() {
    val config = KafkaConfig(hosts = listOf("localhost:9094"), groupId = "kafkaApp")

    val service = OrderService()

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
