package com.example.aniview.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aniview.ui.screens.components.AnimeRow
import com.example.aniview.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController
) {
    val trending = viewModel.trending
    val latest = viewModel.latest
    val anticipated = viewModel.anticipated

    LazyColumn {
        item {
            AnimeRow("Trending", trending) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
        item {
            AnimeRow("Updated Recently", latest) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
        item {
            AnimeRow("Anticipated", anticipated) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
    }
}
