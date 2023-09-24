package com.guap.vkr.playlistmaker.search.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivitySearchBinding
import com.guap.vkr.playlistmaker.player.ui.activity.PlayerActivity
import com.guap.vkr.playlistmaker.search.domain.model.TrackSearchModel
import com.guap.vkr.playlistmaker.search.ui.model.ScreenState
import com.guap.vkr.playlistmaker.search.ui.TracksAdapter
import com.guap.vkr.playlistmaker.search.ui.view_model.SearchViewModel
import com.guap.vkr.playlistmaker.utils.TRACK

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<TrackSearchModel>()
    private val tracksHistory = ArrayList<TrackSearchModel>()
    private val searchAdapter = TracksAdapter(tracks) { trackClickListener(it) }
    private val historyAdapter = TracksAdapter(tracksHistory) { trackClickListener(it) }
    private val handler = Handler(Looper.getMainLooper())
    private var userInput = ""
    private var clickAllowed = true
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        viewModel = ViewModelProvider(
                this, SearchViewModel.getViewModelFactory(applicationContext)
        )[SearchViewModel::class.java]

        viewModel.stateLiveData().observe(this) {
            updateScreen(it)
        }

        binding.apply {
            rvSearchResult.adapter = searchAdapter
            rvHistory.adapter = historyAdapter
        }

        buttonsConfig()
        queryInputConfig(initTextWatcher())
    }

    private fun initTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.apply {
                btnClear.visibility = clearButtonVisibility(s)
                placeholderSearchHistory.visibility = View.GONE
            }

            userInput = s.toString()
            viewModel.searchDebounce(userInput, false)
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    private fun buttonsConfig() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }

            btnClear.setOnClickListener {
                etSearch.setText("")
                hideKeyboard()
                tracks.clear()
                viewModel.getTracksHistory()
                searchAdapter.notifyDataSetChanged()
            }

            btnClearHistory.setOnClickListener {
                viewModel.clearHistory()
                historyAdapter.notifyDataSetChanged()
                placeholderSearchHistory.visibility = View.GONE
            }

            btnRefresh.setOnClickListener {
                viewModel.searchDebounce(userInput, true)
            }
        }
    }

    private fun queryInputConfig(textWatcher: TextWatcher) {
        binding.etSearch.apply {
            addTextChangedListener(textWatcher)
            setText(userInput)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && this.text.isEmpty())
                    viewModel.getTracksHistory()
                else
                    binding.placeholderSearchHistory.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(USER_INPUT, userInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInput = savedInstanceState.getString(USER_INPUT, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun trackClickListener(track: TrackSearchModel) {
        if (isClickAllowed()) {
            viewModel.addTrackToHistory(track)
            val playIntent =
                    Intent(this, PlayerActivity::class.java).putExtra(TRACK, Gson().toJson(track))
            startActivity(playIntent)
        }
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    private fun updateScreen(state: ScreenState) {
        binding.apply {
            when (state) {
                is ScreenState.Content -> {
                    tracks.clear()
                    tracks.addAll(state.tracks as ArrayList<TrackSearchModel>)
                    progressBar.visibility = View.GONE
                    rvSearchResult.visibility = View.VISIBLE
                    errorContainer.visibility = View.GONE
                    searchAdapter.notifyDataSetChanged()
                }

                is ScreenState.Error -> {
                    progressBar.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                    ivErrorMessage.setImageResource(R.drawable.ic_internet_err)
                    tvErrorMessage.setText(R.string.error_network_faild)
                    btnRefresh.visibility = View.VISIBLE
                }

                is ScreenState.Empty -> {
                    progressBar.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                    ivErrorMessage.setImageResource(R.drawable.ic_search_err)
                    tvErrorMessage.setText(R.string.error_nothing_found)
                    btnRefresh.visibility = View.GONE
                }

                is ScreenState.Loading -> {
                    errorContainer.visibility = View.GONE
                    placeholderSearchHistory.visibility = View.GONE
                    rvSearchResult.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

                is ScreenState.ContentHistoryList -> {
                    placeholderSearchHistory.visibility = View.VISIBLE
                    tracksHistory.clear()
                    tracksHistory.addAll(state.historyList)
                    historyAdapter.notifyDataSetChanged()
                }

                is ScreenState.EmptyHistoryList -> {
                    placeholderSearchHistory.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val USER_INPUT = "USER_INPUT"
    }
}
