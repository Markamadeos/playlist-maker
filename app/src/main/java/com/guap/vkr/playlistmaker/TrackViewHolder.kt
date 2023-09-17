package com.guap.vkr.playlistmaker

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.guap.vkr.playlistmaker.databinding.TrackItemViewBinding
import com.guap.vkr.playlistmaker.domain.player.model.Track

class TrackViewHolder(private val binding: TrackItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2dp)
        binding.tvTrackName.text = model.trackName
        binding.tvArtistName.text = model.artistName
        binding.tvTrackTime.text = model.getDuration()


        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivCover)
    }
}
