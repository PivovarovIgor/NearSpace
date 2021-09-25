package ru.brauer.nearspace.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.brauer.nearspace.databinding.FragmentMainBinding

const val SAVING_STATE_IS_MAIN = "TAG_IS_MAIN"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var isMain: Boolean = true

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        savedInstanceState?.let {
            isMain = it.getBoolean(SAVING_STATE_IS_MAIN)
        }
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.includingBottomSheet.bottomSheetContainer)
        setPager()
    }

    private fun setPager() {
        val pagerAdapter = ApodViewPagerAdapter(childFragmentManager)
        binding.pagerPhotoOfDay.adapter = pagerAdapter
        binding.pagerPhotoOfDay.currentItem = pagerAdapter.count - 1
        binding.pagerPhotoOfDay.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val photoOfDayFragment = pagerAdapter.getItem(position) as? PhotoOfDayFragment
                photoOfDayFragment?.let {
                    _binding?.includingBottomSheet?.bottomSheetDescriptionHeader?.text =
                        it.photoDescription
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    it.setOnClickListener(object : PhotoOfDayFragment.OnClickListener {
                        override fun onClickOnPhotoOfDay() {
                            bottomSheetBehavior.state =
                                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                                    BottomSheetBehavior.STATE_HIDDEN
                                } else {
                                    BottomSheetBehavior.STATE_EXPANDED
                                }
                        }
                    })
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVING_STATE_IS_MAIN, isMain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

