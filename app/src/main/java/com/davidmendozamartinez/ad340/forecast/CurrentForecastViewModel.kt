package com.davidmendozamartinez.ad340.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository

class CurrentForecastViewModelFactory(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(locationRepository, forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(
    locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    private val _viewState: MutableLiveData<CurrentForecastViewState> = MutableLiveData()
    val viewState: LiveData<CurrentForecastViewState> = _viewState

    init {
        locationRepository.registerZipCodeChangeListener { location ->
            if (location != null) {
                _savedLocation.value = location
            }
        }
    }

    fun loadCurrentForecastInvoked(zipCode: String) {
        forecastRepository.loadCurrentForecast(zipCode) { currentForecast ->
            _viewState.value =
                CurrentForecastViewState(currentForecast.name, currentForecast.forecast.temp)
        }
    }
}