package ru.brauer.nearspace.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.nearspace.databinding.FragmentMainBinding
import ru.brauer.nearspace.domain.repository.RepositoryImpl
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

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

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        val repository = RepositoryImpl()
        repository.getApod(object : Callback<ApodDTO> {
            override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                val serverResponse = response.body()
                if (response.isSuccessful && serverResponse != null) {
                    _binding?.let {
                        it.astronomyPictureOfTheDey.load(serverResponse.url)
                        it.includingBottomSheet.bottomSheetDescriptionHeader.text =
                            serverResponse.explanation
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                } else {
                    showMessageAndToRepeat(response.message())
                }
            }

            override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                showMessageAndToRepeat(t.message ?: "Undefine problem.")
            }

            private fun showMessageAndToRepeat(message: String) {
                context?.let {
                    Snackbar.make(
                        it,
                        binding.root,
                        message,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Repeat") {
                            repository.getApod(this)
                        }
                }
            }
        })
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}