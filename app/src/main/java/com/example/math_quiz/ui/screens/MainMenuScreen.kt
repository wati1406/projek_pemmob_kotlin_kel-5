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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

    val floatOffset3 by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menuFloat3"
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

        // Kiri Tengah: Angka "7" (Ungu)
        Text(
            text = "7",
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF7C3AED),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 14.dp)
                .offset(y = floatOffset3.dp)
                .rotate(12f)
        )

        // Kanan Tengah: Simbol "×" (Pink)
        Text(
            text = "×",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFEC4899),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp)
                .offset(y = floatOffset2.dp)
                .rotate(-8f)
        )

        // Kiri Bawah: Simbol "−" (Biru)
        Text(
            text = "−",
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            color = QuizBlue.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, bottom = 260.dp)
                .offset(y = floatOffset1.dp)
        )

        // Kanan Bawah: Angka "8" (Oranye)
        Text(
            text = "8",
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            color = QuizOrange.copy(alpha = 0.80f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 280.dp)
                .offset(y = floatOffset3.dp)
                .rotate(10f)
        )

        // Dot aksen dekoratif - kiri atas
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 95.dp, top = 40.dp)
                .size(12.dp)
                .background(QuizBlueLight.copy(alpha = 0.5f), CircleShape)
        )

        // Dot aksen - kiri atas kecil
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 60.dp, top = 110.dp)
                .size(7.dp)
                .background(QuizCoral.copy(alpha = 0.55f), CircleShape)
        )

        // Dot aksen - kanan atas
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 70.dp, top = 50.dp)
                .size(9.dp)
                .background(QuizYellow.copy(alpha = 0.65f), CircleShape)
        )

        // Dot aksen di bagian bawah kiri
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 28.dp, bottom = 220.dp)
                .size(12.dp)
                .background(QuizBlueLight.copy(alpha = 0.4f), CircleShape)
        )

        // Dot aksen bawah kiri tambahan
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 55.dp, bottom = 170.dp)
                .size(8.dp)
                .background(QuizGreen.copy(alpha = 0.5f), CircleShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 38.dp, bottom = 200.dp)
                .size(14.dp)
                .background(QuizBlueLight.copy(alpha = 0.5f), CircleShape)
        )

        // Dot aksen bawah kanan kecil
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 65.dp, bottom = 155.dp)
                .size(7.dp)
                .background(QuizCoral.copy(alpha = 0.6f), CircleShape)
        )

        // Kotak aksen pudar (mirip confetti) sesuai contoh
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 220.dp)
                .size(12.dp)
                .background(Color(0xFF22C55E).copy(alpha = 0.35f), RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 180.dp)
                .size(16.dp)
                .background(Color(0xFF3B82F6).copy(alpha = 0.35f), RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 40.dp, bottom = 40.dp)
                .size(10.dp)
                .background(Color(0xFFFB7185).copy(alpha = 0.45f), RoundedCornerShape(2.dp))
        )

        // 3. Konten Utama Layar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(95.dp))

            // A. Bagian Maskot Buku dengan Radiant Halo & Percikan
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(240.dp) // Diperbesar
                    .offset(y = (floatOffset1 * 0.35f).dp)
            ) {
                // Background Halo Kuning
                Canvas(modifier = Modifier.size(210.dp)) {
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

                // Spark / percikan kuning di atas kanan kepala buku
                Canvas(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-12).dp, y = 8.dp)
                ) {
                    val color = QuizYellow
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(16f, 0f),
                        size = Size(8f, 22f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(0f, 18f),
                        size = Size(22f, 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                // Maskot Buku Cute dari Drawable `buku.png`
                Image(
                    painter = painterResource(id = R.drawable.buku),
                    contentDescription = "Math Quiz Mascot",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(190.dp) // Diperbesar
                )
            }

            Spacer(modifier = Modifier.height(0.dp)) // Jarak didekatkan

            // B. Judul Aplikasi: "Math Quiz" Melengkung Halus dengan Outline & Shadow Stiker
            Row(
                verticalAlignment = Alignment.Bottom, // Align bawah agar arch natural
                horizontalArrangement = Arrangement.Center
            ) {
                // Spark kiri
                Canvas(modifier = Modifier.size(width = 12.dp, height = 18.dp).rotate(-15f).offset(y = (-10).dp)) {
                    drawRoundRect(color = QuizYellow, topLeft = Offset(0f, 6f), size = Size(4f, 10f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = QuizYellow, topLeft = Offset(8f, 0f), size = Size(4f, 18f), cornerRadius = CornerRadius(2f, 2f))
                }
                Spacer(modifier = Modifier.width(6.dp))

                val title = "Math Quiz"
                val middle = (title.length - 1) / 2f
                
                title.forEachIndexed { index, char ->
                    if (char == ' ') {
                        Spacer(modifier = Modifier.width(4.dp)) // Jarak antar kata didekatkan
                    } else {
                        val isMath = index < 4
                        val color = if (isMath) QuizBlue else QuizOrange
                        val diff = index - middle
                        val angle = diff * 1.5f // Lengkungan dikurangi jauh
                        val yOffset = (diff * diff * 0.4f).dp // Parabola dikurangi jauh
                        
                        val outlineWidth = 16f
                        
                        Box(
                            modifier = Modifier
                                .offset(y = yOffset)
                                .rotate(angle),
                            contentAlignment = Alignment.Center
                        ) {
                            // 1. Drop Shadow (Stroke tebal di-offset)
                            Text(
                                text = char.toString(),
                                fontSize = 54.sp,
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
                            // 2. Outline Putih Tebal
                            Text(
                                text = char.toString(),
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                style = androidx.compose.ui.text.TextStyle(
                                    drawStyle = Stroke(
                                        width = outlineWidth,
                                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                                    )
                                )
                            )
                            // 3. Teks Utama (Isi warna)
                            Text(
                                text = char.toString(),
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                color = color
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
                // Spark kanan
                Canvas(modifier = Modifier.size(width = 12.dp, height = 18.dp).rotate(15f).offset(y = (-10).dp)) {
                    drawRoundRect(color = QuizYellow, topLeft = Offset(8f, 6f), size = Size(4f, 10f), cornerRadius = CornerRadius(2f, 2f))
                    drawRoundRect(color = QuizYellow, topLeft = Offset(0f, 0f), size = Size(4f, 18f), cornerRadius = CornerRadius(2f, 2f))
                }
            }

            Spacer(modifier = Modifier.height(2.dp)) // Didekatkan

            // C. Tagline: "Test your math skills!"
            Text(
                text = "Test your math skills!",
                fontSize = 21.sp, // Diperbesar
                fontWeight = FontWeight.Black,
                color = QuizTextDark.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp)) // Didekatkan

            // D. Tombol Besar: "PLAY" 
            PrimaryButton(
                text = "PLAY",
                imageResId = R.drawable.btn_play,
                onClick = onPlayClick,
                height = 100.dp, // Diperbesar
                modifier = Modifier.fillMaxWidth(0.92f) // Diperlebar
            )

            Spacer(modifier = Modifier.height(8.dp)) // Didekatkan ke row bawah

            // E. Dua Tombol Sejajar: "HIGH SCORE" & "SETTINGS"
            // Rasio asli: btn_high_score=2.89, btn_settings=2.76 → pakai 2.83 agar keduanya identik
            Row(
                modifier = Modifier.fillMaxWidth(0.88f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuAssetButton(
                    imageResId = R.drawable.btn_high_score,
                    contentDescription = "HIGH SCORE",
                    onClick = onHighScoreClick,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(2.83f)
                )
                MenuAssetButton(
                    imageResId = R.drawable.btn_settings,
                    contentDescription = "SETTINGS",
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(2.83f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // F. Elemen Dekoratif Bawah — Angka dan Operator Matematika
            ScatteredMathDecor()

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/**
 * Tombol interaktif berbasis file aset 3D asli dengan animasi tactile press.
 * Ukuran dikontrol sepenuhnya dari luar melalui modifier (weight + height).
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

/**
 * Dekorasi bawah berupa angka dan simbol matematika yang tersebar,
 * sesuai request agar kelihatan ramai dengan matematika.
 */
@Composable
private fun ScatteredMathDecor() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp) // Ketinggian ditambah agar elemen bawah bisa turun mengisi kekosongan
            .padding(horizontal = 16.dp)
    ) {
        // Angka 3 besar biru di kiri atas + Dot pink
        Text(
            text = "3",
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF3B82F6).copy(alpha = 0.9f),
            modifier = Modifier.align(Alignment.TopStart).offset(x = 20.dp, y = (-5).dp).rotate(-8f)
        )
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 65.dp, y = 45.dp).size(8.dp).background(Color(0xFFFB7185).copy(alpha = 0.6f), CircleShape))

        // Simbol Plus (+) merah di bawah kiri + Kotak hijau kecil
        Text(
            text = "+",
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFB7185).copy(alpha = 0.9f),
            modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = 5.dp).rotate(15f)
        )
        Box(modifier = Modifier.align(Alignment.BottomStart).offset(x = 30.dp, y = 20.dp).size(12.dp).background(Color(0xFF22C55E).copy(alpha = 0.5f)))

        // Simbol Bagi (÷) cyan di tengah atas (bertukar posisi dengan 7)
        Text(
            text = "÷",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF06B6D4).copy(alpha = 0.85f),
            modifier = Modifier.align(Alignment.TopCenter).offset(x = (-10).dp, y = 15.dp).rotate(-15f)
        )

        // Angka 7 ungu di tengah bawah (bertukar posisi dengan ÷)
        Text(
            text = "7",
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF7C3AED).copy(alpha = 0.8f),
            modifier = Modifier.align(Alignment.BottomCenter).offset(x = 15.dp, y = 10.dp).rotate(20f)
        )

        // Angka 9 hijau di kanan atas (diturunkan sedikit)
        Text(
            text = "9",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF22C55E).copy(alpha = 0.85f),
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-30).dp, y = 40.dp).rotate(-5f)
        )

        // Simbol Kali (×) kuning/orange di kanan bawah (dipindah sangat ke bawah) + Dot biru
        Text(
            text = "×",
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFFD026).copy(alpha = 0.9f),
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-50).dp, y = 15.dp).rotate(10f)
        )
        Box(modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-20).dp, y = 30.dp).size(10.dp).background(Color(0xFF3B82F6).copy(alpha = 0.6f), CircleShape))
    }
}



@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MainMenuScreenPreview() {
    MathquizTheme {
        MainMenuScreen()
    }
}
