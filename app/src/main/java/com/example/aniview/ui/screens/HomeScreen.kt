package com.example.aniview.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    val searchedAnime by viewModel.searchedAnime


    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search anime...") }
                )
                Button(
                    onClick = { viewModel.searchAnime(search) }
                ) {
                    Text("Search")
                }
            }
        }

        item {
            searchedAnime?.let { result ->
                AnimeRow("Search: $search", result) {
                    navController.navigate("detail/${it.mal_id}")
                }
            }
        }
        item {
            AnimeRow("Trending", viewModel.trending) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
        item {
            AnimeRow("Updated Recently", viewModel.latest) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
        item {
            AnimeRow("Anticipated", viewModel.anticipated) {
                navController.navigate("detail/${it.mal_id}")
            }
        }
    }
}
