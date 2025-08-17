package com.example.aniview.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aniview.data.model.Anime
import com.example.aniview.data.repository.AnimeRepository
import com.example.aniview.network.RetrofitInstance
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    private val repository = AnimeRepository(RetrofitInstance.api)

    var trending by mutableStateOf<List<Anime>>(emptyList())
        private set

    var latest by mutableStateOf<List<Anime>>(emptyList())
        private set

    var anticipated by mutableStateOf<List<Anime>>(emptyList())
        private set

    private var _searchedAnime = mutableStateOf<List<Anime>?>(null)
    val searchedAnime: State<List<Anime>?> = _searchedAnime


    fun searchAnime(q:String){
        viewModelScope.launch {
            _searchedAnime.value = repository.fetchSearchedAnime(q);
        }
    }


    init {
        viewModelScope.launch {
            trending = repository.fetchTrending()
            latest = repository.fetchLatest()
            anticipated = repository.fetchAnticipated()
        }
    }
}
