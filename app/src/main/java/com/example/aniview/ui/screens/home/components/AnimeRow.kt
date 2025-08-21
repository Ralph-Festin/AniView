package com.example.aniview.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aniview.data.model.Anime

@Composable
fun AnimeRow(
    title: String,
    animeList: List<Anime>,
    isExpanded: Boolean,
    onMoreClick: () -> Unit,
    onAnimeClick: (Anime) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        if (isExpanded) {
            PaginatedAnimeList(animeList, onAnimeClick)
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animeList) { anime ->
                    AnimeCard(anime = anime) {
                        onAnimeClick(anime)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "More...",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onMoreClick() },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
