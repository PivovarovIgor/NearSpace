package ru.brauer.nearspace.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.brauer.nearspace.R
import ru.brauer.nearspace.presentation.earth.EarthFragment
import ru.brauer.nearspace.presentation.main.BottomNavigationDrawerFragment
import ru.brauer.nearspace.presentation.main.MainFragment
import ru.brauer.nearspace.presentation.mars.MarsFragment
import ru.brauer.nearspace.presentation.weather.WeatherFragment

class Router(private val fragmentManager: FragmentManager) {

    private var activeFragment: Fragment = MainFragment.newInstance()
        set(value) {
            fragmentManager
                .beginTransaction()
                .hide(field)
                .show(value)
                .commitNow()
            field = value
        }
    var state: String
        get() = activeFragment::class.java.name
        set(value) {
            activeFragment =
                requireNotNull(fragmentManager.findFragmentByTag(value))
        }

    fun initNavigation() {
        fragmentManager
            .beginTransaction()
            .addAndHide(activeFragment)
            .addAndHide(EarthFragment.newInstance())
            .addAndHide(MarsFragment.newInstance())
            .addAndHide(WeatherFragment.newInstance())
            .commitNow()
    }

    fun gotoPhotoOfDay() {
        MainFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoEarth() {
        EarthFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoMars() {
        MarsFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoWeather() {
        WeatherFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun showBottomNavigationDrawer() {
        BottomNavigationDrawerFragment().show(fragmentManager, "tag")
    }

    private fun FragmentTransaction.addAndHide(fragment: Fragment): FragmentTransaction =
        this.add(R.id.activity_bottom_container, fragment, fragment::class.java.name)
            .hide(fragment)

    private fun String.findFragment(): Fragment =
        requireNotNull(fragmentManager.findFragmentByTag(this))
}