package com.versaiilers.buildmart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.useCase.ChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val chatMessageUseCase: ChatMessageUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ConversationViewModel"
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> get() = _newMessage

    private var chatGlobalId: Long = 0

    init {
        connectWebSocket()
    }

    // Устанавливаем ID чата и загружаем сообщения для этого чата
    fun setChatId(chatId: Long) {
        chatGlobalId = chatId
        loadMessages()
    }

    private fun loadMessages() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val localMessages = chatMessageUseCase.getAllMessagesLocal(chatGlobalId)
                _messages.value = localMessages.getOrElse { emptyList() }

//                val remoteMessages = chatMessageUseCase.refreshMessages(chatGlobalId)
//                remoteMessages.onSuccess { serverMessages ->
//                    _messages.value = serverMessages
//                }.onFailure { exception ->
//                    Log.e(TAG, "Failed to refresh messages: ${exception.message}")
//                }
            } catch (e: Exception) {
                _error.value = "An error occurred: ${e.message}"
                Log.e(TAG, "loadMessages error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun sendMessage(content: String) {
        // Создаем новое сообщение
        val message = Message(
            id = 0,  // Устанавливаем ID (например, автоинкремент или временный идентификатор)
            globalId = 0,  // Возможно, сервер назначит глобальный ID
            userGlobalId = 1,  // ID текущего пользователя
            chatGlobalId = chatGlobalId,
            content = content,
            timestamp = System.currentTimeMillis()  // Устанавливаем текущее время
        )

        viewModelScope.launch {
            try {
                // Сохраняем сообщение в локальном хранилище
                //chatMessageUseCase.saveMessageLocal(message)

                // Отправляем сообщение через WebSocket
                chatMessageUseCase.sendMessageOverWebSocket(message).onFailure { exception ->
                    _error.value = "Failed to send message: ${exception.message}"
                    Log.e(TAG, "Failed to send message: ${exception.message}")
                }

                // Обновляем UI, добавляя новое сообщение в список
                updateMessages(message)

            } catch (e: Exception) {
                _error.value = "An error occurred while sending message: ${e.message}"
                Log.e(TAG, "sendMessage error: ${e.message}")
            }
        }
    }

    private fun connectWebSocket() {
        chatMessageUseCase.connectToWebSocket { message ->
            if (message.chatGlobalId == chatGlobalId) {
                viewModelScope.launch(Dispatchers.Main) {
                    _newMessage.value = message
                    updateMessages(message)
                }
            }
        }
    }

    private fun updateMessages(newMessage: Message) {
        // Если _messages.value пусто, создаем новый список с новым сообщением
        val currentMessages = _messages.value ?: emptyList()
        _messages.value = currentMessages.toMutableList().apply {
            add(newMessage)
        }
    }

    fun disconnectWebSocket() {
        chatMessageUseCase.disconnectWebSocket().onFailure { exception ->
            Log.e(TAG, "Error disconnecting WebSocket: ${exception.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
}