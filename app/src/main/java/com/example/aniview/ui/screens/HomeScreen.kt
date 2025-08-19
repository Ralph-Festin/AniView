package com.example.aniview.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aniview.data.model.Anime
import com.example.aniview.ui.screens.components.AnimeRow
import com.example.aniview.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
) {
    val searchedAnime by viewModel.searchedAnime

    val rows: List<Pair<String, List<Anime>>> = searchedAnime?.let {
        listOf("Search Results" to it)
    } ?: listOf(
        "Trending" to viewModel.trending,
        "Updated Recently" to viewModel.latest,
        "Anticipated" to viewModel.anticipated
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        if (searchedAnime?.isEmpty() == true) {
            item {
                Text("No results found.")
            }
        } else {
            items(rows, key = { it.first }) { (title, list) ->
                AnimeRow(title = title, animeList = list) { selectedAnime ->
                    navController.navigate("detail/${selectedAnime.mal_id}")
                }
            }
        }
    }
}
