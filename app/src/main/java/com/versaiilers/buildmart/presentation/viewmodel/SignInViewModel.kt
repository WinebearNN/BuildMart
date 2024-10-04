package com.versaiilers.buildmart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.Utills.ErrorException
import com.versaiilers.buildmart.Utills.GlobalError
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.useCase.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(private val signInUserUseCase: SignInUserUseCase) : ViewModel()  {
    private val _globalErrors = MutableLiveData<List<GlobalError>>()
    val globalErrors: LiveData<List<GlobalError>> = _globalErrors

    private val _signInResult = MutableLiveData<Result<Unit>>()
    val signInResult: LiveData<Result<Unit>> = _signInResult

    fun signInUser(email: String, password:String){
        val user= User(email=email,password=password)

        viewModelScope.launch {
            val result=signInUserUseCase.execute(user)
            result.onSuccess {
                _signInResult.value = Result.success(Unit)
            }.onFailure { exception ->
                if (exception is ErrorException) {
                    // Если это ошибка валидации, передаем ошибки во фрагмент
                    _globalErrors.value = exception.errors
                } else {
                    // Иные ошибки (например, проблемы с сетью)
                    _signInResult.value = Result.failure(exception)
                }
            }
        }
    }
}