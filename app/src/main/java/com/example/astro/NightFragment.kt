package com.example.astro

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import kotlinx.android.synthetic.main.day_fragment.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.ZoneId

class NightFragment : Fragment() {

    private lateinit var astro: AstroCalculator
    private lateinit var astroDateTime: AstroDateTime
    private lateinit var astroLocation: AstroCalculator.Location

    private var longitudeTv: TextView? = null
    private var latitudeTv: TextView? = null

    private var moonriseTime: TextView? = null
    private var moonSetTime: TextView? = null
    private var newMoon: TextView? = null
    private var fullMoon: TextView? = null
    private var moonPhase: TextView? = null
    private var moonDay: TextView? = null

    private var latitudeText: String = "52"
    private var longitudeText: String = "21"

    private var sharedPreferences = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId: ZoneId = ZoneId.of("Europe/Warsaw")

    @RequiresApi(Build.VERSION_CODES.O)
    private var localDateTime = LocalDateTime.now(zoneId)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.night_fragment,
            container, false
        )
    }


    private fun initTv() {
        moonriseTime = view?.findViewById(R.id.moonrise_time) as TextView
        moonSetTime = view?.findViewById(R.id.moonset_time) as TextView
        newMoon = view?.findViewById(R.id.new_moon) as TextView
        fullMoon = view?.findViewById(R.id.full_moon) as TextView
        moonPhase = view?.findViewById(R.id.moon_phase) as TextView
        moonDay = view?.findViewById(R.id.moon_day) as TextView
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
        moonriseTime?.text = getMoonrise()?.let { formatTimeToString(it) }
        moonSetTime?.text = getMoonSet()?.let { formatTimeToString(it) }
        newMoon?.text = getNewMoon()?.let { formatDateToString(it) }
        fullMoon?.text = getFullMoon()?.let { formatDateToString(it) }
        moonPhase?.text = getMoonPhase()?.let { formatDouble(it) }
        moonDay?.text = getDay()?.let { formatDouble(it) }

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

    private fun formatDateToString(astroDateTime: AstroDateTime): String {
        return String.format("%02d.%02d.%04d", astroDateTime.day, astroDateTime.month, astroDateTime.year)
    }

    private fun formatDouble(value: Double): String {
        val format: NumberFormat = DecimalFormat("#0.0000")
        return format.format(value)
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
