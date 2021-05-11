package com.example.astro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.astrocalculator.*

class MainActivity : AppCompatActivity() {
    private var myViewPager2: ViewPager2? = null
    private var myAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            // do something
        } else {
            myViewPager2 = findViewById(R.id.pager)
            myAdapter = ViewPagerAdapter(this)

            // set Orientation in your ViewPager2
            myViewPager2!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            myViewPager2!!.adapter = myAdapter
        }


    }
}