package com.davidmendozamartinez.ad340.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.davidmendozamartinez.ad340.R
import com.davidmendozamartinez.ad340.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.databinding.FragmentCurrentForecastBinding
import com.davidmendozamartinez.ad340.formatTempForDisplay
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository

class CurrentForecastFragment : Fragment() {
    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CurrentForecastViewModel

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
        viewModel = ViewModelProvider(
            requireActivity(),
            CurrentForecastViewModelFactory(locationRepository, forecastRepository)
        ).get() as CurrentForecastViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        viewModel.savedLocation.observe(viewLifecycleOwner, Observer { savedLocation ->
            when (savedLocation) {
                is Location.ZipCode -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyText.visibility = View.GONE
                    viewModel.loadCurrentForecastInvoked(savedLocation.zipCode)
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            binding.emptyText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.locationName.visibility = View.VISIBLE
            binding.tempText.visibility = View.VISIBLE

            binding.locationName.text = viewState.location
            binding.tempText.text = formatTempForDisplay(
                viewState.temp,
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