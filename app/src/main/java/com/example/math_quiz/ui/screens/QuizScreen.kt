package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.example.math_quiz.data.QuizRepository
import com.example.math_quiz.ui.theme.*

@Composable
fun QuizScreen(
    level: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit = {}
) {
    // Dynamic State
    val questions = remember(level) { QuizRepository.getQuestionsByLevel(level) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = questions[currentIndex]
    
    val questionNumber = currentIndex + 1
    val totalQuestions = questions.size
    val score = 120 // Still placeholder for now as per instructions (we don't handle correct/incorrect)
    val timeLeft = 8
    val options = currentQuestion.options

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBgLight)
    ) {
        // Decorative background elements (bottom shapes)
        QuizBackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp, bottom = 24.dp), 
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            QuizTopBar(
                level = level,
                questionNumber = questionNumber,
                totalQuestions = totalQuestions,
                score = score,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer
            QuizTimer(timeLeft = timeLeft)

            Spacer(modifier = Modifier.height(24.dp))

            // Question Card
            QuizQuestionCard(questionText = currentQuestion.questionText)

            Spacer(modifier = Modifier.height(24.dp))

            // Options Grid
            QuizOptionsGrid(
                options = options,
                onOptionSelected = { selected ->
                    // For now, just advance to next question
                    if (currentIndex < questions.size - 1) {
                        currentIndex++
                    }
                    onOptionSelected(selected)
                }
            )
        }
    }
}

@Composable
fun QuizTopBar(
    level: Int,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextNavy
            )
        }

        // Level Indicator
        Box(
            modifier = Modifier
                .height(42.dp)
                .shadow(2.dp, RoundedCornerShape(21.dp))
                .clip(RoundedCornerShape(21.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Level $level",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextNavy
            )
        }
        
        // Question Number
        Box(
            modifier = Modifier
                .height(42.dp)
                .shadow(2.dp, RoundedCornerShape(21.dp))
                .clip(RoundedCornerShape(21.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$questionNumber/$totalQuestions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextNavy
            )
        }

        // Score
        Box(
            modifier = Modifier
                .height(42.dp)
                .shadow(2.dp, RoundedCornerShape(21.dp))
                .clip(RoundedCornerShape(21.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏆", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Score: $score",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuizTextNavy,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Composable
fun QuizTimer(timeLeft: Int) {
    val totalTime = 15f
    val progress = timeLeft / totalTime

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Yellow clock icon approximation
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(QuizYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "⏱", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%02ds", timeLeft),
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = QuizTextNavy
        )
        Spacer(modifier = Modifier.width(12.dp))
        
        // Custom Progress Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color(0xFFCBD5E1).copy(alpha = 0.5f)) // Light gray background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(QuizYellow, QuizOrange)
                        )
                    )
            )
        }
    }
}

@Composable
fun QuizQuestionCard(questionText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = Color(0x22000000), ambientColor = Color(0x11000000))
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
    ) {
        // Decorative math symbols drawn with Canvas to look chubby and clean
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            val cap = StrokeCap.Round
            
            // Top-left Light Blue Plus
            val plusColor = QuizBlueLight
            val plusCenter = Offset(60.dp.toPx(), 60.dp.toPx())
            val plusSize = 14.dp.toPx()
            drawLine(plusColor, Offset(plusCenter.x - plusSize, plusCenter.y), Offset(plusCenter.x + plusSize, plusCenter.y), strokeWidth, cap)
            drawLine(plusColor, Offset(plusCenter.x, plusCenter.y - plusSize), Offset(plusCenter.x, plusCenter.y + plusSize), strokeWidth, cap)

            // Top-right Yellow Divide
            val divColor = QuizYellow
            val divCenter = Offset(size.width - 60.dp.toPx(), 60.dp.toPx())
            val divSize = 12.dp.toPx()
            drawLine(divColor, Offset(divCenter.x - divSize, divCenter.y), Offset(divCenter.x + divSize, divCenter.y), strokeWidth, cap)
            drawCircle(divColor, radius = 5.dp.toPx(), center = Offset(divCenter.x, divCenter.y - 14.dp.toPx()))
            drawCircle(divColor, radius = 5.dp.toPx(), center = Offset(divCenter.x, divCenter.y + 14.dp.toPx()))

            // Bottom-left Pink Minus
            val minColor = QuizPinkLight
            val minCenter = Offset(60.dp.toPx(), size.height - 60.dp.toPx())
            val minSize = 14.dp.toPx()
            drawLine(minColor, Offset(minCenter.x - minSize, minCenter.y), Offset(minCenter.x + minSize, minCenter.y), strokeWidth, cap)

            // Bottom-right Green Multiply (Cross)
            val mulColor = QuizGreenLight
            val mulCenter = Offset(size.width - 60.dp.toPx(), size.height - 60.dp.toPx())
            val mulSize = 10.dp.toPx()
            drawLine(mulColor, Offset(mulCenter.x - mulSize, mulCenter.y - mulSize), Offset(mulCenter.x + mulSize, mulCenter.y + mulSize), strokeWidth, cap)
            drawLine(mulColor, Offset(mulCenter.x + mulSize, mulCenter.y - mulSize), Offset(mulCenter.x - mulSize, mulCenter.y + mulSize), strokeWidth, cap)
        }

        // Question Text dynamically parsed
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = buildAnnotatedString {
                // Split the question like "10 + 5 = ?" into parts by space
                val parts = questionText.split(" ")
                parts.forEachIndexed { index, part ->
                    val color = when {
                        part == "+" || part == "-" || part == "×" || part == "÷" -> QuizBlue
                        part == "?" -> QuizPink
                        else -> QuizTextNavy // Numbers and equals
                    }
                    withStyle(style = SpanStyle(color = color)) {
                        append(part)
                    }
                    if (index < parts.size - 1) append(" ")
                }
            },
            fontSize = 72.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun QuizOptionsGrid(
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuizOptionButton(
                text = options[0],
                buttonColor = QuizBlue,
                shadowColor = QuizBlueDark,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(options[0]) }
            )
            QuizOptionButton(
                text = options[1],
                buttonColor = QuizGreen,
                shadowColor = QuizGreenDark,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(options[1]) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuizOptionButton(
                text = options[2],
                buttonColor = QuizYellow,
                shadowColor = QuizYellowDark,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(options[2]) }
            )
            QuizOptionButton(
                text = options[3],
                buttonColor = QuizPink,
                shadowColor = QuizPinkDark,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(options[3]) }
            )
        }
    }
}

