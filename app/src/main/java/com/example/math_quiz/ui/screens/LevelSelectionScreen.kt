package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.theme.MathquizTheme

/**
 * Layar Level Selection
 * - Background: aset level_selection_bg (dengan dekorasi math symbols, clouds, dll)
 * - Tombol back di kiri atas
 * - 3 tombol level: Easy, Medium, Hard (masing-masing pakai aset gambar)
 */
@Composable
fun LevelSelectionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEasyClick: () -> Unit = {},
    onMediumClick: () -> Unit = {},
    onHardClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background: aset level selection (berisi cloud, math symbols, buku, kalkulator)
        Image(
            painter = painterResource(id = R.drawable.level_selection_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Tombol Back (kiri atas) menggunakan asset back.png asli yang pas di posisi tombol background
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp, top = 16.dp)
                .size(46.dp)
                .clickable { onBackClick() }
        )

        // Konten utama: 3 tombol level ditumpuk vertikal di tengah layar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Geser sedikit ke atas agar masuk ke area kosong di tengah background
            Spacer(modifier = Modifier.height(60.dp))

            // Tombol Easy
            LevelButton(
                imageResId = R.drawable.btn_level_easy,
                contentDescription = "Easy - Build your basics",
                onClick = onEasyClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Medium
            LevelButton(
                imageResId = R.drawable.btn_level_medium,
                contentDescription = "Medium - A bigger challenge",
                onClick = onMediumClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Hard
            LevelButton(
                imageResId = R.drawable.btn_level_hard,
                contentDescription = "Hard - Test your skills",
                onClick = onHardClick
            )

            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}

/**
 * Tombol level individual dengan animasi tactile press (scale down saat ditekan)
 */
@Composable
private fun LevelButton(
    imageResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "levelBtnScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(8.dp, RoundedCornerShape(50.dp), spotColor = Color(0x33000000))
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

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LevelSelectionScreenPreview() {
    MathquizTheme {
        LevelSelectionScreen()
    }
}
