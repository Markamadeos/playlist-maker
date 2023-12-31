package com.guap.vkr.playlistmaker.library.ui.view_holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.library.domain.model.Playlist

class PlaylistsViewHolder(
    parentView: ViewGroup,
    itemView: View = LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_item_view_grid, parentView, false)
) : RecyclerView.ViewHolder(itemView) {

    private val cover: ImageView = itemView.findViewById(R.id.iv_cover)
    private val name: TextView = itemView.findViewById(R.id.tv_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.imgUri)
            .placeholder(R.drawable.ic_album_placeholder_2x)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    itemView.context.resources.getDimensionPixelSize(R.dimen._8dp)
                )
            )
            .into(cover)

        name.text = playlist.playlistName
        tracksCount.text =
            itemView.resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksCount,
                playlist.tracksCount
            )
    }
}