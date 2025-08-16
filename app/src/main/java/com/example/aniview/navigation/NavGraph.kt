package com.example.aniview.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aniview.ui.screens.DetailScreen
import com.example.aniview.ui.screens.HomeScreen
import com.example.aniview.viewmodel.HomeViewModel

@Composable
fun AniNavGraph(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(viewModel = homeViewModel, navController = navController)
        }

        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()

            val allAnime = homeViewModel.trending +
                    homeViewModel.latest +
                    homeViewModel.anticipated

            val anime = allAnime.find { it.mal_id == animeId }

            if (anime != null) {
                DetailScreen(anime = anime)
            } else {
                Text("Anime not found.")
            }
        }
    }
}