package ru.brauer.nearspace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityBottomBinding
import ru.brauer.nearspace.presentation.earth.EarthFragment
import ru.brauer.nearspace.presentation.main.BottomNavigationDrawerFragment
import ru.brauer.nearspace.presentation.main.MainFragment
import ru.brauer.nearspace.presentation.mars.MarsFragment
import ru.brauer.nearspace.presentation.weather.WeatherFragment

class BottomActivity : AppCompatActivity() {

    private fun Fragment.show(): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_bottom_container, this)
            .commitAllowingStateLoss()
        return true
    }

    private val binding: ActivityBottomBinding by lazy {
        ActivityBottomBinding.inflate(
            LayoutInflater.from(this@BottomActivity)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MainFragment.newInstance().show()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            binding.bottomNavigationView.getBadge(item.itemId)?.let {
                binding.bottomNavigationView.removeBadge(item.itemId)
            }
            when (item.itemId) {
                R.id.bottom_view_photo_of_day -> MainFragment.newInstance().show()
                R.id.bottom_view_earth -> EarthFragment.newInstance().show()
                R.id.bottom_view_mars -> MarsFragment.newInstance().show()
                R.id.bottom_view_weather -> WeatherFragment.newInstance().show()
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
}