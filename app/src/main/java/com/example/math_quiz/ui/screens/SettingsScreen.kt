package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import com.example.math_quiz.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.ui.theme.*

/**
 * Layar Settings
 * - Tombol back kiri atas
 * - Judul "Settings"
 * - Maskot buku + "Customize your quiz experience"
 * - Row item: Sound Effects (toggle), Music (toggle), Language (dropdown), About (arrow)
 * - Footer "Make Math More Fun!"
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    // State untuk toggle dan bahasa
    var soundEffectsEnabled by remember { mutableStateOf(true) }
    var musicEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var showLanguageMenu by remember { mutableStateOf(false) }

    val languages = listOf("English", "Indonesia", "Espanol", "Francais")

    // Animasi mengambang
    val infiniteTransition = rememberInfiniteTransition(label = "settingsFloating")

    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "settingsFloat1"
    )

    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "settingsFloat2"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Background image dari asset settings_bg.png
        Image(
            painter = painterResource(id = R.drawable.settings_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Elemen dekoratif mengambang
        Text(
            text = "+",
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = QuizBlueLight.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 18.dp, top = 160.dp)
                .offset(y = floatOffset2.dp)
                .rotate(-10f)
        )

        Text(
            text = "1",
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = QuizCoral.copy(alpha = 0.55f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 285.dp)
                .offset(y = floatOffset1.dp)
                .rotate(8f)
        )

        Text(
            text = "\u00F7",
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            color = QuizYellow.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 22.dp, top = 120.dp)
                .offset(y = floatOffset1.dp)
        )

        Text(
            text = "2",
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = QuizGreen.copy(alpha = 0.55f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 28.dp, top = 250.dp)
                .offset(y = floatOffset2.dp)
                .rotate(-8f)
        )

        // Dot dekoratif
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 80.dp, top = 180.dp)
                .size(10.dp)
                .background(QuizPink.copy(alpha = 0.5f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 85.dp, top = 60.dp)
                .size(8.dp)
                .background(QuizBlueLight.copy(alpha = 0.4f), CircleShape)
        )

        // Konten utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // Header row: tombol back + judul
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = "Settings",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizTextNavy
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Maskot buku + halo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(165.dp)
                    .offset(y = (floatOffset1 * 0.3f).dp)
            ) {
                Canvas(modifier = Modifier.size(148.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                QuizYellowHalo,
                                QuizYellowHalo.copy(alpha = 0.8f),
                                QuizYellow.copy(alpha = 0.1f)
                            )
                        )
                    )
                }
                Canvas(modifier = Modifier.size(162.dp)) {
                    val cx = size.width / 2
                    val cy = size.height / 2
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(cx + 25f, cy - 65f),
                        size = Size(7f, 16f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(cx + 42f, cy - 52f),
                        size = Size(7f, 13f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(cx - 38f, cy - 62f),
                        size = Size(7f, 15f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                CuteBookMascot(modifier = Modifier.size(118.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Customize your quiz experience",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = QuizTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Sound Effects
            SettingsCard {
                SettingsIconBox(backgroundColor = Color(0xFFDCFCE7), emoji = "\uD83D\uDD0A")
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sound Effects",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuizTextNavy
                    )
                    Text(
                        text = "Play sounds for a better experience",
                        fontSize = 12.sp,
                        color = QuizTextMuted
                    )
                }
                Switch(
                    checked = soundEffectsEnabled,
                    onCheckedChange = { soundEffectsEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = QuizGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Music
            SettingsCard {
                SettingsIconBox(backgroundColor = Color(0xFFFFE4E6), emoji = "\uD83C\uDFB5")
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Music",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuizTextNavy
                    )
                    Text(
                        text = "Play background music",
                        fontSize = 12.sp,
                        color = QuizTextMuted
                    )
                }
                Switch(
                    checked = musicEnabled,
                    onCheckedChange = { musicEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = QuizGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Language
            Box {
                SettingsCard {
                    SettingsIconBox(backgroundColor = Color(0xFFDBEAFE), emoji = "\uD83C\uDF10")
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Language",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuizTextNavy
                        )
                        Text(
                            text = "Choose your language",
                            fontSize = 12.sp,
                            color = QuizTextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            .clickable { showLanguageMenu = !showLanguageMenu }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedLanguage,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = QuizTextNavy
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "\u25BE", fontSize = 11.sp, color = QuizTextMuted)
                        }
                    }
                }
                if (showLanguageMenu) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(y = 60.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(vertical = 6.dp)
                            .width(140.dp)
                    ) {
                        Column {
                            languages.forEach { lang ->
                                Text(
                                    text = lang,
                                    fontSize = 14.sp,
                                    fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                                    color = if (lang == selectedLanguage) QuizBlue else QuizTextNavy,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedLanguage = lang
                                            showLanguageMenu = false
                                        }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. About
            SettingsCard(onClick = onAboutClick) {
                SettingsIconBox(backgroundColor = Color(0xFFFEF9C3), emoji = "\u2139\uFE0F")
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "About",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuizTextNavy
                    )
                    Text(
                        text = "Learn more about Math Quiz",
                        fontSize = 12.sp,
                        color = QuizTextMuted
                    )
                }
                Text(
                    text = "\u203A",
                    fontSize = 24.sp,
                    color = QuizTextMuted,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(3.dp)
                                .background(QuizBlueLight.copy(alpha = 0.45f), RoundedCornerShape(2.dp))
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Make Math\nMore Fun!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = QuizBlueLight.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(3.dp)
                                .background(QuizBlueLight.copy(alpha = 0.45f), RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}

/** Kartu putih dengan shadow untuk setiap item setting */
@Composable
private fun SettingsCard(
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.97f else 1f,
        animationSpec = tween(100),
        label = "cardScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .then(
                if (onClick != null)
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

/** Kotak ikon rounded-square berwarna dengan emoji */
@Composable
private fun SettingsIconBox(
    backgroundColor: Color,
    emoji: String
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 22.sp)
    }
}

/** Tombol back (rounded square putih) */
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
        label = "backBtnScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .size(44.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = Color(0x18000000))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\u2190",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = QuizTextNavy
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SettingsScreenPreview() {
    MathquizTheme {
        SettingsScreen()
    }
}
