package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astro.model.Root
import com.google.gson.Gson

class ExtraWeatherFragment : Fragment() {

    private var cityName: TextView? = null
    private var windSpeed: TextView? = null
    private var windDirection: TextView? = null
    private var cloudiness: TextView? = null
    private var rainVolume: TextView? = null
    private var visibility: TextView? = null

    private var units: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.extra_weather_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTv()
        updateData()
    }

    override fun onResume() {
        initTv()
        val weatherInfo = loadSharedPreferences()
        updateTextViews(weatherInfo)
        super.onResume()
    }

    private fun initTv() {
        windSpeed = view?.findViewById(R.id.wind_speed) as TextView
        windDirection = view?.findViewById(R.id.wind_direction) as TextView
        cloudiness = view?.findViewById(R.id.cloudiness) as TextView
        rainVolume = view?.findViewById(R.id.rain_volume) as TextView
        visibility = view?.findViewById(R.id.visibility) as TextView
        cityName = view?.findViewById(R.id.city_name) as TextView
    }

    private fun updateTextViews(weatherInfo: Root) {
        windSpeed?.text = weatherInfo.current.wind_speed.toString() + " " + units
        windDirection?.text = weatherInfo.current.wind_deg.toString()
        cloudiness?.text = weatherInfo.current.clouds.toString() + "%"
        rainVolume?.text = weatherInfo.daily[0].rain.toString() + " mm"
        visibility?.text = weatherInfo.current.visibility.toString() + " m"
    }

    private fun updateData() {
        val weatherInfo = loadSharedPreferences()
        updateTextViews(weatherInfo)
    }

    private fun loadSharedPreferences(): Root {
        val weatherInfoJson = activity?.let { SharedPrefUtils.getStringData(it, "WEATHER_INFO") }
        units = activity?.let { SharedPrefUtils.getStringData(it, "UNITS") }
        units = if (units == "metric") "m/s" else "mph"
        cityName?.text = activity?.let { SharedPrefUtils.getStringData(it, "CITY_NAME") + " weather" }
        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }

}