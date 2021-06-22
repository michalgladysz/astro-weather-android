package com.example.astro

import androidx.fragment.app.Fragment
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.util.*

class MainFragment : Fragment() {

    private lateinit var setButton: Button
    private lateinit var setCoordsButton: Button
    private lateinit var spinner: Spinner
    private lateinit var longitude: EditText
    private lateinit var latitude: EditText
    private lateinit var cityName: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.main_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setButton = view.findViewById(R.id.button)
        setCoordsButton = view.findViewById(R.id.button_coords)
        spinner = view.findViewById(R.id.spinner)
        longitude = view.findViewById(R.id.editTextNumberLongitude)
        latitude = view.findViewById(R.id.editTextNumberLatitude)
        cityName = view.findViewById(R.id.edit_text_city_name)

        setButton.setOnClickListener {
            if (latitude.text.toString().toDouble() >= -90 && latitude.text.toString()
                    .toDouble() <= 90 && latitude.text.toString().toDouble() >= -180 && latitude.text.toString()
                    .toDouble() <= 180
            ) {
                val geocoder = Geocoder(activity, Locale.ENGLISH)

                val address = geocoder.getFromLocationName(cityName.text.toString(), 1)
                latitude.setText(address[0].latitude.toString())
                longitude.setText(address[0].longitude.toString())

                saveData()
                Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
                (activity as MainActivity).updateSettings()
            } else Toast.makeText(activity, "Incorrect values!", Toast.LENGTH_LONG).show()
        }


        setCoordsButton.setOnClickListener {
            val geocoder = Geocoder(activity, Locale.ENGLISH)

            val address = geocoder.getFromLocation(latitude.text.toString().toDouble(), longitude.text.toString().toDouble(), 1)
            cityName.setText(address[0].locality)

            saveData()
            Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
            (activity as MainActivity).updateSettings()
        }

        loadData()

       // println("Lat: " + latitude.text.toString().toDouble() + "Long: " + longitude.text.toString().toDouble())

        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveData() {
        activity?.let { SharedPrefUtils.saveData(it, "REFRESH_KEY", Integer.parseInt(spinner.selectedItem.toString())) }
        activity?.let { SharedPrefUtils.saveData(it, "SPINNER_POSITION", spinner.selectedItemPosition) }
        activity?.let { SharedPrefUtils.saveData(it, "LATITUDE_KEY", latitude.text.toString()) }
        activity?.let { SharedPrefUtils.saveData(it, "LONGITUDE_KEY", longitude.text.toString()) }
        activity?.let { SharedPrefUtils.saveData(it, "CITY_NAME", cityName.text.toString()) }
    }

    private fun loadData() {
        val spinnerPosition = activity?.let { SharedPrefUtils.getIntData(it,"SPINNER_POSITION") }
        val latitudeText = activity?.let { SharedPrefUtils.getStringData(it,"LATITUDE_KEY") }
        val longitudeText = activity?.let { SharedPrefUtils.getStringData(it,"LONGITUDE_KEY") }

        if (spinnerPosition != null) {
            spinner.setSelection(spinnerPosition)
        }

        longitude.setText(longitudeText)
        latitude.setText(latitudeText)
    }
}