package com.guap.vkr.playlistmaker.screens

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
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.SearchHistory
import com.guap.vkr.playlistmaker.TrackAdapter
import com.guap.vkr.playlistmaker.api.ITunesApi
import com.guap.vkr.playlistmaker.api.SearchResponse
import com.guap.vkr.playlistmaker.databinding.ActivitySearchBinding
import com.guap.vkr.playlistmaker.model.Track
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import com.guap.vkr.playlistmaker.utils.TRACK
import com.guap.vkr.playlistmaker.utils.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {


    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val searchAdapter = TrackAdapter(tracks) { trackClickListener(it) }
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var userInput = ""
    private var clickAllowed = true
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private var screenState = RequestState.DEFAULT_STATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        searchHistory = SearchHistory(getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE))
        historyAdapter = TrackAdapter(searchHistory.getSearchHistory()) { trackClickListener(it) }

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
                placeholderSearchHistory.visibility =
                    searchHistoryVisibility(s, searchHistory, binding.etSearch.hasFocus())
            }
            userInput = s.toString()
            searchDebounce()
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
                searchAdapter.notifyDataSetChanged()
            }

            btnClearHistory.setOnClickListener {
                searchHistory.clearHistory()
                historyAdapter.notifyDataSetChanged()
                placeholderSearchHistory.visibility = View.GONE
            }

            btnRefresh.setOnClickListener {
                search()
            }
        }
    }

    private fun queryInputConfig(textWatcher: TextWatcher) {
        binding.etSearch.apply {
            addTextChangedListener(textWatcher)
            setText(userInput)
            setOnFocusChangeListener { _, hasFocus ->
                binding.placeholderSearchHistory.visibility =
                    searchHistoryVisibility(text, searchHistory, hasFocus)
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

    private fun searchHistoryVisibility(
        s: CharSequence?,
        searchHistory: SearchHistory,
        focus: Boolean
    ): Int {
        return if (s.isNullOrEmpty() && searchHistory.getSearchHistory().isNotEmpty() && focus) {
            binding.errorContainer.visibility = View.GONE
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun searchDebounce() {
        handler.apply {
            removeCallbacks(searchRunnable)
            postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MS)
        }
    }

    private fun search() {
        if (userInput.isNotEmpty()) {
            screenState = RequestState.LOADING
            updateScreen()
            iTunesService.search(userInput).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    tracks.clear()
                    screenState = if (response.code() == RESPONSE_OK) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            RequestState.GOOD_RESPONSE
                        } else {
                            RequestState.EMPTY_RESPONSE
                        }

                    } else {
                        RequestState.NETWORK_ERROR
                    }
                    updateScreen()
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    screenState = RequestState.NETWORK_ERROR
                    updateScreen()
                }
            })
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

    private fun trackClickListener(track: Track) {
        if (isClickAllowed()) {
            searchHistory.addTrackToHistory(track)
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

    private fun updateScreen() {
        binding.apply {
            when (screenState) {
                RequestState.GOOD_RESPONSE -> {
                    progressBar.visibility = View.GONE
                    rvSearchResult.visibility = View.VISIBLE
                    errorContainer.visibility = View.GONE
                }

                RequestState.NETWORK_ERROR -> {
                    progressBar.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                    ivErrorMessage.setImageResource(R.drawable.ic_internet_err)
                    tvErrorMessage.setText(R.string.error_network_faild)
                    btnRefresh.visibility = View.VISIBLE
                }

                RequestState.EMPTY_RESPONSE -> {
                    progressBar.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                    ivErrorMessage.setImageResource(R.drawable.ic_search_err)
                    tvErrorMessage.setText(R.string.error_nothing_found)
                    btnRefresh.visibility = View.GONE
                }

                RequestState.LOADING -> {
                    errorContainer.visibility = View.GONE
                    placeholderSearchHistory.visibility = View.GONE
                    rvSearchResult.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

                RequestState.DEFAULT_STATE -> {}
            }
        }
        searchAdapter.notifyDataSetChanged()
    }

    enum class RequestState {
        DEFAULT_STATE,
        GOOD_RESPONSE,
        NETWORK_ERROR,
        EMPTY_RESPONSE,
        LOADING
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val USER_INPUT = "USER_INPUT"
        private const val RESPONSE_OK = 200
    }
}
