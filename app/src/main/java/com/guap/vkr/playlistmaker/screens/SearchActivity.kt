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
import android.view.inputmethod.EditorInfo
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
import com.guap.vkr.playlistmaker.utils.iTunesBaseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val searchAdapter = TrackAdapter(tracks) { trackClickListener(it) }
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var userInput = ""
    private var isClickAllowed = true
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding

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
            binding.btnClear.visibility = clearButtonVisibility(s)
            binding.placeholderSearchHistory.visibility =
                searchHistoryVisibility(s, searchHistory, binding.etSearch.hasFocus())
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
        binding.etSearch.addTextChangedListener(textWatcher)
        binding.etSearch.setText(userInput)
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.placeholderSearchHistory.visibility =
                searchHistoryVisibility(binding.etSearch.text, searchHistory, hasFocus)
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MS)
    }

    private fun search() {
        if (userInput.isNotEmpty()) {
            binding.errorContainer.visibility = View.GONE
            binding.placeholderSearchHistory.visibility = View.GONE
            binding.rvSearchResult.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            iTunesService.search(userInput).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            binding.rvSearchResult.visibility = View.VISIBLE
                            binding.errorContainer.visibility = View.GONE
                            searchAdapter.notifyDataSetChanged()
                        } else {
                            showErrorMessage(EMPTY_RESPONSE)
                        }

                    } else {
                        showErrorMessage(NETWORK_ERROR)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showErrorMessage(NETWORK_ERROR)
                }
            })
        }
    }

    private fun showErrorMessage(status: String) {
        tracks.clear()
        searchAdapter.notifyDataSetChanged()
        binding.errorContainer.visibility = View.VISIBLE
        if (status == EMPTY_RESPONSE) {
            binding.ivErrorMessage.setImageResource(R.drawable.ic_search_err)
            binding.tvErrorMessage.setText(R.string.error_nothing_found)
            binding.btnRefresh.visibility = View.GONE
        } else {
            binding.ivErrorMessage.setImageResource(R.drawable.ic_internet_err)
            binding.tvErrorMessage.setText(R.string.error_network_faild)
            binding.btnRefresh.visibility = View.VISIBLE
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
        if (clickDebounce()) {
            searchHistory.addTrackToHistory(track)
            historyAdapter.notifyDataSetChanged()
            val playIntent =
                Intent(this, PlayerActivity::class.java).putExtra(TRACK, Gson().toJson(track))
            startActivity(playIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MS = 1000L
        private const val USER_INPUT = "USER_INPUT"
        private const val NETWORK_ERROR = "NETWORK_ERROR"
        private const val EMPTY_RESPONSE = "EMPTY_RESPONSE"
    }
}
