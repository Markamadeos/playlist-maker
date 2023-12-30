package com.guap.vkr.playlistmaker.library.data.converters

import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistEntity
import com.guap.vkr.playlistmaker.library.domain.model.Playlist

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            imgUri = playlist.imgUri,
            trackIds = playlist.trackIds,
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            imgUri = playlist.imgUri,
            trackIds = playlist.trackIds,
            tracksCount = playlist.tracksCount
        )
    }
}