package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class PARKING_SPACE(val value: Int, val translation: String) {
    GARAGE(1, "Гараж"),
    PAVED_AREA(2, "Асфальтированная площадка"),
    UNDERGROUND_PARKING(3, "Подземный паркинг"),
    MULTILEVEL_PARKING(4, "Многоэтажный паркинг"),
    GROUND_PARKING(5, "Наземный паркинг"),
    ABSENT(6, "Отсутствует");

    companion object {
        fun fromValue(value: Int): PARKING_SPACE? {
            return values().find { it.value == value }
        }
    }
}