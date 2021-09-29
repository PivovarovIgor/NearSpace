package ru.brauer.nearspace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainRouterHolder {

    companion object {
        private const val TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            LayoutInflater.from(this@MainActivity)
        )
    }

    override val mainRouter: MainRouter by lazy { MainRouter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            mainRouter.initNavigation()
        } else {
            savedInstanceState.getString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT)
                ?.let { mainRouter.state = it }
        }
        binding.bottomNavigationView.selectedItemId
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            selectOnBottomNavigation(item)
        }

        binding.bottomNavigationView.setOnItemReselectedListener { item ->
            selectOnBottomNavigation(item)
        }
    }

    private fun selectOnBottomNavigation(item: MenuItem) = when (item.itemId) {
        R.id.bottom_view_photo_of_day -> {
            mainRouter.gotoPhotoOfDay()
            true
        }
        R.id.bottom_view_earth -> {
            mainRouter.gotoEarth()
            true
        }
        R.id.bottom_view_mars -> {
            mainRouter.gotoMars()
            true
        }
        R.id.bottom_view_weather -> {
            mainRouter.gotoWeather()
            true
        }
        R.id.bottom_view_more -> {
            mainRouter.showBottomNavigationDrawer()
            false
        }
        else -> false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT, mainRouter.state)
    }
}