package com.example.adl_09.utils

import com.example.adl_09.entities.HeroeEntity

data class HeroesResponse(
    val estado: Int,
    val msg: String,
    val heroes: List<HeroeEntity>,
    val links: List<Link>
)