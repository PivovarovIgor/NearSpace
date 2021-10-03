package ru.brauer.nearspace.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.brauer.nearspace.R
import ru.brauer.nearspace.presentation.earth.EarthFragment
import ru.brauer.nearspace.presentation.main.BottomNavigationDrawerFragment
import ru.brauer.nearspace.presentation.main.MainFragment
import ru.brauer.nearspace.presentation.mars.MarsFragment
import ru.brauer.nearspace.presentation.notes.NotesFragment
import ru.brauer.nearspace.presentation.settings.SettingsFragment
import ru.brauer.nearspace.presentation.weather.WeatherFragment

class MainRouter(private val fragmentManager: FragmentManager) {

    companion object {
        private const val TAG_BOTTOM_NAVIGATION = "TAG_BOTTOM_NAVIGATION"
    }

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
        gotoPhotoOfDay()
    }

    fun gotoPhotoOfDay() {
        fragmentManager.popBackStack()
        MainFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoEarth() {
        fragmentManager.popBackStack()
        EarthFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoMars() {
        fragmentManager.popBackStack()
        MarsFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoWeather() {
        fragmentManager.popBackStack()
        WeatherFragment::class.java.name
            .findFragment()
            .let { activeFragment = it }
    }

    fun gotoSettings() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.activity_bottom_container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun gotoNotes() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.activity_bottom_container, NotesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun showBottomNavigationDrawer() {
        BottomNavigationDrawerFragment().show(fragmentManager, TAG_BOTTOM_NAVIGATION)
    }

    private fun FragmentTransaction.addAndHide(fragment: Fragment): FragmentTransaction =
        this.add(R.id.activity_bottom_container, fragment, fragment::class.java.name)
            .hide(fragment)

    private fun String.findFragment(): Fragment =
        requireNotNull(fragmentManager.findFragmentByTag(this))
}