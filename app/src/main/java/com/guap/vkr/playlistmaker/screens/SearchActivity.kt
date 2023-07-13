package com.guap.vkr.playlistmaker.screens

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.SearchHistory
import com.guap.vkr.playlistmaker.TrackAdapter
import com.guap.vkr.playlistmaker.api.ITunesApi
import com.guap.vkr.playlistmaker.api.SearchResponse
import com.guap.vkr.playlistmaker.model.Track
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import com.guap.vkr.playlistmaker.utils.iTunesBaseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val searchAdapter = TrackAdapter(tracks) { trackClickListener(it) }
    private var userInput = ""
    private lateinit var placeholderContainer: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var placeholderSearchHistory: LinearLayout
    private lateinit var searchHistory: SearchHistory
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var rvTrackList: RecyclerView
    private lateinit var rvHistoryList: RecyclerView
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initVariables()
        initSearchResultRecycler()
        initSearchHistoryRecycler()
        buttonsConfig()
        queryInputConfig(initTextWatcher())
    }

    private fun initTextWatcher(): TextWatcher {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                placeholderSearchHistory.visibility =
                    searchHistoryVisibility(s, searchHistory, queryInput.hasFocus())
                userInput = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        return textWatcher
    }

    private fun buttonsConfig() {
        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            hideKeyboard()
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            historyAdapter.notifyDataSetChanged()
            placeholderSearchHistory.visibility = View.GONE
        }

        refreshButton.setOnClickListener {
            search()
        }
    }

    private fun queryInputConfig(textWatcher: TextWatcher) {
        queryInput.addTextChangedListener(textWatcher)
        queryInput.setText(userInput)
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            placeholderSearchHistory.visibility =
                searchHistoryVisibility(queryInput.text, searchHistory, hasFocus)
        }
    }

    private fun initSearchHistoryRecycler() {
        rvHistoryList.adapter = historyAdapter
        rvHistoryList.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun initSearchResultRecycler() {
        rvTrackList.adapter = searchAdapter
        rvTrackList.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun initVariables() {
        searchHistory = SearchHistory(getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE))
        placeholderContainer = findViewById(R.id.error_container)
        placeholderImage = findViewById(R.id.iv_error_message)
        placeholderMessage = findViewById(R.id.tv_error_pic)
        placeholderSearchHistory = findViewById(R.id.placeholder_search_history)
        refreshButton = findViewById(R.id.btn_refresh)
        clearHistoryButton = findViewById(R.id.btn_clear_history)
        queryInput = findViewById(R.id.et_search)
        clearButton = findViewById(R.id.iv_clear)
        backButton = findViewById(R.id.btn_back)
        rvTrackList = findViewById(R.id.recycler_view)
        rvHistoryList = findViewById(R.id.recycler_view_history)
        historyAdapter = TrackAdapter(searchHistory.getSearchHistory()) { trackClickListener(it) }
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
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun search() {
        if (userInput.isNotEmpty()) {
            iTunesService.search(userInput).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            placeholderContainer.visibility = View.GONE
                            searchAdapter.notifyDataSetChanged()
                        } else {
                            showErrorMessage(EMPTY_RESPONSE)
                        }

                    } else {
                        showErrorMessage(NETWORK_ERROR)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showErrorMessage(NETWORK_ERROR)
                }
            })
        }
    }

    private fun showErrorMessage(status: String) {
        tracks.clear()
        searchAdapter.notifyDataSetChanged()
        placeholderContainer.visibility = View.VISIBLE
        if (status == EMPTY_RESPONSE) {
            placeholderImage.setImageResource(R.drawable.ic_search_err)
            placeholderMessage.setText(R.string.error_nothing_found)
            refreshButton.visibility = View.GONE
        } else {
            placeholderImage.setImageResource(R.drawable.ic_internet_err)
            placeholderMessage.setText(R.string.error_network_faild)
            refreshButton.visibility = View.VISIBLE
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
        searchHistory.addTrackToHistory(track)
        historyAdapter.notifyItemInserted(INSERT_POSITION)
        Toast.makeText(
            applicationContext, track.trackName + " saved!", Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val INSERT_POSITION = 0
        private const val USER_INPUT = "USER_INPUT"
        private const val NETWORK_ERROR = "NETWORK_ERROR"
        private const val EMPTY_RESPONSE = "EMPTY_RESPONSE"
    }
}
