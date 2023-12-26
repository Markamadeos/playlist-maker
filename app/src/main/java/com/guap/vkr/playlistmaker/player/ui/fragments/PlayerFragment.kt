package com.guap.vkr.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlayerBinding
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.utils.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(getTrack())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val track = getTrack()
        bind(track)

        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }

        viewModel.observeTimer().observe(viewLifecycleOwner) {
            updateTimer(it)
        }

        viewModel.observeLike().observe(viewLifecycleOwner) {
            updateLikeButton(it)
        }

        binding.btnPlay.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLike.setOnClickListener {
            viewModel.isLikeButtonClicked(track = track)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(track: Track) {
        val cornerRadius = this.resources.getDimensionPixelSize(R.dimen._8dp)

        Glide.with(this).load(track.getCoverArtwork()).placeholder(R.drawable.iv_track_cover)
            .transform(CenterCrop(), RoundedCorners(cornerRadius)).into(binding.ivCover)

        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvPlaytime.text = getString(R.string.default_playtime_value)
            tvDurationValue.text = track.getDuration()
            tvAlbumValue.text = track.collectionName
            tvYearValue.text = track.getReleaseYear()
            tvGenreValue.text = track.primaryGenreName
            tvCountryValue.text = track.country
            tvTrackName.isSelected = true
            tvArtistName.isSelected = true
        }
    }

    private fun updateTimer(time: String) {
        binding.tvPlaytime.text = time
    }

    private fun updateScreen(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.Playing -> {
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
            }

            is MediaPlayerState.Paused -> {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
            }

            is MediaPlayerState.Prepared -> {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
                binding.tvPlaytime.setText(R.string.default_playtime_value)
            }

            else -> {}
        }
    }

    private fun updateLikeButton(isFavorite: Boolean) {
        binding.btnLike.setImageResource(
            if (isFavorite) R.drawable.ic_like_pressed
            else R.drawable.ic_like_unpressed
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.releaseRecourses()
    }

    private fun getTrack() =  Gson().fromJson(requireArguments().getString(TRACK), Track::class.java)

}