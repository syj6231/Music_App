package com.example.music_app.network

import android.util.Base64
import android.util.Log
import com.example.music_app.model.AccessTokenResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyAuthClient {
    private const val AUTH_URL = "https://accounts.spotify.com/api/"
    private const val CLIENT_ID =
        "28b55a3e26544c3aae452b34dea896e8"       // Spotify Developer에서 발급받은 Client ID
    private const val CLIENT_SECRET =
        "39d49e4037fd4e60bb653c5a87c62ecf" // Spotify Developer에서 발급받은 Client Secret

    private val authService: SpotifyAuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        retrofit.create(SpotifyAuthService::class.java)
    }

    fun requestAccessToken(callback: (String?) -> Unit) {
        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        Log.d("SpotifyAuth", "Requesting Access Token with authHeader: $authHeader")

        val call = authService.getAccessToken(authHeader)
        call.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token
                    Log.d("SpotifyAuth", "Access Token 발급 성공: $accessToken")
                    callback(accessToken)
                } else {
                    Log.e("SpotifyAuth", "Access Token 발급 실패: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                Log.e("SpotifyAuth", "Failed to request Access Token: ${t.message}")
                callback(null)
            }
        })
    }
}