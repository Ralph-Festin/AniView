package com.example.aniview.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aniview.Genres
import com.example.aniview.data.model.Aired
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


    fun searchAnime(q: String){
        viewModelScope.launch {
            Log.d("ViewModelDebut", "Parsing search query: '$q'")

            var actualQuery: String? = null
            var rating: String? = null
            var genresList: List<String>? = null

            val lowerCaseQuery = q.lowercase().trim()

            if (lowerCaseQuery in listOf("g", "pg", "pg13", "r", "r+", "rx")) {
                rating = when (lowerCaseQuery) {
                    "g" -> "g"
                    "pg" -> "pg"
                    "r" -> "r"
                    "r+" -> "r+"
                    "rx" -> "rx"
                    else -> null
                }

            }
            else if (Genres.VALID_GENRES_MAP.keys.any { it.equals(q.trim(), ignoreCase = true) }) {
                val matchedGenreName = Genres.VALID_GENRES_MAP.keys.first { it.equals(q.trim(), ignoreCase = true) }
                val genreId = Genres.VALID_GENRES_MAP[matchedGenreName]

                if (genreId != null) {
                    genresList = listOf(genreId.toString())
                }
            }
            else {
                actualQuery = q
            }

            _searchedAnime.value = repository.fetchSearchedAnime(
                q = actualQuery ?: "",
                r = rating,
                g = genresList
            )
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
