package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TV_TYPE(val value: Int, val translation: String) {
    INTERNET(1, "Интернет"),
    TV(2, "Телевидение"),
    ABSENT(3, "Отсутствует");


    companion object {
        fun fromValue(value: Int): TV_TYPE? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): TV_TYPE? {
            return entries.find { it.translation == translation }
        }
    }
}