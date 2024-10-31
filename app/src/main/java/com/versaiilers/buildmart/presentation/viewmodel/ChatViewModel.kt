package com.versaiilers.buildmart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.data.repository.ChatRepositoryImpl
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.useCase.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> get() = _newMessage

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> get() = _chats

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        // Подключение к WebSocket
        connectWebSocket()
    }

    fun loadChats(userGlobalId: String) {
        Log.d(TAG, "Loading chats")
        _loading.value = true
        viewModelScope.launch {
            try {
                val localResult = chatUseCase.getAllChatsLocal(userGlobalId)
                _chats.value = localResult.getOrElse { emptyList() }

                val remoteResult = chatUseCase.refreshChats(userGlobalId)
                remoteResult.onSuccess { serverChats ->
                    _chats.value = serverChats
                }.onFailure { exception ->
                    Log.e(TAG, "Failed to refresh chats: ${exception.message}")
                }
            } catch (e: Exception) {
                _error.value = "An error occurred: ${e.message}"
                Log.e(TAG, "loadChats error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun connectWebSocket() {
        chatUseCase.connectToWebSocket { message ->
            // Обновляем _newMessage и обновляем последнее сообщение в чате
            viewModelScope.launch(Dispatchers.Main) {
                _newMessage.value = message
                onNewMessageReceived(message)
            }
        }
    }

    fun disconnectWebSocket() {
        chatUseCase.disconnectWebSocket().onFailure { exception ->
            Log.e(TAG, "Error disconnecting WebSocket: ${exception.message}")
        }
    }

    private fun onNewMessageReceived(newMessage: Message) {
        val updatedChats = _chats.value?.map { chat ->
            if (chat.globalId == newMessage.chatGlobalId) {
                chat.copy(lastMessage = newMessage.content) // Обновляем последнее сообщение
            } else {
                chat
            }
        }
        // Проверка на случай, если updatedChats оказалось null
        updatedChats?.let {
            _chats.value = it
        }
    }
}