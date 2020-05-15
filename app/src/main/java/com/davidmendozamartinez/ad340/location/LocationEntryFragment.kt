package com.davidmendozamartinez.ad340.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.davidmendozamartinez.ad340.R

/**
 * A simple [Fragment] subclass.
 */
class LocationEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)

        val zipCodeEditText: EditText = view.findViewById(R.id.zipCodeEditText)
        val enterButton: Button = view.findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val zipCode: String = zipCodeEditText.text.toString()

            if (zipCode.length != 5) {
                Toast.makeText(requireContext(), R.string.zip_code_entry_error, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "ZIP Code Entered", Toast.LENGTH_SHORT).show()
                //forecastRepository.loadForecast(zipCode)
            }
        }

        return view
    }


}
