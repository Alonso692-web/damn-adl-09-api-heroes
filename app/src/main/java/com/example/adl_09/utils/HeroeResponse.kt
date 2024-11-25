package com.example.adl_09.utils

import com.example.adl_09.entities.HeroeEntity

data class HeroeResponse(
    val estado: Int,
    val msg: String,
    val heroes: HeroeEntity,
    val links: List<Link>
)
