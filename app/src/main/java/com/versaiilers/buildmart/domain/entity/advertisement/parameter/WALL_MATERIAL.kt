package com.versaiilers.buildmart.domain.entity.advertisement.parameter

enum class WALL_MATERIAL(val value: Int, val translation: String) {
    BRICK(1, "Кирпич"),
    GAS_BLOCK(2, "Газоблок"),
    WOOD(3, "Брус"),
    LOG(4, "Бревно"),
    METAL(5, "Металл"),
    FOAM_BLOCKS(6, "Пеноблок"),
    SANDWICH_PANEL(7, "Сендвич-панель"),
    RC_PANEL(8, "Ж/б панель");

    companion object {
        fun fromValue(value: Int): WALL_MATERIAL? {
            return values().find { it.value == value }
        }
    }
}