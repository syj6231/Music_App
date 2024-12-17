package com.example.music_app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music_app.databinding.ActivityMainBinding
import com.example.music_app.network.SpotifyAuthClient
import com.example.music_app.network.SpotifyApiClient
import com.example.music_app.viewmodel.MusicViewModel

class MainActivity : AppCompatActivity() {
    private val musicViewModel: MusicViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding  // View Binding 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Access Token 요청 및 설정
        SpotifyAuthClient.requestAccessToken { token ->
            if (token != null) {
                SpotifyApiClient.setAccessToken(token)  // Access Token 설정
                Log.d("MainActivity", "Access Token 설정 완료: $token")
            } else {
                Log.e("MainActivity", "Access Token 발급 실패")
            }
        }

        // 검색창 이벤트 설정
        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE

                // Access Token이 설정된 후에만 API 호출
                if (SpotifyApiClient.getAccessToken().isNotEmpty()) {
                    musicViewModel.fetchTracks(query).observe(this) { tracks ->
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.adapter = TrackAdapter(tracks)
                    }
                } else {
                    Log.e("MainActivity", "Access Token이 설정되지 않았습니다.")
                    binding.progressBar.visibility = View.GONE
                }
            }
            true
        }
    }
}
