package com.example.music_app.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyApiClient {
    private const val BASE_URL = "https://api.spotify.com/v1/"
    private var accessToken: String? = null


    fun setAccessToken(token: String) {
        accessToken = token
    }


    fun getAccessToken(): String {
        Log.d("SpotifyApiClient", "Current Access Token: $accessToken")
        return accessToken ?: throw IllegalStateException("Access Token is not set")
    }


    val instance: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                accessToken?.let {
                    request.addHeader("Authorization", "Bearer $it")
                }
                chain.proceed(request.build())
            }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
