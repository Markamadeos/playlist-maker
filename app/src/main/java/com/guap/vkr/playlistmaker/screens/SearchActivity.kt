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
import com.guap.vkr.playlistmaker.TrackAdapter
import com.guap.vkr.playlistmaker.api.ITunesApi
import com.guap.vkr.playlistmaker.api.SearchResponse
import com.guap.vkr.playlistmaker.model.Track
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var userInput = ""
    private val iTunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private lateinit var placeholderContainer : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val queryInput = findViewById<EditText>(R.id.et_search)
        val clearButton = findViewById<ImageView>(R.id.img_clear)
        val backButton = findViewById<ImageView>(R.id.btn_back)
        val trackList = findViewById<RecyclerView>(R.id.recycler_view)
        placeholderContainer = findViewById<LinearLayout>(R.id.placeholder_container)
        val placeholderImage = findViewById<ImageView>(R.id.iv_placeholder_message)
        val placeholderMessage = findViewById<TextView>(R.id.tv_placeholder)
        val refreshButton = findViewById<Button>(R.id.btn_refresh)
        trackList.adapter = adapter
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
            adapter.notifyDataSetChanged()
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
                            adapter.notifyDataSetChanged()
                        } else {
                            showMessage(EMPTY_RESPONSE, this)
                        }

                    } else {
                        showMessage(NETWORK_ERROR, this)
                    }

                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showMessage(NETWORK_ERROR, this)
                }
            })
    }

    private fun showMessage(mess: String, context: Context) {
        if(mess == EMPTY_RESPONSE){
            placeholderContainer.visibility = View.VISIBLE
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


    companion object {
        private const val USER_INPUT = "USER_INPUT"
        private const val NETWORK_ERROR = "NETWORK_ERROR"
        private const val EMPTY_RESPONSE = "EMPTY_RESPONSE"

    }
}
