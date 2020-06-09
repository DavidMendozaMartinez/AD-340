package com.davidmendozamartinez.ad340.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.davidmendozamartinez.ad340.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.databinding.FragmentForecastDetailsBinding
import com.davidmendozamartinez.ad340.formatTempForDisplay
import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")

class ForecastDetailsFragment : Fragment() {
    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ForecastDetailsFragmentArgs by navArgs()
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        binding.forecastIcon.load("http://openweathermap.org/img/wn/${args.iconId}@2x.png")
        binding.tempText.text =
            formatTempForDisplay(args.temp, tempDisplaySettingManager.getTempDisplaySetting())
        binding.descriptionText.text = args.description
        binding.dateText.text = DATE_FORMAT.format(Date(args.date * 1000))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}