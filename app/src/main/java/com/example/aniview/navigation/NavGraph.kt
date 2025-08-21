package com.example.aniview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aniview.ui.screens.DetailScreen
import com.example.aniview.ui.screens.home.HomeScreen
import com.example.aniview.viewmodel.HomeViewModel
import com.example.aniview.viewmodel.SearchViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                searchViewModel = searchViewModel,
                navController = navController,
                contentPadding = contentPadding
            )
        }

        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()
            val allAnime = homeViewModel.trending +
                    homeViewModel.latest +
                    homeViewModel.anticipated +
                    (searchViewModel.searchedAnime.value ?: emptyList())

            val anime = allAnime.find { it.mal_id == animeId }

            if (anime != null) {
                DetailScreen(anime = anime)
            } else {
                Text("Anime not found.")
            }
        }
    }
}
