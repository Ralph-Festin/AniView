package com.example.aniview.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniview.data.model.Anime
import com.example.aniview.data.model.Genre
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

    private var genreMap = emptyMap<String, String>() // genre name → genre ID

    private var _searchedAnime = mutableStateOf<List<Anime>?>(null)
    val searchedAnime: State<List<Anime>?> = _searchedAnime

    init {
        viewModelScope.launch {
            trending = repository.fetchTrending()
            latest = repository.fetchLatest()
            anticipated = repository.fetchAnticipated()

            val genres = repository.fetchGenres()
            genreMap = genres
                .filter { it.name != null }
                .associate { it.name!!.lowercase() to it.mal_id.toString() }
        }
    }

    fun searchAnime(query: String) {
        if (query.isBlank()) {
            _searchedAnime.value = null
            return
        }

        viewModelScope.launch {
            try {
                val parsed = parseMultiFilterQuery(query)

                val results = repository.fetchSearchedAnime(
                    q = parsed.actualQuery?.ifBlank { null },
                    r = parsed.rating,
                    g = parsed.genres?.takeIf { it.isNotEmpty() },
                    startDate = parsed.startDate,
                    endDate = parsed.endDate
                )

                val targetYear = parsed.startDate?.take(4)

                _searchedAnime.value = results
                    .filter { anime ->
                        anime.aired?.from?.startsWith(targetYear ?: "") == true
                    }
                    .sortedByDescending { it.aired?.from }
                    .distinctBy { it.mal_id }

            } catch (e: Exception) {
                e.printStackTrace()
                _searchedAnime.value = emptyList()
            }
        }
    }

    private fun parseMultiFilterQuery(q: String): ParsedQuery {
        val tokens = q.split(",", " ")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        var rating: String? = null
        var startDate: String? = null
        var endDate: String? = null
        val genresList = mutableListOf<String>()
        val keywords = mutableListOf<String>()

        for (token in tokens) {
            when {
                token in listOf("g", "pg", "pg13", "r", "r+", "rx") -> rating = token

                genreMap.containsKey(token) -> {
                    genreMap[token]?.let { genreId -> genresList.add(genreId) }
                }

                token.matches(Regex("^\\d{4}$")) -> {
                    startDate = "$token-01-01"
                    endDate = "$token-12-31"
                }

                else -> keywords.add(token)
            }
        }

        val actualQuery = keywords.joinToString(" ").ifBlank { null }
        return ParsedQuery(actualQuery, rating, genresList, startDate, endDate)
    }

    data class ParsedQuery(
        val actualQuery: String?,
        val rating: String?,
        val genres: List<String>?,
        val startDate: String?,
        val endDate: String?
    )
}
