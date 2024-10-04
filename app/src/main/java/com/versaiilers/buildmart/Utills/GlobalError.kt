package com.versaiilers.buildmart.Utills

data class GlobalError(val code: Int, val description: String)

// Класс для исключения с ошибками
class ErrorException(val errors: List<GlobalError>) : Exception("Errors occurred")


