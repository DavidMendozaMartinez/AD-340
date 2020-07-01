package com.davidmendozamartinez.ad340.api

import com.davidmendozamartinez.ad340.api.model.CurrentForecast
import com.davidmendozamartinez.ad340.api.model.WeeklyForecast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun createOpenWeatherMapService(): OpenWeatherMapService {
    val retrofit = Retrofit.Builder()
        .baseUrl(Routes.OPEN_WEATHER_MAP_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET(Routes.CURRENT_WEATHER_DATA)
    fun getCurrentForecast(
        @Query("appid") apiKey: String,
        @Query("lang") lang: String,
        @Query("units") units: String = "imperial",
        @Query("zip") zipCode: String
    ): Call<CurrentForecast>

    @GET(Routes.ONE_CALL_API)
    fun getWeeklyForecast(
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String = "current,minutely,hourly",
        @Query("lang") lang: String,
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "imperial"
    ): Call<WeeklyForecast>
}