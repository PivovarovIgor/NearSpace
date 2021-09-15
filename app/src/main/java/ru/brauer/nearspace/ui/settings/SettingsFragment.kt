package ru.brauer.nearspace.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val settingsStorage = SettingsStorage(it)
            when (settingsStorage.theme) {
                R.style.Theme_NearSpace -> binding.choiceThemeDefault.isChecked = true
                R.style.Theme_NearSpace_Martian -> binding.choiceThemeMartian.isChecked = true
            }
            binding.choiceTheme.setOnCheckedChangeListener { group, checkedId ->
                val themeId = if (checkedId == R.id.choice_theme_martian) {
                    R.style.Theme_NearSpace_Martian
                } else {
                    R.style.Theme_NearSpace
                }
                if (themeId != settingsStorage.theme) {
                    settingsStorage.theme = themeId
                    it.setTheme(themeId)
                    it.recreate()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}