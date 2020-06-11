package com.davidmendozamartinez.ad340.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.davidmendozamartinez.ad340.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.databinding.FragmentForecastDetailsBinding
import com.davidmendozamartinez.ad340.formatTempForDisplay

class ForecastDetailsFragment : Fragment() {
    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer { viewState ->
            binding.tempText.text = formatTempForDisplay(
                viewState.temp,
                tempDisplaySettingManager.getTempDisplaySetting()
            )
            binding.descriptionText.text = viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load(viewState.iconUrl)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}