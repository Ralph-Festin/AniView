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
    private val _genreMap = mutableStateOf<Map<String, String>>(emptyMap())
    val genreMap: State<Map<String, String>> = _genreMap

    private val _searchedAnime = mutableStateOf<List<Anime>?>(null)
    val searchedAnime: State<List<Anime>?> = _searchedAnime

    val selectedGenres  = mutableStateOf(setOf<String>())
    val selectedRatings = mutableStateOf(setOf<String>())
    val selectedYears   = mutableStateOf(setOf<String>())

    init {
        viewModelScope.launch {
            val fetched = repository
                .fetchGenres()
                .mapNotNull { genre ->
                    genre.name
                        ?.lowercase()
                        ?.let { name -> name to genre.mal_id.toString() }
                }
                .toMap()

            _genreMap.value = fetched
        }
    }

    fun toggleGenre(genre: String) {
        selectedGenres.value = selectedGenres.value.toggle(genre)
    }

    fun toggleRating(rating: String) {
        selectedRatings.value =  if (rating.isBlank()) emptySet() else setOf(rating)
    }

    fun setYear(year: String) {
        selectedYears.value = if (year.isBlank()) emptySet() else setOf(year)
    }

    fun searchAnime(query: String) {
        _searchedAnime.value = null
        if (genreMap.value.isEmpty()) return

        viewModelScope.launch {
            runCatching {
                val selectedGenreIds = selectedGenres.value
                    .mapNotNull { genreMap.value[it.lowercase()] }

                val rating = selectedRatings.value.firstOrNull()
                val year = selectedYears.value.firstOrNull()
                val (startDate, endDate) = year
                    ?.let { "$it-01-01" to "$it-12-31" }
                    ?: (null to null)

                repository.fetchSearchedAnime(
                    q = query.ifBlank { null },
                    r = rating,
                    g = selectedGenreIds.takeIf { it.isNotEmpty() },
                    startDate = startDate,
                    endDate = endDate
                )
                    .filter { anime ->
                        val matchesYear = year == null || anime.aired?.from?.startsWith(year) == true

                        val animeGenreIds = anime.genres?.mapNotNull { it.mal_id?.toString() } ?: emptyList()
                        val matchesGenres = selectedGenreIds.all { it in animeGenreIds }

                        matchesYear && matchesGenres
                    }
                    .sortedByDescending { it.aired?.from }
                    .distinctBy { it.mal_id }
            }
                .onSuccess { results ->
                    _searchedAnime.value = results
                }
                .onFailure {
                    it.printStackTrace()
                    _searchedAnime.value = emptyList()
                }
        }
    }


    fun resetSearchState() {
        selectedGenres.value = emptySet()
        selectedRatings.value = emptySet()
        selectedYears.value = emptySet()
        _searchedAnime.value = null
    }

    fun shouldExitSearch(query: String): Boolean {
        return query.isBlank() &&
                selectedGenres.value.isEmpty() &&
                selectedRatings.value.isEmpty() &&
                selectedYears.value.isEmpty()
    }
    private fun Set<String>.toggle(item: String): Set<String> =
        if (contains(item)) this - item else this + item
}
