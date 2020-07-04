package com.davidmendozamartinez.ad340.forecast.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.util.Resource
import com.davidmendozamartinez.ad340.util.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.util.formatTempForDisplay
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository

class CurrentForecastViewModelFactory(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(
                tempDisplaySettingManager,
                locationRepository,
                forecastRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<CurrentForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<CurrentForecastViewState>> = _viewState

    init {
        loadLocation()
    }

    private fun loadLocation() {
        locationRepository.registerZipCodeChangeListener { location ->
            when (location) {
                is Location.ZipCode -> {
                    _viewState.value = Resource.loading()
                    loadCurrentForecast(location.zipCode)
                }
                else -> _viewState.value = Resource.error(CurrentForecastError.NO_LOCATION.resId)
            }
        }
    }

    private fun loadCurrentForecast(zipCode: String) {
        forecastRepository.loadCurrentForecast(zipCode, { currentForecast ->
            _viewState.value = Resource.success(
                CurrentForecastViewState(
                    currentForecast.name,
                    formatTempForDisplay(
                        currentForecast.forecast.temp,
                        tempDisplaySettingManager.getTempDisplaySetting()
                    )
                )
            )
        }, {
            _viewState.value = Resource.error(CurrentForecastError.REQUEST_ERROR.resId)
        })
    }
}