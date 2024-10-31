package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.utills.ErrorCode
import com.versaiilers.buildmart.utills.CheckValidation.Companion.isValidEmail
import com.versaiilers.buildmart.utills.CheckValidation.Companion.isValidPassword
import com.versaiilers.buildmart.utills.CheckValidation.Companion.isValidPhoneNumber
import com.versaiilers.buildmart.utills.ErrorException
import com.versaiilers.buildmart.utills.GlobalError
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    // Выполняем логику регистрации пользователя
    suspend fun execute(user: User): Result<Unit> {
        val globalErrors = mutableListOf<GlobalError>()

        validateUser(user, globalErrors)

        // Если есть ошибки, возвращаем их
        if (globalErrors.isNotEmpty()) {
            return Result.failure(ErrorException(globalErrors))
        }

        // Регистрируем пользователя через репозиторий
        return userRepository.register(user)
    }

    // Валидация пользователя
    private fun validateUser(user: User, globalErrors: MutableList<GlobalError>) {
        // Валидация email
        if (!isValidEmail(user.email)) {
            globalErrors.add(GlobalError(100, ErrorCode.CODE_100.description))
        }

        // Валидация телефона
        if (!isValidPhoneNumber(user.phoneNumber)) {
            globalErrors.add(GlobalError(102, ErrorCode.CODE_102.description))
        }

        // Валидация пароля
        if (!isValidPassword(user.password)) {
            globalErrors.add(GlobalError(101, ErrorCode.CODE_101.description))
        }
    }


}