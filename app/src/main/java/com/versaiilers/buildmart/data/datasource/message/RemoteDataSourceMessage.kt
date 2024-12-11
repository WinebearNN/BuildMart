package com.versaiilers.buildmart.data.datasource.message

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.versaiilers.buildmart.data.network.apiService.ApiServiceMessage
import com.versaiilers.buildmart.data.network.WebSocketConnection
import com.versaiilers.buildmart.domain.entity.Message
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class RemoteDataSourceMessage @Inject constructor(
    private val apiServiceMessage: ApiServiceMessage,
    private val webSocketConnection: WebSocketConnection // Внедряем WebSocketConnection
) {

    companion object {
        private const val TAG = "RemoteDataSourceMessage"
        private val DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU"))
    }

    private val gson: Gson = GsonBuilder()
        .setDateFormat(DATE_FORMAT.toPattern())
        .create()

    /**
     * Получает все сообщения для заданного чата.
     */
    suspend fun getAllMessages(chatGlobalId: Long): Result<List<Message>> {
        return runCatching {
            apiServiceMessage.getMessagesByChatGlobalId(chatGlobalId).let { response ->
                if (response.success) {
                    val type = object : TypeToken<List<Message>>() {}.type
                    gson.fromJson<List<Message>>(response.message, type).also {
                        Log.d(TAG, "Successfully fetched messages data in chat: $chatGlobalId.")
                    }
                } else {
                    throw Exception("Failed to fetch messages in chat: $chatGlobalId: ${response.message}")
                }
            }
        }.onFailure {
            Log.e(TAG, "Error fetching messages: ${it.message}", it)
        }
    }

    /**
     * Отправляет сообщение через WebSocket.
     */
    suspend fun sendMessageOverWebSocket(message: Message): Result<Unit> {
        return try {
            // Сериализуем сообщение в JSON
            val messageJson = gson.toJson(message)

            // Используем WebSocketConnection для отправки сообщения
            webSocketConnection.sendMessage(messageJson)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message over WebSocket: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Подключается к WebSocket для получения сообщений в реальном времени.
     */
    fun connectWebSocket(onNewMessage: (Message) -> Unit) {
        webSocketConnection.connectWebSocket(onNewMessage)
    }

    /**
     * Отключается от WebSocket.
     */
    fun disconnectWebSocket(): Result<Unit> {
        return try {
            webSocketConnection.disconnectWebSocket()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка отключения WebSocket: ${e.message}")
            Result.failure(e)
        }
    }
}