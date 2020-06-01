package com.davidmendozamartinez.ad340.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidmendozamartinez.ad340.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WeeklyForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipCode = arguments?.getString(KEY_ZIP_CODE) ?: ""

        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val dailyForecastList: RecyclerView = view.findViewById(R.id.dailyForecastList)
        dailyForecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastListAdapter(tempDisplaySettingManager) { forecast ->
            showForecastDetails(forecast)
        }
        dailyForecastList.adapter = dailyForecastAdapter

        forecastRepository.weaklyForecast.observe(viewLifecycleOwner, Observer { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        })

        forecastRepository.loadForecast(zipCode)

        return view
    }

    private fun showLocationEntry() {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                forecast.temp,
                forecast.description
            )
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIP_CODE = "key_zip_code"

        fun newInstance(zipCode: String): WeeklyForecastFragment {
            val fragment = WeeklyForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIP_CODE, zipCode)
            fragment.arguments = args

            return fragment
        }
    }
}