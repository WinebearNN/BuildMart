package com.versaiilers.buildmart.data.repository

import com.versaiilers.buildmart.data.datasource.advertisement.RemoteDataSourceAdvertisement
import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement
import com.versaiilers.buildmart.domain.repository.AdvertisementRepository
import javax.inject.Inject

class AdvertisementRepositoryImpl @Inject constructor(
    private val remoteDataSourceAdvertisement: RemoteDataSourceAdvertisement
) : AdvertisementRepository {

    companion object {
        private const val TAG = "AdvertisementRepositoryImpl"
    }

    override suspend fun getAllAdvertisements(): Result<List<Advertisement>> {
        return remoteDataSourceAdvertisement.getAll();
    }
}