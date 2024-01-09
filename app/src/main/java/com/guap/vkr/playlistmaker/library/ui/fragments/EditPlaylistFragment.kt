package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.EditPlaylistState
import com.guap.vkr.playlistmaker.utils.PLAYLIST_ID

class EditPlaylistFragment : NewPlaylistFragment() {


    private fun getPlaylistId() = requireArguments().getLong(PLAYLIST_ID)

    override val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNewPlaylistTitle.text = getString(R.string.edit_playlist_title)
        binding.btnCreatePlaylist.text = getString(R.string.save_button)
        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }
        viewModel.updateData(getPlaylistId())
    }

    private fun updateScreen(state: EditPlaylistState?) {
        when (state) {
            is EditPlaylistState.FilledPlaylistData -> {
                fillData(state.playlist)
            }

            else -> {}
        }

    }

    private fun fillData(playlist: Playlist) {
        with(binding) {
            btnCreatePlaylist.setOnClickListener {
                viewModel.updatePlaylist(
                    getPlaylistId(),
                    etPlaylistName.text.toString(),
                    etPlaylistDescription.text.toString(),
                    coverUri
                )
                playlistCreatedShowNotification()
                findNavController().popBackStack()
            }
            Glide.with(requireContext())
                .load(playlist.imgUri)
                .placeholder(R.drawable.ic_album_placeholder_2x)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        requireContext().resources.getDimensionPixelSize(R.dimen._8dp)
                    )
                )
                .into(binding.ivPlaylistPicture)
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivPlaylistPicturePlaceholder.visibility = View.GONE
            etPlaylistName.setText(playlist.playlistName)
            etPlaylistDescription.setText(playlist.playlistDescription)
        }
    }

    override fun playlistCreatedShowNotification() {
        Snackbar.make(
            binding.root,
            getString(R.string.playlist_saved),
            Snackbar.LENGTH_SHORT
        ).show()
    }

}