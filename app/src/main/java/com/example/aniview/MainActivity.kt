package com.example.aniview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aniview.navigation.NavGraph
import com.example.aniview.ui.layout.TopBar
import com.example.aniview.ui.theme.AniViewTheme
import com.example.aniview.viewmodel.HomeViewModel
import com.example.aniview.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniViewTheme {
                AniViewApp()
            }
        }
    }
}

@Composable
fun AniViewApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isHome = currentRoute == "home"

    val homeViewModel: HomeViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()

    Scaffold(
        topBar = {
            TopBar(
                title = if (isHome) "AniView" else "Details",
                showBackButton = !isHome,
                onBackClick = { navController.popBackStack() },
                onSearchSubmit = { query ->
                    searchViewModel.searchAnime(query)
                    if (!isHome) navController.popBackStack("home", false)
                }
            )
        },
        content = { padding ->
            NavGraph(
                navController = navController,
                modifier = Modifier.padding(padding),
                homeViewModel = homeViewModel,
                searchViewModel = searchViewModel,
                contentPadding = padding
            )
        }
    )
}
