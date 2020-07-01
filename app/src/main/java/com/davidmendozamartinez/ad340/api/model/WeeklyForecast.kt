package com.davidmendozamartinez.ad340.api.model

import com.squareup.moshi.Json

data class WeeklyForecast(val daily: List<DailyForecast>)

data class DailyForecast(
    @field:Json(name = "dt") val date: Long,
    val temp: Temp,
    val weather: List<WeatherDescription>
)

data class Temp(val min: Float, val max: Float)
data class WeatherDescription(val main: String, val description: String, val icon: String)