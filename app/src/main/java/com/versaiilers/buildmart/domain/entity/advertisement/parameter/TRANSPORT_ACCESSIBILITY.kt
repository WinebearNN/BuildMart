package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class TRANSPORT_ACCESSIBILITY(val value: Int, val translation: String) {
    PAVED_ROAD(1, "Асфальтированная дорога"),
    PUBLIC_TRANSPORT_STOP(2, "Остановка общественного транспорта"),
    RAILWAY_STATION(3, "Железнодорожная станция");

    companion object {
        fun fromValue(value: Int): TRANSPORT_ACCESSIBILITY? {
            return values().find { it.value == value }
        }
    }
}