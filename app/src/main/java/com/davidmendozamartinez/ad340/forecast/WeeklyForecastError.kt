package com.davidmendozamartinez.ad340.forecast

import com.davidmendozamartinez.ad340.R

enum class WeeklyForecastError(val resId: Int) {
    NO_LOCATION(R.string.label_weekly_forecast_enter_zip_code),
    REQUEST_ERROR(R.string.label_forecast_request_error)
}