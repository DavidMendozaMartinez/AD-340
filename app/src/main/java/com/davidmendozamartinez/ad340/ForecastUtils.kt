package com.davidmendozamartinez.ad340

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTempForDisplay(temp: Float, tempDisplaySetting: TempDisplaySetting): String {
    return when (tempDisplaySetting) {
        TempDisplaySetting.Fahrenheit -> String.format("%.2f F°", temp)
        TempDisplaySetting.Celsius -> {
            val celsiusTemp = (temp - 32f) * (5f / 9f)
            String.format("%.2f C°", celsiusTemp)
        }
    }
}

fun showTempDisplaySettingDialog(
    context: Context,
    tempDisplaySettingManager: TempDisplaySettingManager
) {
    val dialogBuilder = AlertDialog.Builder(context)
        .setTitle(R.string.temp_display_setting_dialog_title)
        .setMessage(R.string.temp_display_setting_dialog_message)
        .setPositiveButton("F°") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Fahrenheit)
        }
        .setNeutralButton("C°") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Celsius)
        }
        .setOnDismissListener {
            Toast.makeText(
                context,
                R.string.temp_display_setting_dialog_dismiss_message,
                Toast.LENGTH_SHORT
            ).show()
        }

    dialogBuilder.show()
}