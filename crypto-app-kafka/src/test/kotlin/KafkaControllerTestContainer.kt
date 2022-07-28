import com.crypto.api.v1.models.*
import crypto.app.kafka.*
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.Test
import org.rnorth.ducttape.unreliables.Unreliables
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TestContainerKafka {
    private val log = KotlinLogging.logger {}

    companion object {
        private val KAFKA_TEST_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest")
    }

    private val kafka by lazy { KafkaContainer(KAFKA_TEST_IMAGE).apply { start() } }

    private val config by lazy { KafkaConfig(hosts = listOf(kafka.bootstrapServers)) }

    private val clientProducer by lazy { config.createKafkaProducer() }
    private val clientConsumer by lazy { config.createKafkaConsumer() }

    private val topicInput by lazy { config.topicOutbound }
    private val topicOutput by lazy { config.topicInbound }

    private val processor by lazy { KafkaProcessor(config = config) }
    private val controller by lazy { KafkaController(setOf(processor)) }

    @BeforeTest
    fun start() {
        controller.start()
    }

    @AfterTest
    fun stop() {
        controller.stop()
    }

    @Test
    fun `test create order`() {

        val request = OrderCreateRequest(
            requestId = "requestId-123",
            requestType = "OrderCreate",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            quantity = "1.4",
            price = "100.3",
            orderType = OrderType.SELL,
            pair = TickerPair(
                first = "BTC",
                second = "ETH"
            ),
            userId = "123"
        )

        test(request) {
            val response = apiV1ResponseDeserialize<OrderCreateResponse>(it)

            assertEquals("requestId-123", response.requestId)
            assertEquals("orderID-123213", response.orderId)
        }
    }

    @Test
    fun `test delete order`() {

        val request = OrderDeleteRequest(
            requestId = "requestId-123",
            requestType = "OrderDelete",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            orderId = "123"
        )

        test(request) {
            val response = apiV1ResponseDeserialize<OrderDeleteResponse>(it)

            assertEquals(DeleteResult.SUCCESS, response.deleteResult)
        }
    }

    @Test
    fun `test read order`() {

        val request = OrderReadRequest(
            requestId = "requestId-123",
            requestType = "OrderRead",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "123"
        )

        test(request) {
            val response = apiV1ResponseDeserialize<OrderReadResponse>(it)

            assertEquals(true, response.orders?.isNotEmpty())
        }
    }

    private fun test(request: IRequest, block: (String) -> Unit) {
        clientConsumer.subscribe(listOf(topicInput))

        val record = ProducerRecord(topicOutput, UUID.randomUUID().toString(), apiV1RequestSerialize(request))
        clientProducer.send(record).get()

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS) {
            val records = clientConsumer.poll(Duration.ofMillis(100))

            if (records.isEmpty) {
                return@retryUntilTrue false
            }

            assertEquals(1, records.count())

            val message = records.first()

            log.info { "[x] receiving message from topic = ${message.topic()} with key = ${message.key()} and value = ${message.value()}" }

            block(message.value())

            return@retryUntilTrue true
        }

        clientConsumer.unsubscribe()
    }
}
