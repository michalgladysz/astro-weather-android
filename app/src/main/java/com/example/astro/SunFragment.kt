package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.astrocalculator.*
import java.math.BigDecimal
import java.math.RoundingMode

class SunFragment : Fragment() {

    private lateinit var astro : AstroCalculator
    private lateinit var astroDateTime : AstroDateTime
    private lateinit var astroLocation : AstroCalculator.Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.day_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        astroDateTime = AstroDateTime(2021,5,5,22,10,10,24,true)
        astroLocation = AstroCalculator.Location(5.0, 3.0)

        astro = AstroCalculator(astroDateTime, astroLocation)

        updateTextViews(view)

    }

    private fun updateTextViews(view : View){
        val sunriseTime = view.findViewById(R.id.sunrise_time) as TextView
        val sunriseAzimuth = view.findViewById(R.id.sunrise_azimuth) as TextView
        val sunsetTime = view.findViewById(R.id.sunset_time) as TextView
        val sunsetAzimuth = view.findViewById(R.id.sunset_azimuth) as TextView
        val duskTime = view.findViewById(R.id.dusk_time) as TextView
        val twilightTime = view.findViewById(R.id.twilight_time) as TextView


        sunriseTime.text = getSunrise()?.let { formatTimeToString(it) }
        sunriseAzimuth.text = getAzimuthRise()?.let { BigDecimal(it).setScale(3, RoundingMode.HALF_EVEN).toString() }
        sunsetTime.text = getSunset()?.let { formatTimeToString(it) }
        sunsetAzimuth.text = getAzimuthSet()?.let { BigDecimal(it).setScale(3, RoundingMode.HALF_EVEN).toString() }
        duskTime.text = getDusk()?.let { formatTimeToString(it) }
        twilightTime.text = getTwilight()?.let { formatTimeToString(it) }
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