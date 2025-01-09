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
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TRANSPORT_ACCESSIBILITY
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TV_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WALL_MATERIAL
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WARM_FLOOR
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WATER_SUPPLY

data class Advertisement(
    var globalId: Long,//A1
    var userGlobalId: Long,//A2
    var srcLink: String,//A3
    var title: String,//A4
    var location: String,//A5
    var rate: Float,//A6
    var price: Int,//A7
    var floor: Int,//A8
    var saveFlag: Boolean,//A9
    var type: ADVERTISEMENT_TYPE,//A10
    var description: String = ""//A11
)

data class HouseAd(
    var globalId: Long = 0,//A6
    var userGlobalId: Long = 0,//A7
    var powerSupply: Boolean = false,//H1
    var ceilingHeight: Float = 0f,//H2
    var finishStage: FINISH_STAGE = FINISH_STAGE.ROUGH,//H3
    var foundationType: FOUNDATION_TYPE = FOUNDATION_TYPE.PILE,//H4
    var gas: GAS_TYPE = GAS_TYPE.ABSENT,//H5
    var warmFloor: WARM_FLOOR = WARM_FLOOR.ABSENT,//H6
    var heatingType: HEATING_TYPE = HEATING_TYPE.ABSENT,//H7
    var sewerageType: SEWERAGE_TYPE = SEWERAGE_TYPE.ABSENT,//H8
    var tvType: TV_TYPE = TV_TYPE.ABSENT,//H9
    var wallMaterial: WALL_MATERIAL = WALL_MATERIAL.LOG,//H10
    var waterSupply: WATER_SUPPLY = WATER_SUPPLY.ABSENT,//H11
    var yearOfConstruction: Int = 0//H12
)

data class LandHouseAd(
    var globalId: Long = 0,//A6
    var userGlobalId: Long = 0,//A7
    var landSquare: Double = 0.0,//L1
    var additionalBuildings: MutableSet<ADDITIONAL_BUILDINGS> = mutableSetOf(),//L2
    var landType: LAND_TYPE = LAND_TYPE.KUP,//L3
    var parkingSpace: MutableSet<PARKING_SPACE> = mutableSetOf(),//L4
    var settlementTerritory: SETTLEMENT_TERRITORY = SETTLEMENT_TERRITORY.OPEN,//L5
    var transportAccessibility: MutableSet<TRANSPORT_ACCESSIBILITY> = mutableSetOf()//L6
)