package ru.brauer.nearspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding
import ru.brauer.nearspace.ui.settings.SettingsStorage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(SettingsStorage(this).theme)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setCustomTabs()

        binding.indicator.setViewPager(binding.viewPager)
    }

    private fun setCustomTabs() {
        val layoutInfl = LayoutInflater.from(this)
        binding.tabLayout.getTabAt(0)?.customView =
            layoutInfl.inflate(R.layout.activity_custom_tab_picture_of_day, null)
        binding.tabLayout.getTabAt(1)?.customView =
            layoutInfl.inflate(R.layout.activity_custom_tab_earth, null)
        binding.tabLayout.getTabAt(2)?.customView =
            layoutInfl.inflate(R.layout.activity_custom_tab_mars, null)
        binding.tabLayout.getTabAt(3)?.customView =
            layoutInfl.inflate(R.layout.activity_custom_tab_weather, null)
    }
}