package com.davidmendozamartinez.ad340.repository

import android.util.Log
import com.davidmendozamartinez.ad340.BuildConfig
import com.davidmendozamartinez.ad340.R
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
        failureCallback: (ForecastError) -> Unit,
        countryCode: String = "es"
    ) {
        val call = createOpenWeatherMapService().getCurrentForecast(
            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
            lang = language,
            zipCode = "$zipCode,$countryCode"
        )
        call.enqueue(object : Callback<CurrentForecast> {
            override fun onFailure(call: Call<CurrentForecast>, t: Throwable) {
                failureCallback(ForecastError.REQUEST_ERROR)
                Log.e(
                    ForecastRepository::class.java.simpleName,
                    "error loading current forecast",
                    t
                )
            }

            override fun onResponse(
                call: Call<CurrentForecast>,
                response: Response<CurrentForecast>
            ) {
                println(response.toString())
                response.body()?.let { currentForecast ->
                    successCallback(currentForecast)
                } ?: failureCallback(ForecastError.ZIP_CODE_NOT_FOUND)
            }
        })
    }

    fun loadWeeklyForecast(
        zipCode: String,
        successCallback: (WeeklyForecast) -> Unit,
        failureCallback: (ForecastError) -> Unit
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
                    failureCallback(ForecastError.REQUEST_ERROR)
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
                    } ?: failureCallback(ForecastError.ZIP_CODE_NOT_FOUND)
                }
            })
        }, {
            failureCallback(it)
        })
    }
}

enum class ForecastError(val resId: Int) {
    ZIP_CODE_NOT_FOUND(R.string.label_forecast_request_error_zip_code_not_found),
    REQUEST_ERROR(R.string.label_forecast_request_error)
}