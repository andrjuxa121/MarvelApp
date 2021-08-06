package com.great.testapp.retrofit

import com.great.testapp.model.Character
import com.great.testapp.model.DataWrapper
import com.great.testapp.utils.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET("characters")
    fun getCharacters(
        @Query("ts") ts: String = Constant.TIME_STAMP,
        @Query("apikey") apikey: String = Constant.getApiKey(),
        @Query("hash") hash: String = Constant.getHash(),
        @Query("limit") limit: String = Constant.LIMIT
    ): Call<DataWrapper>
}