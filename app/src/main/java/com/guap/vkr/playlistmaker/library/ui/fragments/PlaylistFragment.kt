package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentPlaylistBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.adapters.PlaylistsAdapter
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistsState
import com.guap.vkr.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlists = ArrayList<Playlist>()
    private val playlistAdapter = PlaylistsAdapter(playlists)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        bind()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            updateScreen(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    private fun bind() {
        with(binding) {
            ivEmptyList.setImageResource(R.drawable.ic_search_err)
            tvEmptyList.text = getString(R.string.empty_playlists_text)
            btnCreatePlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
            }
            rvPlaylists.adapter = playlistAdapter
        }
    }

    private fun updateScreen(state: PlaylistsState) {
        with(binding) {
            when (state) {
                is PlaylistsState.StateEmpty -> {
                    ivEmptyList.visibility = View.VISIBLE
                    tvEmptyList.visibility = View.VISIBLE
                    rvPlaylists.visibility = View.GONE
                }

                is PlaylistsState.StateContent -> {
                    ivEmptyList.visibility = View.GONE
                    tvEmptyList.visibility = View.GONE
                    rvPlaylists.visibility = View.VISIBLE
                    playlists.clear()
                    playlists.addAll(state.playlists as ArrayList<Playlist>)
                    playlistAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}