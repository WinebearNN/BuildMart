package com.versaiilers.buildmart.utills

class CheckValidation {



    companion object {
        fun isValidEmail(email: String): Boolean {
            return email.contains("@") // Можно улучшить регулярным выражением для более строгой проверки
        }
        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return phoneNumber.length == 11 // Можно добавить дополнительную логику проверки
        }
        fun isValidPassword(password: String): Boolean {
            return password.length >= 6 // Можно добавить дополнительные проверки, такие как наличие цифр и специальных символов
        }
    }
}