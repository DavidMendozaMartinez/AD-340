package com.davidmendozamartinez.ad340.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davidmendozamartinez.ad340.Location
import com.davidmendozamartinez.ad340.LocationRepository
import com.davidmendozamartinez.ad340.R

class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationRepository = LocationRepository(requireContext())

        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)

        val zipCodeEditText: EditText = view.findViewById(R.id.zipCodeEditText)
        val enterButton: Button = view.findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val zipCode: String = zipCodeEditText.text.toString()

            if (zipCode.length != 5) {
                Toast.makeText(requireContext(), R.string.zip_code_entry_error, Toast.LENGTH_SHORT)
                    .show()
            } else {
                locationRepository.saveLocation(Location.ZipCode(zipCode))
                findNavController().navigateUp()
            }
        }

        return view
    }
}
