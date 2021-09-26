package ru.brauer.nearspace.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import ru.brauer.nearspace.databinding.FragmentPhotoOfDayBinding
import ru.brauer.nearspace.domain.entities.Apod

private const val KEY_DATE = "date"
private const val MEDIA_TYPE_VIDEO = "video"

class PhotoOfDayFragment : Fragment() {

    private var binding: FragmentPhotoOfDayBinding? = null

    private val viewModel: PhotoOfDayViewModel by activityViewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var date: Long? = null

    companion object {

        fun newInstance(date: Long? = null): PhotoOfDayFragment =
            PhotoOfDayFragment().apply {
                arguments = date?.let {
                    Bundle().apply { putLong(KEY_DATE, it) }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoOfDayBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = arguments?.getLong(KEY_DATE)
        binding?.apply {

            setBottomSheetBehavior(includingBottomSheet.bottomSheetContainer)

            showVideo.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }
            }
            showVideo.settings.javaScriptEnabled = true

            astronomyPictureOfTheDey.setOnClickListener {
                bottomSheetBehavior.state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        BottomSheetBehavior.STATE_COLLAPSED
                    } else {
                        BottomSheetBehavior.STATE_EXPANDED
                    }
            }
        }
        viewModel.observe(
            viewLifecycleOwner,
            object : PhotoOfDayViewModel.RenderData {
                override fun renderData(appState: PhotoOfDayAppState?) {
                    this@PhotoOfDayFragment.renderData(appState)
                }
            },
            date
        )
    }

    private fun renderData(photoOfDayAppState: PhotoOfDayAppState?) {
        when (photoOfDayAppState) {
            is PhotoOfDayAppState.Loading -> {

            }
            is PhotoOfDayAppState.Error -> {
                showMessageAndToRepeat(photoOfDayAppState.exception.message ?: "Undefine problem.")
            }
            is PhotoOfDayAppState.Success -> {
                showPhotoOfDay(photoOfDayAppState.apod)
            }
        }
    }

    private fun showPhotoOfDay(apod: Apod) {
        binding?.apply {
            astronomyPictureOfTheDey.contentDescription = apod.title
            if (apod.mediaType == MEDIA_TYPE_VIDEO
                && apod.url != null
            ) {
                showVideo.visibility = View.VISIBLE
                showVideo.loadUrl(apod.url)
            } else {
                showVideo.visibility = View.GONE
                astronomyPictureOfTheDey.load(apod.url)
            }
            includingBottomSheet.bottomSheetDescriptionHeader.text =
                apod.photoDescription
        }
    }


    private fun showMessageAndToRepeat(message: String) {
        context?.let { contextNitNull ->
            binding?.let { bindingNotNull ->
                Snackbar.make(
                    contextNitNull,
                    bindingNotNull.root,
                    message,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Repeat") {
                        viewModel.getPhotoOfDay(date)
                    }
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}