package com.example.math_quiz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = QuizBlueLight,
    secondary = QuizGreenLight,
    tertiary = QuizYellow,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = QuizTextDark,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = QuizBlue,
    secondary = QuizGreen,
    tertiary = QuizYellow,
    background = QuizBgLight,
    surface = QuizBgCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = QuizTextDark,
    onBackground = QuizTextDark,
    onSurface = QuizTextDark
)

@Composable
fun MathquizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set dynamicColor default to false so custom colorful theme is preserved
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}