package com.guap.vkr.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guap.vkr.playlistmaker.databinding.TrackItemViewBinding
import com.guap.vkr.playlistmaker.search.domain.model.Track

open class TracksAdapter(
    val tracks: ArrayList<Track>,
    private val onTrackClickListener: (Track) -> Unit,
    private val onTrackLongClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            TrackItemViewBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener.invoke(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener.invoke(tracks[position])
            return@setOnLongClickListener true
        }
    }
}