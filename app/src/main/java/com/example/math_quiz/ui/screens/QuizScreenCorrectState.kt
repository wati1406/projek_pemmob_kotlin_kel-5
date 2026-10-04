package com.example.math_quiz.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.ui.components.AnswerOptionButton
import com.example.math_quiz.ui.components.CorrectPopupContent
import com.example.math_quiz.ui.components.IncorrectPopupContent

// ── Tombol Back tanpa painterResource (aman untuk Preview & Runtime) ──────────
@Composable
private fun BackButton(onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF7DD3FC))
            .border(2.dp, Color(0xFF3B82F6), CircleShape)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            val strokeWidth = 4.dp.toPx()
            val mid = size.height / 2f
            val arrowX = size.width * 0.55f
            val tailX = size.width * 0.15f
            // Shaft ←
            drawLine(
                color = Color.White,
                start = Offset(arrowX, mid),
                end = Offset(tailX, mid),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // Arrow head top-left
            drawLine(
                color = Color.White,
                start = Offset(tailX, mid),
                end = Offset(tailX + size.width * 0.25f, mid - size.height * 0.28f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // Arrow head bottom-left
            drawLine(
                color = Color.White,
                start = Offset(tailX, mid),
                end = Offset(tailX + size.width * 0.25f, mid + size.height * 0.28f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

// ── Badge kapsul kecil (Level / Nomor soal) ───────────────────────────────────
@Composable
private fun CapsuleBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFDBEAFE))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E3A8A))
    }
}

// ── Layar Quiz saat jawaban BENAR ─────────────────────────────────────────────
@Composable
fun QuizScreenCorrectState(
    onNextQuestion: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2FE))
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CapsuleBadge(text = "Level 3")
                CapsuleBadge(text = "3/10")
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("🏆 Score: 130", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E3A8A))
                Text("(+10)", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. TIMER BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⏱ 08s", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF1E3A8A))
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBFDBFE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .background(Color(0xFFF97316), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. KARTU SOAL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(28.dp), spotColor = Color(0x22000000))
                .background(Color.White, RoundedCornerShape(28.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("10 ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                    Text("+ ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF3B82F6))
                    Text("5 = ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                }
            }
        }

        // 4. POP-UP CORRECT (pakai CorrectPopupContent — aman Preview & Runtime)
        CorrectPopupContent(pointsEarned = 10)

        Spacer(modifier = Modifier.height(26.dp))

        // 5. GRID PILIHAN JAWABAN (2x2)
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AnswerOptionButton(
                text = "10",
                baseColor = Color(0xFF3B82F6),
                darkColor = Color(0xFF1D4ED8),
                lightColor = Color(0xFF60A5FA),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            AnswerOptionButton(
                text = "15",
                baseColor = Color(0xFF22C55E),
                darkColor = Color(0xFF16A34A),
                lightColor = Color(0xFF4ADE80),
                isCorrectAnswer = true,
                isSelected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AnswerOptionButton(
                text = "20",
                baseColor = Color(0xFFFBBF24),
                darkColor = Color(0xFFD97706),
                lightColor = Color(0xFFFDE047),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            AnswerOptionButton(
                text = "25",
                baseColor = Color(0xFFFB7185),
                darkColor = Color(0xFFE11D48),
                lightColor = Color(0xFFFDA4AF),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. TOMBOL NEXT
        QuizNextButton(onClick = onNextQuestion)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ── Layar Quiz saat jawaban SALAH ─────────────────────────────────────────────
@Composable
fun QuizScreenIncorrectState(
    onNextQuestion: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2FE))
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CapsuleBadge(text = "Level 3")
                CapsuleBadge(text = "3/10")
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("🏆 Score: 120", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E3A8A))
                Text("(+0)", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. TIMER BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⏱ 08s", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF1E3A8A))
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBFDBFE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .background(Color(0xFFF97316), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. KARTU SOAL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(28.dp), spotColor = Color(0x22000000))
                .background(Color.White, RoundedCornerShape(28.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("10 ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                    Text("+ ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF3B82F6))
                    Text("5 = ", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                    Text("?", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFFFB7185))
                }
            }
        }

        // 4. POP-UP INCORRECT (pakai IncorrectPopupContent — aman Preview & Runtime)
        Spacer(modifier = Modifier.height(12.dp))
        IncorrectPopupContent(correctAnswer = "15")

        Spacer(modifier = Modifier.height(14.dp))

        // 5. GRID PILIHAN JAWABAN (2x2)
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AnswerOptionButton(
                text = "10",
                baseColor = Color(0xFFEF4444),
                darkColor = Color(0xFFDC2626),
                lightColor = Color(0xFFF87171),
                isSelected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            AnswerOptionButton(
                text = "15",
                baseColor = Color(0xFF22C55E),
                darkColor = Color(0xFF16A34A),
                lightColor = Color(0xFF4ADE80),
                isCorrectAnswer = true,
                isSelected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AnswerOptionButton(
                text = "20",
                baseColor = Color(0xFFFBBF24),
                darkColor = Color(0xFFD97706),
                lightColor = Color(0xFFFDE047),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            AnswerOptionButton(
                text = "25",
                baseColor = Color(0xFFFB7185),
                darkColor = Color(0xFFE11D48),
                lightColor = Color(0xFFFDA4AF),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. TOMBOL NEXT
        QuizNextButton(onClick = onNextQuestion)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@androidx.compose.ui.tooling.preview.Preview(
    name = "Quiz Correct Screen",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun QuizScreenCorrectPreview() {
    com.example.math_quiz.ui.theme.MathquizTheme {
        QuizScreenCorrectState()
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    name = "Quiz Incorrect Screen",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun QuizScreenIncorrectPreview() {
    com.example.math_quiz.ui.theme.MathquizTheme {
        QuizScreenIncorrectState()
    }
}
