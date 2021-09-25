package ru.brauer.nearspace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding
import ru.brauer.nearspace.presentation.earth.EarthFragment
import ru.brauer.nearspace.presentation.main.BottomNavigationDrawerFragment
import ru.brauer.nearspace.presentation.main.MainFragment
import ru.brauer.nearspace.presentation.mars.MarsFragment
import ru.brauer.nearspace.presentation.weather.WeatherFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            LayoutInflater.from(this@MainActivity)
        )
    }

    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .addAndHide(MainFragment.newInstance())
                .addAndHide(EarthFragment.newInstance())
                .addAndHide(MarsFragment.newInstance())
                .addAndHide(WeatherFragment.newInstance())
                .commitNow()
        }

        activeFragment = requireNotNull(
            supportFragmentManager.findFragmentByTag(
                savedInstanceState?.getString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT)
                    ?: MainFragment::class.java.name
            )
        )
        supportFragmentManager
            .beginTransaction()
            .show(activeFragment)
            .commitNow()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            binding.bottomNavigationView.getBadge(item.itemId)?.let {
                binding.bottomNavigationView.removeBadge(item.itemId)
            }
            when (item.itemId) {
                R.id.bottom_view_photo_of_day -> reactivateFragment(MainFragment::class.java.name)
                R.id.bottom_view_earth -> reactivateFragment(EarthFragment::class.java.name)
                R.id.bottom_view_mars -> reactivateFragment(MarsFragment::class.java.name)
                R.id.bottom_view_weather -> reactivateFragment(WeatherFragment::class.java.name)
                R.id.bottom_view_more -> {
                    BottomNavigationDrawerFragment().show(supportFragmentManager, "tag")
                    false
                }
                else -> false
            }
        }

        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
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
        outState.putString(TAG_SAVE_STATE_TAG_ACTIVE_FRAGMENT, activeFragment::class.java.name)
    }

    private fun reactivateFragment(tag: String): Boolean {
        supportFragmentManager
            .findFragmentByTag(tag)
            ?.let {
                supportFragmentManager
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(it)
                    .commitNow()
                activeFragment = it
            }
        return true
    }

    private fun FragmentTransaction.addAndHide(fragment: Fragment): FragmentTransaction =
        this.add(R.id.activity_bottom_container, fragment, fragment::class.java.name)
            .hide(fragment)
}