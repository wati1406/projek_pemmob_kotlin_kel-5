package com.example.math_quiz.ui.screens

import android.graphics.drawable.ColorDrawable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.ui.theme.FredokaFontFamily
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.math_quiz.R

// ── Satu elemen dekorasi dengan animasi float + rotasi ────────────────────────
private data class MathDeco(
    val text: String,
    val color: Color,
    val sizeSp: Float,
    val xDp: Dp,
    val yDp: Dp,
    val initRot: Float,
    val durationMs: Int
)

// Posisi dekorasi: semua di tepi kiri/kanan card, tersebar vertikal, tidak sejajar
private val decorations = listOf(
    // Sisi KIRI — spread vertikal, x sangat pinggir (2–14dp)
    MathDeco("3",  Color(0xFF3B82F6), 34f,  4.dp, 105.dp, -30f, 1700),
    MathDeco("×",  Color(0xFFF97316), 26f, 12.dp, 195.dp,  20f, 2100),
    MathDeco("+",  Color(0xFF22C55E), 30f,  2.dp, 285.dp, -18f, 1900),
    MathDeco("5",  Color(0xFF10B981), 28f, 10.dp, 380.dp,  25f, 2300),
    MathDeco("∞",  Color(0xFF6366F1), 22f,  6.dp, 455.dp, -12f, 1800),

    // Sisi KANAN — spread vertikal berbeda, x sangat pinggir (285–300dp)
    MathDeco("÷",  Color(0xFFE11D48), 28f, 290.dp,  80.dp,  22f, 2000),
    MathDeco("7",  Color(0xFFFBBF24), 36f, 286.dp, 160.dp, -28f, 1600),
    MathDeco("2",  Color(0xFFF472B6), 26f, 292.dp, 248.dp,  16f, 2200),
    MathDeco("9",  Color(0xFFA855F7), 30f, 288.dp, 340.dp, -20f, 1750),
    MathDeco("8",  Color(0xFF0284C7), 24f, 294.dp, 425.dp,  14f, 2400),
)

@Composable
private fun CardDecorations() {
    val transition = rememberInfiniteTransition(label = "deco")
    Box(modifier = Modifier.fillMaxWidth().height(500.dp)) {
        decorations.forEach { d ->
            val fy by transition.animateFloat(
                initialValue = -6f, targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    tween(d.durationMs, easing = FastOutSlowInEasing), RepeatMode.Reverse
                ), label = "fy${d.text}${d.xDp}"
            )
            val rt by transition.animateFloat(
                initialValue = d.initRot - 8f, targetValue = d.initRot + 8f,
                animationSpec = infiniteRepeatable(
                    tween((d.durationMs * 1.3f).toInt(), easing = LinearEasing), RepeatMode.Reverse
                ), label = "rt${d.text}${d.xDp}"
            )
            Text(
                text       = d.text,
                fontSize   = d.sizeSp.sp,
                fontWeight = FontWeight.Black,
                color      = d.color.copy(alpha = 0.50f),
                modifier   = Modifier
                    .absoluteOffset(x = d.xDp, y = d.yDp + fy.dp)
                    .rotate(rt)
            )
        }
    }
}

