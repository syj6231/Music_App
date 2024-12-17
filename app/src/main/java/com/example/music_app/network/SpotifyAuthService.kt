package com.example.music_app.network

import com.example.music_app.model.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SpotifyAuthService {
    @POST("token")
    @FormUrlEncoded
    fun getAccessToken(
        @Header("Authorization") authHeader: String, // Base64 인코딩된 Client ID:Client Secret
        @Field("grant_type") grantType: String = "client_credentials"
    ): Call<AccessTokenResponse>
}
