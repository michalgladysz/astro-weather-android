package com.example.astro

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.astrocalculator.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.text.DecimalFormat
import java.text.NumberFormat


class SunFragment : Fragment() {

    private lateinit var astro: AstroCalculator
    private lateinit var astroDateTime: AstroDateTime
    private lateinit var astroLocation: AstroCalculator.Location

    private var longitudeTv: TextView? = null
    private var latitudeTv: TextView? = null
    private var sunriseTime: TextView? = null
    private var sunriseAzimuth: TextView? = null
    private var sunsetTime: TextView? = null
    private var sunsetAzimuth: TextView? = null
    private var duskTime: TextView? = null
    private var twilightTime: TextView? = null

    private var latitudeText: String = "52"
    private var longitudeText: String = "21"
    private var sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId: ZoneId = ZoneId.of("Europe/Warsaw")

    @RequiresApi(Build.VERSION_CODES.O)
    private var localDateTime = LocalDateTime.now(zoneId)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.day_fragment,
            container, false
        )
    }

    private fun initTv() {
        sunriseTime = view?.findViewById(R.id.sunrise_time) as TextView
        sunriseAzimuth = view?.findViewById(R.id.sunrise_azimuth) as TextView
        sunsetTime = view?.findViewById(R.id.sunset_time) as TextView
        sunsetAzimuth = view?.findViewById(R.id.sunset_azimuth) as TextView
        duskTime = view?.findViewById(R.id.dusk_time) as TextView
        twilightTime = view?.findViewById(R.id.twilight_time) as TextView
        latitudeTv = view?.findViewById(R.id.latitudeTv) as TextView
        longitudeTv = view?.findViewById(R.id.longitudeTv) as TextView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTv()
        loadSharedPreferences()
        updateData()
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onResume() {
        initTv()
        loadSharedPreferences()
        updateData()
        super.onResume()
    }


    fun updateData() {
        updateAstroCalculator()
        updateTextViews()
    }

    private fun updateAstroCalculator() {
        localDateTime = LocalDateTime.now(zoneId)

        astroDateTime = AstroDateTime(
            localDateTime.year,
            localDateTime.monthValue,
            localDateTime.dayOfMonth,
            localDateTime.hour,
            localDateTime.minute,
            localDateTime.second,
            1,
            true
        )

        astroLocation = AstroCalculator.Location(latitudeText.toDouble(), longitudeText.toDouble())

        astro = AstroCalculator(astroDateTime, astroLocation)
    }

    private fun updateTextViews() {
        sunriseTime?.text = getSunrise()?.let { formatTimeToString(it) }
        sunriseAzimuth?.text = getAzimuthRise()?.let { formatDouble(it) }
        sunsetTime?.text = getSunset()?.let { formatTimeToString(it) }
        sunsetAzimuth?.text = getAzimuthSet()?.let { formatDouble(it) }
        duskTime?.text = getDusk()?.let { formatTimeToString(it) }
        twilightTime?.text = getTwilight()?.let { formatTimeToString(it) }

    }


    fun loadSharedPreferences() {
        sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        latitudeText = sharedPreferences?.getString("LATITUDE_KEY", "52").toString()
        longitudeText = sharedPreferences?.getString("LONGITUDE_KEY", "21").toString()

        longitudeTv?.text = longitudeText
        latitudeTv?.text = latitudeText
    }

    private fun formatTimeToString(astroDateTime: AstroDateTime): String {
        return String.format("%02d:%02d:%02d", astroDateTime.hour, astroDateTime.minute, astroDateTime.second)
    }

    private fun formatDouble(value: Double): String {
        val format: NumberFormat = DecimalFormat("#0.00000")
        return format.format(value)
    }

    private fun getSunrise(): AstroDateTime? {
        return astro.sunInfo?.sunrise
    }

    private fun getSunset(): AstroDateTime? {
        return astro.sunInfo?.sunset
    }

    private fun getAzimuthRise(): Double? {
        return astro.sunInfo?.azimuthRise
    }

    private fun getAzimuthSet(): Double? {
        return astro.sunInfo?.azimuthSet
    }

    private fun getDusk(): AstroDateTime? {
        return astro.sunInfo?.twilightMorning
    }

    private fun getTwilight(): AstroDateTime? {
        return astro.sunInfo?.twilightEvening
    }
}