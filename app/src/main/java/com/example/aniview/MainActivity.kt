package com.example.aniview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.aniview.navigation.AniNavGraph
import com.example.aniview.ui.theme.AniViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniViewTheme {
                val navController = rememberNavController()
                AniNavGraph(navController)
            }
        }
    }
}

