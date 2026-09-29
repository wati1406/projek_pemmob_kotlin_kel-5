package com.example.math_quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.math_quiz.ui.screens.LevelSelectionScreen
import com.example.math_quiz.ui.screens.MainMenuScreen
import com.example.math_quiz.ui.screens.SettingsScreen
import com.example.math_quiz.ui.screens.SplashScreen
import com.example.math_quiz.ui.theme.MathquizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathquizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by remember { mutableStateOf("SPLASH") }

                    when (currentScreen) {
                        "SPLASH" -> {
                            SplashScreen(
                                modifier = Modifier.padding(innerPadding),
                                onTimeout = { currentScreen = "MAIN_MENU" }
                            )
                        }
                        "MAIN_MENU" -> {
                            MainMenuScreen(
                                modifier = Modifier.padding(innerPadding),
                                currentLevel = 3,
                                onPlayClick = { currentScreen = "LEVEL_SELECTION" },
                                onHighScoreClick = { /* High Score dialog/screen */ },
                                onSettingsClick = { currentScreen = "SETTINGS" }
                            )
                        }
                        "SETTINGS" -> {
                            SettingsScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = { currentScreen = "MAIN_MENU" },
                                onAboutClick = { /* TODO: About dialog */ }
                            )
                        }
                        "LEVEL_SELECTION" -> {
                            LevelSelectionScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = { currentScreen = "MAIN_MENU" },
                                onEasyClick = { /* Navigasi ke game Easy nanti */ },
                                onMediumClick = { /* Navigasi ke game Medium nanti */ },
                                onHardClick = { /* Navigasi ke game Hard nanti */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Splash Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SplashPreview() {
    MathquizTheme {
        SplashScreen()
    }
}

@Preview(name = "Main Menu Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MainMenuPreview() {
    MathquizTheme {
        MainMenuScreen(currentLevel = 3)
    }
}