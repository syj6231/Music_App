package com.example.music_app.repository

import android.util.Log
import com.example.music_app.model.Track
import com.example.music_app.network.SpotifyApiClient
import com.example.music_app.network.SpotifyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicRepository {
    private val apiService = SpotifyApiClient.instance.create(SpotifyApiService::class.java)

    suspend fun searchTracks(query: String): List<Track> {
        val accessToken = SpotifyApiClient.getAccessToken() // Access Token 가져오기
        return withContext(Dispatchers.IO) {  // 네트워크 작업은 IO 스레드에서 실행
            try {
                val response = apiService.searchTracks(
                    query = query,
                    token = "Bearer $accessToken"
                ).execute()

                if (response.isSuccessful) {
                    val tracks = response.body()?.tracks?.items ?: emptyList()
                    Log.d("Repository", "Tracks received: ${tracks.size}")
                    tracks
                } else {
                    Log.e("Repository", "API Error: ${response.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("Repository", "Exception: ${e.message}")
                emptyList()
            }
        }
    }
}

