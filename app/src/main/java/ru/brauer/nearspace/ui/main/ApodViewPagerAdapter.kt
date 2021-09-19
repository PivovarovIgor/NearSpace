package ru.brauer.nearspace.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.brauer.nearspace.util.getBeforeYesterday
import ru.brauer.nearspace.util.getYesterdayDate

class ApodViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        PhotoOfDayFragment.newInstance(getBeforeYesterday()),
        PhotoOfDayFragment.newInstance(getYesterdayDate()),
        PhotoOfDayFragment.newInstance()
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]
}