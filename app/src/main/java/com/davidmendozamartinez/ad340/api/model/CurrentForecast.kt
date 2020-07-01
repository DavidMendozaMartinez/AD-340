package com.davidmendozamartinez.ad340.api.model

import com.squareup.moshi.Json

data class CurrentForecast(
    val name: String,
    @field:Json(name = "coord") val coordinates: Coordinates,
    @field:Json(name = "main") val forecast: Forecast
)

data class Coordinates(val lat: Float, val lon: Float)
data class Forecast(val temp: Float)