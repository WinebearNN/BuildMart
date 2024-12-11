package com.versaiilers.buildmart.domain.useCase

import com.versaiilers.buildmart.data.repository.AdvertisementRepositoryImpl
import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement
import javax.inject.Inject

class AdvertisementUseCase @Inject constructor(
    private val advertisementRepositoryImpl: AdvertisementRepositoryImpl) {

    suspend fun getAllAdvertisements():Result<List<Advertisement>>{
        return advertisementRepositoryImpl.getAllAdvertisements()
    }
}