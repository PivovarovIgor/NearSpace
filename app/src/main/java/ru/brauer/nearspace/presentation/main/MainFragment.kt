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

const val SAVING_STATE_PAGE = "SAVING_STATE_PAGE"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.includingBottomSheet.bottomSheetContainer)
        setPager(savedInstanceState)
    }

    private fun setPager(savedInstanceState: Bundle?) {
        val pagerAdapter = ApodViewPagerAdapter(childFragmentManager)
        binding.pagerPhotoOfDay.adapter = pagerAdapter
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
                    it.showDescriptionOfPhoto = { description ->
                        _binding?.includingBottomSheet?.bottomSheetDescriptionHeader?.text = description
                    }
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
        binding.pagerPhotoOfDay.currentItem =
            savedInstanceState?.getInt(SAVING_STATE_PAGE) ?: pagerAdapter.count - 1
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVING_STATE_PAGE, binding.pagerPhotoOfDay.currentItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

