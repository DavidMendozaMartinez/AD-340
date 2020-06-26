package com.davidmendozamartinez.ad340.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davidmendozamartinez.ad340.BuildConfig
import com.davidmendozamartinez.ad340.api.CurrentWeather
import com.davidmendozamartinez.ad340.api.WeeklyForecast
import com.davidmendozamartinez.ad340.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository(private val language: String) {

    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast

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

    fun loadWeeklyForecast(zipCode: String, countryCode: String = "es") {
        val call = createOpenWeatherMapService().currentWeather(
            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
            lang = language,
            zipCode = "$zipCode,$countryCode"
        )
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(
                    ForecastRepository::class.java.simpleName,
                    "error loading location for weekly forecast",
                    t
                )
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                        lang = language,
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon
                    )
                    forecastCall.enqueue(object : Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(
                                ForecastRepository::class.java.simpleName,
                                "error weekly forecast",
                                t
                            )
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>
                        ) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) {
                                _weeklyForecast.value = weeklyForecastResponse
                            }
                        }

                    })
                }
            }
        })
    }
}