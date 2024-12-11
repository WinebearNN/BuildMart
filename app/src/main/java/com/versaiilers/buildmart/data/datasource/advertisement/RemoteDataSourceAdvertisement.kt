package com.versaiilers.buildmart.data.datasource.advertisement

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.versaiilers.buildmart.data.network.apiService.ApiServiceAdvertisement
import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class RemoteDataSourceAdvertisement @Inject constructor(
    private val apiServiceAdvertisement: ApiServiceAdvertisement,
) {
    private val gson: Gson = GsonBuilder()
        .setDateFormat(SimpleDateFormat("MMM d, yyyy", Locale("ru", "RU")).toPattern())
        .create()

    companion object{
        private const val TAG="RemoteDataSourceAdvertisement"
    }

    suspend fun getAll():Result<List<Advertisement>>{
        return runCatching {
            apiServiceAdvertisement.getAll().let { response ->
                if (response.success){
                    Log.d(TAG,"Message before deserialization ${response.message}")
                    val type = object : TypeToken<List<Advertisement>>() {}.type
                    gson.fromJson<List<Advertisement>>(response.message, type).also {
                        Log.d(TAG, "Ads were successfully gotten.")
                    }
                }else{
                    throw Exception("Failed to get ads: ${response.message}")
                }
            }
        }.onFailure {
            Log.e(TAG,"Error getting ads: ${it.message}",it)
        }
    }
}