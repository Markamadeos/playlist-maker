package com.guap.vkr.playlistmaker.library.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.view_holders.PlaylistsViewHolder

class PlaylistsAdapter(var playlists: ArrayList<Playlist>, private val onPlaylistClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}