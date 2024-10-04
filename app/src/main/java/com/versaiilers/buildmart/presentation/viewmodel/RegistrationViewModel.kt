package com.versaiilers.buildmart.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.Utills.ErrorException
import com.versaiilers.buildmart.Utills.GlobalError
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.useCase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _globalErrors = MutableLiveData<List<GlobalError>>()
    val globalErrors: LiveData<List<GlobalError>> = _globalErrors

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> = _registrationResult

    fun registerUser(email: String, password: String, phoneNumber: String) {
        val user = User(email = email, password = password, phoneNumber = phoneNumber)

        viewModelScope.launch {
            val result = registerUserUseCase.execute(user)
            handleRegistrationResult(result)
        }
    }

    private fun handleRegistrationResult(result: Result<Unit>) {
        result.onSuccess {
            _registrationResult.value = Result.success(Unit)
        }.onFailure { exception ->
            if (exception is ErrorException) {
                // Если это ошибка валидации, передаем ошибки во фрагмент
                _globalErrors.value = exception.errors
            } else {
                // Иные ошибки (например, проблемы с сетью)
                _registrationResult.value = Result.failure(exception)
            }
        }
    }
}