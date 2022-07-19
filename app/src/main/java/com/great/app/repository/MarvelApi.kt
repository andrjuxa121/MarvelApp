package com.great.app.repository

import com.great.app.BuildConfig
import com.great.app.model.DataWrapper
import com.great.app.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
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
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MarvelApi::class.java)
}

fun getMarvelApiService() = apiService

interface MarvelApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("ts") timeStamp: String = Constant.TIME_STAMP,
        @Query("apikey") apiKey: String = Constant.getApiKey(),
        @Query("hash") hash: String = Constant.getHash(),
        @Query("limit") limit: String = Constant.LIMIT
    ): DataWrapper

    @GET("characters/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timeStamp: String = Constant.TIME_STAMP,
        @Query("apikey") apiKey: String = Constant.getApiKey(),
        @Query("hash") hash: String = Constant.getHash(),
        @Query("limit") limit: String = Constant.LIMIT
    ): DataWrapper
}