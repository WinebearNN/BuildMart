package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class ADVERTISEMENT_TYPE(val value: Int, val translation: String) {
    FLAT(1, "Квартира"),
    HOUSE(2, "Дом");

    companion object {
        fun fromValue(value: Int): ADVERTISEMENT_TYPE? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): ADVERTISEMENT_TYPE? {
            return entries.find { it.translation == translation }
        }
    }
}