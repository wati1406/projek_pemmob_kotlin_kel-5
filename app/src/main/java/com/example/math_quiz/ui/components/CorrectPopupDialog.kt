package com.example.math_quiz.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.math_quiz.ui.theme.MathquizTheme

// ── Dialog wrapper dengan animasi bounce ──────────────────────────────────────
@Composable
fun CorrectPopupDialog(
    pointsEarned: Int = 10,
    onDismiss: () -> Unit = {}
) {
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue   = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMedium
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress      = true,
            dismissOnClickOutside   = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .graphicsLayer { scaleX = scale.value; scaleY = scale.value }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            CorrectPopupContent(pointsEarned = pointsEarned)
        }
    }
}

// ── Konten visual Popup Correct — style sama dengan Incorrect, warna hijau ─────
@Composable
fun CorrectPopupContent(
    pointsEarned: Int = 10,
    modifier: Modifier = Modifier
) {
    val popScale = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        popScale.animateTo(
            targetValue   = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMediumLow
            )
        )
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .graphicsLayer { scaleX = popScale.value; scaleY = popScale.value },
        contentAlignment = Alignment.Center
    ) {
        // Percikan hijau animatif di kiri & kanan
        GreenSparksDecoration(
            modifier = Modifier
                .width(320.dp)
                .height(130.dp)
        )

        // Kapsul hijau lembut — struktur identik dengan Incorrect
        Box(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(40.dp), spotColor = Color(0x4016A34A))
                .clip(RoundedCornerShape(40.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0FDF4), // Hijau sangat lembut atas
                            Color(0xFFDCFCE7)  // Hijau lembut bawah
                        )
                    )
                )
                .border(
                    width = 2.5.dp,
                    color = Color(0xFFBBF7D0),
                    shape = RoundedCornerShape(40.dp)
                )
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Baris atas: ✓ lingkaran hijau + Teks "Correct!"
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GreenCheckCircle(size = 52.dp)

                    Text(
                        text       = "Correct!",
                        fontSize   = 30.sp,
                        fontWeight = FontWeight.Black,
                        color      = Color(0xFF15803D),
                        textAlign  = TextAlign.Center,
                        style      = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color      = Color(0x3014532D),
                                offset     = Offset(0f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }

                // Sub-pill putih: "+10 pts" — identik dengan "Answer: X" di Incorrect
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(50.dp), spotColor = Color(0x2016A34A))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(50.dp))
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF475569), fontWeight = FontWeight.Bold)) {
                                append("+")
                            }
                            withStyle(SpanStyle(color = Color(0xFF15803D), fontWeight = FontWeight.Black)) {
                                append("$pointsEarned pts")
                            }
                        },
                        fontSize  = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ── Lingkaran hijau 3D dengan centang putih ────────────────────────────────────
@Composable
fun GreenCheckCircle(size: Dp = 52.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val r            = this.size.minDimension / 2f
        val center       = Offset(r, r)

        // Ring luar hijau tua
        drawCircle(color = Color(0xFF14532D), radius = r, center = center)

        // Gradien hijau 3D
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF4ADE80), Color(0xFF15803D)),
                startY = 0f, endY = this.size.height
            ),
            radius = r * 0.90f,
            center = center
        )

        // Gloss putih atas
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x88FFFFFF), Color(0x00FFFFFF)),
                startY = 0f, endY = r * 0.8f
            ),
            radius = r * 0.82f,
            center = center
        )

        // Centang putih
        val sw        = r * 0.22f
        val checkPath = Path().apply {
            moveTo(this@Canvas.size.width * 0.27f, this@Canvas.size.height * 0.52f)
            lineTo(this@Canvas.size.width * 0.44f, this@Canvas.size.height * 0.68f)
            lineTo(this@Canvas.size.width * 0.72f, this@Canvas.size.height * 0.36f)
        }
        drawPath(
            path  = checkPath,
            color = Color.White,
            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

// ── Percikan hijau animatif ────────────────────────────────────────────────────
@Composable
private fun GreenSparksDecoration(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "greenSparks")
    val sparkRotation by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkRot"
    )

    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        val sparkColor = Color(0xFF4ADE80)

        val leftSparks  = listOf(
            Triple(w * 0.05f, h * 0.20f, -40f),
            Triple(w * 0.02f, h * 0.50f,   0f),
            Triple(w * 0.05f, h * 0.80f,  40f)
        )
        val rightSparks = listOf(
            Triple(w * 0.95f, h * 0.20f,  40f),
            Triple(w * 0.98f, h * 0.50f,   0f),
            Triple(w * 0.95f, h * 0.80f, -40f)
        )

        val rayW = 7.dp.toPx(); val rayH = 18.dp.toPx()
        val cr   = CornerRadius(rayW / 2f, rayW / 2f)

        (leftSparks + rightSparks).forEach { (cx, cy, angle) ->
            rotate(degrees = angle + (sparkRotation * 0.05f), pivot = Offset(cx, cy)) {
                drawRoundRect(
                    color        = sparkColor.copy(alpha = 0.85f),
                    topLeft      = Offset(cx - rayW / 2f, cy - rayH / 2f),
                    size         = Size(rayW, rayH),
                    cornerRadius = cr
                )
            }
        }
    }
}

// ── Preview ────────────────────────────────────────────────────────────────────
@Preview(
    name            = "Correct Popup Preview",
    showBackground  = true,
    backgroundColor = 0xFFBAE6FD,
    widthDp         = 390,
    heightDp        = 220
)
@Composable
fun CorrectPopupPreview() {
    MathquizTheme {
        Box(
            modifier         = Modifier.fillMaxSize().background(Color(0xFFBAE6FD)),
            contentAlignment = Alignment.Center
        ) {
            CorrectPopupContent(pointsEarned = 10)
        }
    }
}
