package com.example.astro

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    private lateinit var setButton : Button
    private lateinit var spinner : Spinner
    private lateinit var longitude: EditText
    private lateinit var latitude: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.main_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setButton = view.findViewById(R.id.button)
        spinner = view.findViewById(R.id.spinner)
        longitude = view.findViewById(R.id.editTextNumberLongitude)
        latitude = view.findViewById(R.id.editTextNumberLatitude)

        setButton.setOnClickListener {
            saveData()
            Toast.makeText(activity, "New settings set", Toast.LENGTH_LONG).show()
            (activity as MainActivity).updateSettings()
        }

        loadData()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveData() {
        val sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE) ?: return
        with (sharedPreferences.edit()) {
            val value = Integer.parseInt(spinner.selectedItem.toString())
            putInt("REFRESH_KEY", value)
            putInt("SPINNER_POSITION", spinner.selectedItemPosition)
            putString("LATITUDE_KEY", latitude.text.toString())
            putString("LONGITUDE_KEY", longitude.text.toString())
            apply()
        }
    }

    private fun loadData() {
        val sharedPreferences = activity!!.applicationContext.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val spinnerPosition = sharedPreferences?.getInt("SPINNER_POSITION", 0)
        val latitudeText = sharedPreferences?.getString("LATITUDE_KEY", "51")
        val longitudeText = sharedPreferences?.getString("LONGITUDE_KEY", "13")

        if (spinnerPosition != null) {
            spinner.setSelection(spinnerPosition)
        }
        longitude.setText(longitudeText)
        latitude.setText(latitudeText)
    }



}