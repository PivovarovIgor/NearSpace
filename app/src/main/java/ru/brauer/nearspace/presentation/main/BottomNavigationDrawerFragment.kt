package ru.brauer.nearspace.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.BottomNavigationLayoutBinding
import ru.brauer.nearspace.presentation.MainRouterHolder

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    private var mainRouterHolder: MainRouterHolder? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainRouterHolder = context as? MainRouterHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_settings -> {
                    mainRouterHolder?.apply {
                        mainRouter.gotoSettings()
                        dismissAllowingStateLoss()
                    }
                }
                R.id.navigation_notes -> {
                    mainRouterHolder?.apply {
                        mainRouter.gotoNotes()
                        dismissAllowingStateLoss()
                    }
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        mainRouterHolder = null
    }
}