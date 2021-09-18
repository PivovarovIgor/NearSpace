package ru.brauer.nearspace.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        binding.indicator.setViewPager(binding.viewPager)
    }
}