package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class WATER_SUPPLY(val value: Int, val translation: String) {
    WELL(1, "Скважина"),
    CENTRAL(2, "Центральное"),
    SUMP(3, "Колодец");

    companion object {
        fun fromValue(value: Int): WATER_SUPPLY? {
            return values().find { it.value == value }
        }
    }
}