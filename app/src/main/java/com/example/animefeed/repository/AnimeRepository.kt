package com.example.animefeed.repository

import com.example.animefeed.db.AnimeDatabase
import com.example.animefeed.api.RetrofitInstance
import com.example.animefeed.model.Result

class AnimeRepository(
    //access db function and API
    val db: AnimeDatabase
) {

    /*
    queries our api for the animes
     */
    suspend fun getAnime(animeName:String) =
       RetrofitInstance.api.getAnimes(animeName)

    /*
    queries our api for search  animes
     */
    suspend fun searchAnime(animeName:String) =
        RetrofitInstance.api.getAnimes(animeName)


    /*
    function to insert anime to db
     */
    suspend fun  upsert(anime:Result) = db.getAnimeDao().upsert(anime)


    /*
    function to get saved anime from db
     */
    fun getSavedAnime() = db.getAnimeDao().getAllAnine()


    /*
   function to delete anime from db
    */
    suspend fun deteteAnime(anime: Result) = db.getAnimeDao().deleteAnime(anime)
}