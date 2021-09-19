package ru.brauer.nearspace.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.FragmentMainBinding
import ru.brauer.nearspace.ui.BottomActivity
import ru.brauer.nearspace.ui.MainActivity
import ru.brauer.nearspace.ui.settings.SettingsFragment

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.includingBottomSheet.bottomSheetContainer)
        setBottomAppBar(view)
        setPager()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> activity?.let {
                startActivity(Intent(it, BottomActivity::class.java))
            }
            R.id.app_bar_settings -> activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val cont = activity as MainActivity
        cont.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        with(binding) {
            fab.setOnClickListener {
                if (isMain) {
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_back_fab
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.app_bar_other_screen)
                } else {
                    bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        requireNotNull(context),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_plus_fab
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                }
                isMain = !isMain
            }
        }
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

