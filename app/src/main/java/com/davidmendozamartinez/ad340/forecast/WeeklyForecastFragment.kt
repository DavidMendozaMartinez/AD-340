package com.davidmendozamartinez.ad340.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmendozamartinez.ad340.*
import com.davidmendozamartinez.ad340.api.DailyForecast
import com.davidmendozamartinez.ad340.databinding.FragmentWeeklyForecastBinding

class WeeklyForecastFragment : Fragment() {
    private var _binding: FragmentWeeklyForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private val forecastRepository = ForecastRepository(getString(R.string.language_code))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeeklyForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val dailyForecastAdapter = DailyForecastListAdapter(tempDisplaySettingManager) { forecast ->
            showForecastDetails(forecast)
        }
        binding.dailyForecastList.adapter = dailyForecastAdapter

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.ZipCode -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyText.visibility = View.GONE
                    forecastRepository.loadWeeklyForecast(savedLocation.zipCode)
                }
            }
        })

        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, Observer { weeklyForecast ->
            binding.emptyText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        })
    }

    private fun showLocationEntry() {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val iconId = forecast.weather[0].icon
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date

        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                iconId,
                temp,
                date,
                description
            )
        findNavController().navigate(action)
    }
}