package com.example.aniview.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniview.Genres
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

    fun searchAnime(query: String) {
        viewModelScope.launch {
            val parsed = parseMultiFilterQuery(query)
            _searchedAnime.value = repository.fetchSearchedAnime(
                q = parsed.actualQuery.ifBlank { null },
                r = parsed.rating,
                g = parsed.genres?.takeIf { it.isNotEmpty() }
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

    private fun parseMultiFilterQuery(q: String): ParsedQuery {
        val tokens = q.split(",", " ")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        var rating: String? = null
        val genresList = mutableListOf<String>()
        val keywords = mutableListOf<String>()

        for (token in tokens) {
            when {
                token in listOf("g", "pg", "pg13", "r", "r+", "rx") -> rating = token

                Genres.VALID_GENRES_MAP.containsKey(token.replaceFirstChar { it.uppercase() }) -> {
                    val normalizedToken = token.replaceFirstChar { it.uppercase() }
                    Genres.VALID_GENRES_MAP[normalizedToken]?.let { genresList.add(it.toString()) }
                }

                else -> keywords.add(token)
            }
        }

        val actualQuery = keywords.joinToString(" ")
        return ParsedQuery(actualQuery, rating, genresList)
    }

    data class ParsedQuery(
        val actualQuery: String,
        val rating: String?,
        val genres: List<String>?
    )
}
