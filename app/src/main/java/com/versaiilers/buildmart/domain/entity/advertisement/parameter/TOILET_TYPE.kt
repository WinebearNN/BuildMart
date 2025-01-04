package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TOILET_TYPE(val value: Int, val translation: String) {
    OUTDOOR(1, "На улице"),
    INDOOR(2, "В доме"),
    NONE(-1, "Отсутствует");


    companion object {
        fun fromValue(value: Int): TOILET_TYPE? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): TOILET_TYPE? {
            return entries.find { it.translation == translation }
        }
    }
}