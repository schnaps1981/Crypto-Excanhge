package crypto.app.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

data class KafkaConfig(
    val hosts: List<String> = KAFKA_HOSTS,
    val groupId: String = KAFKA_GROUP_ID,
    val topicInbound: String = KAFKA_TOPIC_INBOUND,
    val topicOutbound: String = KAFKA_TOPIC_OUTBOUND,
) {
    companion object {
        private const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        private const val KAFKA_TOPIC_INBOUND_VAR = "KAFKA_TOPIC_IN_V1"
        private const val KAFKA_TOPIC_OUTBOUND_VAR = "KAFKA_TOPIC_OUT_V1"
        private const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "crypto" }
        val KAFKA_TOPIC_INBOUND by lazy { System.getenv(KAFKA_TOPIC_INBOUND_VAR) ?: "crypto-inbound" }
        val KAFKA_TOPIC_OUTBOUND by lazy { System.getenv(KAFKA_TOPIC_OUTBOUND_VAR) ?: "crypto-outbound" }
    }
}

fun KafkaConfig.createKafkaConsumer(): KafkaConsumer<String, String> {
    val props = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
        put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
    }
    return KafkaConsumer<String, String>(props)
}

fun KafkaConfig.createKafkaProducer(): KafkaProducer<String, String> {
    val props = Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }
    return KafkaProducer<String, String>(props)
}
