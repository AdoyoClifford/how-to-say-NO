package com.adoyo.howtosayno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.adoyo.howtosayno.ui.screen.NoReasonScreen
import com.adoyo.howtosayno.ui.theme.HowToSayNOTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the How to Say NO app
 * 
 * This activity serves as the entry point for the application and hosts
 * the NoReasonScreen composable with Material 3 Expressive theming.
 * 
 * Requirements addressed:
 * - 1.1, 1.2, 1.3: Main app entry point and UI integration
 * - 6.1: Material 3 Expressive theme application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HowToSayNOTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoReasonScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}