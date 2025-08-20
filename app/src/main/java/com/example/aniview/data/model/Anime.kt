package com.example.aniview.data.model

data class Anime(
    val mal_id: Int,
    val title: String?,
    val images: Images,
    val synopsis: String?,
    val status: String?,
    val rating: String?,
    val episodes: Int?,
    val aired: Aired?,
    val genres: List<Genre>?
)

data class Aired(
    val from: String?,
    val to: String?
)

data class Images(
    val jpg: Jpg
)

data class Jpg(
    val image_url: String
)

data class Genre(
    val mal_id: Int,
    val type: String?,
    val name: String?,
    val url: String?
)

data class AnimeResponse(val data: List<Anime>)
data class GenreResponse(val data: List<Genre>)
