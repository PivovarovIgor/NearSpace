package ru.brauer.nearspace.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.snackbar.Snackbar
import ru.brauer.nearspace.databinding.FragmentPhotoOfDayBinding
import ru.brauer.nearspace.domain.entities.Apod

private const val KEY_DATE = "date"
private const val MEDIA_TYPE_VIDEO = "video"

class PhotoOfDayFragment : Fragment() {

    private var binding: FragmentPhotoOfDayBinding? = null

    private val viewModel: PhotoOfDayViewModel by activityViewModels()

    var photoDescription: String = ""
        private set

    private var date: Long? = null

    private var clickListenerHolder: OnClickListener? = null

    companion object {

        fun newInstance(date: Long? = null): PhotoOfDayFragment =
            PhotoOfDayFragment().apply {
                arguments = date?.let {
                    Bundle().apply { putLong(KEY_DATE, it) }
                }
            }
    }

    fun setOnClickListener(clickListener: OnClickListener?) {
        clickListenerHolder = clickListener
        binding?.apply {
            astronomyPictureOfTheDey.setOnClickListener {
                clickListenerHolder?.onClickOnPhotoOfDay()
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
        }
        viewModel.observe(viewLifecycleOwner,
            object : PhotoOfDayViewModel.RenderData {
                override fun renderData(appState: PhotoOfDayAppState?) {
                    this@PhotoOfDayFragment.renderData(appState)
                }
            },
            date)
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
            photoDescription = apod.photoDescription
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        clickListenerHolder = null
    }

    interface OnClickListener {
        fun onClickOnPhotoOfDay()
    }
}