package com.example.aniview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.aniview.navigation.AniNavGraph
import com.example.aniview.ui.theme.AniViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniViewTheme {
                Scaffold() { innerPadding ->
                    val navController = rememberNavController()
                    AniNavGraph(navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

