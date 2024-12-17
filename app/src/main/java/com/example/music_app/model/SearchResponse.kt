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
    val album: Album? = null
) {
    // album 객체에서 첫 번째 이미지 URL을 가져오는 프로퍼티
    val albumCoverUrl: String?
        get() = album?.images?.firstOrNull()?.url
}


data class Image(
    val url: String  // 앨범 커버 이미지 URL
)

data class Album(
    val images: List<Image>  // 이미지 목록
)

data class Artist(
    val name: String
)

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)