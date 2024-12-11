package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TOILET_TYPE(val value: Int, val translation: String) {
    OUTDOOR(1, "На улице"),
    INDOOR(2, "В доме");

    companion object {
        fun fromValue(value: Int): TOILET_TYPE? {
            return values().find { it.value == value }
        }
    }
}