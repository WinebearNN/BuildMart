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
            return values().find { it.value == value }
        }
    }
}