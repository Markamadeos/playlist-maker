package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentFavoriteBinding
import com.guap.vkr.playlistmaker.library.ui.model.FavoriteState
import com.guap.vkr.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.TracksAdapter
import com.guap.vkr.playlistmaker.utils.TRACK
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()
    private val tracks = ArrayList<Track>()
    private val favoriteAdapter = TracksAdapter(tracks) { trackClickListener(it) }
    private var clickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        with(binding) {
            ivEmptyList.setImageResource(R.drawable.ic_search_err)
            tvEmptyList.text = getString(R.string.empty_favorite_text)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }
        binding.rvFavorite.adapter = favoriteAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavoriteTracksList()
    }

    private fun updateScreen(screenState: FavoriteState?) {
        if (screenState is FavoriteState.Content) {
            tracks.clear()
            tracks.addAll(screenState.tracks as ArrayList<Track>)
            favoriteAdapter.notifyDataSetChanged()
            binding.rvFavorite.visibility = View.VISIBLE
            binding.tvEmptyList.visibility = View.GONE
            binding.ivEmptyList.visibility = View.GONE
        } else {
            binding.rvFavorite.visibility = View.GONE
            binding.tvEmptyList.visibility = View.VISIBLE
            binding.ivEmptyList.visibility = View.VISIBLE
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

    private fun trackClickListener(track: Track) {
        if (isClickAllowed()) {
            val trackBundle = bundleOf(TRACK to Gson().toJson(track))
            findNavController().navigate(R.id.action_libraryFragment_to_playerFragment, trackBundle)
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
        private const val CLICK_DEBOUNCE_DELAY_MS = 300L
    }
}