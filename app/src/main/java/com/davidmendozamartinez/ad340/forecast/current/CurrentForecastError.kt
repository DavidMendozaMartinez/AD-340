package com.davidmendozamartinez.ad340.forecast.current

import com.davidmendozamartinez.ad340.R

enum class CurrentForecastError(val resId: Int) {
    NO_LOCATION(R.string.label_current_forecast_enter_zip_code),
    REQUEST_ERROR(R.string.label_forecast_request_error)
}