package com.versaiilers.buildmart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.domain.entity.Chat
import com.versaiilers.buildmart.domain.entity.Message
import com.versaiilers.buildmart.domain.useCase.ChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatMessageUseCase: ChatMessageUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> get() = _newMessage

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> get() = _chats

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadLocalChats("1")  // Загружаем локальные чаты сразу при инициализации
        connectWebSocket()
    }

    private fun loadLocalChats(userGlobalId: String) {
        viewModelScope.launch {
            _loading.value = true
            val localResult = chatMessageUseCase.getAllChatsLocal(userGlobalId)
            _chats.value = localResult.getOrElse { emptyList() }
            _loading.value = false
        }
    }

    fun refreshChats(userGlobalId: String) {
        viewModelScope.launch {
            _loading.value = true
            chatMessageUseCase.refreshChats(userGlobalId).onSuccess { serverChats ->
                _chats.value = serverChats
            }.onFailure { exception ->
                Log.e(TAG, "Failed to refresh chats: ${exception.message}")
                _error.value = "An error occurred: ${exception.message}"
            }
            _loading.value = false
        }
    }

    private fun connectWebSocket() {
        chatMessageUseCase.connectToWebSocket { message ->
            viewModelScope.launch(Dispatchers.Main) {
                _newMessage.value = message
                updateLastMessageInChats(message)
            }
        }
    }

    private fun updateLastMessageInChats(newMessage: Message) {
        _chats.value = _chats.value?.map { chat ->
            if (chat.globalId == newMessage.chatGlobalId) {
                chat.copy(lastMessage = newMessage.content)
            } else {
                chat
            }
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