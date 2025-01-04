package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class GAS_TYPE(val value: Int, val translation: String) {
    HOUSE(1, "Подведен к дому"),
    LAND(2, "Подведен к земельному участку"),
    ABSENT(3, "Отсутствует");

    companion object {
        fun fromValue(value: Int): GAS_TYPE? {
            return entries.find { it.value == value }
        }
        fun fromTranslation(translation: String): GAS_TYPE? {
            return entries.find { it.translation == translation }
        }
    }
}