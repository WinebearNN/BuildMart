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
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): HEATING_TYPE? {
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