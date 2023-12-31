package com.guap.vkr.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentSearchBinding
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.model.ScreenState
import com.guap.vkr.playlistmaker.search.ui.view_model.SearchViewModel
import com.guap.vkr.playlistmaker.utils.TRACK
import com.guap.vkr.playlistmaker.utils.hideKeyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val tracks = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()
    private val searchAdapter = TracksAdapter(tracks) { trackClickListener(it) }
    private val historyAdapter = TracksAdapter(tracksHistory) { trackClickListener(it) }
    private var userInput = ""
    private var clickAllowed = true
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData().observe(viewLifecycleOwner) {
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
            btnClear.setOnClickListener {
                etSearch.setText("")
                hideKeyboard()
                tracks.clear()
                viewModel.getTracksHistory()
                searchAdapter.notifyDataSetChanged()
            }

            btnClearHistory.setOnClickListener {
                viewModel.clearHistory()
                viewModel.getTracksHistory()
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun trackClickListener(track: Track) {
        if (isClickAllowed()) {
            viewModel.addTrackToHistory(track)
            viewModel.getTracksHistory()
            val trackBundle = bundleOf(TRACK to Gson().toJson(track))
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment, trackBundle)
        }
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MS)
                clickAllowed = true
            }
        }
        return current
    }

    private fun updateScreen(state: ScreenState) {
        binding.apply {
            when (state) {
                is ScreenState.Content -> {
                    tracks.clear()
                    tracks.addAll(state.tracks as ArrayList<Track>)
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
                    errorContainer.visibility = View.GONE
                    historyAdapter.notifyDataSetChanged()
                }

                is ScreenState.EmptyHistoryList -> {
                    placeholderSearchHistory.visibility = View.GONE
                    errorContainer.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 300L
    }
}