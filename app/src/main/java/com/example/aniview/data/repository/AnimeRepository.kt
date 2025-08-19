package com.example.aniview.data.repository

import com.example.aniview.data.model.Anime
import com.example.aniview.network.JikanApiService

class AnimeRepository(private val api: JikanApiService) {

    suspend fun fetchTrending(): List<Anime> = api.getTrendingAnime().data
    suspend fun fetchLatest(): List<Anime> = api.getLatestAnime().data
    suspend fun fetchAnticipated(): List<Anime> = api.getAnticipatedAnime().data
    suspend fun fetchSearchedAnime(q: String?, r: String?, g: List<String>?): List<Anime> = api.searchAnime(q, r, g).data
}
