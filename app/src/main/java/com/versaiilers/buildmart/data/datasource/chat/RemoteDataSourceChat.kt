package com.versaiilers.buildmart.data.datasource.chat

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.versaiilers.buildmart.data.network.ApiServiceChat
import com.versaiilers.buildmart.data.network.ApiServiceProvider
import com.versaiilers.buildmart.data.network.WebSocketConnection
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import jakarta.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.text.SimpleDateFormat
import java.util.Locale

class RemoteDataSourceChat @Inject constructor(
    private val apiServiceChat: ApiServiceChat,
    private val webSocketConnection: WebSocketConnection // Внедряем WebSocketConnection
) {
    companion object {
        private const val TAG = "RemoteDataSourceChat"
        private val DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU"))
    }

    private val gson: Gson = GsonBuilder()
        .setDateFormat(DATE_FORMAT.toPattern())
        .create()

    /**
     * Создает чат через API.
     */
    suspend fun createChat(chat: Chat): Result<Unit> {
        return runCatching {
            apiServiceChat.createChat(chat).let { response ->
                if (response.success) {
                    Unit
                } else {
                    throw Exception("Message sending error: ${response.message}")
                }
            }
        }.onFailure {
            Log.e(TAG, "Error creating chat: ${it.message}", it)
        }
    }

    /**
     * Получает все чаты и сообщения для пользователя.
     */
    suspend fun getAllChats(userGlobalId: String): Result<Pair<List<Chat>, List<List<Message>>>> {
        return runCatching {
            apiServiceChat.getChatByUserGlobalId(userGlobalId).let { response ->
                if (response.success) {
                    val type = object : TypeToken<Pair<List<Chat>, List<List<Message>>>>() {}.type
                    gson.fromJson<Pair<List<Chat>, List<List<Message>>>>(response.message, type).also {
                        Log.d(TAG, "Successfully fetched chat data.")
                    }
                } else {
                    throw Exception("Failed to fetch chats: ${response.message}")
                }
            }
        }.onFailure {
            Log.e(TAG, "Error fetching chats: ${it.message}", it)
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