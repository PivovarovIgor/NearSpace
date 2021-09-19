package ru.brauer.nearspace.ui

import android.os.Bundle
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
        binding.indicator.setViewPager(binding.viewPager)
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_baseline_photo_size_select_actual_24)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_earth)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_mars)
        binding.tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_system)
    }
}