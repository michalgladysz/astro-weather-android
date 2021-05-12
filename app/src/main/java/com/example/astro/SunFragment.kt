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
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneId
import android.preference.PreferenceManager

import android.content.SharedPreferences
import android.widget.Toast
import java.text.DecimalFormat
import java.text.NumberFormat


class SunFragment : Fragment() {

    private lateinit var astro : AstroCalculator
    private lateinit var astroDateTime : AstroDateTime
    private lateinit var astroLocation : AstroCalculator.Location

    private var longitudeTv : TextView? = null
    private var latitudeTv: TextView? = null
    private var sunriseTime : TextView? = null
    private var sunriseAzimuth : TextView? = null
    private var sunsetTime : TextView? = null
    private var sunsetAzimuth : TextView? = null
    private var duskTime : TextView? = null
    private var twilightTime : TextView? = null

    private var latitudeText : String = "51"
    private var longitudeText : String = "13"
    private var sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId : ZoneId = ZoneId.of("Europe/Warsaw")
    @RequiresApi(Build.VERSION_CODES.O)
    private var localDateTime = LocalDateTime.now(zoneId)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.day_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sunriseTime = view.findViewById(R.id.sunrise_time) as TextView
        sunriseAzimuth = view.findViewById(R.id.sunrise_azimuth) as TextView
        sunsetTime = view.findViewById(R.id.sunset_time) as TextView
        sunsetAzimuth = view.findViewById(R.id.sunset_azimuth) as TextView
        duskTime = view.findViewById(R.id.dusk_time) as TextView
        twilightTime = view.findViewById(R.id.twilight_time) as TextView

        latitudeTv = view.findViewById(R.id.latitudeTv) as TextView
        longitudeTv = view.findViewById(R.id.longitudeTv) as TextView
        loadSharedPreferences()
        updateData()
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onResume() {
        sunriseTime = view?.findViewById(R.id.sunrise_time) as TextView
        sunriseAzimuth = view?.findViewById(R.id.sunrise_azimuth) as TextView
        sunsetTime = view?.findViewById(R.id.sunset_time) as TextView
        sunsetAzimuth = view?.findViewById(R.id.sunset_azimuth) as TextView
        duskTime = view?.findViewById(R.id.dusk_time) as TextView
        twilightTime = view?.findViewById(R.id.twilight_time) as TextView
        latitudeTv = view?.findViewById(R.id.latitudeTv) as TextView
        longitudeTv = view?.findViewById(R.id.longitudeTv) as TextView
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
            true)

        astroLocation = AstroCalculator.Location(latitudeText.toDouble(), longitudeText.toDouble())

        astro = AstroCalculator(astroDateTime, astroLocation)
    }

    private fun updateTextViews(){
        sunriseTime?.text = getSunrise()?.let { formatTimeToString(it) }
        sunriseAzimuth?.text = getAzimuthRise()?.let { reformatDouble(it) }
        sunsetTime?.text = getSunset()?.let { formatTimeToString(it) }
        sunsetAzimuth?.text = getAzimuthSet()?.let { reformatDouble(it) }
        duskTime?.text = getDusk()?.let { formatTimeToString(it) }
        twilightTime?.text = getTwilight()?.let { formatTimeToString(it) }

    }
    private fun reformatDouble(value: Double): String {
        val formatter: NumberFormat = DecimalFormat("#0.00000")
        return formatter.format(value)
    }
     fun loadSharedPreferences() {
        sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        latitudeText = sharedPreferences?.getString("LATITUDE_KEY", "51").toString()
        longitudeText = sharedPreferences?.getString("LONGITUDE_KEY", "13").toString()

        longitudeTv?.text = longitudeText
        latitudeTv?.text = latitudeText
    }

    private fun formatTimeToString(astroDateTime: AstroDateTime) : String {
       return String.format("%02d:%02d:%02d", astroDateTime.hour, astroDateTime.minute, astroDateTime.second)
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