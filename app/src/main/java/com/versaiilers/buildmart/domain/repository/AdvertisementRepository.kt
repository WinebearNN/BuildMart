package com.versaiilers.buildmart.domain.repository

import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement

interface AdvertisementRepository {
    suspend fun getAllAdvertisements():Result<List<Advertisement>>
}