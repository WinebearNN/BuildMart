package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class ADDITIONAL_BUILDINGS(val value: Int, val translation: String) {
    BATHHOUSE(1, "Баня"),
    GARDEN(2, "Сад"),
    GARAGE(3, "Гараж"),
    GREENHOUSE(4, "Теплица"),
    GAZEBO(5, "Беседка"),
//    VERANDA_TERRACE(6, "Веранда или терраса"),
    ABSENT(6, "Отсутствует");

    companion object {
        fun fromValue(value: Int): ADDITIONAL_BUILDINGS? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): ADDITIONAL_BUILDINGS? {
            return entries.find { it.translation == translation }
        }
    }
}