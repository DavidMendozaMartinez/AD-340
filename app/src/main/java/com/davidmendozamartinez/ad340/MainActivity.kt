package com.davidmendozamartinez.ad340

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.davidmendozamartinez.ad340.forecast.CurrentForecastFragmentDirections
import com.davidmendozamartinez.ad340.location.LocationEntryFragmentDirections

class MainActivity : AppCompatActivity(), AppNavigator {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempDisplaySettingManager = TempDisplaySettingManager(this)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
        )
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

    override fun navigateToForecastDetails(forecast: DailyForecast) {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(
                forecast.temp,
                forecast.description
            )
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}