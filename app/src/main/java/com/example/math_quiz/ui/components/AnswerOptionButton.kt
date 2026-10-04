package com.example.math_quiz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.ui.theme.FredokaFontFamily

@Composable
fun AnswerOptionButton(
    text: String,
    baseColor: Color,
    darkColor: Color,
    lightColor: Color,
    isCorrectAnswer: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
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
        // 1. EFEK BERSINAR (OUTER GLOW) JIKA INI JAWABAN BENAR
        if (isCorrectAnswer && isSelected) {
            Canvas(modifier = Modifier.matchParentSize().padding(-8.dp)) {
                drawRoundRect(
                    color = Color(0x664ADE80), // Aura hijau transparan bersinar
                    cornerRadius = CornerRadius(26.dp.toPx(), 26.dp.toPx())
                )
            }
        }

        // 2. BADAN TOMBOL 3D GLOSSY
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .shadow(
                    elevation = if (isCorrectAnswer) 12.dp else 6.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = darkColor.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(lightColor, baseColor, darkColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Highlight kilauan cahaya 3D di sudut kiri atas tombol
            Canvas(modifier = Modifier.matchParentSize()) {
                drawOval(
                    color = Color.White.copy(alpha = 0.35f),
                    topLeft = Offset(size.width * 0.08f, size.height * 0.12f),
                    size = Size(size.width * 0.22f, size.height * 0.35f)
                )
            }

            // Teks Angka Opsi (3D Font)
            Text(
                text = text,
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FredokaFontFamily,
                modifier = Modifier.shadow(2.dp, CircleShape, spotColor = Color(0x40000000))
            )
        }

        // 3. BADGE CENTANG HIJAU DI SUDUT KANAN ATAS (JIKA BENAR)
        if (isCorrectAnswer && isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(28.dp)
                    .shadow(3.dp, CircleShape)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color(0xFF16A34A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FredokaFontFamily
                )
            }
        }
    }
}
