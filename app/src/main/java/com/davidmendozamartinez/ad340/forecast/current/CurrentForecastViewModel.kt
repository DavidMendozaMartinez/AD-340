package com.davidmendozamartinez.ad340.forecast.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.LocationError
import com.davidmendozamartinez.ad340.util.Resource
import com.davidmendozamartinez.ad340.util.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.util.formatTempForDisplay

class CurrentForecastViewModelFactory(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(tempDisplaySettingManager, forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<CurrentForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<CurrentForecastViewState>> = _viewState

    fun onLocationObtained(zipCode: String) {
        _viewState.value = Resource.loading()

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
            _viewState.value = Resource.error(it.resId)
        })
    }

    fun onLocationError() {
        _viewState.value = Resource.error(LocationError.NO_LOCATION.resId)
    }
}