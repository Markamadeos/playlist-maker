package com.guap.vkr.playlistmaker.library.data.converters

import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.guap.vkr.playlistmaker.search.domain.model.Track

class PlaylisttrackDbConverter {
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorite = true
        )
    }
}