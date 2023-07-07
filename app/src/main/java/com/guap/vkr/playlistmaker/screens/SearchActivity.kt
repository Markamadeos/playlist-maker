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
import com.guap.vkr.playlistmaker.utils.SEARCH_HISTORY_KEY
import com.guap.vkr.playlistmaker.utils.iTunesBaseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<Track>()
    private var userInput = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val searchAdapter = TrackAdapter(tracks) { trackClick(it) }
    private lateinit var placeholderContainer: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var refreshButton: Button
    private lateinit var placeholderSearchHistory: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val queryInput = findViewById<EditText>(R.id.et_search)
        val clearButton = findViewById<ImageView>(R.id.iv_clear)
        val backButton = findViewById<ImageView>(R.id.btn_back)
        val trackList = findViewById<RecyclerView>(R.id.recycler_view)
        val sharedPref = getSharedPreferences(SEARCH_HISTORY_KEY, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPref)

        placeholderContainer = findViewById(R.id.error_container)
        placeholderImage = findViewById(R.id.iv_error_message)
        placeholderMessage = findViewById(R.id.tv_error_pic)
        refreshButton = findViewById(R.id.btn_refresh)

        trackList.adapter = searchAdapter
        trackList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            hideKeyboard()
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        refreshButton.setOnClickListener {
            search()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userInput = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        queryInput.addTextChangedListener(textWatcher)
        queryInput.setText(userInput)
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
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

    private fun search() {
        iTunesService.search(userInput)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            placeholderContainer.visibility = View.GONE
                            searchAdapter.notifyDataSetChanged()
                        } else {
                            showMessage(EMPTY_RESPONSE)
                        }

                    } else {
                        showMessage(NETWORK_ERROR)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showMessage(NETWORK_ERROR)
                }
            })
    }

    private fun showSearchHistory() {
        //TODO
    }

    private fun showMessage(status: String) {
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

    private fun trackClick(track: Track) {
        Toast.makeText(applicationContext, track.trackId.toString(), Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val USER_INPUT = "USER_INPUT"
        private const val NETWORK_ERROR = "NETWORK_ERROR"
        private const val EMPTY_RESPONSE = "EMPTY_RESPONSE"
    }
}
