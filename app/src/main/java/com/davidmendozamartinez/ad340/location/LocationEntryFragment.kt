package com.davidmendozamartinez.ad340.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davidmendozamartinez.ad340.R
import com.davidmendozamartinez.ad340.databinding.FragmentLocationEntryBinding
import com.davidmendozamartinez.ad340.repository.Location
import com.davidmendozamartinez.ad340.repository.LocationRepository

class LocationEntryFragment : Fragment() {
    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)
        locationRepository = LocationRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterButton.setOnClickListener {
            val zipCode: String = binding.zipCodeEditText.text.toString()

            if (zipCode.length != 5) {
                Toast.makeText(requireContext(), R.string.zip_code_entry_error, Toast.LENGTH_SHORT)
                    .show()
            } else {
                locationRepository.saveLocation(Location.ZipCode(zipCode))
                findNavController().navigateUp()
            }
        }
    }
}
