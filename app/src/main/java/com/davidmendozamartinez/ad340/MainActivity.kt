package com.davidmendozamartinez.ad340

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidmendozamartinez.ad340.details.ForecastDetailsActivity

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipCodeEditText: EditText = findViewById(R.id.zipCodeEditText)
        val enterButton: Button = findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val zipCode: String = zipCodeEditText.text.toString()

            if (zipCode.length != 5) {
                Toast.makeText(this, R.string.zip_code_entry_error, Toast.LENGTH_SHORT).show()
            } else {
                forecastRepository.loadForecast(zipCode)
            }
        }

        val forecastList: RecyclerView = findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(this)
        val dailyForecastAdapter = DailyForecastAdapter { forecast ->
            showForecastDetails(forecast)
        }
        forecastList.adapter = dailyForecastAdapter

        forecastRepository.weaklyForecast.observe(this, Observer { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        })
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val forecastDetailsIntent = Intent(this, ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp)
        forecastDetailsIntent.putExtra("key_description", forecast.description)
        startActivity(forecastDetailsIntent)
    }
}