// ── Dialog Hasil Quiz ─────────────────────────────────────────────────────────
@Composable
fun QuizResultDialog(
    score: Int = 0,
    onNextLevel: () -> Unit = {},
    onPlayAgain: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val roundedFont = FredokaFontFamily

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress      = false,
            dismissOnClickOutside   = false,
            usePlatformDefaultWidth = false
        )
    ) {
        // Transparankan background dialog bawaan Android
        val view = LocalView.current
        if (!view.isInEditMode) {
            val win = (view.parent as? DialogWindowProvider)?.window
            SideEffect {
                win?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                win?.setDimAmount(0.55f)
            }
        }

        // Card putih
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .wrapContentHeight()
                .shadow(18.dp, RoundedCornerShape(32.dp), spotColor = Color(0x55000000))
                .background(Color.White, RoundedCornerShape(32.dp))
        ) {
            // Layer 1: Dekorasi di belakang
            CardDecorations()

            // Layer 2: Konten
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 20.dp)
            ) {
                // ── Piala — di dalam card, ATAS teks ─────────────────────────
                Image(
                    painter            = painterResource(id = R.drawable.piala),
                    contentDescription = "Gold Trophy",
                    modifier           = Modifier
                        .size(90.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ── Judul ─────────────────────────────────────────────────────
                Text(
                    text       = "Quiz Complete!",
                    fontFamily = FontFamily.SansSerif,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Black,
                    color      = Color(0xFF1E3A8A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ── Label skor ────────────────────────────────────────────────
                Text(
                    text       = "Score",
                    fontFamily = FontFamily.SansSerif,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF64748B)
                )

                // ── Angka skor ────────────────────────────────────────────────
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter            = painterResource(id = R.drawable.kiri),
                        contentDescription = null,
                        modifier           = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text       = "$score",
                        fontFamily = FontFamily.SansSerif,
                        fontSize   = 68.sp,
                        fontWeight = FontWeight.Black,
                        color      = Color(0xFF1E3A8A)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter            = painterResource(id = R.drawable.kanan),
                        contentDescription = null,
                        modifier           = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // ── Performance pill ──────────────────────────────────────────
                val (pillText, pillBg, pillColor) = when {
                    score >= 90 -> Triple("⭐ Perfect!",     Color(0xFFFEF9C3), Color(0xFFCA8A04))
                    score >= 70 -> Triple("🎉 Great Job!",   Color(0xFFDCFCE7), Color(0xFF16A34A))
                    score >= 50 -> Triple("👍 Good Job!",    Color(0xFFDBEAFE), Color(0xFF1D4ED8))
                    else        -> Triple("💪 Keep Trying!", Color(0xFFFFE4E6), Color(0xFFE11D48))
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(pillBg)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text       = pillText,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Black,
                        color      = pillColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Tombol NEXT LEVEL ─────────────────────────────────────────
                ResultMenuButton(
                    imageResId      = R.drawable.btn_next_level,
                    textFallback    = "NEXT LEVEL",
                    backgroundColor = Color(0xFF22C55E),
                    onClick         = onNextLevel
                )
                Spacer(modifier = Modifier.height(6.dp))

                // ── Tombol PLAY AGAIN ─────────────────────────────────────────
                ResultMenuButton(
                    imageResId      = R.drawable.btn_play_again,
                    textFallback    = "PLAY AGAIN",
                    backgroundColor = Color(0xFF3B82F6),
                    onClick         = onPlayAgain
                )
                Spacer(modifier = Modifier.height(6.dp))

                // ── Tombol HOME ───────────────────────────────────────────────
                ResultMenuButton(
                    imageResId      = R.drawable.btn_home,
                    textFallback    = "HOME",
                    backgroundColor = Color.White,
                    isHome          = true,
                    onClick         = onHome
                )
            }
        }
    }
}

// ── Tombol menu hasil ─────────────────────────────────────────────────────────
@Composable
private fun ResultMenuButton(
    imageResId: Int?,
    textFallback: String,
    backgroundColor: Color,
    isHome: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (isPressed) 0.94f else 1f)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId != null) {
            Image(
                painter            = painterResource(id = imageResId),
                contentDescription = textFallback,
                modifier           = Modifier.fillMaxWidth().height(62.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = textFallback,
                    color      = if (isHome) Color(0xFF1E3A8A) else Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize   = 20.sp
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@androidx.compose.ui.tooling.preview.Preview(
    name            = "Quiz Result",
    showBackground  = true,
    backgroundColor = 0xFF1E40AF,
    widthDp         = 390,
    heightDp        = 844
)
@Composable
fun QuizResultPreview() {
    com.example.math_quiz.ui.theme.MathquizTheme {
        QuizResultDialog(score = 80)
    }
}
