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

class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private val forecastRepository = ForecastRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val dailyForecastList: RecyclerView = view.findViewById(R.id.dailyForecastList)
        dailyForecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastListAdapter(tempDisplaySettingManager) { forecast ->
            showForecastDetails(forecast)
        }
        dailyForecastList.adapter = dailyForecastAdapter

        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository = LocationRepository(requireContext())
        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.ZipCode -> forecastRepository.loadCurrentForecast(savedLocation.zipCode)
            }
        })

        forecastRepository.currentForecast.observe(viewLifecycleOwner, Observer { forecastItem ->
            dailyForecastAdapter.submitList(listOf(forecastItem))
        })

        return view
    }

    private fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(
                forecast.temp,
                forecast.description
            )
        findNavController().navigate(action)
    }
}