package com.example.music_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.music_app.model.Track
import com.example.music_app.network.SpotifyApiClient
import com.example.music_app.repository.MusicRepository
import kotlinx.coroutines.Dispatchers

class MusicViewModel : ViewModel() {
    private val repository = MusicRepository()

    fun fetchTracks(query: String) = liveData(Dispatchers.IO) {
        try {
            val tracks = repository.searchTracks(query)
            emit(tracks)
        } catch (e: Exception) {
            emit(emptyList<Track>())
            Log.e("MusicViewModel", "Error fetching tracks: ${e.message}")
        }
    }
}
