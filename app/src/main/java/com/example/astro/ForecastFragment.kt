package com.example.astro

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astro.model.Root
import com.google.gson.Gson
import kotlinx.android.synthetic.main.forecast_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class ForecastFragment : Fragment() {

    private var weatherInfo: Root? = null

    private var cityName: TextView? = null
    private var outdatedInfo: TextView? = null

    private var units: String? = "C"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.forecast_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTv()
        updateData()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = units?.let { weatherInfo?.let { it1 -> ForecastAdapter(it1, it) } }
        }
    }

    private fun updateData() {
        loadSharedPreferences()
        weatherInfo?.let { updateTextViews(it) }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = units?.let { weatherInfo?.let { it1 -> ForecastAdapter(it1, it) } }
        }
    }

    override fun onResume() {
        initTv()
        updateData()
        super.onResume()
    }

    private fun updateTextViews(weatherInfo: Root) {
        val dateString = getLastUpdateDt(weatherInfo)

        if (!isInternetConnected()) {
            outdatedInfo?.text = "No internet connection, last update: $dateString"
        } else outdatedInfo?.text = "Updated: $dateString"
    }

    private fun initTv() {
        cityName = view?.findViewById(R.id.city_name) as TextView
        outdatedInfo = view?.findViewById(R.id.outdatedInfo) as TextView
    }

    private fun loadSharedPreferences() {
        val weatherInfoJson = activity?.let { SharedPrefUtils.getStringData(it, "WEATHER_INFO").toString() }
        cityName?.text = activity?.let { SharedPrefUtils.getStringData(it, "CITY_NAME").toString() + " forecast" }
        units = activity?.let { SharedPrefUtils.getStringData(it, "UNITS") }
        units = if (units == "metric") "C" else "F"
        val gson = Gson()
        weatherInfo = gson.fromJson(weatherInfoJson, Root::class.java)
    }

    private fun isInternetConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun getLastUpdateDt(weatherInfo: Root): String {
        val formatter = SimpleDateFormat("hh:mm:ss, d/MM/yyyy");
        return formatter.format(Date(weatherInfo.current.dt * 1000))
    }
}
