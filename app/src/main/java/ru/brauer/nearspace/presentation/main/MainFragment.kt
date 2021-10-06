package ru.brauer.nearspace.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.brauer.nearspace.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null

    companion object {

        private const val SAVING_STATE_PAGE = "SAVING_STATE_PAGE"

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPager(savedInstanceState)
    }

    private fun setPager(savedInstanceState: Bundle?) {
        val pagerAdapter = ApodViewPagerAdapter(childFragmentManager)
        binding?.apply {
            pagerPhotoOfDay.adapter = pagerAdapter
            pagerPhotoOfDay.currentItem =
                savedInstanceState?.getInt(SAVING_STATE_PAGE) ?: pagerAdapter.count - 1
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.run {
            outState.putInt(SAVING_STATE_PAGE, pagerPhotoOfDay.currentItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

