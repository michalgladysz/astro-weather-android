package com.example.astro

import android.content.Context
import androidx.fragment.app.Fragment
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*
import android.widget.AdapterView
import java.math.BigDecimal


class MainFragment : Fragment() {

    private lateinit var setButton: Button
    private lateinit var setCoordsButton: Button
    private lateinit var refreshButton: Button
    private lateinit var addToFavButton: Button
    private lateinit var removeFromFavButton: Button
    private lateinit var toggleButton: ToggleButton
    private lateinit var spinner: Spinner
    private lateinit var spinnerFavCities: Spinner
    private lateinit var longitude: EditText
    private lateinit var latitude: EditText
    private lateinit var cityName: EditText

    private lateinit var adapter : ArrayAdapter<String>
    private var favoriteCities = mutableSetOf("Warsaw")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.main_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setButton = view.findViewById(R.id.button)
        setCoordsButton = view.findViewById(R.id.button_coords)
        refreshButton = view.findViewById(R.id.button_refresh)
        addToFavButton = view.findViewById(R.id.button_add_to_fav)
        removeFromFavButton = view.findViewById(R.id.button_remove_from_fav)
        toggleButton = view.findViewById(R.id.toggleButton)
        spinner = view.findViewById(R.id.spinner)
        spinnerFavCities = view.findViewById(R.id.spinner_fav_cities)
        longitude = view.findViewById(R.id.editTextNumberLongitude)
        latitude = view.findViewById(R.id.editTextNumberLatitude)
        cityName = view.findViewById(R.id.edit_text_city_name)

        adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                favoriteCities.toMutableList()
            )
        }!!

        spinnerFavCities.adapter = adapter

        setButton.setOnClickListener {
            if (isInternetConnected()) {

                setCoordsFromLocationName()

                saveData()
                Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
                (activity as MainActivity).updateSettings()
            } else Toast.makeText(activity, "No internet connection, new setting cannot be set", Toast.LENGTH_LONG).show()
        }


        setCoordsButton.setOnClickListener {
            if (isInternetConnected()) {
                if (latitude.text.toString().toDouble() >= -90 && latitude.text.toString()
                        .toDouble() <= 90 && latitude.text.toString().toDouble() >= -180 && latitude.text.toString()
                        .toDouble() <= 180
                ) {

                    setLocationFromCoords()

                    saveData()
                    Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
                    (activity as MainActivity).updateSettings()
                } else Toast.makeText(activity, "Incorrect values!", Toast.LENGTH_LONG).show()
            } else Toast.makeText(activity, "No internet connection, new setting cannot be set", Toast.LENGTH_LONG).show()
        }

        refreshButton.setOnClickListener {
            (activity as MainActivity).refreshData()
        }

        addToFavButton.setOnClickListener {
            favoriteCities.add(cityName.text.toString())
            updateSavedCitiesSpinner()
            saveData()
        }

        removeFromFavButton.setOnClickListener {
            favoriteCities.remove(cityName.text.toString())
            updateSavedCitiesSpinner()
            saveData()
        }

        var iCurrentSelection: Int = spinnerFavCities.selectedItemPosition

        spinnerFavCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isInternetConnected()) {
                    if (iCurrentSelection != position){
                        cityName.setText(spinnerFavCities.selectedItem.toString())

                        setCoordsFromLocationName()

                        saveData()
                        Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
                        (activity as MainActivity).updateSettings()
                    }
                    iCurrentSelection = position
                } else Toast.makeText(activity, "No internet connection, new setting cannot be set", Toast.LENGTH_LONG).show()
            }
        }

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isInternetConnected()) {
                if (isChecked) {
                    activity?.let { SharedPrefUtils.saveData(it, "UNITS", "imperial") }
                } else {
                    activity?.let { SharedPrefUtils.saveData(it, "UNITS", "metric") }
                }
                activity?.let { SharedPrefUtils.saveData(it, "UNITS_TOGGLE_POSITION", toggleButton.isChecked) }
                (activity as MainActivity).updateSettings()
                (activity as MainActivity).refreshData()
            } else Toast.makeText(activity, "No internet connection, new setting cannot be set", Toast.LENGTH_LONG).show()
        }

        loadData()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveData() {
        activity?.let { SharedPrefUtils.saveData(it, "REFRESH_KEY", Integer.parseInt(spinner.selectedItem.toString())) }
        activity?.let { SharedPrefUtils.saveData(it, "SPINNER_POSITION", spinner.selectedItemPosition) }
        activity?.let { SharedPrefUtils.saveData(it, "LATITUDE_KEY", latitude.text.toString()) }
        activity?.let { SharedPrefUtils.saveData(it, "LONGITUDE_KEY", longitude.text.toString()) }
        activity?.let { SharedPrefUtils.saveData(it, "CITY_NAME", cityName.text.toString()) }
        activity?.let { SharedPrefUtils.saveData(it, "FAVORITE_CITIES",java.lang.String.join(",", favoriteCities)) }
    }

    private fun loadData() {
        val spinnerPosition = activity?.let { SharedPrefUtils.getIntData(it,"SPINNER_POSITION") }
        val latitudeText = activity?.let { SharedPrefUtils.getStringData(it,"LATITUDE_KEY") }
        val longitudeText = activity?.let { SharedPrefUtils.getStringData(it,"LONGITUDE_KEY") }
        val cityNameSp = activity?.let { SharedPrefUtils.getStringData(it,"CITY_NAME") }
        val isCheckedToggle = activity?.let { SharedPrefUtils.getBooleanData(it,"UNITS_TOGGLE_POSITION") }
        val favoriteCitiesString = activity?.let { SharedPrefUtils.getStringData(it,"FAVORITE_CITIES") }

        if (favoriteCitiesString != null) {
            val list = favoriteCitiesString.split(",")
            favoriteCities = list.toMutableSet()
        }

        if (spinnerPosition != null) {
            spinner.setSelection(spinnerPosition)
        }

        if (isCheckedToggle != null) {
            toggleButton.isChecked = isCheckedToggle
        }

        longitude.setText(longitudeText)
        latitude.setText(latitudeText)
        cityName.setText(cityNameSp)

        updateSavedCitiesSpinner()
    }

    private fun updateSavedCitiesSpinner() {
        adapter.clear()
        adapter.addAll(favoriteCities)
        adapter.notifyDataSetChanged()
    }

    private fun setCoordsFromLocationName() {
        val geocoder = Geocoder(activity, Locale.ENGLISH)

        val address = geocoder.getFromLocationName(cityName.text.toString(), 1)

        var lat = BigDecimal(address[0].latitude)
        lat = lat.setScale(4, BigDecimal.ROUND_HALF_UP)

        var lon = BigDecimal(address[0].longitude)
        lon = lon.setScale(4, BigDecimal.ROUND_HALF_UP)

        latitude.setText(lat.toString())
        longitude.setText(lon.toString())
    }

    private fun setLocationFromCoords() {
        val geocoder = Geocoder(activity, Locale.ENGLISH)

        val address = geocoder.getFromLocation(latitude.text.toString().toDouble(), longitude.text.toString().toDouble(), 1)
        cityName.setText(address[0].locality)
    }

    private fun isInternetConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}