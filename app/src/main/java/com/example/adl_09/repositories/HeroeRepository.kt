package com.example.adl_09.repositories

import com.example.adl_09.entities.HeroeEntity
import com.example.adl_09.utils.HeroeResponse
import com.example.adl_09.network.ClienteRetrofit
import com.example.adl_09.services.HeroeService
import com.example.adl_09.utils.HeroesResponse

class HeroeRepository(private val heroeService: HeroeService = ClienteRetrofit.getInstanciaRetrofit) {

    suspend fun getAllHeroes() : HeroesResponse = heroeService.getAllHeroes()

    suspend fun getHeroeById(id: Long) : HeroeResponse = heroeService.getHeroeById(id)

    suspend fun createHeroe(heroe: HeroeEntity) : HeroeResponse = heroeService.createHeroe(heroe)

    suspend fun updateHeroe(id: Long, heroe: HeroeEntity) : HeroeResponse = heroeService.updateHeroes(id, heroe)

    suspend fun deleteHeroe(id: Long) : HeroeResponse = heroeService.deleteHeroe(id)
}