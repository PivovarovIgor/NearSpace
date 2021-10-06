package ru.brauer.nearspace.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.FragmentPhotoOfDayBinding
import ru.brauer.nearspace.domain.entities.Apod

class PhotoOfDayFragment : Fragment() {

    private var binding: FragmentPhotoOfDayBinding? = null

    private val viewModel: PhotoOfDayViewModel by activityViewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var date: Long? = null

    companion object {

        private const val KEY_DATE = "date"
        private const val MEDIA_TYPE_VIDEO = "video"

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

            astronomyPictureOfTheDey.setOnLongClickListener {
                it as AppCompatImageView
                TransitionManager.beginDelayedTransition(
                    root,
                    TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeImageTransform())
                )

                it.scaleType =
                    if (it.scaleType == ImageView.ScaleType.CENTER_CROP) {
                        ImageView.ScaleType.FIT_CENTER
                    } else {
                        ImageView.ScaleType.CENTER_CROP
                    }
                true
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
                binding?.apply {
                    showVideo.hide()
                    astronomyPictureOfTheDey.hide()
                    errorMessage.hide()
                }
            }
            is PhotoOfDayAppState.Error -> {
                binding?.apply {
                    errorMessage.text = photoOfDayAppState.exception.message ?: "Undefine problem."
                    errorMessage.show()
                }
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
            ) {
                showVideo.show()
                showVideo.loadUrl(apod.url)
            } else {
                astronomyPictureOfTheDey.show()
                astronomyPictureOfTheDey.load(apod.url) {
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_photo_size_select_actual_24)
                }
            }
            includingBottomSheet.bottomSheetDescriptionHeader.text =
                apod.photoDescription
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

    private fun View.hide() {
        visibility = View.GONE
    }

    private fun View.show() {
        visibility = View.VISIBLE
    }
}
