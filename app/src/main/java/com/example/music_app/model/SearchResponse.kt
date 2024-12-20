package com.example.music_app.model

data class SearchResponse(
    val tracks: Tracks
)

data class Tracks(
    val items: List<Track>
)

data class Track(
    val name: String,
    val artists: List<Artist>,
    val album: Album? = null,
    var rating: Float = 0f
) {
    val albumCoverUrl: String?
        get() = album?.images?.firstOrNull()?.url
}


data class Image(
    val url: String
)

data class Album(
    val images: List<Image>
)

data class Artist(
    val name: String
)

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)