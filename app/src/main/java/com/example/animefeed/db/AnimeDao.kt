package com.example.animefeed.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.animefeed.model.Result

/*
Data Access Object
This is an interface for accessing database
Here we define functions to access the local database
save animes, read animes, delete animes
 */

@Dao
interface AnimeDao {

    /*
    function to insert or update an article
    conflictStrategy is to define what to do, if conflict occurs in the database, like already exists in database
    here we, REPLACE
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(anime: Result):Long

    /*
    query function to return all animes available in our database
    LiveData: class of Android architecture components that enables fragments to subscribe to changes of live data
    when data changes, liveData will notify all the fragments so they can update the views
    useful in rotation
     */
    @Query("SELECT * FROM animes")
    fun getAllAnine(): LiveData<List<Result>>


    /*
    Delete function
     */
    @Delete
    suspend fun deleteAnime(anime: Result)
}