package com.davidmendozamartinez.ad340.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.Resource
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
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<WeeklyForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<WeeklyForecastViewState>> = _viewState

    init {
        loadLocation()
    }

    private fun loadLocation() {
        locationRepository.registerZipCodeChangeListener { location ->
            when (location) {
                is Location.ZipCode -> {
                    _viewState.value = Resource.loading()
                    loadWeeklyForecast(location.zipCode)
                }
                else -> _viewState.value = Resource.error(WeeklyForecastError.NO_LOCATION.resId)
            }
        }
    }

    private fun loadWeeklyForecast(zipCode: String) {
        forecastRepository.loadWeeklyForecast(zipCode, {
            _viewState.value = Resource.success(WeeklyForecastViewState(it.daily))
        }, {
            _viewState.value = Resource.error(WeeklyForecastError.REQUEST_ERROR.resId)
        })
    }
}