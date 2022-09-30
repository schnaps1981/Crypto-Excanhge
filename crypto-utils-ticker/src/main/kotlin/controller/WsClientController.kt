package controller

import common.ExmoContext
import common.models.ExmoEvent
import common.models.ExmoId
import common.models.ExmoMethod
import common.models.ExmoOutData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import mappers.fromTransport
import mappers.toTransport
import serialize.apiExmoRequestSerialize
import serialize.apiExmoResponseDeserialize
import service.ExmoService
import java.util.concurrent.Executors

class WsClientController(
    private val url: String,
    private val exmoService: ExmoService,
    tickerPairs: List<Pair<String, String>> = listOf("BTC" to "USD")
) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private val topics = tickerPairs.map {
        "spot/ticker:${it.first}_${it.second}"
    }

    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName("ws-client")
    )

    fun start() = scope.launch {
        client.webSocket(url) {
            incoming
                .receiveAsFlow()
                .mapNotNull { it as? Frame.Text }
                .map { frame ->
                    val jsonStr = frame.readText()

                    apiExmoResponseDeserialize(jsonStr)
                }

                .map { response ->
                    val ctx = ExmoContext().apply { fromTransport(response) }

                    if (ctx.exmoInData.event == ExmoEvent.SUBSCRIBED && ctx.exmoInData.topic.isNotBlank()) {
                        ctx.exmoOutData = ExmoOutData.EMPTY
                    }

                    if (ctx.exmoInData.event == ExmoEvent.INFO && ctx.exmoInData.sessionId.isNotBlank() && ctx.exmoInData.code == 1) {
                        ctx.exmoOutData = ExmoOutData(
                            id = ExmoId(1),
                            method = ExmoMethod.SUBSCRIBE,
                            topics = topics
                        )
                    }
                    if (ctx.exmoInData.event == ExmoEvent.UPDATE) {
                        exmoService.exec(ctx)
                    }

                    ctx
                }
                .map {
                    if (!it.exmoOutData.isEmpty()) {
                        val frame = apiExmoRequestSerialize(it.toTransport())
                        outgoing.send(Frame.Text(frame))
                    }
                }
                .collect()
        }
    }

    fun stop() {
        client.close()

        scope.cancel()
    }
}