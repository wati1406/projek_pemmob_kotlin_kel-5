package com.example.math_quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.math_quiz.audio.SoundManager
import com.example.math_quiz.ui.screens.AboutScreen
import com.example.math_quiz.ui.screens.LevelSelectionScreen
import com.example.math_quiz.ui.screens.MainMenuScreen
import com.example.math_quiz.ui.screens.SettingsScreen
import com.example.math_quiz.ui.screens.SplashScreen
import com.example.math_quiz.ui.screens.QuizScreen
import com.example.math_quiz.ui.theme.MathquizTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundManager.init(this)
        enableEdgeToEdge()
        setContent {
            MathquizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    var currentScreen by remember { mutableStateOf("SPLASH") }
                    var selectedLevel by remember { mutableStateOf(1) }
                    var finalScore by remember { mutableStateOf(0) }
                    var showResultDialog by remember { mutableStateOf(false) }

                    // Putar BGM global, ganti ke result.mp3 saat halaman RESULT
                    LaunchedEffect(currentScreen) {
                        when (currentScreen) {
                            "RESULT" -> SoundManager.playResult(context)
                            else     -> SoundManager.playBgm()
                        }
                    }

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
                                onPlayClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "LEVEL_SELECTION"
                                },
                                onSettingsClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "SETTINGS"
                                }
                            )
                        }

                        "SETTINGS" -> {
                            SettingsScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "MAIN_MENU"
                                },
                                onAboutClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "ABOUT"
                                },
                                soundEffectsEnabled = SoundManager.isSoundEffectsEnabled,
                                onSoundEffectsChange = { SoundManager.setSoundEffectsEnabled(it) },
                                musicEnabled = SoundManager.isMusicEnabled,
                                onMusicChange = { SoundManager.setMusicEnabled(it) }
                            )
                        }

                        "ABOUT" -> {
                            AboutScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "SETTINGS"
                                }
                            )
                        }

                        "LEVEL_SELECTION" -> {
                            LevelSelectionScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "MAIN_MENU"
                                },
                                onEasyClick = {
                                    SoundManager.playSfx(SoundManager.SFX.LEVEL)
                                    selectedLevel = 1
                                    currentScreen = "QUIZ"
                                },
                                onMediumClick = {
                                    SoundManager.playSfx(SoundManager.SFX.LEVEL)
                                    selectedLevel = 2
                                    currentScreen = "QUIZ"
                                },
                                onHardClick = {
                                    SoundManager.playSfx(SoundManager.SFX.LEVEL)
                                    selectedLevel = 3
                                    currentScreen = "QUIZ"
                                }
                            )
                        }

                        "QUIZ" -> {
                            QuizScreen(
                                level = selectedLevel,
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "LEVEL_SELECTION"
                                },
                                onOptionSelected = { /* handled inside QuizScreen */ },
                                onCorrectAnswer = {
                                    SoundManager.playSfx(SoundManager.SFX.CORRECT)
                                },
                                onWrongAnswer = {
                                    SoundManager.playSfx(SoundManager.SFX.INCORRECT)
                                },
                                onTimeUp = {
                                    SoundManager.playSfx(SoundManager.SFX.TIME_UP)
                                },
                                onNextClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                },
                                onQuizCompleted = { score ->
                                    finalScore = score
                                    // Pop-up ditampilkan langsung di atas QuizScreen
                                },
                                onNextLevel = {
                                    if (selectedLevel < 3) {
                                        selectedLevel++
                                    } else {
                                        currentScreen = "LEVEL_SELECTION"
                                    }
                                },
                                onHome = {
                                    currentScreen = "MAIN_MENU"
                                }
                            )
                        }

                        "RESULT" -> {
                            // Tetap tampilkan QuizScreen di latar belakang
                            QuizScreen(
                                level = selectedLevel,
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "LEVEL_SELECTION"
                                },
                                onNextLevel = {
                                    if (selectedLevel < 3) {
                                        selectedLevel++
                                    } else {
                                        currentScreen = "LEVEL_SELECTION"
                                    }
                                    currentScreen = "QUIZ"
                                },
                                onHome = {
                                    currentScreen = "MAIN_MENU"
                                }
                            )

                            com.example.math_quiz.ui.screens.QuizResultDialog(
                                score = finalScore,
                                onNextLevel = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    if (selectedLevel < 3) {
                                        selectedLevel++
                                    } else {
                                        currentScreen = "LEVEL_SELECTION"
                                    }
                                    currentScreen = "QUIZ"
                                },
                                onPlayAgain = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    finalScore = 0
                                    currentScreen = "QUIZ"
                                },
                                onHome = {
                                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                                    currentScreen = "MAIN_MENU"
                                }
                            )
                        }

                        "CORRECT" -> {
                            com.example.math_quiz.ui.screens.QuizScreenCorrectState(
                                onNextQuestion = { currentScreen = "QUIZ" }

                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SoundManager.resumeBgm()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseBgm()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
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