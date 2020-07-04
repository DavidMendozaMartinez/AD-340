package com.davidmendozamartinez.ad340.forecast.current

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
import com.davidmendozamartinez.ad340.databinding.FragmentCurrentForecastBinding
import com.davidmendozamartinez.ad340.repository.ForecastRepository
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository
import com.davidmendozamartinez.ad340.util.TempDisplaySettingManager

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
            CurrentForecastViewModelFactory(tempDisplaySettingManager, forecastRepository)
        ).get() as CurrentForecastViewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { location ->
            when (location) {
                is Location.ZipCode -> viewModel.onLocationObtained(location.zipCode)
                else -> viewModel.onLocationError()
            }
        })
    }

    private fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }
}