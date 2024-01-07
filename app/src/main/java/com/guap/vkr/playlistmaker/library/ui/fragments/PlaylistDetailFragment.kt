package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.utils.PLAYLIST

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

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
        }
    }

    private fun getPlaylist() =
        Gson().fromJson(requireArguments().getString(PLAYLIST), Playlist::class.java)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}