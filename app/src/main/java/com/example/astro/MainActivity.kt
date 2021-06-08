package com.example.astro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.astrocalculator.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private var myViewPager2: ViewPager2? = null
    private var myAdapter: ViewPagerAdapter? = null

    private lateinit var sunFragment: SunFragment
    private lateinit var nightFragment: NightFragment
    private lateinit var mainFragment: MainFragment
    private lateinit var weatherFragment: WeatherFragment
    private lateinit var forecastFragment: ForecastFragment

    private var refreshRate = 10
    private var tabletSize by Delegates.notNull<Boolean>()

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            mainFragment = supportFragmentManager.findFragmentById(R.id.fragment0) as MainFragment
            sunFragment = supportFragmentManager.findFragmentById(R.id.fragment1) as SunFragment
            nightFragment = supportFragmentManager.findFragmentById(R.id.fragment2) as NightFragment

        } else {
            myViewPager2 = findViewById(R.id.pager)
            myAdapter = ViewPagerAdapter(this)

            myViewPager2!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            myViewPager2!!.adapter = myAdapter

            myAdapter!!.addFragment(MainFragment())
            myAdapter!!.addFragment(SunFragment())
            myAdapter!!.addFragment(NightFragment())
            myAdapter!!.addFragment(WeatherFragment())
            myAdapter!!.addFragment(ForecastFragment())

            mainFragment = myAdapter?.getFragment(0) as MainFragment
            sunFragment = myAdapter?.getFragment(1) as SunFragment
            nightFragment = myAdapter?.getFragment(2) as NightFragment
            weatherFragment = myAdapter?.getFragment(3) as WeatherFragment
            forecastFragment = myAdapter?.getFragment(4) as ForecastFragment


        }
        updateSettings()
        handler.postDelayed(runnableCode, (refreshRate * 1000).toLong())

    }

    private val runnableCode = object : Runnable {
        override fun run() {
            Toast.makeText(this@MainActivity, "Data refreshed", Toast.LENGTH_SHORT).show()
            sunFragment.updateData()
            nightFragment.updateData()
            handler.postDelayed(this, (refreshRate * 1000).toLong())
        }
    }

    override fun onResume() {
        updateSettings()
        handler.postDelayed(runnableCode, (refreshRate * 1000).toLong())
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnableCode)
        super.onPause()
    }

    fun updateSettings() {
        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        refreshRate = sharedPreferences.getInt("REFRESH_KEY", 1)
        if (tabletSize) {
            sunFragment.loadSharedPreferences()
            sunFragment.updateData()
            nightFragment.loadSharedPreferences()
            nightFragment.updateData()
        }
//        println("set pushed $refreshRate")
    }

}