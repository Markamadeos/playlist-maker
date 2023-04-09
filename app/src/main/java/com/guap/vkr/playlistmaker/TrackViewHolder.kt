package com.guap.vkr.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
) {

    private val cover = itemView.findViewById<ImageView>(R.id.iv_cover)
    private val trackName = itemView.findViewById<TextView>(R.id.tv_track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.tv_artist_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.tv_track_time)

    fun bind(model: Track) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2dp)

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(cover)
    }
}
