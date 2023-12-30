package com.guap.vkr.playlistmaker.player.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.player.ui.view_holders.BottomSheetPlaylistViewHolder

class BottomSheetPlaylistAdapter(
    var playlists: ArrayList<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit
) :
    RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_item_view_linear, parent, false)
        return BottomSheetPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}