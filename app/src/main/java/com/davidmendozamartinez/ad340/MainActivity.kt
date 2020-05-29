package com.davidmendozamartinez.ad340

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.davidmendozamartinez.ad340.forecast.CurrentForecastFragmentDirections
import com.davidmendozamartinez.ad340.location.LocationEntryFragmentDirections

class MainActivity : AppCompatActivity(), AppNavigator {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempDisplaySettingManager = TempDisplaySettingManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tempDisplaySetting -> {
                showTempDisplaySettingDialog(this, tempDisplaySettingManager)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun navigateToCurrentForecast(zipCode: String) {
        val action =
            LocationEntryFragmentDirections.actionLocationEntryFragmentToCurrentForecastFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateToLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}