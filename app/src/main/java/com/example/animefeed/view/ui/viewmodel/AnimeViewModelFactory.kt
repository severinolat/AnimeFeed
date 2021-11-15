package com.example.animefeed.view.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animefeed.repository.AnimeRepository


//Define how our anime viewmodel should be create

class AnimeViewModelFactory (
    val app: Application,
    val animeRepository: AnimeRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnimeViewModel(app,animeRepository) as T
    }
}