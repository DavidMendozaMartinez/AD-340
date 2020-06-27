package com.davidmendozamartinez.ad340.repository

import android.util.Log
import com.davidmendozamartinez.ad340.BuildConfig
import com.davidmendozamartinez.ad340.api.CurrentWeather
import com.davidmendozamartinez.ad340.api.WeeklyForecast
import com.davidmendozamartinez.ad340.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository(private val language: String) {

    fun loadCurrentForecast(
        zipCode: String,
        countryCode: String = "es",
        successCallback: (CurrentWeather) -> Unit
    ) {
        val call = createOpenWeatherMapService().currentWeather(
            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
            lang = language,
            zipCode = "$zipCode,$countryCode"
        )
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading current weather", t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                response.body()?.let { currentWeather ->
                    successCallback(currentWeather)
                }
            }
        })
    }

    fun loadWeeklyForecast(
        zipCode: String,
        countryCode: String = "es",
        successCallback: (WeeklyForecast) -> Unit
    ) {
        loadCurrentForecast(zipCode, countryCode) { currentForecast ->
            val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                lang = language,
                lat = currentForecast.coord.lat,
                lon = currentForecast.coord.lon
            )
            forecastCall.enqueue(object : Callback<WeeklyForecast> {
                override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                    Log.e(
                        ForecastRepository::class.java.simpleName,
                        "error loading weekly forecast",
                        t
                    )
                }

                override fun onResponse(
                    call: Call<WeeklyForecast>,
                    response: Response<WeeklyForecast>
                ) {
                    response.body()?.let { weeklyForecast ->
                        successCallback(weeklyForecast)
                    }
                }
            })
        }
    }
}