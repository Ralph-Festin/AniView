package com.example.aniview.data.model

data class Anime(
    val mal_id: Int,
    val title: String,
    val images: Images,
    val synopsis: String,
    val status: String,
    val rating: String,
    val episodes: Int?,
    val aired: Aired?
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
