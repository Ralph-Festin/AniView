package com.example.aniview.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aniview.data.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginatedAnimeList(
    animeList: List<Anime>,
    onAnimeClick: (Anime) -> Unit
) {
    var page by remember { mutableIntStateOf(0) }
    val pageSize = 8
    val pageCount = (animeList.size + pageSize - 1) / pageSize
    val currentPageItems = animeList.drop(page * pageSize).take(pageSize)

    Column(modifier = Modifier.padding(16.dp)) {
        if (currentPageItems.isEmpty()) {
            Text("No anime found.")
            return@Column
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 1000.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(currentPageItems) { anime ->
                AnimeCard(anime = anime) { onAnimeClick(anime) }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Prev button
            if (page > 0) {
                Button(
                    onClick = { page-- },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous page",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Prev", color = MaterialTheme.colorScheme.onSecondary)
                }
            } else {
                Spacer(Modifier.width(96.dp))
            }

            // Page indicator
            Text(
                text = "Page ${page + 1} of $pageCount",
                style = MaterialTheme.typography.bodyMedium
            )

            // Next button
            if (page < pageCount - 1) {
                Button(
                    onClick = { page++ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Next", color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Next page",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                Spacer(Modifier.width(96.dp))
            }
        }
    }
}
