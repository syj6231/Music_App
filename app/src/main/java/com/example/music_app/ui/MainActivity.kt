package com.example.music_app.ui

import android.widget.Toast
import com.example.music_app.model.Track
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val musicViewModel: MusicViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var isAccessTokenSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val trackList = mutableListOf<Track>()
        val trackAdapter = TrackAdapter(trackList) { track, rating ->
            handleRatingChange(track, rating)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trackAdapter

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        SpotifyAuthClient.requestAccessToken { token ->
            if (token != null) {
                SpotifyApiClient.setAccessToken(token)
                isAccessTokenSet = true
                Log.d("MainActivity", "Access Token 설정 완료: $token")
            } else {
                Log.e("MainActivity", "Access Token 발급 실패")
                isAccessTokenSet = false // Access Token 설정 실패
            }
        }


        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                if (isAccessTokenSet) {
                    binding.progressBar.visibility = View.VISIBLE
                    musicViewModel.fetchTracks(query).observe(this) { tracks ->
                        binding.progressBar.visibility = View.GONE
                        trackList.clear()
                        trackList.addAll(tracks)
                        trackAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("MainActivity", "Access Token이 설정되지 않았습니다.")
                    Toast.makeText(this, "Access Token이 설정되지 않았습니다. 잠시 후 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun handleRatingChange(track: Track, rating: Float) {
        track.rating = rating
        saveRatingToFirestore(track)
        Toast.makeText(this, "${track.name}의 별점이 ${rating}으로 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun saveRatingToFirestore(track: Track) {
        val db = FirebaseFirestore.getInstance()
        db.collection("tracks").document(track.name)
            .set(track)
            .addOnSuccessListener {
                Log.d("MainActivity", "별점이 저장되었습니다: ${track.name}")
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "별점 저장 실패: ${e.message}")
            }
    }
}
