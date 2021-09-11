package ru.brauer.nearspace.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.BottomNavigationLayoutBinding

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                R.id.navigation_two -> Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}