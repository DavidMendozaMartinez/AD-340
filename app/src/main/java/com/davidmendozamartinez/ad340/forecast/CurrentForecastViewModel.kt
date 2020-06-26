package com.davidmendozamartinez.ad340.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository

class CurrentForecastViewModelFactory(
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    private val _viewState: MutableLiveData<CurrentForecastViewState> = MutableLiveData()
    val viewState: LiveData<CurrentForecastViewState> = _viewState

    fun loadCurrentForecastInvoked(zipCode: String) {
        forecastRepository.loadCurrentForecast(zipCode) { currentForecast ->
            _viewState.value =
                CurrentForecastViewState(currentForecast.name, currentForecast.forecast.temp)
        }
    }
}