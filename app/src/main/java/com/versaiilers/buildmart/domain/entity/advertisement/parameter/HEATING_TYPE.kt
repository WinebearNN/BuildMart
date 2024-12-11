package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class HEATING_TYPE(val value: Int, val translation: String) {
    GAS(1, "Газовое"),
    ELECTRIC(2, "Электрическое"),
    MIXED(3, "Смешанное"),
    WOOD(4, "Дровяное"),
    CENTRAL(5, "Центральное"),
    ABSENT(6, "Отсутствует");

    companion object {
        fun fromValue(value: Int): HEATING_TYPE? {
            return values().find { it.value == value }
        }
    }
}