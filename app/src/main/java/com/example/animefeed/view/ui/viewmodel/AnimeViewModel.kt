package com.example.animefeed.view.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animefeed.AnimeApplication
import com.example.animefeed.model.Anime
import com.example.animefeed.model.Result
import com.example.animefeed.repository.AnimeRepository
import com.example.animefeed.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AnimeViewModel(
    app: Application,
    val animeRepository: AnimeRepository
) : AndroidViewModel(app){

    val animes:MutableLiveData<Resource<Anime>> = MutableLiveData()



    val searchAnimes:MutableLiveData<Resource<Anime>> = MutableLiveData()


    init {
        getAnimes("naruto")
    }


    // coroutine function with viewModelScope as coroutine need scope
    fun getAnimes(animeName:String) = viewModelScope.launch {
//        animes.postValue(Resource.Loading())
//        val response = animeRepository.getAnime(animeName,animePage)
//
//        //postValue with liveDate and check response
//        animes.postValue(handleAnimeResponse(response))

        safeAnimeCall(animeName)


    }

    fun searchAnimes(searchQuery: String) = viewModelScope.launch {
//        searchAnimes.postValue(Resource.Loading())
//        val response = animeRepository.searchAnime(searchQuery,searchAnimePage)
//
//        //postValue with liveDate and check response
//        searchAnimes.postValue(handleSearchAnimeResponse(response))
        safeSearchAnimeCall(searchQuery)
    }


    //For handle our response
    private fun handleAnimeResponse(response:Response<Anime>) : Resource<Anime>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchAnimeResponse(response:Response<Anime>) : Resource<Anime>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveAnime(anime: Result) = viewModelScope.launch {
        animeRepository.upsert(anime)
    }

    fun getSavdAnime() = animeRepository.getSavedAnime()

    fun deleteAnime(anime: Result) = viewModelScope.launch {
        animeRepository.deteteAnime(anime)
    }


    private suspend fun safeAnimeCall(animeName:String){
        animes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = animeRepository.getAnime(animeName)

                //postValue with liveDate and check response
                animes.postValue(handleAnimeResponse(response))
            } else {
                animes.postValue(Resource.Error("No internet connection"))
            }

        } catch (t:Throwable){
            when(t){
                is IOException -> animes.postValue(Resource.Error("Network Failure"))
                else -> animes.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private suspend fun safeSearchAnimeCall(searchQuery: String){
        searchAnimes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = animeRepository.searchAnime(searchQuery)

                //postValue with liveDate and check response
                searchAnimes.postValue(handleSearchAnimeResponse(response))
            } else {
                searchAnimes.postValue(Resource.Error("No internet connection"))
            }

        } catch (t:Throwable){
            when(t){
                is IOException -> searchAnimes.postValue(Resource.Error("Network Failure"))
                else -> searchAnimes.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<AnimeApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}