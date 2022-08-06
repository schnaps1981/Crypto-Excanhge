package crypto.app.kafka

import OrderService
import apiV1RequestDeserialize
import apiV1ResponseSerialize
import com.crypto.api.v1.models.IRequest
import com.crypto.api.v1.models.IResponse
import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import fromTransport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import repository.IOrderRepository
import toTransport
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

//TODO нужно написать универсальный процессор для любого типа контекста  и сервиса

class KafkaProcessor(
    val config: KafkaConfig,
    private val service: OrderService = OrderService(OrderRepositoryInMemory()),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {

    fun process() = runBlocking {
        try {
            consumer.subscribe(listOf(config.topicInbound))

            val context = CryptoOrderContext(
                timeStart = Clock.System.now()
            )

            while (true) {

                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1L))
                }

                if (!records.isEmpty) {
                    log.info { "[x] Receive ${records.count()} messages" }

                    records.forEach { record: ConsumerRecord<String, String> ->
                        try {
                            log.info { "[x] process ${record.key()} from ${record.topic()}:\n${record.value()}" }

                            val request: IRequest = apiV1RequestDeserialize(record.value())
                            context.fromTransport(request)

                            service.exec(context)

                            sendResponse(context)

                        } catch (t: Throwable) {
                            log.error(t) { "[x] error" }
                        }
                    }
                }
            }

        } catch (t: WakeupException) {
            // ignore for shutdown
        } catch (t: RuntimeException) {
            withContext(NonCancellable) {
                throw t
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: CryptoOrderContext) {

        val response: IResponse = context.toTransport()
        val json = apiV1ResponseSerialize(response)

        val resRecord = ProducerRecord(config.topicOutbound, UUID.randomUUID().toString(), json)

        log.info { "[x] sending ${resRecord.key()} to ${config.topicOutbound}:\n$json" }

        producer.send(resRecord)
    }
}
