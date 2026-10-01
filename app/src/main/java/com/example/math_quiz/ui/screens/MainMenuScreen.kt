package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.components.PrimaryButton
import com.example.math_quiz.ui.theme.*

/**
 * Layar 2: MainMenuScreen
 * - Maskot buku di tengah dengan judul "Math Quiz" + tagline "Test your math skills!"
 * - Tombol besar "PLAY" (hijau, pill-shaped 3D)
 * - Dua tombol kecil sejajar "HIGH SCORE" dan "SETTINGS"
 * - Indikator level saat ini di bawah
 */
@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    currentLevel: Int = 3,
    onPlayClick: () -> Unit = {},
    onHighScoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // Animasi mengambang untuk dekorasi latar (floating elements)
    val infiniteTransition = rememberInfiniteTransition(label = "menuFloating")

    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menuFloat1"
    )

    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 7f,
        targetValue = -7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menuFloat2"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBgLight)
    ) {
        val w = maxWidth
        val h = maxHeight

        // 1. Background Image (dari file ChatGPT Image)
        Image(
            painter = painterResource(id = R.drawable.main_menu_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Elemen Matematika Mengambang di Area Atas
        // Kiri Atas: Angka "3" (Biru Muda 3D)
        Text(
            text = "3",
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = QuizBlue,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 32.dp, top = 55.dp)
                .offset(y = floatOffset1.dp)
                .rotate(-10f)
        )

        // Kiri: Simbol "+" (Merah Muda/Coral)
        Text(
            text = "+",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = QuizCoral,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 22.dp, top = 190.dp)
                .offset(y = floatOffset2.dp)
        )

        // Kanan Atas: Angka "5" (Hijau Segar 3D)
        Text(
            text = "5",
            fontSize = 54.sp,
            fontWeight = FontWeight.Black,
            color = QuizGreen,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 36.dp, top = 95.dp)
                .offset(y = floatOffset2.dp)
                .rotate(8f)
        )

        // Kanan: Simbol "÷" (Kuning/Oranye)
        Text(
            text = "÷",
            fontSize = 46.sp,
            fontWeight = FontWeight.Black,
            color = QuizYellow,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 28.dp, top = 205.dp)
                .offset(y = floatOffset1.dp)
        )

        // Dot aksen dekoratif
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 95.dp, top = 40.dp)
                .size(12.dp)
                .background(QuizBlueLight.copy(alpha = 0.5f), CircleShape)
        )

        // Dot aksen di bagian bawah
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 28.dp, bottom = 220.dp)
                .size(12.dp)
                .background(QuizBlueLight.copy(alpha = 0.4f), CircleShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 38.dp, bottom = 100.dp)
                .size(14.dp)
                .background(QuizBlueLight.copy(alpha = 0.5f), CircleShape)
        )

        // 3. Konten Utama Layar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            // A. Bagian Maskot Buku dengan Radiant Halo & Percikan
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = (floatOffset1 * 0.35f).dp)
            ) {
                // Background Halo Kuning
                Canvas(modifier = Modifier.size(175.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                QuizYellowHalo,
                                QuizYellowHalo.copy(alpha = 0.85f),
                                QuizYellow.copy(alpha = 0.15f)
                            )
                        )
                    )
                }

                // Maskot Buku Cute
                CuteBookMascot(
                    modifier = Modifier.size(145.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // B. Judul Aplikasi: "Math Quiz" dengan aksen percikan di samping
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Spark kiri (Dua pill kuning)
                Canvas(
                    modifier = Modifier
                        .size(width = 12.dp, height = 18.dp)
                        .rotate(-15f)
                ) {
                    // Pill pendek kiri
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(0f, 6f),
                        size = Size(4f, 10f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    // Pill tinggi kanan
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(8f, 0f),
                        size = Size(4f, 18f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Math",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizBlue,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .shadow(3.dp, CircleShape, spotColor = QuizBlueDark.copy(alpha = 0.35f))
                )

                Text(
                    text = "Quiz",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizOrange,
                    modifier = Modifier.shadow(
                        3.dp,
                        CircleShape,
                        spotColor = QuizOrangeDark.copy(alpha = 0.35f)
                    )
                )

                // Dot kanan
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(8.dp)
                        .background(QuizBlueLight, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // C. Tagline: "Test your math skills!"
            Text(
                text = "Test your math skills!",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextDark.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(42.dp))

            // D. Tombol Besar: "PLAY" 
            PrimaryButton(
                text = "PLAY",
                imageResId = null,
                onClick = onPlayClick,
                height = 64.dp,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(0.55f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // E. Dua Tombol Sejajar: "HIGH SCORE" & "SETTINGS"
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol HIGH SCORE
                MenuAssetButton(
                    imageResId = R.drawable.btn_high_score,
                    contentDescription = "HIGH SCORE",
                    onClick = onHighScoreClick,
                    height = 70.dp,
                    modifier = Modifier.weight(1.05f)
                )

                // Tombol SETTINGS
                MenuAssetButton(
                    imageResId = R.drawable.btn_settings,
                    contentDescription = "SETTINGS",
                    onClick = onSettingsClick,
                    height = 70.dp,
                    modifier = Modifier.weight(0.95f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // F. Indikator Level Saat Ini di Bagian Bawah
            LevelIndicatorBadge(
                level = currentLevel,
                modifier = Modifier.padding(bottom = 36.dp)
            )
        }
    }
}

/**
 * Tombol interaktif berbasis file aset 3D asli dengan animasi tactile press
 */
@Composable
private fun MenuAssetButton(
    imageResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "assetBtnScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
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
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

/**
 * Indikator Level Saat Ini: [—] [ 📶 Level X ] [—]
 */
@Composable
private fun LevelIndicatorBadge(
    level: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Garis aksen kiri
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(4.dp)
                .background(Color(0xFFCBD5E1), RoundedCornerShape(2.dp))
        )

        // Kartu Badge Level Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(4.dp, CircleShape, spotColor = Color(0x1A000000))
                .clip(CircleShape)
                .background(Color.White)
                .border(1.5.dp, Color(0xFFE2E8F0), CircleShape)
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            // Ikon Bar Signal Hijau (3 batang bertingkat)
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.height(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(6.dp)
                        .background(QuizGreen, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(10.dp)
                        .background(QuizGreen, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(14.dp)
                        .background(QuizGreen, RoundedCornerShape(2.dp))
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Level $level",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }

        // Garis aksen kanan
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(4.dp)
                .background(Color(0xFFCBD5E1), RoundedCornerShape(2.dp))
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MainMenuScreenPreview() {
    MathquizTheme {
        MainMenuScreen()
    }
}
