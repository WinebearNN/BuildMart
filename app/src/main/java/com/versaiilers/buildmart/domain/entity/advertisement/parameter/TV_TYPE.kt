package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TV_TYPE(val value: Int, val translation: String) {
    INTERNET(1, "Интернет"),
    TV(2, "Телевидение");

    companion object {
        fun fromValue(value: Int): TV_TYPE? {
            return values().find { it.value == value }
        }
    }
}