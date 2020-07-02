package com.davidmendozamartinez.ad340.repository

import android.util.Log
import com.davidmendozamartinez.ad340.BuildConfig
import com.davidmendozamartinez.ad340.api.createOpenWeatherMapService
import com.davidmendozamartinez.ad340.api.model.CurrentForecast
import com.davidmendozamartinez.ad340.api.model.WeeklyForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository(private val language: String) {

    fun loadCurrentForecast(
        zipCode: String,
        successCallback: (CurrentForecast) -> Unit,
        failureCallback: (Throwable) -> Unit,
        countryCode: String = "es"
    ) {
        val call = createOpenWeatherMapService().getCurrentForecast(
            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
            lang = language,
            zipCode = "$zipCode,$countryCode"
        )
        call.enqueue(object : Callback<CurrentForecast> {
            override fun onFailure(call: Call<CurrentForecast>, t: Throwable) {
                failureCallback(t)
                Log.e(ForecastRepository::class.java.simpleName, "error loading current weather", t)
            }

            override fun onResponse(
                call: Call<CurrentForecast>,
                response: Response<CurrentForecast>
            ) {
                response.body()?.let { currentForecast ->
                    successCallback(currentForecast)
                }
            }
        })
    }

    fun loadWeeklyForecast(
        zipCode: String,
        successCallback: (WeeklyForecast) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        loadCurrentForecast(zipCode, { currentForecast ->
            val call = createOpenWeatherMapService().getWeeklyForecast(
                apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                lang = language,
                lat = currentForecast.coordinates.lat,
                lon = currentForecast.coordinates.lon
            )
            call.enqueue(object : Callback<WeeklyForecast> {
                override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                    failureCallback(t)
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
        }, {
            failureCallback(it)
            Log.e(
                ForecastRepository::class.java.simpleName,
                "error loading location for weekly forecast",
                it
            )
        })
    }
}