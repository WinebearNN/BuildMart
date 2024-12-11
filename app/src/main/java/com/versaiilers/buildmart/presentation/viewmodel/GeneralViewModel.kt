package com.versaiilers.buildmart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement
import com.versaiilers.buildmart.domain.useCase.AdvertisementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val advertisementUseCase: AdvertisementUseCase
):ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _advertisements = MutableLiveData<List<Advertisement>>()
    val advertisements: LiveData<List<Advertisement>> get() = _advertisements

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    companion object{
        private const val TAG="GeneralViewModel"
    }

//    init{
//        loadAdvertisements(1)
//    }

    fun loadAdvertisements(userGlobalId:Long){
        viewModelScope.launch {
            _loading.value=true
            advertisementUseCase.getAllAdvertisements().onSuccess { value: List<Advertisement> ->
                _advertisements.value=value
            }.onFailure { exception ->
                Log.e(TAG,"Failed to load advertisements $exception")
                _error.value = "An error occurred: ${exception.message}"
            }
            _loading.value=false
        }
    }



}