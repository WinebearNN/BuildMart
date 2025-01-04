package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class SEWERAGE_TYPE(val value: Int, val translation: String) {
    SEPTIC(1, "Септик"),
    OVERFLOW_SEPTIC(2, "Переливной септик"),
    BIOLOGICAL_TREATMENT_SYSTEM(3, "Биологические очистные сооружения"),
    CESSPOOL(4, "Выгребная яма"),
    CENTRALIZED_SEWERAGE_SYSTEM(5, "Централизованная канализация"),
    ABSENT(6, "Отсутствует");

    companion object {
        fun fromValue(value: Int): SEWERAGE_TYPE? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): SEWERAGE_TYPE? {
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