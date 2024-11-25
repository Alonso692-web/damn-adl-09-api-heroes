package com.example.adl_09.network

import com.example.adl_09.services.HeroeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRetrofit {
    private val BASE_URL = "https://adl-wcbdf-eje-10-superheroes.onrender.com/api/v1/"
    val getInstanciaRetrofit: HeroeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeroeService::class.java)
    }

}