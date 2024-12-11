package com.versaiilers.buildmart.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.versaiilers.buildmart.data.network.apiService.ApiServiceProvider
import com.versaiilers.buildmart.domain.entity.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class WebSocketConnection @Inject constructor(private val webSocketClient: OkHttpClient) {

    companion object {
        private const val TAG = "WebSocketConnection"
        private const val WEBSOCKET_URL = "/chat/test/"
        private val DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU"))
    }

    private val gson: Gson = GsonBuilder()
        .setDateFormat(DATE_FORMAT.toPattern())
        .create()

    // Экземпляр WebSocket для повторного использования
    @Volatile
    private var webSocket: WebSocket? = null
    private var onNewMessageHandler: ((Message) -> Unit)? = null


    // Подключение к WebSocket. Если соединение уже установлено, повторное подключение не выполняется.
    fun connectWebSocket(onNewMessage: (Message) -> Unit) {
        // Проверяем, если WebSocket уже подключен, используем его
        if (webSocket != null) {
            Log.d(TAG, "Reusing existing WebSocket connection.")
            onNewMessageHandler = onNewMessage
            return
        }

        // Устанавливаем обработчик onNewMessage для нового подключения
        onNewMessageHandler = onNewMessage


        val request = Request.Builder().url(ApiServiceProvider.BASE_URL + WEBSOCKET_URL).build()
        webSocket = webSocketClient.newWebSocket(request, object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")
                parseMessage(text)?.let{
                    onNewMessageHandler?.invoke(it)
                } ?: Log.e(TAG, "Failed to parse message: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket connection failed: ${t.message}", t)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: code=$code, reason=$reason")
                disconnectWebSocket()
            }
        })
    }

    fun sendMessage(messageJson: String): Result<Unit> {
        return runCatching {
            webSocket?.let {
                if (it.send(messageJson)) {
                    Result.success(Unit)
                } else {
                    throw Exception("Failed to send message over WebSocket.")
                }
            } ?: throw IllegalStateException("WebSocket is not connected.")
        }.getOrElse { exception ->
            Result.failure(exception)
        }
    }

    // Отключение от WebSocket, освобождая ресурс
    fun disconnectWebSocket() {
        webSocket?.close(1000, "Goodbye!")?.also {
            Log.d(TAG, "WebSocket closed.")
        }
        webSocket = null
    }

    // Парсинг JSON сообщения
    private fun parseMessage(json: String): Message? {
        return runCatching {
            gson.fromJson(json, Message::class.java)
        }.onFailure {
            Log.e(TAG, "Failed to parse message: ${it.message}", it)
        }.getOrNull()
    }
}