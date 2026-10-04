package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.math_quiz.R
import com.example.math_quiz.ui.theme.MathquizTheme

/**
 * Layar Level Selection
 * - Background: aset level_selection_bg
 * - Tombol back di kiri atas (animasi spring bounce)
 * - 3 tombol level: Easy, Medium, Hard
 *   dengan idle floating, pulse glow shadow, spring press + sedikit rotasi
 */
@Composable
fun LevelSelectionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEasyClick: () -> Unit = {},
    onMediumClick: () -> Unit = {},
    onHardClick: () -> Unit = {}
) {
    // --- Animasi idle mengambang (beda fase tiap tombol agar terasa hidup) ---
    val infiniteTransition = rememberInfiniteTransition(label = "levelIdle")

    val floatEasy by infiniteTransition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatEasy"
    )
    val floatMedium by infiniteTransition.animateFloat(
        initialValue = 3f, targetValue = -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatMedium"
    )
    val floatHard by infiniteTransition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatHard"
    )

    // Pulse glow — alpha shadow berdenyut
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.20f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(modifier = modifier.fillMaxSize()) {

        // Background
        Image(
            painter = painterResource(id = R.drawable.level_selection_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Tombol Back — spring bounce saat ditekan
        BackButtonAnimated(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 50.dp)
        )

        // Konten utama: 3 tombol level
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            LevelButton(
                imageResId = R.drawable.btn_level_easy,
                contentDescription = "Easy - Build your basics",
                floatOffset = floatEasy,
                glowAlpha = glowAlpha,
                shadowColor = Color(0xFF2ECC71),
                onClick = onEasyClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            LevelButton(
                imageResId = R.drawable.btn_level_medium,
                contentDescription = "Medium - A bigger challenge",
                floatOffset = floatMedium,
                glowAlpha = glowAlpha,
                shadowColor = Color(0xFFF39C12),
                onClick = onMediumClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            LevelButton(
                imageResId = R.drawable.btn_level_hard,
                contentDescription = "Hard - Test your skills",
                floatOffset = floatHard,
                glowAlpha = glowAlpha,
                shadowColor = Color(0xFFE74C3C),
                onClick = onHardClick
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * Tombol level dengan:
 * - Idle floating (offset Y dari animasi)
 * - Pulse glow shadow berwarna sesuai level
 * - Spring bounce + sedikit rotasi saat ditekan
 */
@Composable
private fun LevelButton(
    imageResId: Int,
    contentDescription: String,
    floatOffset: Float,
    glowAlpha: Float,
    shadowColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.91f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 380f),
        label = "levelBtnScale"
    )
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) -1.8f else 0f,
        animationSpec = tween(durationMillis = 110, easing = FastOutSlowInEasing),
        label = "levelBtnRotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = floatOffset.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
            }
            .shadow(
                elevation = if (isPressed) 2.dp else 14.dp,
                shape = RoundedCornerShape(50.dp),
                spotColor = shadowColor.copy(alpha = glowAlpha)
            )
            .clip(RoundedCornerShape(50.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Tombol Back dengan spring bounce saat ditekan
 */
@Composable
private fun BackButtonAnimated(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 500f),
        label = "backBtnScale"
    )

    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Back",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LevelSelectionScreenPreview() {
    MathquizTheme {
        LevelSelectionScreen()
    }
}
