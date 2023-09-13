package com.great.app.repository

import com.great.app.BuildConfig
import com.great.app.model.DataWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val apiService: MarvelApi by lazy {

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(ApiCredentials.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MarvelApi::class.java)
}

fun getMarvelApiService() = apiService

interface MarvelApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ts") timeStamp: String = ApiCredentials.TIME_STAMP,
        @Query("apikey") apiKey: String = ApiCredentials.getApiKey(),
        @Query("hash") hash: String = ApiCredentials.getHash()
    ): DataWrapper

    @GET("characters/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timeStamp: String = ApiCredentials.TIME_STAMP,
        @Query("apikey") apiKey: String = ApiCredentials.getApiKey(),
        @Query("hash") hash: String = ApiCredentials.getHash(),
    ): DataWrapper
}