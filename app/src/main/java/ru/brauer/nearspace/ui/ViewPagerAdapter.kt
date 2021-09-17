package ru.brauer.nearspace.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.brauer.nearspace.ui.earth.EarthFragment
import ru.brauer.nearspace.ui.main.MainFragment
import ru.brauer.nearspace.ui.mars.MarsFragment
import ru.brauer.nearspace.ui.weather.WeatherFragment

class ViewPagerAdapter(private val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val fragments = arrayOf(
        MainFragment.newInstance(),
        EarthFragment.newInstance(),
        MarsFragment.newInstance(),
        WeatherFragment.newInstance()
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

}