package ru.brauer.nearspace.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.nearspace.R
import ru.brauer.nearspace.databinding.FragmentMainBinding
import ru.brauer.nearspace.domain.repository.Repository
import ru.brauer.nearspace.domain.repository.RepositoryImpl
import ru.brauer.nearspace.domain.repository.dto.ApodDTO
import ru.brauer.nearspace.ui.MainActivity
import ru.brauer.nearspace.ui.chips.ChipsFragment
import ru.brauer.nearspace.ui.settings.SettingsFragment
import ru.brauer.nearspace.util.getBeforeYesterday
import ru.brauer.nearspace.util.getYesterdayDate
import ru.brauer.nearspace.util.toFormate

const val SAVING_STATE_IS_MAIN = "TAG_IS_MAIN"
private const val MEDIA_TYPE_VIDEO = "video"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var isMain: Boolean = true
    private val repository: Repository by lazy { RepositoryImpl() }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        savedInstanceState?.let {
            isMain = it.getBoolean(SAVING_STATE_IS_MAIN)
        }

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.includingBottomSheet.bottomSheetContainer)
        setBottomAppBar(view)
        setChoiceDate(view)

        binding.showVideo.webViewClient = MyWebViewClient()
        binding.showVideo.settings.javaScriptEnabled = true

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        binding.astronomyPictureOfTheDey.setOnClickListener {
            bottomSheetBehavior.state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    BottomSheetBehavior.STATE_HIDDEN
                } else {
                    BottomSheetBehavior.STATE_EXPANDED
                }
        }

        getApod()
    }

    private fun getApod(date: Long? = null) {
        repository.getApod(date?.toFormate("yyyy-MM-dd"),
            object : Callback<ApodDTO> {
                override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                    val serverResponse = response.body()
                    if (response.isSuccessful && serverResponse != null) {
                        _binding?.let {
                            it.astronomyPictureOfTheDey.contentDescription = serverResponse.title
                            if (serverResponse.mediaType == MEDIA_TYPE_VIDEO
                                && serverResponse.url != null) {
                                it.showVideo.visibility = View.VISIBLE
                                it.showVideo.loadUrl(serverResponse.url)
                            } else {
                                it.showVideo.visibility = View.GONE
                                it.astronomyPictureOfTheDey.load(serverResponse.url)
                            }
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
                                repository.getApod(null, this)
                            }
                    }
                }
            })
    }

    private fun setChoiceDate(view: View) {
        with(binding) {
            if (choiceDate.checkedChipId == View.NO_ID) {
                choiceDate.check(R.id.choice_date_today)
            }
            choiceDate.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.choice_date_today -> getApod()
                    R.id.choice_date_yesterday -> getApod(getYesterdayDate())
                    R.id.choice_date_before_yesterday -> getApod(getBeforeYesterday())
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context, R.string.favourite, Toast.LENGTH_LONG)
                .show()
            R.id.app_bar_settings -> activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val cont = activity as MainActivity
        cont.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        with(binding) {
            fab.setOnClickListener {
                if (isMain) {
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_back_fab
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.app_bar_other_screen)
                } else {
                    bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        requireNotNull(context),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_plus_fab
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                }
                isMain = !isMain
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVING_STATE_IS_MAIN, isMain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return true
        }

        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?
        ): Boolean {
            return super.onRenderProcessGone(view, detail)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            super.onUnhandledKeyEvent(view, event)
        }
    }
}

