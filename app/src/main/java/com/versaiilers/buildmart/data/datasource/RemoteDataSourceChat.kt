package com.versaiilers.buildmart.data.datasource

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.versaiilers.buildmart.data.network.ApiServiceChat
import com.versaiilers.buildmart.data.network.ApiServiceProvider
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.text.SimpleDateFormat
import java.util.Locale

class RemoteDataSourceChat @Inject constructor(
    private val apiServiceChat: ApiServiceChat,
    private val webSocketClient: OkHttpClient
) {
    private var webSocket: WebSocket? = null

    companion object {
        private const val TAG = "RemoteDataSourceChat"
        private const val WEBSOCKET_URL = "/chat/test/"
        private val DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU"))
    }

    private val gson: Gson = GsonBuilder()
        .setDateFormat(DATE_FORMAT.toPattern())
        .create()

    /**
     * Создание чата через API.
     */
    suspend fun createChat(chat: Chat): Result<Unit> {
        return runCatching {
            val response = apiServiceChat.createChat(chat)
            if (response.success) {
                Result.success(Unit)
            } else {
                throw Exception("Message sending error: ${response.message}")
            }
        }.getOrElse {
            Result.failure(Exception("Inner error: ${it.message}"))
        }
    }

    /**
     * Получение всех чатов и сообщений пользователя.
     */
    suspend fun getAllChats(userGlobalId: String): Result<Pair<List<Chat>, List<List<Message>>>> {
        return runCatching {
            val response = apiServiceChat.getChatByUserGlobalId(userGlobalId)
            if (response.success) {
                val type = object : TypeToken<Pair<List<Chat>, List<List<Message>>>>() {}.type
                val chatMessages = gson.fromJson<Pair<List<Chat>, List<List<Message>>>>(response.message, type)
                Log.d(TAG, chatMessages.toString())
                Result.success(chatMessages)
            } else {
                throw Exception(response.message)
            }
        }.getOrElse {
            Log.e(TAG, it.message ?: "Unknown error")
            Result.failure(Exception("Inner error: ${it.message}"))
        }
    }

    /**
     * Подключение к WebSocket для получения сообщений в реальном времени.
     */
    fun connectWebSocket(onNewMessage: (Message) -> Unit) {
        val request = Request.Builder().url(ApiServiceProvider.BASE_URL + WEBSOCKET_URL).build()
        webSocket = webSocketClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")
                parseMessage(text)?.let { message ->
                    // Вызов onNewMessage для обновления в ViewModel
                    onNewMessage(message)
                } ?: Log.e(TAG, "Failed to parse message: $text")
            }
            // Обработка ошибок соединения
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "Connection failed: ${t.message}", t)
            }
        })
    }

    /**
     * Закрытие WebSocket соединения.
     */
    fun disconnectWebSocket() {
        webSocket?.close(1000, "Goodbye!")
        webSocket = null
    }

    /**
     * Парсинг JSON сообщения.
     */
    private fun parseMessage(json: String): Message? {
        return runCatching { gson.fromJson(json, Message::class.java) }
            .onFailure { Log.e(TAG, "Failed to parse message", it) }
            .getOrNull()
    }
}