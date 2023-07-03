package com.guap.vkr.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guap.vkr.playlistmaker.model.Track

class TrackAdapter(private val tracks: ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(parent)

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

}