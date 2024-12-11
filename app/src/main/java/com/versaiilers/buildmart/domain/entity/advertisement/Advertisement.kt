package com.versaiilers.buildmart.domain.entity.advertisement

import com.versaiilers.buildmart.domain.entity.advertisement.parameter.ADDITIONAL_BUILDINGS
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.ADVERTISEMENT_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.FINISH_STAGE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.FOUNDATION_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.GAS_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.HEATING_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.LAND_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.PARKING_SPACE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.SETTLEMENT_TERRITORY
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.SEWERAGE_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TOILET_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TRANSPORT_ACCESSIBILITY
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TV_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WALL_MATERIAL
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WATER_SUPPLY

open class Advertisement(
    var globalId: Long,
    var userGlobalId: Long,
    var srcLink: String,
    var title: String,
    var location: String,
    var rate: Float,
    var price: Int,
    var floor: Int,
    var saveFlag: Boolean,
    var type: ADVERTISEMENT_TYPE
)

class HouseAd(
    globalId: Long,
    userGlobalId: Long,
    srcLink: String,
    title: String,
    location: String,
    rate: Float,
    price: Int,
    floor: Int,
    saveFlag: Boolean,
    type: ADVERTISEMENT_TYPE,
    var description: String,
    var landSquare: Double,
    var powerSupply: Boolean,
    var yearOfConstruction: Int,
    var warmFloor: Boolean,
    var ceilingHeight: Float,
    var additionalBuildings: ADDITIONAL_BUILDINGS,
    var finishStage: FINISH_STAGE,
    var foundationType: FOUNDATION_TYPE,
    var gas: GAS_TYPE,
    var heatingType: HEATING_TYPE,
    var landType: LAND_TYPE,
    var parkingSpace: PARKING_SPACE,
    var settlementTerritory: SETTLEMENT_TERRITORY,
    var sewerageType: SEWERAGE_TYPE,
    var toiletType: TOILET_TYPE,
    var transportAccessibility: TRANSPORT_ACCESSIBILITY,
    var tvType: TV_TYPE,
    var wallMaterial: WALL_MATERIAL,
    var waterSupply: WATER_SUPPLY
) : Advertisement(
    globalId,
    userGlobalId,
    srcLink,
    title,
    location,
    rate,
    price,
    floor,
    saveFlag,
    type
)