@Composable
fun QuizOptionButton(
    text: String,
    buttonColor: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "btnScale"
    )

    Box(
        modifier = modifier
            .height(110.dp)
            .scale(scale)
            .shadow(if (isPressed) 2.dp else 10.dp, RoundedCornerShape(24.dp), spotColor = shadowColor, ambientColor = shadowColor)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(buttonColor.copy(alpha = 0.9f), buttonColor),
                    startY = 0f,
                    endY = 300f
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner shadow / bottom darken for 3D feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.15f)),
                        startY = 150f,
                        endY = 300f
                    )
                )
        )

        // Glossy highlight effect at the top (curved)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.3f),
                topLeft = Offset(16.dp.toPx(), 4.dp.toPx()),
                size = Size(size.width - 32.dp.toPx(), 20.dp.toPx()),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
        }
        
        Text(
            text = text,
            color = Color.White,
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.2f),
                    offset = Offset(0f, 4f),
                    blurRadius = 4f
                )
            )
        )
    }
}

@Composable
fun QuizBackgroundDecorations() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Top-left light blue blob
        val pathTopLeft = Path().apply {
            moveTo(0f, 0f)
            lineTo(width * 0.4f, 0f)
            cubicTo(
                width * 0.3f, height * 0.05f,
                width * 0.15f, height * 0.15f,
                0f, height * 0.12f
            )
            close()
        }
        drawPath(pathTopLeft, color = BlobBlue.copy(alpha = 0.4f))

        // Top-right light blue blob
        val pathTopRight = Path().apply {
            moveTo(width, 0f)
            lineTo(width * 0.6f, 0f)
            cubicTo(
                width * 0.7f, height * 0.08f,
                width * 0.85f, height * 0.2f,
                width, height * 0.15f
            )
            close()
        }
        drawPath(pathTopRight, color = BlobBlue)

        // Bottom-left yellow blob
        val pathBottomLeft = Path().apply {
            moveTo(0f, height)
            lineTo(width * 0.6f, height)
            cubicTo(
                width * 0.5f, height * 0.85f,
                width * 0.2f, height * 0.75f,
                0f, height * 0.8f
            )
            close()
        }
        drawPath(pathBottomLeft, color = BlobYellow)

        // Bottom-right light blue blob
        val pathBottomRight = Path().apply {
            moveTo(width, height)
            lineTo(width * 0.4f, height)
            cubicTo(
                width * 0.6f, height * 0.85f,
                width * 0.9f, height * 0.7f,
                width, height * 0.8f
            )
            close()
        }
        drawPath(pathBottomRight, color = BlobBlue)
    }

    // Scattered Numbers and Symbols at the bottom
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "1",
            color = QuizBlueLight.copy(alpha = 0.6f),
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 60.dp, y = (-70).dp)
                .rotate(-15f)
        )
        
        Text(
            text = "3",
            color = QuizGreenLight.copy(alpha = 0.4f),
            fontSize = 80.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 20.dp, y = (-30).dp)
                .rotate(10f)
        )
        
        Text(
            text = "➕",
            color = QuizYellowDark.copy(alpha = 0.5f),
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = (-30).dp, y = (-15).dp)
                .rotate(25f)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun QuizScreenPreview() {
    MathquizTheme {
        QuizScreen(level = 3)
    }
}
