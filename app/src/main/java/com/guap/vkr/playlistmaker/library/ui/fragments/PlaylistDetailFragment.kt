package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailState
import com.guap.vkr.playlistmaker.library.ui.view_model.PlaylistDetailViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.TracksAdapter
import com.guap.vkr.playlistmaker.utils.PLAYLIST
import com.guap.vkr.playlistmaker.utils.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistDetailViewModel> {
        parametersOf(getPlaylist())
    }
    private val tracks = ArrayList<Track>()
    private val tracksAdapter =
        TracksAdapter(tracks, { trackClickListener(it) }, { trackLongClickListener(it) })
    private lateinit var deleteTrackModalWindow: MaterialAlertDialogBuilder

    private fun trackLongClickListener(track: Track) {
        deleteTrackModalWindow =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(getString(R.string.do_you_want_delete_a_track))
                .setNegativeButton(getString(R.string.answer_yes)) { _, _ ->
                    viewModel.deleteTrack(track, playlist = getPlaylist())
                }
                .setPositiveButton(getString(R.string.answer_no)) { dialog, _ ->
                    dialog.cancel()
                }
        deleteTrackModalWindow.show()
    }

    private fun trackClickListener(track: Track) {
        val trackBundle = bundleOf(TRACK to Gson().toJson(track))
        findNavController().navigate(
            R.id.action_playlistDetailFragment_to_playerFragment,
            trackBundle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = getPlaylist()
        bind(playlist)
        viewModel.getTracks()
        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }
    }

    private fun updateScreen(state: PlaylistDetailState?) {
        when (state) {
            is PlaylistDetailState.Empty -> {
                binding.tvDuration.text = getString(R.string.zero_duration_playlist)
                binding.tvEmptyPlaylist.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.GONE
            }

            is PlaylistDetailState.Content -> {
                binding.tvEmptyPlaylist.visibility = View.GONE
                tracks.clear()
                tracks.addAll(state.tracks)
                tracksAdapter.notifyDataSetChanged()
                binding.rvTracks.visibility = View.VISIBLE
            }

            is PlaylistDetailState.TrackDeleted -> {
                // TODO update screen some how
            }

            else -> {}
        }
    }

    private fun bind(playlist: Playlist) {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            Glide.with(requireContext()).load(playlist.imgUri)
                .placeholder(R.drawable.ic_album_placeholder_2x)
                .transform(CenterCrop()).into(ivCover)

            tvPlaylistName.text = playlist.playlistName
            if (playlist.playlistDescription.isNullOrEmpty()) {
                tvDescription.visibility = View.GONE
            } else {
                tvDescription.text = playlist.playlistDescription
            }
            tvDuration.text = "120 минут" // TODO
            tvTracksCount.text = resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksCount,
                playlist.tracksCount
            )
            btnShare.setOnClickListener {
                // TODO
            }
            tvPlaylistNameBs.text = playlist.playlistName
            tvTracksCountBs.text = resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksCount,
                playlist.tracksCount
            )

            Glide.with(requireContext()).load(playlist.imgUri)
                .placeholder(R.drawable.ic_album_placeholder_2x)
                .transform(CenterCrop()).into(ivCoverBs)

            rvTracks.adapter = tracksAdapter
        }
    }

    private fun getPlaylist() =
        Gson().fromJson(requireArguments().getString(PLAYLIST), Playlist::class.java)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}