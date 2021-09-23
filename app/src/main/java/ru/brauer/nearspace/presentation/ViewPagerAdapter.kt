package ru.brauer.nearspace.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.brauer.nearspace.presentation.earth.EarthFragment
import ru.brauer.nearspace.presentation.main.MainFragment
import ru.brauer.nearspace.presentation.mars.MarsFragment
import ru.brauer.nearspace.presentation.weather.WeatherFragment

class ViewPagerAdapter(private val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    val fragments = arrayOf(
        "Picture of day" to MainFragment.newInstance(),
        "Earth" to EarthFragment.newInstance(),
        "Mars" to MarsFragment.newInstance(),
        "Weather" to WeatherFragment.newInstance()
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position].second

    override fun getPageTitle(position: Int): CharSequence = fragments[position].first
}