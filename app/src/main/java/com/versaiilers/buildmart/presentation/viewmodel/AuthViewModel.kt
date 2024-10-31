package com.versaiilers.buildmart.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.useCase.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase
) : ViewModel() {



    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    fun authUser() {

        viewModelScope.launch {
            val result = authUserUseCase.execute()
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<User>) {
        result.onSuccess {
            _authResult.value = Result.success(result.getOrNull()!!)
        }.onFailure { exception ->
            _authResult.value = Result.failure(exception)
        }

    }
}