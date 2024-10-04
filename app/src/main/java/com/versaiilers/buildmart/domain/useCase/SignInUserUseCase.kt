package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.Utills.ErrorCode
import com.versaiilers.buildmart.Utills.ErrorException
import com.versaiilers.buildmart.Utills.GlobalError
import com.versaiilers.buildmart.domain.entity.User
import com.versaiilers.buildmart.domain.repository.UserRepository
import javax.inject.Inject



class SignInUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute(user: User): Result<User> {
        val globalErrors = mutableListOf<GlobalError>()

        validateUser(user, globalErrors)

        // Если есть ошибки, возвращаем их
        if (globalErrors.isNotEmpty()) {
            return Result.failure(ErrorException(globalErrors))
        }

        // Вызов метода для входа пользователя
        return userRepository.loginUserByEmail(user)
    }

    // Валидация пользователя
    private fun validateUser(user: User, globalErrors: MutableList<GlobalError>) {
        // Валидация email
        if (!isValidEmail(user.email)) {
            globalErrors.add(GlobalError(100, ErrorCode.CODE_100.description))
        }

        // Валидация пароля
        if (!isValidPassword(user.password)) {
            globalErrors.add(GlobalError(101, ErrorCode.CODE_101.description))
        }
    }

    // Валидация email
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") // Можно улучшить регулярным выражением для более строгой проверки
    }

    // Валидация пароля
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // Можно добавить дополнительные проверки
    }
}