package com.example.math_quiz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.R
import com.example.math_quiz.ui.theme.QuizGreen
import com.example.math_quiz.ui.theme.QuizGreenDark
import com.example.math_quiz.ui.theme.QuizGreenLight

/**
 * Reusable Component: PrimaryButton
 * - Mendukung render langsung dari file aset 3D asli (seperti btn_play.png / ChatGPT Image Sep 15, 2026, 02_06_47 PM.png)
 * - Juga mendukung fallback render Compose 3D Pill glossy dengan animasi tactile press.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageResId: Int? = R.drawable.btn_play,
    showPlayIcon: Boolean = true,
    height: Dp = 82.dp,
    fontSize: TextUnit = 32.sp,
    gradientColors: List<Color> = listOf(QuizGreenLight, QuizGreen, QuizGreenDark),
    shadowColor: Color = QuizGreenDark,
    showSparks: Boolean = true,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "btnScale"
    )

    if (imageResId != null) {
        // Menggunakan aset gambar 3D persis seperti file mockup
        Box(
            modifier = modifier
                .scale(scale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = text,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
            )
        }
    } else {
        // Fallback Compose 3D Pill Button
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSparks) {
                ButtonSparks(
                    color = gradientColors[1],
                    isLeft = true,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .scale(scale)
                    .height(height)
                    .shadow(
                        elevation = if (isPressed) 4.dp else 12.dp,
                        shape = CircleShape,
                        spotColor = shadowColor.copy(alpha = 0.5f),
                        ambientColor = shadowColor.copy(alpha = 0.25f)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradientColors
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height

                    drawOval(
                        color = Color.White.copy(alpha = 0.38f),
                        topLeft = Offset(w * 0.08f, h * 0.12f),
                        size = Size(w * 0.18f, h * 0.32f)
                    )

                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.22f),
                        topLeft = Offset(2f, 2f),
                        size = Size(w - 4f, h - 4f),
                        cornerRadius = CornerRadius(h / 2, h / 2),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    if (showPlayIcon) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .shadow(2.dp, CircleShape, spotColor = Color(0x33000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(24.dp)) {
                                val w = size.width
                                val h = size.height
                                val playPath = Path().apply {
                                    moveTo(w * 0.22f, h * 0.12f)
                                    lineTo(w * 0.88f, h * 0.50f)
                                    lineTo(w * 0.22f, h * 0.88f)
                                    close()
                                }
                                drawPath(
                                    path = playPath,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        modifier = Modifier.shadow(
                            elevation = 2.dp,
                            shape = CircleShape,
                            spotColor = Color(0x44000000)
                        )
                    )
                }
            }

            if (showSparks) {
                ButtonSparks(
                    color = gradientColors[1],
                    isLeft = false,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ButtonSparks(
    color: Color,
    isLeft: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width = 18.dp, height = 36.dp)) {
        val w = size.width
        val h = size.height

        val xStart = if (isLeft) w else 0f

        drawRoundRect(
            color = color,
            topLeft = Offset(xStart - (if (isLeft) 16f else -4f), h * 0.18f),
            size = Size(14f, 7f),
            cornerRadius = CornerRadius(3.5f, 3.5f)
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(xStart - (if (isLeft) 18f else -2f), h * 0.64f),
            size = Size(16f, 7f),
            cornerRadius = CornerRadius(3.5f, 3.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    Box(modifier = Modifier.padding(24.dp)) {
        PrimaryButton(
            text = "PLAY",
            onClick = {}
        )
    }
}
