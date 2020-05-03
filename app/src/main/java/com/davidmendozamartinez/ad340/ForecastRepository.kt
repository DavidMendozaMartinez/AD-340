package com.davidmendozamartinez.ad340

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRepository {

    private val _weaklyForecast = MutableLiveData<List<DailyForecast>>()
    val weaklyForecast: LiveData<List<DailyForecast>> = _weaklyForecast

    fun loadForecast(zipCode: String) {
        val randomValues = List(7) { Random.nextFloat().rem(100) * 100 }
        val forecastItems = randomValues.map { temp ->
            DailyForecast(temp, "Partly Cloudy")
        }
        _weaklyForecast.value = forecastItems
    }
}