package com.great.app.repository

import com.great.app.model.DataWrapper
import com.great.app.utils.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {
    @GET("characters")
    fun getCharacters(
        @Query("ts") timeStamp: String = Constant.TIME_STAMP,
        @Query("apikey") apiKey: String = Constant.getApiKey(),
        @Query("hash") hash: String = Constant.getHash(),
        @Query("limit") limit: String = Constant.LIMIT
    ): Call<DataWrapper>

    @GET("characters/{characterId}")
    fun getCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timeStamp: String = Constant.TIME_STAMP,
        @Query("apikey") apiKey: String = Constant.getApiKey(),
        @Query("hash") hash: String = Constant.getHash(),
        @Query("limit") limit: String = Constant.LIMIT
    ): Call<DataWrapper>
}