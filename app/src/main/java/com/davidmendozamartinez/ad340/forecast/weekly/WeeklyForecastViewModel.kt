package com.davidmendozamartinez.ad340.forecast.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationError
import com.davidmendozamartinez.ad340.repository.LocationRepository
import com.davidmendozamartinez.ad340.util.Resource

class WeeklyForecastViewModelFactory(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyForecastViewModel::class.java)) {
            return WeeklyForecastViewModel(
                locationRepository,
                forecastRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyForecastViewModel(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    val location: LiveData<Location> get() = locationRepository.savedLocation

    private val _viewState: MutableLiveData<Resource<WeeklyForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<WeeklyForecastViewState>> = _viewState

    fun onLocationObtained(zipCode: String) {
        _viewState.value = Resource.loading()

        forecastRepository.loadWeeklyForecast(zipCode, {
            _viewState.value = Resource.success(
                WeeklyForecastViewState(
                    it.daily
                )
            )
        }, {
            _viewState.value = Resource.error(it.resId)
        })
    }

    fun onLocationError() {
        _viewState.value = Resource.error(LocationError.NO_LOCATION.resId)
    }
}