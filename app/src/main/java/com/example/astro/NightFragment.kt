package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import java.math.BigDecimal
import java.math.RoundingMode

class NightFragment : Fragment() {

    private lateinit var astro : AstroCalculator
    private lateinit var astroDateTime : AstroDateTime
    private lateinit var astroLocation : AstroCalculator.Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.night_fragment,
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
        val moonriseTime = view.findViewById(R.id.moonrise_time) as TextView
        val moonSetTime = view.findViewById(R.id.moonset_time) as TextView
        val newMoon = view.findViewById(R.id.new_moon) as TextView
        val fullMoon = view.findViewById(R.id.full_moon) as TextView
        val moonPhase = view.findViewById(R.id.moon_phase) as TextView
        val moonDay = view.findViewById(R.id.moon_day) as TextView


        moonriseTime.text = getMoonrise()?.let { formatTimeToString(it) }
        moonSetTime.text = getMoonSet()?.let { formatTimeToString(it) }
        newMoon.text = getNewMoon()?.let { formatTimeToString(it) }
        fullMoon.text = getFullMoon()?.let { formatTimeToString(it) }
        moonPhase.text = getMoonPhase()?.let { BigDecimal(it).setScale(3, RoundingMode.HALF_EVEN).toString() }
        moonDay.text = getDay()?.let { BigDecimal(it).setScale(3, RoundingMode.HALF_EVEN).toString() }
    }

    private fun formatTimeToString(astroDateTime: AstroDateTime) : String {
        return String.format("%02d:%02d:%02d", astroDateTime.hour, astroDateTime.minute, astroDateTime.second)
    }


    private fun getMoonrise(): AstroDateTime? {
        return astro.moonInfo?.moonrise
    }

    private fun getMoonSet(): AstroDateTime? {
        return astro.moonInfo?.moonset
    }

    private fun getNewMoon(): AstroDateTime? {
        return astro.moonInfo?.nextNewMoon
    }

    private fun getFullMoon(): AstroDateTime? {
        return astro.moonInfo?.nextFullMoon
    }

    private fun getMoonPhase(): Double? {
        return astro.moonInfo?.illumination
    }

    private fun getDay(): Double? {
        return astro.moonInfo?.age
    }
}
