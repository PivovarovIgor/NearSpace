package ru.brauer.nearspace.presentation.chips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import ru.brauer.nearspace.databinding.FragmentChipsBinding

class ChipsFragment : Fragment() {

    private var _binding: FragmentChipsBinding? = null
    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        fun newInstance() = ChipsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(requireContext(), "chosen ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(requireContext(), "Close is Clicked", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}