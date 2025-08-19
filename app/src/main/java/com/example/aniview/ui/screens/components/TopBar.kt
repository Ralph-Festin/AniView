package com.example.aniview.ui.screens.components

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onSearchSubmit: ((String) -> Unit)? = null
) {
    var query by remember { mutableStateOf("") }

    TopAppBar(
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF673AB7),
            titleContentColor = Color.White
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)

                onSearchSubmit?.let {
                    SearchField(query, { query = it }, it)
                }
            }
        }
    )
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
        placeholder = { Text("Search anime by title, genre, etc...", fontSize = 14.sp, color = Color.LightGray) },
        singleLine = true,
        modifier = Modifier.width(300.dp).height(59.dp),
        textStyle = textStyle,
        colors = colors,
        trailingIcon = {
            IconButton(onClick = { onSearchSubmit(query) }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchSubmit(query) })
    )
}
