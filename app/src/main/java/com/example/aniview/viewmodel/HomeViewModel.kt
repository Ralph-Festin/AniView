package com.example.aniview.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniview.data.model.Anime
import com.example.aniview.data.repository.AnimeRepository
import com.example.aniview.network.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = AnimeRepository(RetrofitInstance.api)

    var trending by mutableStateOf(emptyList<Anime>())
        private set

    var latest by mutableStateOf(emptyList<Anime>())
        private set

    var anticipated by mutableStateOf(emptyList<Anime>())
        private set

    init {
        viewModelScope.launch {
            trending = repository.fetchTrending()
            latest = repository.fetchLatest()
            anticipated = repository.fetchAnticipated()

        }
    }
}
