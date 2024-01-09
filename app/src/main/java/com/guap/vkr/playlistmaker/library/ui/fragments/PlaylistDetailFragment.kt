package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailShareState
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailState
import com.guap.vkr.playlistmaker.library.ui.view_model.PlaylistDetailViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.TracksAdapter
import com.guap.vkr.playlistmaker.utils.PLAYLIST_ID
import com.guap.vkr.playlistmaker.utils.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistDetailViewModel> {
        parametersOf(getPlaylistId())
    }
    private val tracks = ArrayList<Track>()
    private val tracksAdapter =
        TracksAdapter(tracks, { trackClickListener(it) }, { trackLongClickListener(it) })
    private lateinit var deleteModalWindow: MaterialAlertDialogBuilder

    private var _bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehaviorMenu get() = _bottomSheetBehaviorMenu!!


    private fun trackClickListener(track: Track) {
        val trackBundle = bundleOf(TRACK to Gson().toJson(track))
        findNavController().navigate(
            R.id.action_playlistDetailFragment_to_playerFragment,
            trackBundle
        )
    }

    private fun trackLongClickListener(track: Track) {
        deleteTrack(track)
    }

    private fun deleteTrack(track: Track) {
        deleteModalWindow =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(getString(R.string.do_you_want_delete_a_track))
                .setNegativeButton(getString(R.string.answer_yes)) { _, _ ->
                    viewModel.deleteTrack(
                        track,
                        getPlaylistId()
                    )
                }
                .setPositiveButton(getString(R.string.answer_no)) { dialog, _ ->
                    dialog.cancel()
                }
        deleteModalWindow.show()
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
        setupButtons()
        binding.rvTracks.adapter = tracksAdapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }
        viewModel.observeShareState().observe(viewLifecycleOwner) {
            shareStateUpdate(it)
        }
        viewModel.updateData()
        val bottomSheetContainer = binding.bsMenu
        _bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
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
    }

    private fun shareStateUpdate(state: PlaylistDetailShareState?) {
        when (state) {
            is PlaylistDetailShareState.NothingToShare -> {
                showMessage(getString(R.string.nothing_to_share))
            }

            else -> {}
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun updateScreen(state: PlaylistDetailState?) {
        when (state) {
            is PlaylistDetailState.Empty -> {
                bind(state.playlist, ZERO_DURATION)
                binding.tvEmptyPlaylist.visibility = View.VISIBLE
                binding.rvTracks.visibility = View.GONE
            }

            is PlaylistDetailState.Content -> {
                bind(state.playlist, state.duration)
                binding.tvEmptyPlaylist.visibility = View.GONE
                tracks.clear()
                tracks.addAll(state.tracks)
                tracksAdapter.notifyDataSetChanged()
                binding.rvTracks.visibility = View.VISIBLE
            }

            else -> {}
        }
    }

    private fun bind(playlist: Playlist, duration: Long) {
        with(binding) {
            Glide.with(requireContext()).load(playlist.imgUri)
                .placeholder(R.drawable.ic_album_placeholder_2x)
                .transform(CenterCrop()).into(ivCover)

            tvPlaylistName.text = playlist.playlistName
            if (playlist.playlistDescription.isNullOrEmpty()) {
                tvDescription.visibility = View.GONE
            } else {
                tvDescription.text = playlist.playlistDescription
            }
            val durationInMinutes =
                SimpleDateFormat("m", Locale.getDefault()).format(duration).toInt()
            tvDuration.text = resources.getQuantityString(
                R.plurals.tracks_duration,
                durationInMinutes,
                durationInMinutes
            )
            tvTracksCount.text = resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksCount,
                playlist.tracksCount
            )
            tvPlaylistNameBs.text = playlist.playlistName
            tvTracksCountBs.text = resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksCount,
                playlist.tracksCount
            )

            Glide.with(requireContext()).load(playlist.imgUri)
                .placeholder(R.drawable.ic_album_placeholder_2x)
                .transform(CenterCrop()).into(ivCoverBs)

        }
    }

    private fun setupButtons() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnShare.setOnClickListener {
                sharePlaylist()
            }
            btnShareBs.setOnClickListener {
                sharePlaylist()
            }
            btnDotsMenu.setOnClickListener {
                bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btnDelete.setOnClickListener {
                deletePlaylist()
            }
            btnEdit.setOnClickListener {
                val playlistBundle = bundleOf(PLAYLIST_ID to getPlaylistId())
                findNavController().navigate(
                    R.id.action_playlistDetailFragment_to_editPlaylistFragment,
                    playlistBundle
                )
            }
        }
    }

    private fun sharePlaylist() {
        viewModel.sharePlaylist()
    }

    private fun deletePlaylist() {
        deleteModalWindow =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(getString(R.string.delete_playlist_question, binding.tvPlaylistName.text))
                .setNegativeButton(getString(R.string.answer_yes)) { _, _ ->
                    viewModel.deletePlaylist()
                    findNavController().popBackStack()
                }
                .setPositiveButton(getString(R.string.answer_no)) { dialog, _ ->
                    dialog.cancel()
                }
        deleteModalWindow.show()
    }

    private fun getPlaylistId() = requireArguments().getLong(PLAYLIST_ID)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ZERO_DURATION = 0L
    }
}