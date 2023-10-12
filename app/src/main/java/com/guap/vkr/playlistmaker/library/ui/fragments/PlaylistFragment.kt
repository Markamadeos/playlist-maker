package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlaylistBinding

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        binding.ivEmptyList.setImageResource(R.drawable.ic_search_err)
        binding.tvEmptyList.text = getString(R.string.empty_playlists_text)
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}