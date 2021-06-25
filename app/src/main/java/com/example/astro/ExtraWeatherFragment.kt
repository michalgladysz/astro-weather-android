package com.example.astro

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astro.model.Root
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class ExtraWeatherFragment : Fragment() {

    private var cityName: TextView? = null
    private var windSpeed: TextView? = null
    private var windDirection: TextView? = null
    private var cloudiness: TextView? = null
    private var rainVolume: TextView? = null
    private var visibility: TextView? = null
    private var outdatedInfo: TextView? = null

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
        updateData()
        super.onResume()
    }

    private fun initTv() {
        windSpeed = view?.findViewById(R.id.wind_speed) as TextView
        windDirection = view?.findViewById(R.id.wind_direction) as TextView
        cloudiness = view?.findViewById(R.id.cloudiness) as TextView
        rainVolume = view?.findViewById(R.id.rain_volume) as TextView
        visibility = view?.findViewById(R.id.visibility) as TextView
        cityName = view?.findViewById(R.id.city_name) as TextView
        outdatedInfo = view?.findViewById(R.id.outdatedInfo) as TextView
    }

    private fun updateTextViews(weatherInfo: Root) {
        windSpeed?.text = weatherInfo.current.wind_speed.toString() + " " + units
        windDirection?.text = weatherInfo.current.wind_deg.toString()
        cloudiness?.text = weatherInfo.current.clouds.toString() + "%"
        rainVolume?.text = weatherInfo.daily[0].rain.toString() + " mm"
        visibility?.text = weatherInfo.current.visibility.toString() + " m"

        val dateString = getLastUpdateDt(weatherInfo)

        if (!isInternetConnected()) {
            outdatedInfo?.text = "No internet connection, last update: $dateString"
        } else outdatedInfo?.text = "Updated: $dateString"
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

    private fun isInternetConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun getLastUpdateDt(weatherInfo : Root) : String {
        val formatter = SimpleDateFormat("hh:mm:ss, d/MM/yyyy");
        return formatter.format(Date(weatherInfo.current.dt * 1000))
    }
}