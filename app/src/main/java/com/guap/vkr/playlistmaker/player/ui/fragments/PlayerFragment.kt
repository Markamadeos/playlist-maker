package com.guap.vkr.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlayerBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.player.ui.adapters.BottomSheetPlaylistAdapter
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.player.ui.model.PlayerBottomSheetState
import com.guap.vkr.playlistmaker.player.ui.model.TrackInPlaylistState
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
    private var _bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehavior get() = _bottomSheetBehavior!!
    private var playlists = ArrayList<Playlist>()
    private val playlistAdapter = BottomSheetPlaylistAdapter(playlists) {
        onPlaylistClick(it)
    }

    private fun onPlaylistClick(playlist: Playlist) {
        viewModel.addTrackToPlaylist(playlist, getTrack())
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = getTrack()
        bind(track)
        val bottomSheetContainer = binding.playlistsBottomSheet
        _bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }

        viewModel.observeTimer().observe(viewLifecycleOwner) {
            updateTimer(it)
        }

        viewModel.observeLike().observe(viewLifecycleOwner) {
            updateLikeButton(it)
        }

        viewModel.observeBottomSheetState().observe(viewLifecycleOwner) {
            updateBottomSheetState(it)
        }

        viewModel.observeTrackInPlaylistState().observe(viewLifecycleOwner) {
            showStatusMessage(it)
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

        binding.btnAddToPlaylist.setOnClickListener {
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        binding.rvPlaylists.adapter = playlistAdapter
        binding.btnCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showStatusMessage(state: TrackInPlaylistState) {
        when (state) {
            is TrackInPlaylistState.Added -> {
                showSnackBar(getString(R.string.added_to_playlist, state.playlist.playlistName))
            }

            is TrackInPlaylistState.Exist -> {
                showSnackBar(
                    getString(
                        R.string.alrady_added_to_playlist,
                        state.playlist.playlistName
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun updateBottomSheetState(state: PlayerBottomSheetState) {
        when (state) {
            is PlayerBottomSheetState.Empty -> {
                binding.rvPlaylists.visibility = View.GONE
            }

            is PlayerBottomSheetState.Content -> {
                playlists.clear()
                playlists.addAll(state.playlists as ArrayList<Playlist>)
                binding.rvPlaylists.visibility = View.VISIBLE
                playlistAdapter.notifyDataSetChanged()
            }
        }
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
        viewModel.releaseResources()
    }

    private fun getTrack() = Gson().fromJson(requireArguments().getString(TRACK), Track::class.java)

}