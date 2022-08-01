package crypto.app.kafka

import kotlinx.coroutines.*
import java.util.concurrent.Executors

class KafkaController(private val processors: Set<KafkaProcessor>) {
    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName("kafka-controller")
    )

    fun start() = scope.launch {
        processors.forEach { processor ->
            launch(
                Executors.newSingleThreadExecutor()
                    .asCoroutineDispatcher() + CoroutineName("kafka-process-${processor.config.groupId}")
            ) {
                try {
                    processor.process()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    fun stop() = scope.cancel("[x] controller was stopped")
}
