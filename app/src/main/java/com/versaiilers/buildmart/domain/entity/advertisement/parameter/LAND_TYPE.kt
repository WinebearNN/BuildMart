package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class LAND_TYPE(val value: Int, val translation: String) {
    IHC(1, "Индивидуальное жилищное строительство"),
    PSP(2, "Личное подсобное хозяйство"),
    DUP(3, "Дачное некоммерческое партнерство/товарищество"),
    GUP(4, "Садовое некоммерческое товарищество"),
    KUP(5, "Фермерские некоммерческие товарищества");

    companion object {
        fun fromValue(value: Int): LAND_TYPE? {
            return values().find { it.value == value }
        }
    }
}