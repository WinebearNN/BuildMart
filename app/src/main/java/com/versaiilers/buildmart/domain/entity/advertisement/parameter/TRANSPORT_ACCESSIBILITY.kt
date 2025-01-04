package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TRANSPORT_ACCESSIBILITY(val value: Int, val translation: String) {
    PAVED_ROAD(1, "Асфальтированная дорога"),
    PUBLIC_TRANSPORT_STOP(2, "Остановка общественного транспорта"),
    RAILWAY_STATION(3, "Железнодорожная станция"),
    ABSENT(4, "Отсутствует");


    companion object {
        fun fromValue(value: Int): TRANSPORT_ACCESSIBILITY? {
            return entries.find { it.value == value }
        }

        fun fromTranslation(translation: String): TRANSPORT_ACCESSIBILITY? {
            return entries.find { it.translation == translation }
        }
    }
}