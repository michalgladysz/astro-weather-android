package com.example.astro

import android.content.Context
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


class ForecastFragment : Fragment() {

    private lateinit var weatherInfo: Root

    private var cityName: TextView? = null

    private var units: String? = "C"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.forecast_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateData()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = units?.let { ForecastAdapter(weatherInfo, it) }
        }
        initTv()
    }

    private fun updateData() {
        loadSharedPreferences()
        recyclerView.apply {
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        initTv()
        loadSharedPreferences()
        super.onResume()
    }

    private fun initTv() {
        cityName = view?.findViewById(R.id.city_name) as TextView
    }

    private fun loadSharedPreferences() {
        val weatherInfoJson = activity?.let { SharedPrefUtils.getStringData(it, "WEATHER_INFO").toString() }
        cityName?.text = activity?.let { SharedPrefUtils.getStringData(it, "CITY_NAME").toString() + " forecast" }
        units = activity?.let { SharedPrefUtils.getStringData(it, "UNITS") }
        units = if (units == "metric") "C" else "F"
        val gson = Gson()
        weatherInfo = gson.fromJson(weatherInfoJson, Root::class.java)
    }

}