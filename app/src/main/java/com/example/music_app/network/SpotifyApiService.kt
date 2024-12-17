package com.example.music_app.network

import com.example.music_app.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("search")
    fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Header("Authorization") token: String
    ): Call<SearchResponse>
}
