package com.example.astro

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astro.model.Root
import com.google.gson.Gson
import kotlin.math.roundToInt
import kotlinx.android.synthetic.main.activity_main.*



class WeatherFragment : Fragment() {

    private var weatherDescription: TextView? = null
    private var temperature: TextView? = null
    private var minTemperature: TextView? = null
    private var maxTemperature: TextView? = null
    private var feelsLikeTemperature: TextView? = null
    private var cityName: TextView? = null
    private var pressure: TextView? = null
    private var humidity: TextView? = null
    private var latitude: TextView? = null
    private var longitude: TextView? = null

    private var units : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.weather_fragment,
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
        weatherDescription = view?.findViewById(R.id.weather_description) as TextView
        temperature = view?.findViewById(R.id.temperature) as TextView
        minTemperature = view?.findViewById(R.id.min_temperature) as TextView
        maxTemperature = view?.findViewById(R.id.max_temperature) as TextView
        feelsLikeTemperature = view?.findViewById(R.id.feels_like_temperature) as TextView
        cityName = view?.findViewById(R.id.city_name) as TextView
        pressure = view?.findViewById(R.id.pressure) as TextView
        humidity = view?.findViewById(R.id.humidity) as TextView
        latitude = view?.findViewById(R.id.latitudeTv) as TextView
        longitude = view?.findViewById(R.id.longitudeTv) as TextView
    }

    private fun updateTextViews(weatherInfo : Root) {

        temperature!!.text = String.format("%d°$units", weatherInfo.current.temp.roundToInt())
        feelsLikeTemperature!!.text = String.format("%d°$units", weatherInfo.current.feels_like.roundToInt())
        weatherDescription!!.text =  weatherInfo.current.weather[0].description.toUpperCase()
        maxTemperature!!.text = String.format("%d°$units", weatherInfo.daily[0].temp.max.roundToInt())
        minTemperature!!.text = String.format("%d°$units", weatherInfo.daily[0].temp.min.roundToInt())
        pressure?.text = weatherInfo.current.pressure.toString() + " hPa"
        humidity?.text =  weatherInfo.current.humidity.toString() + "%"

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            when (weatherInfo.current.weather[0].icon) {
                "01d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_clear_sun_100,0,0)
                "02d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sun_100,0,0)
                "03d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
                "04d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
                "09d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_rain_100,0,0)
                "10d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_rain_100,0,0)
                "11d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_storm_100,0,0)
                "13d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_snow_100,0, 0)
                "50d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
            }
        } else {
            when (weatherInfo.current.weather[0].icon) {
                "01d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_sun_50,0, 0,0)
                "02d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sun_50,0,0, 0)
                "03d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_50,0,0, 0)
                "04d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_50,0,0, 0)
                "09d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rain_50,0,0, 0)
                "10d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rain_50,0,0, 0)
                "11d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_storm_50,0,0, 0)
                "13d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_snow_50,0, 0, 0)
                "50d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_50,0,0, 0)
            }
        }

    }

    private fun updateData() {
        val weatherInfo = loadSharedPreferences()
        updateTextViews(weatherInfo)
    }

    private fun loadSharedPreferences() : Root {
        val weatherInfoJson = activity?.let { SharedPrefUtils.getStringData(it,"WEATHER_INFO") }
        cityName?.text = activity?.let { SharedPrefUtils.getStringData(it, "CITY_NAME") + " weather" }
        latitude?.text = activity?.let { SharedPrefUtils.getStringData(it, "LATITUDE_KEY")}
        longitude?.text =  activity?.let { SharedPrefUtils.getStringData(it, "LONGITUDE_KEY")}
        units = activity?.let { SharedPrefUtils.getStringData(it, "UNITS") }
        units = if (units == "metric") "C" else "F"
        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }

}