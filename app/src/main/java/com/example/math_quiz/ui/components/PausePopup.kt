package com.example.math_quiz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.theme.FredokaFontFamily

private val PausePanelBg = Color(0xFFF9FAFC)
private val PauseNavy = Color(0xFF0B1F4D)
private val PauseYellow = Color(0xFFFFCC33)

/**
 * Popup Pause — tampil di atas QuizScreen saat user menekan tombol back.
 * Desain mengikuti asset "projek pemmob/asset pause".
 */
@Composable
fun PausePopup(
    score: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Overlay redup, menahan klik agar tidak tembus ke layar quiz
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x99CBD5E1))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .widthIn(max = 380.dp)
        ) {
            // ── Panel putih
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(36.dp),
                        spotColor = Color(0x33000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .clip(RoundedCornerShape(36.dp))
                    .background(PausePanelBg)
                    .border(2.dp, Color.White, RoundedCornerShape(36.dp))
            ) {
                // Dekorasi simbol matematika
                Canvas(modifier = Modifier.matchParentSize()) { drawPanelDecorations() }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Paused",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 44.sp,
                        color = PauseNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chip skor
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFFFF4D6))
                            .padding(horizontal = 18.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.piala),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Score: $score",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = PauseNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PauseImageButton(R.drawable.pause_btn_resume, "Resume", 560f / 150f, onResume)
                    PauseImageButton(R.drawable.pause_btn_restart, "Restart", 560f / 145f, onRestart)
                    PauseImageButton(R.drawable.pause_btn_exit, "Exit", 560f / 145f, onExit)
                }
            }

            // ── Sinar kuning di samping ikon pause
            Canvas(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(width = 180.dp, height = 120.dp)
            ) {
                val cx = size.width / 2f
                val cy = 60.dp.toPx()
                val w = 7.dp.toPx()
                fun ray(x1: Float, y1: Float, x2: Float, y2: Float) = drawLine(
                    PauseYellow,
                    Offset(cx + x1.dp.toPx(), cy + y1.dp.toPx()),
                    Offset(cx + x2.dp.toPx(), cy + y2.dp.toPx()),
                    w, StrokeCap.Round
                )
                ray(-72f, -8f, -58f, 0f)
                ray(-72f, 24f, -58f, 16f)
                ray(72f, -8f, 58f, 0f)
                ray(72f, 24f, 58f, 16f)
            }

            // ── Ikon pause bulat di atas panel
            Image(
                painter = painterResource(id = R.drawable.pause_icon),
                contentDescription = "Paused",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(120.dp)
            )
        }
    }
}

@Composable
private fun PauseImageButton(
    @DrawableRes res: Int,
    label: String,
    aspectRatio: Float,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100, easing = FastOutSlowInEasing),
        label = "pauseBtnScale"
    )
    Image(
        painter = painterResource(id = res),
        contentDescription = label,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

private fun DrawScope.drawPanelDecorations() {
    val w = size.width
    val h = size.height
    val stroke = 11.dp.toPx()
    val cap = StrokeCap.Round

    // Plus biru (kiri atas)
    run {
        val c = Offset(w * 0.12f, h * 0.15f)
        val a = 17.dp.toPx()
        val col = Color(0xFF93C5FD)
        drawLine(col, Offset(c.x - a, c.y + 4.dp.toPx()), Offset(c.x + a, c.y - 4.dp.toPx()), stroke, cap)
        drawLine(col, Offset(c.x - 4.dp.toPx(), c.y - a), Offset(c.x + 4.dp.toPx(), c.y + a), stroke, cap)
    }
    // Segitiga kuning (kanan atas)
    run {
        val c = Offset(w * 0.88f, h * 0.13f)
        val r = 18.dp.toPx()
        val path = Path().apply {
            moveTo(c.x + r, c.y - r)
            lineTo(c.x + r * 0.9f, c.y + r)
            lineTo(c.x - r, c.y + r * 0.1f)
            close()
        }
        drawPath(path, Color(0xFFFDE7B0))
    }
    // Strip pink (kiri tengah)
    run {
        val c = Offset(w * 0.13f, h * 0.34f)
        val a = 14.dp.toPx()
        drawLine(Color(0xFFF9C2D0), Offset(c.x - a, c.y + 6.dp.toPx()), Offset(c.x + a, c.y - 6.dp.toPx()), stroke, cap)
    }
    // Silang hijau (kanan tengah)
    run {
        val c = Offset(w * 0.88f, h * 0.335f)
        val a = 13.dp.toPx()
        val col = Color(0xFFA7F3D0)
        drawLine(col, Offset(c.x - a, c.y - a), Offset(c.x + a, c.y + a), stroke, cap)
        drawLine(col, Offset(c.x + a, c.y - a), Offset(c.x - a, c.y + a), stroke, cap)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PausePopupPreview() {
    PausePopup(score = 120, onResume = {}, onRestart = {}, onExit = {})
}
