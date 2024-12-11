package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class FOUNDATION_TYPE(val value: Int, val translation: String) {
    PILE(1, "Свайный"),
    PILE_FILL(2, "Свайно-заливной"),
    STRIP(3, "Ленточный"),
    SLAB(4, "Монолитная плита");

    companion object {
        fun fromValue(value: Int): FOUNDATION_TYPE? {
            return values().find { it.value == value }
        }
    }
}