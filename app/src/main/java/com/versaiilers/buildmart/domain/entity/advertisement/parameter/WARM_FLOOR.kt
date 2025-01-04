package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class WARM_FLOOR(val value: Int, val translation: String) {
    LIQUID(1, "Водяной"),
    ELECTRIC(2, "Электрический"),
    ABSENT(3, "Отсутствует");

    companion object {
        fun fromValue(value: Int): WARM_FLOOR? {
            return WARM_FLOOR.entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): WARM_FLOOR? {
            return entries.find { it.translation == translation }
        }
    }
}