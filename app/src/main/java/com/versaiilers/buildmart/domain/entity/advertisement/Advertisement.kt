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
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WARM_FLOOR
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

data class HouseAd(
    var description: String = "",
    var landSquare: Double = 0.0,
    var powerSupply: Boolean = false,
    var yearOfConstruction: Int = 0,
    var ceilingHeight: Float = 0f,
    var additionalBuildings: ADDITIONAL_BUILDINGS = ADDITIONAL_BUILDINGS.ABSENT,
    var finishStage: FINISH_STAGE = FINISH_STAGE.ROUGH,
    var foundationType: FOUNDATION_TYPE = FOUNDATION_TYPE.PILE,
    var gas: GAS_TYPE = GAS_TYPE.ABSENT,
    var warmFloor: WARM_FLOOR = WARM_FLOOR.ABSENT,
    var heatingType: HEATING_TYPE = HEATING_TYPE.ABSENT,
    var landType: LAND_TYPE = LAND_TYPE.KUP,
    var parkingSpace: PARKING_SPACE = PARKING_SPACE.ABSENT,
    var settlementTerritory: SETTLEMENT_TERRITORY = SETTLEMENT_TERRITORY.OPEN,
    var sewerageType: SEWERAGE_TYPE = SEWERAGE_TYPE.ABSENT,
    var toiletType: TOILET_TYPE = TOILET_TYPE.NONE,//TODO remove
    var transportAccessibility: TRANSPORT_ACCESSIBILITY = TRANSPORT_ACCESSIBILITY.ABSENT,
    var tvType: TV_TYPE = TV_TYPE.ABSENT,
    var wallMaterial: WALL_MATERIAL = WALL_MATERIAL.LOG,
    var waterSupply: WATER_SUPPLY = WATER_SUPPLY.ABSENT
) : Advertisement(
    globalId = 0,
    userGlobalId = 0,
    srcLink = "",
    title = "",
    location = "",
    rate = 0f,
    price = 0,
    floor = 0,
    saveFlag = false,
    type = ADVERTISEMENT_TYPE.HOUSE
)