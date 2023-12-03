package com.guap.vkr.playlistmaker.library.data.converters

import com.guap.vkr.playlistmaker.library.data.db.entity.TrackEntity
import com.guap.vkr.playlistmaker.library.domain.model.TrackLibraryModel
import com.guap.vkr.playlistmaker.search.data.dto.TrackDto

class TrackDbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackEntity): TrackLibraryModel {
        return TrackLibraryModel(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}