package com.davidmendozamartinez.ad340.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository

class WeeklyForecastViewModelFactory(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyForecastViewModel::class.java)) {
            return WeeklyForecastViewModel(locationRepository, forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyForecastViewModel(
    locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    private val _viewState: MutableLiveData<WeeklyForecastViewState> = MutableLiveData()
    val viewState: LiveData<WeeklyForecastViewState> = _viewState

    init {
        locationRepository.registerZipCodeChangeListener { location ->
            if (location != null) {
                _savedLocation.value = location
            }
        }
    }

    fun loadWeeklyForecastInvoked(zipCode: String) {
        forecastRepository.loadWeeklyForecast(zipCode) { weeklyForecast ->
            _viewState.value = WeeklyForecastViewState(weeklyForecast.daily)
        }
    }
}