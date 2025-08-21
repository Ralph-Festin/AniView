package com.example.aniview.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aniview.viewmodel.SearchViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel,
    onSearchSubmit: ((String) -> Unit)? = null
) {
    var query by remember { mutableStateOf("") }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val yearOptions = (1980..currentYear).map { it.toString() }.reversed()

    Surface(
        color = Color(0xFF673AB7),
        contentColor = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                onSearchSubmit?.let { handleSearch ->
                    SearchField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearchSubmit = {
                            if (viewModel.shouldExitSearch(it)) {
                                viewModel.resetSearchState()
                                onBackClick()
                            } else {
                                viewModel.searchAnime(it)
                                handleSearch(it)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterDropdown(
                    label = "Genre",
                    options = viewModel.genreMap.value.keys.map { it.uppercase() },
                    selectedOptions = viewModel.selectedGenres.value.map { it.uppercase() }.toSet(),
                    onOptionToggle = { displayValue ->
                        viewModel.toggleGenre(displayValue.lowercase())
                    }
                )

                FilterDropdown(
                    label = "Rating",
                    options = listOf("G", "PG", "PG13", "R17"),
                    selectedOptions = viewModel.selectedRatings.value,
                    onOptionToggle = viewModel::toggleRating,
                    singleSelection = true
                )

                FilterDropdown(
                    label = "Year",
                    options = yearOptions,
                    selectedOptions = viewModel.selectedYears.value,
                    onOptionToggle = viewModel::setYear,
                    singleSelection = true
                )
            }
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit
) {
    val textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color.White)
    val colors = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.LightGray
    )

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Search anime by title, genre, etc...",
                fontSize = 14.sp,
                color = Color.LightGray
            )
        },
        singleLine = true,
        modifier = Modifier
            .widthIn(max = 180.dp)
            .height(59.dp),
        textStyle = textStyle,
        colors = colors,
        trailingIcon = {
            IconButton(onClick = { onSearchSubmit(query) }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchSubmit(query) })
    )
}
