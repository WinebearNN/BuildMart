package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class FINISH_STAGE(val value: Int, val translation: String) {
    ROUGH(1, "Черновая"),
    ALMOST(2, "Предчистовая"),
    FINAL(3, "Чистовая");

    companion object {
        fun fromValue(value: Int): FINISH_STAGE? {
            return values().find { it.value == value }
        }
    }
}