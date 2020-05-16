package com.davidmendozamartinez.ad340.forecast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidmendozamartinez.ad340.*
import com.davidmendozamartinez.ad340.details.ForecastDetailsActivity

class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipCode = arguments!!.getString(KEY_ZIP_CODE) ?: ""

        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) { forecast ->
            showForecastDetails(forecast)
        }
        forecastList.adapter = dailyForecastAdapter

        forecastRepository.weaklyForecast.observe(this, Observer { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        })

        forecastRepository.loadForecast(zipCode)

        return view
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp)
        forecastDetailsIntent.putExtra("key_description", forecast.description)
        startActivity(forecastDetailsIntent)
    }

    companion object {
        const val KEY_ZIP_CODE = "key_zip_code"

        fun newInstance(zipCode: String): CurrentForecastFragment {
            val fragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIP_CODE, zipCode)
            fragment.arguments = args

            return fragment
        }
    }
}