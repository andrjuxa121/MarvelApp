package com.great.testapp.retrofit

import com.great.testapp.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface RetroService {
    @GET("marvel")
    fun getCharacters(): Call<MutableList<Character>>
}