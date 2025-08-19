package com.example.aniview.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aniview.ui.screens.components.AnimeRow
import com.example.aniview.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
) {
    val searchedAnime by viewModel.searchedAnime

    val rows = if (searchedAnime != null) listOf(
        "Search Results" to searchedAnime!!,
        "Trending" to viewModel.trending,
        "Updated Recently" to viewModel.latest
    ) else listOf(
        "Trending" to viewModel.trending,
        "Updated Recently" to viewModel.latest,
        "Anticipated" to viewModel.anticipated
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        rows.forEach { (title, list) ->
            item {
                AnimeRow(title, list) {
                    navController.navigate("detail/${it.mal_id}")
                }
            }
        }
    }
}
