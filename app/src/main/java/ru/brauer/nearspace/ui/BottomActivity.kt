package ru.brauer.nearspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityBottomBinding
import ru.brauer.nearspace.ui.earth.EarthFragment
import ru.brauer.nearspace.ui.mars.MarsFragment
import ru.brauer.nearspace.ui.weather.WeatherFragment


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
        EarthFragment.newInstance().show()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> EarthFragment.newInstance().show()
                R.id.bottom_view_mars -> MarsFragment.newInstance().show()
                R.id.bottom_view_weather -> WeatherFragment.newInstance().show()
                else -> false
            }
        }

        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    true
                }
                R.id.bottom_view_mars -> {
                    true
                }
                R.id.bottom_view_weather -> {
                    true
                }
            }
        }
    }
}