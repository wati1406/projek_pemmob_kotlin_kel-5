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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.components.PrimaryButton
import com.example.math_quiz.ui.theme.*

/**
 * Layar 2: MainMenuScreen — Fully Responsive
 * Semua ukuran (font, padding, mascot) diturunkan dari maxWidth / maxHeight
 * via BoxWithConstraints agar tampil proporsional di semua ukuran layar.
 */
@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    currentLevel: Int = 3,
    onPlayClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // ── Animasi floating ────────────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "menuFloating")

    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -7f, targetValue = 7f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "menuFloat1"
    )
    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 7f, targetValue = -7f,
        animationSpec = infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "menuFloat2"
    )
    val floatOffset3 by infiniteTransition.animateFloat(
        initialValue = -5f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "menuFloat3"
    )
    val floatOffset4 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = -6f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "menuFloat4"
    )

    // Animasi tombol PLAY
    val btnPlayBounce by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -6f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "btnPlayBounce"
    )
    val btnPlayPulse by infiniteTransition.animateFloat(
        initialValue = 1.00f, targetValue = 1.025f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "btnPlayPulse"
    )

    // Animasi tombol SETTINGS
    val btnSettingsBounce by infiniteTransition.animateFloat(
        initialValue = -4f, targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "btnSettingsBounce"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBgLight)
    ) {
        val w = maxWidth   // lebar layar
        val h = maxHeight  // tinggi layar

        // ── Skala responsif ─────────────────────────────────────────────────
        // Font elemen dekorasi: ~10% lebar layar, diclamp agar tidak terlalu besar
        val decorFontLg = (w * 0.115f).value.sp   // ~45sp di 390dp
        val decorFontMd = (w * 0.098f).value.sp   // ~38sp
        val decorFontSm = (w * 0.090f).value.sp   // ~35sp

        // Ukuran maskot & halo
        val mascotBoxSize  = w * 0.62f   // ~240dp di 390dp
        val haloSize       = w * 0.54f   // ~210dp
        val mascotImgSize  = w * 0.49f   // ~190dp

        // Font judul & tagline
        val titleFontSp    = (w * 0.138f).value.sp  // ~54sp
        val taglineFontSp  = (w * 0.054f).value.sp  // ~21sp

        // Tinggi tombol PLAY: ~12% tinggi layar
        val playBtnHeight  = h * 0.118f  // ~100dp di 844dp
        // Padding horizontal kolom utama: ~6% lebar
        val colPadH        = w * 0.062f  // ~24dp
        // Spacer atas sebelum maskot: ~11% tinggi
        val topSpacer      = h * 0.112f  // ~95dp

        // ── 1. Background ────────────────────────────────────────────────────
        Image(
            painter = painterResource(id = R.drawable.main_menu_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // ── 2. Elemen Matematika Floating ────────────────────────────────────

        // Kiri Atas: "3"
        Text(
            text = "3",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontLg,
            fontWeight = FontWeight.Black,
            color = QuizBlue.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = w * 0.072f, top = h * 0.095f)
                .offset(y = floatOffset1.dp)
                .rotate(-10f)
        )

        // Kanan Atas: "5"
        Text(
            text = "5",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontLg,
            fontWeight = FontWeight.Black,
            color = QuizGreen.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = w * 0.077f, top = h * 0.130f)
                .offset(y = floatOffset2.dp)
                .rotate(8f)
        )

        // Atas Tengah Kiri: "÷"
        Text(
            text = "÷",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontSm,
            fontWeight = FontWeight.Black,
            color = QuizPurple.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = w * 0.18f, top = h * 0.059f)
                .offset(y = floatOffset4.dp)
                .rotate(12f)
        )

        // Atas Tengah Kanan: "7"
        Text(
            text = "7",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontMd,
            fontWeight = FontWeight.Black,
            color = QuizCoral.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = w * 0.185f, top = h * 0.071f)
                .offset(y = floatOffset3.dp)
                .rotate(-12f)
        )

        // Kiri Tengah: "+"
        Text(
            text = "+",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontMd,
            fontWeight = FontWeight.Black,
            color = QuizCoral.copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = w * 0.056f, top = h * 0.071f)
                .offset(y = floatOffset3.dp)
        )

        // Kanan Tengah: "×"
        Text(
            text = "×",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontMd,
            fontWeight = FontWeight.Black,
            color = QuizOrange.copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = w * 0.056f, top = h * 0.095f)
                .offset(y = floatOffset2.dp)
                .rotate(-8f)
        )

        // Bawah Kiri: "2"
        Text(
            text = "2",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontLg,
            fontWeight = FontWeight.Black,
            color = QuizGreen.copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = w * 0.067f, bottom = h * 0.107f)
                .offset(y = floatOffset1.dp)
                .rotate(10f)
        )

        // Bawah Kanan: "−"
        Text(
            text = "−",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontLg,
            fontWeight = FontWeight.Black,
            color = QuizBlue.copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = w * 0.072f, bottom = h * 0.118f)
                .offset(y = floatOffset4.dp)
                .rotate(-10f)
        )

        // Bawah Kanan Tengah: "9"
        Text(
            text = "9",
            fontFamily = FredokaFontFamily,
            fontSize = decorFontSm,
            fontWeight = FontWeight.Black,
            color = QuizOrange.copy(alpha = 0.70f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = w * 0.18f, bottom = h * 0.065f)
                .offset(y = floatOffset2.dp)
                .rotate(8f)
        )

        // ── 3. Konten Utama ──────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = colPadH),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(topSpacer))

            // A. Maskot Buku dengan Halo & Spark
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(mascotBoxSize)
                    .offset(y = (floatOffset1 * 0.35f).dp)
            ) {
                // Halo kuning
                Canvas(modifier = Modifier.size(haloSize)) {
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

                // Spark percikan kuning
                Canvas(
                    modifier = Modifier
                        .size(w * 0.108f)   // ~42dp
                        .align(Alignment.TopEnd)
                        .offset(x = (-w * 0.031f), y = w * 0.021f)
                ) {
                    val sparkColor = QuizYellow
                    drawRoundRect(
                        color = sparkColor,
                        topLeft = Offset(16f, 0f),
                        size = Size(8f, 22f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = sparkColor,
                        topLeft = Offset(0f, 18f),
                        size = Size(22f, 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                // Maskot gambar buku
                Image(
                    painter = painterResource(id = R.drawable.buku),
                    contentDescription = "Math Quiz Mascot",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(mascotImgSize)
                )
            }

            // B. Judul "Math Quiz" melengkung dengan outline & shadow
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                // Spark kiri
                val sparkW = w * 0.031f  // ~12dp
                val sparkH = w * 0.046f  // ~18dp
                Canvas(
                    modifier = Modifier
                        .size(width = sparkW, height = sparkH)
                        .rotate(-15f)
                        .offset(y = -sparkH * 0.55f)
                ) {
                    drawRoundRect(QuizYellow, Offset(0f, 6f), Size(4f, 10f), CornerRadius(2f, 2f))
                    drawRoundRect(QuizYellow, Offset(8f, 0f), Size(4f, 18f), CornerRadius(2f, 2f))
                }
                Spacer(modifier = Modifier.width(w * 0.015f))

                val title = "Math Quiz"
                val middle = (title.length - 1) / 2f
                title.forEachIndexed { index, char ->
                    if (char == ' ') {
                        Spacer(modifier = Modifier.width(w * 0.010f))
                    } else {
                        val isMath = index < 4
                        val color  = if (isMath) QuizBlue else QuizOrange
                        val diff   = index - middle
                        val angle  = diff * 1.5f
                        val yOffset = (diff * diff * 0.4f).dp
                        val outlineWidth = 16f

                        Box(
                            modifier = Modifier.offset(y = yOffset).rotate(angle),
                            contentAlignment = Alignment.Center
                        ) {
                            // Drop shadow
                            Text(
                                text = char.toString(),
                                fontFamily = FredokaFontFamily,
                                fontSize = titleFontSp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black.copy(alpha = 0.15f),
                                style = androidx.compose.ui.text.TextStyle(
                                    drawStyle = Stroke(
                                        width = outlineWidth,
                                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                                    )
                                ),
                                modifier = Modifier.offset(x = 0.dp, y = 6.dp)
                            )
                            // Outline putih
                            Text(
                                text = char.toString(),
                                fontFamily = FredokaFontFamily,
                                fontSize = titleFontSp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                style = androidx.compose.ui.text.TextStyle(
                                    drawStyle = Stroke(
                                        width = outlineWidth,
                                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                                    )
                                )
                            )
                            // Teks isi
                            Text(
                                text = char.toString(),
                                fontFamily = FredokaFontFamily,
                                fontSize = titleFontSp,
                                fontWeight = FontWeight.Black,
                                color = color
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(w * 0.015f))
                // Spark kanan
                Canvas(
                    modifier = Modifier
                        .size(width = sparkW, height = sparkH)
                        .rotate(15f)
                        .offset(y = -sparkH * 0.55f)
                ) {
                    drawRoundRect(QuizYellow, Offset(8f, 6f), Size(4f, 10f), CornerRadius(2f, 2f))
                    drawRoundRect(QuizYellow, Offset(0f, 0f), Size(4f, 18f), CornerRadius(2f, 2f))
                }
            }

            Spacer(modifier = Modifier.height(h * 0.003f))

            // C. Tagline
            Text(
                text = "Test your math skills!",
                fontFamily = FredokaFontFamily,
                fontSize = taglineFontSp,
                fontWeight = FontWeight.Black,
                color = QuizTextDark.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(h * 0.014f))

            // D. Tombol PLAY — animasi float + pulse scale
            PrimaryButton(
                text = "PLAY",
                imageResId = R.drawable.btn_play,
                onClick = onPlayClick,
                height = playBtnHeight,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .offset(y = btnPlayBounce.dp)
                    .scale(btnPlayPulse)
            )

            Spacer(modifier = Modifier.height(h * 0.010f))

            // E. Tombol SETTINGS — animasi float halus
            MenuAssetButton(
                imageResId = R.drawable.btn_settings,
                contentDescription = "SETTINGS",
                onClick = onSettingsClick,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(2.83f)
                    .offset(y = btnSettingsBounce.dp)
            )

            Spacer(modifier = Modifier.height(h * 0.036f))
        }
    }
}

/**
 * Tombol interaktif berbasis file aset 3D asli dengan animasi tactile press.
 */
@Composable
private fun MenuAssetButton(
    imageResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
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

@Preview(showBackground = true, widthDp = 360, heightDp = 780, name = "Small Phone")
@Composable
fun MainMenuScreenSmallPreview() {
    MathquizTheme {
        MainMenuScreen()
    }
}

@Preview(showBackground = true, widthDp = 430, heightDp = 932, name = "Large Phone")
@Composable
fun MainMenuScreenLargePreview() {
    MathquizTheme {
        MainMenuScreen()
    }
}
