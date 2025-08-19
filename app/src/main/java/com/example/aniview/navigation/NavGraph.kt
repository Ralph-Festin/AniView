package com.example.aniview.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aniview.ui.screens.DetailScreen
import com.example.aniview.ui.screens.HomeScreen
import com.example.aniview.viewmodel.HomeViewModel

@Composable
fun AniNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(viewModel = homeViewModel, navController = navController)
        }

        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()
            val allAnime = homeViewModel.trending +
                    homeViewModel.latest +
                    homeViewModel.anticipated +
                    (homeViewModel.searchedAnime.value ?: emptyList())

            val anime = allAnime.find { it.mal_id == animeId }

            if (anime != null) {
                DetailScreen(anime = anime)
            } else {
                Text("Anime not found.")
            }
        }
    }
}
