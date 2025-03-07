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
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): PARKING_SPACE? {
            return entries.find { it.translation == translation }
        }

        fun getAllTranslations(): Array<String> {
            return entries
                .filter { it.value != -1 } // Исключаем объекты с value == -1
                .map { it.translation } // Преобразуем оставшиеся объекты в их translation
                .toTypedArray() // Преобразуем список в массив
        }
    }
}