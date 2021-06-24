package com.example.astro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.astrocalculator.*
import com.example.astro.model.Root
import com.example.astro.network.ApiEndpoints
import com.example.astro.network.ServiceBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.*
import kotlinx.android.synthetic.main.day_fragment.*
import kotlinx.android.synthetic.main.main_fragment.*


class MainActivity : AppCompatActivity() {
    private var myViewPager2: ViewPager2? = null
    private var myAdapter: ViewPagerAdapter? = null

    private lateinit var sunFragment: SunFragment
    private lateinit var nightFragment: NightFragment
    private lateinit var mainFragment: MainFragment
    private lateinit var weatherFragment: WeatherFragment
    private lateinit var extraWeatherFragment: ExtraWeatherFragment
    private lateinit var forecastFragment: ForecastFragment

    private var latitude: String = "51"
    private var longitude: String = "21"

    private var units: String = "metric"

    private var refreshRate = 10

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (SharedPrefUtils.getBooleanData(this, "FIRST_RUN")) {
            SharedPrefUtils.saveData(this, "LATITUDE_KEY", "52.2297")
            SharedPrefUtils.saveData(this, "LONGITUDE_KEY", "21.0122")
            SharedPrefUtils.saveData(this, "CITY_NAME", "Warsaw")
            SharedPrefUtils.saveData(this, "REFRESH_KEY", 5)
            SharedPrefUtils.saveData(this, "UNITS", "metric")
            SharedPrefUtils.saveData(this, "FIRST_RUN", false)
        }

        loadData()

        myViewPager2 = findViewById(R.id.pager)
        myAdapter = ViewPagerAdapter(this)

        myViewPager2!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2!!.adapter = myAdapter

        myAdapter!!.addFragment(MainFragment())
        myAdapter!!.addFragment(SunFragment())
        myAdapter!!.addFragment(NightFragment())
        myAdapter!!.addFragment(WeatherFragment())
        myAdapter!!.addFragment(ExtraWeatherFragment())
        myAdapter!!.addFragment(ForecastFragment())

        mainFragment = myAdapter?.getFragment(0) as MainFragment
        sunFragment = myAdapter?.getFragment(1) as SunFragment
        nightFragment = myAdapter?.getFragment(2) as NightFragment
        weatherFragment = myAdapter?.getFragment(3) as WeatherFragment
        extraWeatherFragment = myAdapter?.getFragment(4) as ExtraWeatherFragment
        forecastFragment = myAdapter?.getFragment(5) as ForecastFragment


        updateSettings()
        handler.postDelayed(runnableCode, 1)

    }

    private val runnableCode = object : Runnable {
        override fun run() {
            sunFragment.updateData()
            nightFragment.updateData()
            getWeather()
            handler.postDelayed(this, (refreshRate * 60000).toLong())
        }
    }

    override fun onResume() {
        updateSettings()
        handler.postDelayed(runnableCode, 1)
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnableCode)
        super.onPause()
    }

    fun refreshData() {
        handler.postDelayed(runnableCode, 1)
    }

    fun updateSettings() {
        refreshRate = SharedPrefUtils.getIntData(this, "REFRESH_KEY")
        units = SharedPrefUtils.getStringData(this, "UNITS").toString()
        getWeather()
    }

    private fun getWeather() {
        loadData()
        val request = ServiceBuilder.buildService(ApiEndpoints::class.java)
        val call =
            request.getWeather(latitude, longitude, units, "hourly,minutely,alerts", "ea96da650b548546f840ea1f5a8f13af")
        call.enqueue(object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                val gson = Gson()
                val jsonString = gson.toJson(response.body())
                saveWeatherInfo(jsonString)
                Toast.makeText(this@MainActivity, "Data refreshed", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Toast.makeText(this@MainActivity, "No internet connection. Cannot refresh data.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun saveWeatherInfo(weatherInfo: String) {
        SharedPrefUtils.saveData(this, "WEATHER_INFO", weatherInfo)
    }

    private fun loadData() {
        latitude = SharedPrefUtils.getStringData(this, "LATITUDE_KEY").toString()
        longitude = SharedPrefUtils.getStringData(this, "LONGITUDE_KEY").toString()
        units = SharedPrefUtils.getStringData(this, "UNITS").toString()
    }

}