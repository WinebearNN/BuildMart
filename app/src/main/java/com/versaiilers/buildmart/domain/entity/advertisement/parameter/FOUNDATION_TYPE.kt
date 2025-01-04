package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class FOUNDATION_TYPE(val value: Int, val translation: String) {
    PILE(1, "Свайный"),
    PILE_FILL(2, "Свайно-заливной"),
    STRIP(3, "Ленточный"),
    SLAB(4, "Монолитная плита");


    companion object {
        fun fromValue(value: Int): FOUNDATION_TYPE? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): FOUNDATION_TYPE? {
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