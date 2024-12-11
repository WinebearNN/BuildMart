package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class ADDITIONAL_BUILDINGS(val value: Int, val translation: String) {
    BATHHOUSE(1, "Баня"),
    GARDEN(2, "Сад"),
    GARAGE(3, "Гараж"),
    GREENHOUSE(4, "Теплица");

    companion object {
        fun fromValue(value: Int): ADDITIONAL_BUILDINGS? {
            return values().find { it.value == value }
        }
    }
}