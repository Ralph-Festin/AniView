package com.example.aniview.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniview.data.model.Anime
import com.example.aniview.data.repository.AnimeRepository
import com.example.aniview.network.RetrofitInstance
import kotlinx.coroutines.launch

enum class ExpandedCategory { NONE, TRENDING, LATEST, ANTICIPATED }

class HomeViewModel : ViewModel() {

    private val repository = AnimeRepository(RetrofitInstance.api)

    var trending by mutableStateOf(emptyList<Anime>())
        private set

    var latest by mutableStateOf(emptyList<Anime>())
        private set

    var anticipated by mutableStateOf(emptyList<Anime>())
        private set

    var expandedCategory by mutableStateOf(ExpandedCategory.NONE)
        private set

    val currentTitle: String
        get() = when (expandedCategory) {
            ExpandedCategory.TRENDING -> "Trending"
            ExpandedCategory.LATEST -> "Updated Recently"
            ExpandedCategory.ANTICIPATED -> "Anticipated"
            ExpandedCategory.NONE -> "AniView"
        }

    fun expandCategory(category: ExpandedCategory) {
        expandedCategory = category
    }

    fun collapseCategory() {
        expandedCategory = ExpandedCategory.NONE
    }

    init {
        viewModelScope.launch {
            trending = repository.fetchTrending()
            latest = repository.fetchLatest()
            anticipated = repository.fetchAnticipated()
        }
    }
}
