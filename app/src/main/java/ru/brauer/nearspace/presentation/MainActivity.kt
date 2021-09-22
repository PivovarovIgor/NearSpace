package ru.brauer.nearspace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.ActivityMainBinding
import ru.brauer.nearspace.presentation.settings.SettingsStorage

private const val PHOTO_OF_DAY = 0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewPageAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(SettingsStorage(this).theme)
        setContentView(binding.root)

        binding.viewPager.adapter = viewPageAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setHighLightedTab(PHOTO_OF_DAY)
        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setHighLightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setHighLightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(this) ?: return
        viewPageAdapter.fragments.forEachIndexed { index, _ ->
            binding.tabLayout.getTabAt(index)?.customView = null
            val viewTab = layoutInflater.inflate(getIdLayout(index), null)
            if (position == index) {
                viewTab.findViewById<AppCompatTextView>(R.id.tab_image_textview)
                    .setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color_accent))
            }
            binding.tabLayout.getTabAt(index)?.customView = viewTab
        }
    }

    private fun getIdLayout(position: Int) =
        when (position) {
            1 -> R.layout.activity_custom_tab_earth
            2 -> R.layout.activity_custom_tab_mars
            3 -> R.layout.activity_custom_tab_weather
            else -> R.layout.activity_custom_tab_picture_of_day
        }
}