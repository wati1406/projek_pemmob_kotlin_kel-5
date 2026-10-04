package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.theme.*

/**
 * Layar About
 * Menampilkan informasi lengkap aplikasi Math Quiz, fitur, tingkat kesulitan, dan versi aplikasi.
 * Desain konsisten dengan halaman Settings (background, warna, typography, card style, dan back button).
 */
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Animasi mengambang untuk dekorasi latar
    val infiniteTransition = rememberInfiniteTransition(label = "aboutFloating")

    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aboutFloat1"
    )

    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aboutFloat2"
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background image konsisten dengan Settings
        Image(
            painter = painterResource(id = R.drawable.settings_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Elemen dekoratif mengambang
        Text(
            text = "+",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = QuizBlueLight.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 18.dp, top = 140.dp)
                .offset(y = floatOffset2.dp)
                .rotate(-12f)
        )

        Text(
            text = "×",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            color = QuizYellow.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 22.dp, top = 110.dp)
                .offset(y = floatOffset1.dp)
        )

        Text(
            text = "÷",
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            color = QuizCoral.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 310.dp)
                .offset(y = floatOffset1.dp)
                .rotate(8f)
        )

        // Konten utama dengan vertical scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // Header: Tombol Back + Judul "About"
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = "About",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizTextNavy
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Section: Maskot + Nama Aplikasi + Tagline
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .offset(y = (floatOffset1 * 0.3f).dp)
            ) {
                CuteBookMascot(modifier = Modifier.size(90.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Math Quiz",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = QuizTextNavy
            )

            Text(
                text = "Make Math More Fun!✨",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = QuizBlue
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Deskripsi Aplikasi Card
            AboutContentCard {
                Text(
                    text = "Math Quiz adalah aplikasi kuis matematika interaktif yang dirancang untuk membantu pengguna melatih kemampuan berhitung dengan cara yang sederhana, menyenangkan, dan tidak membosankan.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuizTextDark,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Features
            AboutContentCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        iconEmoji = "✨",
                        iconBgColor = Color(0xFFFEF08A),
                        title = "Features"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureItem(
                        emoji = "📐",
                        title = "Practice Math",
                        description = "Latihan soal matematika dengan berbagai tingkat kesulitan."
                    )
                    FeatureDivider()
                    FeatureItem(
                        emoji = "🎯",
                        title = "Choose Your Level",
                        description = "Tersedia Level 1, Level 2, dan Level 3 dengan tingkat kesulitan yang berbeda."
                    )
                    FeatureDivider()
                    FeatureItem(
                        emoji = "⏱️",
                        title = "Beat the Timer",
                        description = "Setiap soal memiliki batas waktu untuk dijawab."
                    )
                    FeatureDivider()
                    FeatureItem(
                        emoji = "🏆",
                        title = "Earn Scores",
                        description = "Dapatkan skor dari jawaban yang benar."
                    )
                    FeatureDivider()
                    FeatureItem(
                        emoji = "🎵",
                        title = "Sound & Music",
                        description = "Tersedia sound effects dan background music."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Difficulty Levels
            AboutContentCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        iconEmoji = "📊",
                        iconBgColor = Color(0xFFDBEAFE),
                        title = "Difficulty Levels"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LevelInfoItem(
                        badgeText = "Level 1",
                        badgeColor = QuizGreen,
                        title = "Easy",
                        description = "Soal dasar sebagai pemanasan."
                    )
                    FeatureDivider()
                    LevelInfoItem(
                        badgeText = "Level 2",
                        badgeColor = QuizYellowDark,
                        title = "Medium",
                        description = "Soal dengan tingkat tantangan yang lebih tinggi."
                    )
                    FeatureDivider()
                    LevelInfoItem(
                        badgeText = "Level 3",
                        badgeColor = QuizRed,
                        title = "Hard",
                        description = "Soal yang membutuhkan kemampuan berhitung lebih tinggi."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: App Information
            AboutContentCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(
                        iconEmoji = "ℹ️",
                        iconBgColor = Color(0xFFDCFCE7),
                        title = "App Information"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow(label = "App Name", value = "Math Quiz")
                    FeatureDivider()
                    InfoRow(label = "Version", value = "1.0.0")
                    FeatureDivider()
                    InfoRow(label = "Platform", value = "Android")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Penutup Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(QuizBlue.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Have fun with math! ✨",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuizBlueDark,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

/** Card container bergaya putih bersih dengan rounded 18dp dan shadow lembut */
@Composable
private fun AboutContentCard(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        content()
    }
}

/** Judul setiap section dengan ikon rounded square */
@Composable
private fun SectionHeader(
    iconEmoji: String,
    iconBgColor: Color,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = iconEmoji, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = QuizTextNavy
        )
    }
}

/** Item baris untuk fitur aplikasi */
@Composable
private fun FeatureItem(
    emoji: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = emoji, fontSize = 16.sp, modifier = Modifier.padding(top = 1.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = QuizTextMuted,
                lineHeight = 17.sp
            )
        }
    }
}

/** Item baris untuk informasi tingkat kesulitan */
@Composable
private fun LevelInfoItem(
    badgeText: String,
    badgeColor: Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeColor)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = badgeText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = QuizTextMuted,
                lineHeight = 17.sp
            )
        }
    }
}

/** Item baris untuk informasi aplikasi (App Name, Version, Platform) */
@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = QuizTextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = QuizTextNavy
        )
    }
}

/** Garis pemisah halus antar item */
@Composable
private fun FeatureDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 0.8.dp,
        color = Color(0xFFF1F5F9)
    )
}

/** Tombol back menggunakan aset back.png asli dengan animasi press */
@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(100),
        label = "aboutBackBtnScale"
    )

    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Back",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .scale(scale)
            .size(44.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun AboutScreenPreview() {
    MathquizTheme {
        AboutScreen()
    }
}
