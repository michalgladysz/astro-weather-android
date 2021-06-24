package com.example.astro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size;
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
