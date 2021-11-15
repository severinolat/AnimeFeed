package com.example.animefeed.api

import com.example.animefeed.model.Anime
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// here we define single request that we can execute from the code

//we use Api interface to access api for request
//function to get all animes from the api
// we need to specify the type of http request -GET here
//and we return the responses from the API
interface AnimeAPI {



    @GET("search/anime")
    // using coroutine for get anime async
    suspend fun getAnimes(
        @Query("q")
        animeName: String ="naruto", //default to naruto

    ): Response<Anime> //return response


    @GET("search/anime")
    // using coroutine for get anime async
    suspend fun searchAnimes(
        @Query("q")
        searchQuery: String,

    ): Response<Anime>



}