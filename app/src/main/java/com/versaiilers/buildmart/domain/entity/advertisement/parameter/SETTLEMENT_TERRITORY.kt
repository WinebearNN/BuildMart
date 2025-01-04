package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class SETTLEMENT_TERRITORY(val value: Int, val translation: String) {
    OPEN(1, "Открытая"),
    CLOSED(2, "Закрытая"),
    GUARDED(3, "Охраняемая");


    companion object {
        fun fromValue(value: Int): SETTLEMENT_TERRITORY? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): SETTLEMENT_TERRITORY? {
            return entries.find { it.translation == translation }
        }
    }
}