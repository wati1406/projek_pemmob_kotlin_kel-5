package com.example.math_quiz.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

/**
 * Popup "Incorrect!" yang muncul ketika user memilih jawaban yang salah pada Quiz.
 *
 * Sesuai referensi desain @incorrect answer.png:
 * - Kapsul pink/merah muda dengan lingkaran silang merah 'X' di kiri
 * - Teks "Incorrect!" warna merah bold di tengah
 * - Pill putih di bawahnya "Answer: [Jawaban Benar]"
 * - Efek percikan (sparks) pink/merah di sekitarnya
 */
@Composable
fun IncorrectPopupDialog(
    correctAnswer: String = "15",
    onDismiss: () -> Unit = {}
) {
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .scale(scale.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            IncorrectPopupContent(correctAnswer = correctAnswer)
        }
    }
}

/**
 * Banner / Content utama Popup Incorrect (Kapsul Pink + Circle X + Teks "Incorrect!" + Badge "Answer: X").
 */
@Composable
fun IncorrectPopupContent(
    correctAnswer: String = "15",
    modifier: Modifier = Modifier
) {
    val popScale = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        popScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .graphicsLayer {
                scaleX = popScale.value
                scaleY = popScale.value
            },
        contentAlignment = Alignment.Center
    ) {
        // Percikan sparks pink di kiri & kanan
        PinkSparksDecoration(
            modifier = Modifier
                .width(320.dp)
                .height(130.dp)
        )

        // Banner Utama Pink Soft
        Box(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(40.dp), spotColor = Color(0x40E11D48))
                .clip(RoundedCornerShape(40.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF1F2), // Very soft pink top
                            Color(0xFFFFE4E6)  // Soft pink bottom
                        )
                    )
                )
                .border(
                    width = 2.5.dp,
                    color = Color(0xFFFECDD3),
                    shape = RoundedCornerShape(40.dp)
                )
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Baris Atas: Red X Circle + Teks "Incorrect!"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Circle X Merah 3D
                    RedCrossCircle(size = 52.dp)

                    // Teks "Incorrect!"
                    Text(
                        text = "Incorrect!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFE11D48),
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color(0x30BE123C),
                                offset = Offset(0f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }

                // Sub-pill Putih: "Answer: [correctAnswer]"
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(50.dp), spotColor = Color(0x20E11D48))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFFECDD3), RoundedCornerShape(50.dp))
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF475569), fontWeight = FontWeight.Bold)) {
                                append("Answer: ")
                            }
                            withStyle(SpanStyle(color = Color(0xFFE11D48), fontWeight = FontWeight.Black)) {
                                append(correctAnswer)
                            }
                        },
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Lingkaran merah 3D dengan tanda silang 'X' putih presisi.
 */
@Composable
fun RedCrossCircle(size: Dp = 52.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val r = this.size.minDimension / 2f
        val centerOffset = Offset(r, r)

        // Shadow / Ring luar merah tua
        drawCircle(
            color = Color(0xFF9F1239),
            radius = r,
            center = centerOffset
        )

        // Gradien merah 3D
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFB7185), Color(0xFFE11D48)),
                startY = 0f,
                endY = this.size.height
            ),
            radius = r * 0.90f,
            center = centerOffset
        )

        // Kilapan (gloss) bagian atas
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x88FFFFFF), Color(0x00FFFFFF)),
                startY = 0f,
                endY = r * 0.8f
            ),
            radius = r * 0.82f,
            center = centerOffset
        )

        // Tanda Silang 'X' Putih
        val strokeWidth = r * 0.22f
        val offset = r * 0.32f

        drawLine(
            color = Color.White,
            start = Offset(r - offset, r - offset),
            end = Offset(r + offset, r + offset),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White,
            start = Offset(r + offset, r - offset),
            end = Offset(r - offset, r + offset),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Percikan 6 sinar pink (3 kiri, 3 kanan) menyebar di sekitar popup.
 */
@Composable
private fun PinkSparksDecoration(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pinkSparks")
    val sparkRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkRotation"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val sparkColor = Color(0xFFFB7185)

        // Definisi posisi ray di sekeliling banner
        val leftSparks = listOf(
            Triple(w * 0.05f, h * 0.20f, -40f),
            Triple(w * 0.02f, h * 0.50f, 0f),
            Triple(w * 0.05f, h * 0.80f, 40f)
        )
        val rightSparks = listOf(
            Triple(w * 0.95f, h * 0.20f, 40f),
            Triple(w * 0.98f, h * 0.50f, 0f),
            Triple(w * 0.95f, h * 0.80f, -40f)
        )

        val rayWidth = 7.dp.toPx()
        val rayHeight = 18.dp.toPx()
        val cornerRadius = CornerRadius(rayWidth / 2f, rayWidth / 2f)

        (leftSparks + rightSparks).forEachIndexed { i, (cx, cy, angle) ->
            rotate(degrees = angle + (sparkRotation * 0.05f), pivot = Offset(cx, cy)) {
                drawRoundRect(
                    color = sparkColor.copy(alpha = 0.85f),
                    topLeft = Offset(cx - rayWidth / 2f, cy - rayHeight / 2f),
                    size = Size(rayWidth, rayHeight),
                    cornerRadius = cornerRadius
                )
            }
        }
    }
}

@Preview(
    name = "Incorrect Popup Preview",
    showBackground = true,
    backgroundColor = 0xFFBAE6FD,
    widthDp = 390,
    heightDp = 220
)
@Composable
fun IncorrectPopupPreview() {
    MathquizTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFBAE6FD)),
            contentAlignment = Alignment.Center
        ) {
            IncorrectPopupContent(correctAnswer = "15")
        }
    }
}
