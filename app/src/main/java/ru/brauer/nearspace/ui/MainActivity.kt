package ru.brauer.nearspace.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding
import ru.brauer.nearspace.ui.main.MainFragment
import ru.brauer.nearspace.ui.settings.SettingsStorage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(SettingsStorage(this).theme)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }
}