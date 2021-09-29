package ru.brauer.nearspace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            LayoutInflater.from(this@MainActivity)
        )
    }

    private val router: Router by lazy { Router(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            router.initNavigation()
        } else {

            savedInstanceState.getString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT)
                ?.let { router.state = it }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            binding.bottomNavigationView.getBadge(item.itemId)?.let {
                binding.bottomNavigationView.removeBadge(item.itemId)
            }
            when (item.itemId) {
                R.id.bottom_view_photo_of_day -> {
                    router.gotoPhotoOfDay()
                    true
                }
                R.id.bottom_view_earth -> {
                    router.gotoEarth()
                    true
                }
                R.id.bottom_view_mars -> {
                    router.gotoMars()
                    true
                }
                R.id.bottom_view_weather -> {
                    router.gotoWeather()
                    true
                }
                R.id.bottom_view_more -> {
                    router.showBottomNavigationDrawer()
                    false
                }
                else -> false
            }
        }

        binding.bottomNavigationView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_photo_of_day -> {
                    true
                }
                R.id.bottom_view_earth -> {
                    true
                }
                R.id.bottom_view_mars -> {
                    true
                }
                R.id.bottom_view_weather -> {
                    true
                }
                R.id.bottom_view_more -> {
                    true
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT, router.state)
    }
}