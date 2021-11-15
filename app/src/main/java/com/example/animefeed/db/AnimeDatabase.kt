package com.example.animefeed.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.animefeed.model.Result


@Database(
    entities = [Result::class],
    version = 2, exportSchema = true
)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun getAnimeDao():AnimeDao

    companion object{
        //Volatile so that other threads can see immediately when thread changes this instance

        @Volatile
        private var instance: AnimeDatabase?= null

        //Lock variable to sync the instance, to make sure there is only one instance of ArticleDatabase at once
        private val LOCK = Any()

        //operator function called whenever we create the instance of our database
        //if instance is null we sync it with LOCK:
        //LOCK: so that anything that happens inside this function cant be accessed by other threads at the same time

        //sync instance if only null
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            // null check "to make sure that there is not another thread that sets the instance to something while we already set it
            instance?: createDatabase(context).also{ instance =it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AnimeDatabase::class.java,
                "anime_db.db"
            ).build()
    }
}