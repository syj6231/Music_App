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
            emit(tracks)  // 결과를 LiveData에 전달
        } catch (e: Exception) {
            emit(emptyList<Track>())  // 에러 발생 시 빈 목록 반환
            Log.e("MusicViewModel", "Error fetching tracks: ${e.message}")
        }
    }
}
