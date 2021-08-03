package com.great.testapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroBuilder {
    private var Retro: Retrofit? = null

    fun getInstance(baseUrl: String): Retrofit {
        if(Retro == null) {
            Retro = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return Retro!!
    }
}