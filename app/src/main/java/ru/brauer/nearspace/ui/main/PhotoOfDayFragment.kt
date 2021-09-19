package ru.brauer.nearspace.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.nearspace.databinding.FragmentPhotoOfDayBinding
import ru.brauer.nearspace.domain.repository.Repository
import ru.brauer.nearspace.domain.repository.RepositoryImpl
import ru.brauer.nearspace.domain.repository.dto.ApodDTO
import ru.brauer.nearspace.util.toFormate

private const val KEY_DATE = "date"
private const val MEDIA_TYPE_VIDEO = "video"

class PhotoOfDayFragment : Fragment() {

    private var binding: FragmentPhotoOfDayBinding? = null

    private val repository: Repository by lazy { RepositoryImpl() }
    var photoDescription: String = ""
        private set

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

        getApod(arguments?.getLong(KEY_DATE))
    }

    private fun getApod(date: Long?) {
        repository.getApod(date?.toFormate("yyyy-MM-dd"),
            object : Callback<ApodDTO> {
                override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                    val serverResponse = response.body()
                    if (response.isSuccessful && serverResponse != null) {
                        binding?.let {
                            it.astronomyPictureOfTheDey.contentDescription = serverResponse.title
                            if (serverResponse.mediaType == MEDIA_TYPE_VIDEO
                                && serverResponse.url != null
                            ) {
                                it.showVideo.visibility = View.VISIBLE
                                it.showVideo.loadUrl(serverResponse.url)
                            } else {
                                it.showVideo.visibility = View.GONE
                                it.astronomyPictureOfTheDey.load(serverResponse.url)
                            }
                            photoDescription = serverResponse.explanation ?: ""
                        }
                    } else {
                        showMessageAndToRepeat(response.message())
                    }
                }

                override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                    showMessageAndToRepeat(t.message ?: "Undefine problem.")
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
                                    repository.getApod(null, this)
                                }
                        }
                    }
                }
            })
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