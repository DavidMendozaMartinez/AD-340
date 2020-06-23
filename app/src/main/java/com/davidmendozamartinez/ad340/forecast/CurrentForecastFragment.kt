package com.davidmendozamartinez.ad340.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmendozamartinez.ad340.*
import com.davidmendozamartinez.ad340.databinding.FragmentCurrentForecastBinding

class CurrentForecastFragment : Fragment() {
    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository(getString(R.string.language_code))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.ZipCode -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyText.visibility = View.GONE
                    forecastRepository.loadCurrentForecast(savedLocation.zipCode)
                }
            }
        })

        forecastRepository.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            binding.emptyText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.locationName.visibility = View.VISIBLE
            binding.tempText.visibility = View.VISIBLE

            binding.locationName.text = weather.name
            binding.tempText.text = formatTempForDisplay(
                weather.forecast.temp,
                tempDisplaySettingManager.getTempDisplaySetting()
            )
        })
    }

    private fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }
}