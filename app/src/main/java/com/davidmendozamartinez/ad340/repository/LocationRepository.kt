package com.davidmendozamartinez.ad340.repository

import android.content.Context

sealed class Location {
    data class ZipCode(val zipCode: String) : Location()
}

private const val KEY_ZIP_CODE = "key_zip_code"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun registerZipCodeChangeListener(listener: (Location?) -> Unit) {
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key != KEY_ZIP_CODE) return@registerOnSharedPreferenceChangeListener
            listener(getLocation())
        }
        listener(getLocation())
    }

    private fun getLocation(): Location? =
        preferences.getString(KEY_ZIP_CODE, null)?.run { Location.ZipCode(this) }

    fun saveLocation(location: Location) {
        when (location) {
            is Location.ZipCode -> preferences.edit().putString(KEY_ZIP_CODE, location.zipCode)
                .apply()
        }
    }
}