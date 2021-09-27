package ru.brauer.nearspace.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.brauer.nearspace.databinding.FragmentMainBinding

const val SAVING_STATE_PAGE = "SAVING_STATE_PAGE"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


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
        setPager(savedInstanceState)
    }

    private fun setPager(savedInstanceState: Bundle?) {
        val pagerAdapter = ApodViewPagerAdapter(childFragmentManager)
        binding.pagerPhotoOfDay.adapter = pagerAdapter
        binding.pagerPhotoOfDay.currentItem =
            savedInstanceState?.getInt(SAVING_STATE_PAGE) ?: pagerAdapter.count - 1
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

