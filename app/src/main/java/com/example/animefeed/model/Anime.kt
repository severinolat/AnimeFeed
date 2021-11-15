package com.example.animefeed.model

data class Anime(
    val last_page: Int,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val results: ArrayList<Result>
)