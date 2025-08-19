package com.example.aniview.network

import com.example.aniview.data.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {

    @GET("top/anime")
    suspend fun getTrendingAnime(@Query("filter") filter: String = "airing"): AnimeResponse

    @GET("seasons/now")
    suspend fun getLatestAnime(): AnimeResponse

    @GET("seasons/upcoming")
    suspend fun getAnticipatedAnime(): AnimeResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") q: String?,
        @Query("rating") rating: String?,
        @Query("genres") genres: List<String>?
    ): AnimeResponse
}



