package com.example.aniview.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aniview.data.model.Anime
import com.example.aniview.ui.screens.home.components.AnimeRow
import com.example.aniview.viewmodel.ExpandedCategory
import com.example.aniview.viewmodel.HomeViewModel
import com.example.aniview.viewmodel.SearchViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    val searchedAnime by searchViewModel.searchedAnime
    val expandedCategory = viewModel.expandedCategory

    val rows: List<Pair<String, List<Anime>>> = when (expandedCategory) {
        ExpandedCategory.TRENDING -> listOf("Trending" to viewModel.trending)
        ExpandedCategory.LATEST -> listOf("Updated Recently" to viewModel.latest)
        ExpandedCategory.ANTICIPATED -> listOf("Anticipated" to viewModel.anticipated)
        ExpandedCategory.NONE -> searchedAnime?.let {
            listOf("Search Results" to it)
        } ?: listOf(
            "Trending" to viewModel.trending,
            "Updated Recently" to viewModel.latest,
            "Anticipated" to viewModel.anticipated
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() * 0.3f,
            bottom = contentPadding.calculateBottomPadding() * 0.3f,
            start = 4.dp,
            end = 4.dp
        )
    ) {
        if (searchedAnime?.isEmpty() == true) {
            item { Text("No results found.") }
        } else {
            items(rows, key = { it.first }) { (title, list) ->
                val category = when (title) {
                    "Trending" -> ExpandedCategory.TRENDING
                    "Updated Recently" -> ExpandedCategory.LATEST
                    "Anticipated" -> ExpandedCategory.ANTICIPATED
                    else -> ExpandedCategory.NONE
                }

                AnimeRow(
                    title = title,
                    animeList = list,
                    isExpanded = expandedCategory == category,
                    onMoreClick = { viewModel.expandCategory(category) },
                    onAnimeClick = { selectedAnime ->
                        navController.navigate("detail/${selectedAnime.mal_id}")
                    }
                )
            }
        }
    }
}
