package com.davidmendozamartinez.ad340.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository

class WeeklyForecastViewModelFactory(
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyForecastViewModel::class.java)) {
            return WeeklyForecastViewModel(forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyForecastViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    private val _viewState: MutableLiveData<WeeklyForecastViewState> = MutableLiveData()
    val viewState: LiveData<WeeklyForecastViewState> = _viewState

    fun loadWeeklyForecastInvoked(zipCode: String) {
        forecastRepository.loadWeeklyForecast(zipCode) { weeklyForecast ->
            _viewState.value = WeeklyForecastViewState(weeklyForecast.daily)
        }
    }
}