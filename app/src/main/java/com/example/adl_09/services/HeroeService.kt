package com.example.adl_09.services

import com.example.adl_09.entities.HeroeEntity
import com.example.adl_09.utils.HeroeResponse
import com.example.adl_09.utils.HeroesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HeroeService {

    @GET("heroes")
    suspend fun getAllHeroes(): HeroesResponse

    @GET("heroes/{id}")
    suspend fun getHeroeById(@Path("id") id: Long): HeroeResponse

    @POST("heroes")
    suspend fun createHeroe(@Body heroe: HeroeEntity): HeroeResponse

    @PUT("heroes/{id}")
    suspend fun updateHeroes(@Path("id") id: Long, @Body heroe: HeroeEntity): HeroeResponse

    @DELETE("heroes/{id}")
    suspend fun deleteHeroe(@Path("id") id: Long): HeroeResponse


}