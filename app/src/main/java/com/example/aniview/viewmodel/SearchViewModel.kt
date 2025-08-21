package com.example.aniview.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniview.data.model.Anime
import com.example.aniview.data.repository.AnimeRepository
import com.example.aniview.network.RetrofitInstance
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = AnimeRepository(RetrofitInstance.api)

    val genreMap: Map<String, String> get() = _genreMap
    private var _genreMap = emptyMap<String, String>()

    private val _searchedAnime = mutableStateOf<List<Anime>?>(null)
    val searchedAnime: State<List<Anime>?> = _searchedAnime

    private val validRatings = setOf("g", "pg", "pg13", "r17", "r", "rx")

    init {
        viewModelScope.launch {
            _genreMap = repository.fetchGenres()
                .mapNotNull { genre ->
                    genre.name?.lowercase()?.let { name -> name to genre.mal_id.toString() }
                }
                .toMap()
        }
    }

    fun searchAnime(query: String) {
        _searchedAnime.value = null
        if (query.isBlank() || genreMap.isEmpty()) return

        viewModelScope.launch {
            runCatching {
                val parsed = parseMultiFilterQuery(query)

                val results = repository.fetchSearchedAnime(
                    q = parsed.actualQuery,
                    r = parsed.rating,
                    g = parsed.genres,
                    startDate = parsed.startDate,
                    endDate = parsed.endDate
                )

                val targetYear = parsed.startDate?.take(4)

                results.filter {
                    it.aired?.from?.startsWith(targetYear.orEmpty()) == true
                }.sortedByDescending { it.aired?.from }
                    .distinctBy { it.mal_id }
            }.onSuccess {
                _searchedAnime.value = it
            }.onFailure {
                it.printStackTrace()
                _searchedAnime.value = emptyList()
            }
        }
    }

    private fun parseMultiFilterQuery(q: String): ParsedQuery {
        val tokens = q.split(",", ", ")
            .mapNotNull { it.trim().lowercase().takeIf { it.isNotEmpty() } }

        val rating = tokens.firstOrNull { it in validRatings }
        val year = tokens.find { it.matches(Regex("^\\d{4}$")) }
        val (startDate, endDate) = year?.let { "$it-01-01" to "$it-12-31" } ?: (null to null)

        val tokenCounts = tokens.groupingBy { it }.eachCount()
        val genresList = mutableListOf<String>()
        val keywords = mutableListOf<String>()

        for (token in tokens) {
            when {
                token == rating || token == year -> Unit

                genreMap.containsKey(token) -> {
                    val count = tokenCounts[token] ?: 0
                    if (count > 1) {
                        if (!genresList.contains(genreMap[token])) {
                            genreMap[token]?.let { genresList.add(it) }
                        } else {
                            keywords.add(token)
                        }
                    } else {
                        genreMap[token]?.let { genresList.add(it) }
                    }
                }

                else -> keywords.add(token)
            }
        }

        return ParsedQuery(
            actualQuery = keywords.joinToString(" ").ifBlank { null },
            rating = rating,
            genres = genresList.takeIf { it.isNotEmpty() },
            startDate = startDate,
            endDate = endDate
        )
    }

    data class ParsedQuery(
        val actualQuery: String?,
        val rating: String?,
        val genres: List<String>?,
        val startDate: String?,
        val endDate: String?
    )
}
