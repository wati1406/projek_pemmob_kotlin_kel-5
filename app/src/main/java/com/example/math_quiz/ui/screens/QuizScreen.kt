package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.activity.compose.BackHandler
import com.example.math_quiz.R
import com.example.math_quiz.audio.SoundManager
import com.example.math_quiz.ui.components.PausePopup
import com.example.math_quiz.data.QuizRepository
import com.example.math_quiz.ui.components.PrimaryButton
import com.example.math_quiz.ui.components.IncorrectPopupContent
import com.example.math_quiz.ui.components.CorrectPopupContent
import com.example.math_quiz.ui.theme.*
import kotlinx.coroutines.delay

enum class FeedbackType {
    NONE,
    CORRECT,
    INCORRECT,
    TIME_UP
}

@Composable
fun QuizScreen(
    level: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
    onCorrectAnswer: () -> Unit = {},
    onWrongAnswer: () -> Unit = {},
    onTimeUp: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onQuizCompleted: (Int) -> Unit = {},
    onNextLevel: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    // Menentukan durasi waktu berdasarkan tingkat kesulitan:
    // Easy (Level 1) = 15 detik, Medium (Level 2) = 13 detik, Hard (Level 3) = 10 detik
    val totalDuration = when (level) {
        1 -> 15
        2 -> 13
        3 -> 10
        else -> 15
    }

    // Dynamic State
    // State pause & restart
    var isPaused by remember { mutableStateOf(false) }
    var restartKey by remember { mutableIntStateOf(0) }

    val questions = remember(level, restartKey) { QuizRepository.getQuestionsByLevel(level) }
    var currentIndex by remember(level, restartKey) { mutableIntStateOf(0) }
    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }

    val questionNumber = currentIndex + 1
    val totalQuestions = questions.size

    // Skor dinamis: bertambah +10 setiap jawaban benar
    var score by remember(level, restartKey) { mutableIntStateOf(0) }
    var showResultDialog by remember(level, restartKey) { mutableStateOf(false) }

    // State jawaban dan feedback
    var selectedOption by remember(currentIndex, restartKey) { mutableStateOf<String?>(null) }
    var feedbackType by remember(currentIndex, restartKey) { mutableStateOf(FeedbackType.NONE) }

    // Timer state per soal (di-reset otomatis setiap kali currentIndex berubah)
    var timeLeft by remember(currentIndex, restartKey) { mutableIntStateOf(totalDuration) }
    val options = currentQuestion.options

    val scrollState = rememberScrollState()

    // Otomatis scroll ke bawah saat feedback muncul (Correct, Incorrect, Time's Up)
    // agar seluruh tombol NEXT terlihat utuh di layar dan tidak terpotong di tepi bawah
    LaunchedEffect(feedbackType) {
        if (feedbackType != FeedbackType.NONE) {
            delay(100L)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    // Coroutine timer countdown real-time:
    // Hanya berjalan saat feedbackType == NONE (belum dijawab dan belum time up).
    // Berhenti otomatis saat pengguna menjawab (CORRECT/INCORRECT) atau waktu habis (TIME_UP).
    // Timer juga berhenti saat popup pause tampil, dan lanjut dari sisa waktu saat Resume.
    LaunchedEffect(currentIndex, feedbackType, isPaused, restartKey) {
        if (feedbackType == FeedbackType.NONE && !isPaused) {
            while (timeLeft > 0 && feedbackType == FeedbackType.NONE) {
                delay(1000L)
                if (feedbackType == FeedbackType.NONE) {
                    timeLeft--
                }
            }
            // Waktu mencapai 0 detik -> ubah state ke TIME_UP, JANGAN auto-advance.
            // Pengguna harus menekan tombol NEXT untuk melanjutkan.
            if (timeLeft == 0 && feedbackType == FeedbackType.NONE) {
                feedbackType = FeedbackType.TIME_UP
                onTimeUp()
            }
        }
    }

    // Tombol back sistem: buka pause, atau tutup pause jika sedang tampil
    BackHandler {
        SoundManager.playSfx(SoundManager.SFX.BUTTON)
        isPaused = !isPaused
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBgLight)
    ) {
        // Decorative background elements (bottom shapes & blobs)
        QuizBackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 36.dp, bottom = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            QuizTopBar(
                level = level,
                questionNumber = questionNumber,
                totalQuestions = totalQuestions,
                score = score,
                onBackClick = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    isPaused = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer & Progress Bar (Visual Only)
            QuizTimer(
                timeLeft = timeLeft,
                totalDuration = totalDuration,
                isTimeUp = feedbackType == FeedbackType.TIME_UP
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card Container — Correct Banner & Time's Up Banner sebagai overlay di tengah Card
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                QuizQuestionCard(
                    questionText = currentQuestion.questionText,
                    feedbackType = feedbackType
                )

                // Popup CORRECT: hanya ada popup saja tanpa elemen confetti/melayang
                if (feedbackType == FeedbackType.CORRECT) {
                    CorrectPopupContent(
                        pointsEarned = 10,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp)
                    )
                }

                if (feedbackType == FeedbackType.INCORRECT) {
                    IncorrectPopupContent(
                        correctAnswer = currentQuestion.correctAnswer,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (feedbackType == FeedbackType.TIME_UP) {
                    TimesUpPopupBanner(
                        correctAnswer = currentQuestion.correctAnswer,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Options Grid (dinonaktifkan saat TIME_UP, CORRECT, atau INCORRECT)
            QuizOptionsGrid(
                options = options,
                selectedOption = selectedOption,
                correctAnswer = currentQuestion.correctAnswer,
                feedbackType = feedbackType,
                onOptionSelected = { selected ->
                    if (feedbackType == FeedbackType.NONE) {
                        selectedOption = selected
                        val isCorrect = selected == currentQuestion.correctAnswer
                        if (isCorrect) {
                            score += 10
                            feedbackType = FeedbackType.CORRECT
                            onCorrectAnswer()
                        } else {
                            feedbackType = FeedbackType.INCORRECT
                            onWrongAnswer()
                        }
                        onOptionSelected(selected)
                    }
                }
            )

            // Tombol NEXT: muncul saat Correct, Incorrect, atau Time's Up
            if (feedbackType != FeedbackType.NONE) {
                Spacer(modifier = Modifier.height(20.dp))
                QuizNextButton(
                    onClick = {
                        onNextClick()
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                            feedbackType = FeedbackType.NONE
                            selectedOption = null
                        } else {
                            showResultDialog = true
                            onQuizCompleted(score)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Popup Pause
        if (isPaused) {
            PausePopup(
                score = score,
                onResume = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    isPaused = false
                },
                onRestart = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    currentIndex = 0
                    score = 0
                    restartKey++
                    isPaused = false
                },
                onExit = {
                    isPaused = false
                    onBackClick()
                }
            )
        }

        // Popup Hasil Quiz (latar belakang tetap halaman Quiz Screen)
        if (showResultDialog) {
            QuizResultDialog(
                score = score,
                onNextLevel = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    showResultDialog = false
                    onNextLevel()
                },
                onPlayAgain = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    showResultDialog = false
                    currentIndex = 0
                    score = 0
                    restartKey++
                    feedbackType = FeedbackType.NONE
                    selectedOption = null
                },
                onHome = {
                    SoundManager.playSfx(SoundManager.SFX.BUTTON)
                    showResultDialog = false
                    onHome()
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
    // Warna badge biru muda playful
    val badgeBg    = Color(0xFFE0F2FE)  // biru muda pastel
    val badgeBorder= Color(0xFF7DD3FC)  // biru terang border
    val badgeText  = Color(0xFF1D4ED8)  // biru gelap teks
    val badgeShape = RoundedCornerShape(50.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // ── Tombol Back (tidak diubah)
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(42.dp)
                .clickable { onBackClick() }
        )

        // ── Badge Level
        Box(
            modifier = Modifier
                .height(36.dp)
                .shadow(3.dp, badgeShape, spotColor = Color(0x307DD3FC))
                .clip(badgeShape)
                .background(badgeBg)
                .border(1.5.dp, badgeBorder, badgeShape)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Level $level",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = badgeText,
                letterSpacing = 0.3.sp
            )
        }

        // ── Badge Nomor Soal (selalu /15)
        Box(
            modifier = Modifier
                .height(36.dp)
                .shadow(3.dp, badgeShape, spotColor = Color(0x307DD3FC))
                .clip(badgeShape)
                .background(badgeBg)
                .border(1.5.dp, badgeBorder, badgeShape)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$questionNumber/$totalQuestions",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = badgeText,
                letterSpacing = 0.3.sp
            )
        }

        // ── Badge Score (logika score tidak diubah)
        Box(
            modifier = Modifier
                .height(36.dp)
                .shadow(3.dp, badgeShape, spotColor = Color(0x307DD3FC))
                .clip(badgeShape)
                .background(badgeBg)
                .border(1.5.dp, badgeBorder, badgeShape)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "🏆 Score: ",
                    fontSize = 13.sp,
                    color = badgeText
                )
                Text(
                    text = "$score",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeText,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Composable
fun QuizTimer(
    timeLeft: Int,
    totalDuration: Int = 15,
    isTimeUp: Boolean = false,
    modifier: Modifier = Modifier
) {
    val progress = if (totalDuration > 0 && !isTimeUp) {
        (timeLeft.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red clock icon when Time's Up, yellow clock icon when normal
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(if (isTimeUp) Color(0xFFFEE2E2) else QuizYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isTimeUp) "⏰" else "⏱",
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%02ds", timeLeft),
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = if (isTimeUp) Color(0xFFDC2626) else QuizTextNavy
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Custom Progress Bar (murni visual, tidak dapat di-drag/interaktif)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color(0xFFCBD5E1).copy(alpha = 0.5f)) // Light gray background
        ) {
            if (progress > 0f) {
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
}

@Composable
fun QuizQuestionCard(
    questionText: String,
    feedbackType: FeedbackType = FeedbackType.NONE,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0x18000000),
                ambientColor = Color(0x0C000000)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .border(2.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.6f), RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.Center
    ) {
        // 1. Hiasan Kiri Atas: + (Tambah) Biru
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 22.dp, top = 20.dp)
                .graphicsLayer {
                    rotationZ = -12f
                    alpha = 0.85f
                }
                .size(34.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 9.dp.toPx()
                val cap = StrokeCap.Round
                val center = Offset(size.width / 2f, size.height / 2f)
                val arm = size.width * 0.38f
                drawLine(QuizBlueLight, Offset(center.x - arm, center.y), Offset(center.x + arm, center.y), strokeWidth, cap)
                drawLine(QuizBlueLight, Offset(center.x, center.y - arm), Offset(center.x, center.y + arm), strokeWidth, cap)
            }
        }

        // 2. Hiasan Kanan Atas: ÷ (Bagi) Kuning
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 20.dp)
                .graphicsLayer {
                    rotationZ = -14f
                    alpha = 0.85f
                }
                .size(34.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 8.dp.toPx()
                val cap = StrokeCap.Round
                val center = Offset(size.width / 2f, size.height / 2f)
                val arm = size.width * 0.38f
                drawLine(QuizYellow, Offset(center.x - arm, center.y), Offset(center.x + arm, center.y), strokeWidth, cap)
                val dotRadius = 3.5.dp.toPx()
                drawCircle(QuizYellow, radius = dotRadius, center = Offset(center.x, center.y - 10.dp.toPx()))
                drawCircle(QuizYellow, radius = dotRadius, center = Offset(center.x, center.y + 10.dp.toPx()))
            }
        }

        // 3. Hiasan Kiri Bawah: − (Kurang) Pink
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 22.dp)
                .graphicsLayer {
                    rotationZ = -14f
                    alpha = 0.85f
                }
                .size(34.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 9.dp.toPx()
                val cap = StrokeCap.Round
                val center = Offset(size.width / 2f, size.height / 2f)
                val arm = size.width * 0.38f
                drawLine(QuizPink, Offset(center.x - arm, center.y), Offset(center.x + arm, center.y), strokeWidth, cap)
            }
        }

        // 4. Hiasan Kanan Bawah: × (Kali) Hijau
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 22.dp)
                .graphicsLayer {
                    rotationZ = 14f
                    alpha = 0.85f
                }
                .size(34.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 9.dp.toPx()
                val cap = StrokeCap.Round
                val center = Offset(size.width / 2f, size.height / 2f)
                val arm = size.width * 0.34f
                drawLine(QuizGreenLight, Offset(center.x - arm, center.y - arm), Offset(center.x + arm, center.y + arm), strokeWidth, cap)
                drawLine(QuizGreenLight, Offset(center.x + arm, center.y - arm), Offset(center.x - arm, center.y + arm), strokeWidth, cap)
            }
        }

        // 5. Teks Soal Utama: Tepat di Tengah (Center), naik sedikit saat CORRECT agar muat bersama pop-up
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .then(
                    if (feedbackType == FeedbackType.CORRECT) Modifier.offset(y = (-36).dp)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            val dynamicFontSize = when {
                questionText.length > 14 -> 30.sp
                questionText.length > 11 -> 34.sp
                questionText.length > 8  -> 40.sp
                else -> 44.sp
            }

            Text(
                text = buildAnnotatedString {
                    val parts = questionText.split(" ")
                    parts.forEachIndexed { index, part ->
                        val color = when {
                            part == "+" || part == "-" || part == "×" || part == "÷" -> QuizBlue
                            part == "?" -> QuizCoral
                            else -> QuizTextDark
                        }
                        withStyle(style = SpanStyle(color = color)) {
                            append(part)
                        }
                        if (index < parts.size - 1) append(" ")
                    }
                },
                fontSize = dynamicFontSize,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
fun QuizOptionsGrid(
    options: List<String>,
    selectedOption: String? = null,
    correctAnswer: String? = null,
    feedbackType: FeedbackType = FeedbackType.NONE,
    onOptionSelected: (String) -> Unit
) {
    val isAnswered = feedbackType != FeedbackType.NONE

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val opt0 = options[0]
            val (btnColor0, shadowColor0) = getOptionColors(opt0, selectedOption, correctAnswer, isAnswered, feedbackType)
            QuizOptionButton(
                text = opt0,
                buttonColor = btnColor0,
                shadowColor = shadowColor0,
                enabled = !isAnswered,
                isCorrect = isAnswered && opt0 == correctAnswer,
                isWrong = feedbackType == FeedbackType.INCORRECT && opt0 == selectedOption && opt0 != correctAnswer,
                textColor = if (btnColor0 == Color.White) QuizTextDark else Color.White,
                showBorder = btnColor0 == Color.White,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(opt0) }
            )
            val opt1 = options[1]
            val (btnColor1, shadowColor1) = getOptionColors(opt1, selectedOption, correctAnswer, isAnswered, feedbackType)
            QuizOptionButton(
                text = opt1,
                buttonColor = btnColor1,
                shadowColor = shadowColor1,
                enabled = !isAnswered,
                isCorrect = isAnswered && opt1 == correctAnswer,
                isWrong = feedbackType == FeedbackType.INCORRECT && opt1 == selectedOption && opt1 != correctAnswer,
                textColor = if (btnColor1 == Color.White) QuizTextDark else Color.White,
                showBorder = btnColor1 == Color.White,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(opt1) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val opt2 = options[2]
            val (btnColor2, shadowColor2) = getOptionColors(opt2, selectedOption, correctAnswer, isAnswered, feedbackType)
            QuizOptionButton(
                text = opt2,
                buttonColor = btnColor2,
                shadowColor = shadowColor2,
                enabled = !isAnswered,
                isCorrect = isAnswered && opt2 == correctAnswer,
                isWrong = feedbackType == FeedbackType.INCORRECT && opt2 == selectedOption && opt2 != correctAnswer,
                textColor = if (btnColor2 == Color.White) QuizTextDark else Color.White,
                showBorder = btnColor2 == Color.White,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(opt2) }
            )
            val opt3 = options[3]
            val (btnColor3, shadowColor3) = getOptionColors(opt3, selectedOption, correctAnswer, isAnswered, feedbackType)
            QuizOptionButton(
                text = opt3,
                buttonColor = btnColor3,
                shadowColor = shadowColor3,
                enabled = !isAnswered,
                isCorrect = isAnswered && opt3 == correctAnswer,
                isWrong = feedbackType == FeedbackType.INCORRECT && opt3 == selectedOption && opt3 != correctAnswer,
                textColor = if (btnColor3 == Color.White) QuizTextDark else Color.White,
                showBorder = btnColor3 == Color.White,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(opt3) }
            )
        }
    }
}

private fun getOptionColors(
    option: String,
    selectedOption: String?,
    correctAnswer: String?,
    isAnswered: Boolean,
    feedbackType: FeedbackType = FeedbackType.NONE
): Pair<Color, Color> {
    // Tombol berwarna putih glossy sebelum dijawab
    if (!isAnswered) return Pair(Color.White, Color(0xFFCBD5E1))

    // Khusus Time's Up: Jawaban benar disorot hijau, opsi lainnya abu-abu netral
    if (feedbackType == FeedbackType.TIME_UP) {
        return if (option == correctAnswer) {
            Pair(Color(0xFF22C55E), Color(0xFF16A34A))
        } else {
            Pair(Color(0xFF94A3B8), Color(0xFF64748B))
        }
    }

    // CORRECT: hanya jawaban benar yang hijau, lainnya abu-abu
    // INCORRECT: jawaban benar hijau, jawaban dipilih merah, lainnya abu-abu
    return when {
        option == correctAnswer -> Pair(Color(0xFF22C55E), Color(0xFF16A34A))
        option == selectedOption -> Pair(Color(0xFFEF4444), Color(0xFFDC2626))
        else -> Pair(Color(0xFF94A3B8), Color(0xFF64748B))
    }
}

@Composable
fun QuizOptionButton(
    text: String,
    buttonColor: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isCorrect: Boolean = false,
    isWrong: Boolean = false,
    textColor: Color = Color.White,
    showBorder: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "btnScale"
    )

    // Warna 3-stop glossy: terang atas, mid tengah, gelap bawah
    val isWhite = buttonColor == Color.White

    if (isWhite) {
        // ── TAMPILAN PUTIH BERSIH MURNI (Solid Clean White) ───────────────────
        Box(
            modifier = modifier
                .height(110.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed && enabled) 2.dp else 6.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color(0x18000000),
                    ambientColor = Color(0x0C000000)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(
                    width = 2.dp,
                    color = Color(0xFFE2E8F0),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            // Teks angka gelap pekat di atas kartu putih bersih
            Text(
                text = text,
                color = textColor,
                fontFamily = FredokaFontFamily,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            // Badge centang hijau jika jawaban benar
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color(0xFF22C55E), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Badge silang merah jika opsi salah yang dipilih
            if (isWrong) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color(0xFFEF4444), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    } else {
        // ── TAMPILAN BERWARNA 3D (Saat dijawab: Hijau Benar / Merah Salah / Abu-abu) ──
        val topColor = Color(
            (buttonColor.red + (1f - buttonColor.red) * 0.35f).coerceIn(0f, 1f),
            (buttonColor.green + (1f - buttonColor.green) * 0.35f).coerceIn(0f, 1f),
            (buttonColor.blue + (1f - buttonColor.blue) * 0.35f).coerceIn(0f, 1f)
        )
        val botColor = Color(
            (buttonColor.red * 0.68f).coerceIn(0f, 1f),
            (buttonColor.green * 0.68f).coerceIn(0f, 1f),
            (buttonColor.blue * 0.68f).coerceIn(0f, 1f)
        )

        Box(
            modifier = modifier
                .height(110.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed && enabled) 2.dp else 14.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = shadowColor.copy(alpha = 0.55f),
                    ambientColor = shadowColor.copy(alpha = 0.28f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to topColor,
                            0.50f to buttonColor,
                            1.00f to botColor
                        )
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
            // Layer 1: bottom darken strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.42f)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.24f)
                            )
                        )
                    )
            )

            // Layer 2: Canvas glossy
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.36f), Color.White.copy(alpha = 0f)),
                        startY = 0f,
                        endY = h * 0.62f
                    ),
                    size = Size(w, h * 0.62f)
                )

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.52f), Color.White.copy(alpha = 0f)),
                        startY = h * 0.04f,
                        endY = h * 0.32f
                    ),
                    topLeft = Offset(w * 0.10f, h * 0.07f),
                    size = Size(w * 0.80f, h * 0.24f),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
            }

            // Teks angka
            Text(
                text = text,
                color = textColor,
                fontFamily = FredokaFontFamily,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.30f),
                        offset = Offset(0f, 4f),
                        blurRadius = 6f
                    )
                )
            )

            // Badge centang hijau
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color(0xFF16A34A),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Badge silang merah
            if (isWrong) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color(0xFFDC2626),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
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

/**
 * Efek confetti/sparkles warna-warni yang tampil di sekitar Question Card saat state CORRECT.
 * Diimplementasikan sebagai Canvas dekorasi yang tidak menginterferensi layout lain.
 */
@Composable
fun CorrectConfettiSparkles(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiRotation"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Sparkle positions — 4 sudut card + 3 sisi tengah
        val sparkles = listOf(
            Triple(w * 0.08f, h * 0.12f, Color(0xFFFBBF24)), // kiri atas — kuning
            Triple(w * 0.92f, h * 0.10f, Color(0xFF4ADE80)), // kanan atas — hijau
            Triple(w * 0.05f, h * 0.88f, Color(0xFFF472B6)), // kiri bawah — pink
            Triple(w * 0.95f, h * 0.90f, Color(0xFF60A5FA)), // kanan bawah — biru
            Triple(w * 0.50f, h * 0.04f, Color(0xFFFBBF24)), // tengah atas — kuning
            Triple(w * 0.15f, h * 0.50f, Color(0xFF4ADE80)), // kiri tengah — hijau
            Triple(w * 0.88f, h * 0.50f, Color(0xFFF472B6))  // kanan tengah — pink
        )

        sparkles.forEachIndexed { i, (cx, cy, color) ->
            val rot = (rotation + i * 51.4f) % 360f
            val rad = Math.toRadians(rot.toDouble())
            val armLen = 8.dp.toPx()
            val strokeW = 5.dp.toPx()
            // Bintang 4 lengan
            for (j in 0 until 4) {
                val angle = rad + j * Math.PI / 2
                val ex = cx + armLen * Math.cos(angle).toFloat()
                val ey = cy + armLen * Math.sin(angle).toFloat()
                drawLine(
                    color = color.copy(alpha = 0.75f),
                    start = Offset(cx, cy),
                    end = Offset(ex, ey),
                    strokeWidth = strokeW,
                    cap = StrokeCap.Round
                )
            }
            // Titik pusat
            drawCircle(color = color.copy(alpha = 0.9f), radius = 4.dp.toPx(), center = Offset(cx, cy))
        }
    }
}

/**
 * Banner Time's Up native Jetpack Compose — tanpa asset PNG.
 * Terdiri dari: container pink gradient, ikon jam alarm Canvas, animasi getar/pulse,
 * sparkles merah-kuning, teks "Time's Up!", dan badge "Answer: X" dinamis.
 */
@Composable
fun TimesUpPopupBanner(
    correctAnswer: String,
    modifier: Modifier = Modifier
) {
    // ── Animasi pop-up muncul (spring bounce)
    val popScale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        popScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMedium
            )
        )
    }

    // ── Animasi pulse pada ikon jam (scale up-down terus)
    val infiniteAnim = rememberInfiniteTransition(label = "timesupAnim")
    val alarmPulse by infiniteAnim.animateFloat(
        initialValue   = 0.92f,
        targetValue    = 1.08f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alarmPulse"
    )
    // ── Sparkles berputar di sekitar banner
    val sparkRotation by infiniteAnim.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2000, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkRot"
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = popScale.value; scaleY = popScale.value }
            .fillMaxWidth(0.82f),
        contentAlignment = Alignment.Center
    ) {
        // ── Sparkles / sinar di luar banner (Canvas overlay)
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = 0.85f }
        ) {
            val cx = size.width / 2f
            val cy = size.height * 0.28f          // sekitar pusat ikon jam
            val sparkPositions = listOf(
                Pair(-36f, -42f), Pair(36f, -42f),
                Pair(-52f, -14f), Pair(52f, -14f),
                Pair(-28f, 20f),  Pair(28f,  20f)
            )
            val sparkColors = listOf(
                Color(0xFFEF4444), Color(0xFFFBBF24),
                Color(0xFFEF4444), Color(0xFFFBBF24),
                Color(0xFFFCA5A5), Color(0xFFFDE68A)
            )
            sparkPositions.forEachIndexed { i, (dx, dy) ->
                val baseAngle = Math.toRadians((sparkRotation + i * 60f).toDouble())
                val px = cx + dx.dp.toPx()
                val py = cy + dy.dp.toPx()
                val armLen = 7.dp.toPx()
                for (j in 0 until 4) {
                    val a = baseAngle + j * Math.PI / 2
                    drawLine(
                        color = sparkColors[i].copy(alpha = 0.8f),
                        start = Offset(px, py),
                        end   = Offset(px + (armLen * Math.cos(a)).toFloat(),
                            py + (armLen * Math.sin(a)).toFloat()),
                        strokeWidth = 4.dp.toPx(),
                        cap   = StrokeCap.Round
                    )
                }
                drawCircle(sparkColors[i].copy(alpha = 0.9f), 3.5.dp.toPx(), Offset(px, py))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Ikon Jam Alarm merah (native Canvas, pulse)
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .graphicsLayer { scaleX = alarmPulse; scaleY = alarmPulse }
                    .shadow(12.dp, CircleShape, spotColor = Color(0x80EF4444))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFF6B6B), Color(0xFFEF4444))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Wajah jam alarm (Canvas)
                Canvas(modifier = Modifier.size(52.dp)) {
                    val r      = size.minDimension / 2f
                    val center = Offset(r, r + 2.dp.toPx())

                    // Lingkaran jam putih
                    drawCircle(Color.White, radius = r * 0.85f, center = center)

                    // Angka 12, 3, 6, 9 sebagai titik
                    val tickPositions = listOf(0f, 90f, 180f, 270f)
                    tickPositions.forEach { deg ->
                        val rad = Math.toRadians(deg.toDouble() - 90)
                        val tx  = center.x + (r * 0.65f * Math.cos(rad)).toFloat()
                        val ty  = center.y + (r * 0.65f * Math.sin(rad)).toFloat()
                        drawCircle(Color(0xFFEF4444), radius = 2.5.dp.toPx(), center = Offset(tx, ty))
                    }

                    // Jarum jam (pendek — jam)
                    val hourAngle = Math.toRadians(-60.0)
                    drawLine(
                        Color(0xFF1E293B),
                        start = center,
                        end   = Offset(
                            center.x + (r * 0.40f * Math.cos(hourAngle)).toFloat(),
                            center.y + (r * 0.40f * Math.sin(hourAngle)).toFloat()
                        ),
                        strokeWidth = 3.5.dp.toPx(), cap = StrokeCap.Round
                    )
                    // Jarum menit (panjang — menunjuk ke atas = 12)
                    val minAngle = Math.toRadians(-90.0)
                    drawLine(
                        Color(0xFF1E293B),
                        start = center,
                        end   = Offset(
                            center.x + (r * 0.56f * Math.cos(minAngle)).toFloat(),
                            center.y + (r * 0.56f * Math.sin(minAngle)).toFloat()
                        ),
                        strokeWidth = 2.5.dp.toPx(), cap = StrokeCap.Round
                    )
                    // Titik pusat merah
                    drawCircle(Color(0xFFEF4444), radius = 3.dp.toPx(), center = center)

                    // Gagang bel kiri
                    drawArc(
                        color    = Color(0xFFEF4444),
                        startAngle = 200f, sweepAngle = 80f,
                        useCenter = false,
                        topLeft   = Offset(center.x - r * 0.88f, center.y - r * 1.05f),
                        size      = Size(r * 0.50f, r * 0.50f),
                        style     = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 3.5.dp.toPx(), cap = StrokeCap.Round
                        )
                    )
                    // Gagang bel kanan
                    drawArc(
                        color     = Color(0xFFEF4444),
                        startAngle = 260f, sweepAngle = 80f,
                        useCenter = false,
                        topLeft   = Offset(center.x + r * 0.38f, center.y - r * 1.05f),
                        size      = Size(r * 0.50f, r * 0.50f),
                        style     = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 3.5.dp.toPx(), cap = StrokeCap.Round
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Container utama banner (Pink gradient rounded)
            Box(
                modifier = Modifier
                    .shadow(14.dp, RoundedCornerShape(24.dp), spotColor = Color(0x80EF4444))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFE4E4), Color(0xFFFFC8C8))
                        )
                    )
                    .border(2.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Teks "Time's Up!"
                    Text(
                        text = "Time's Up!",
                        color = Color(0xFFDC2626),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color      = Color(0x50991B1B),
                                offset     = Offset(0f, 3f),
                                blurRadius = 6f
                            )
                        )
                    )

                    // Badge "Answer: X" — pink/rose pill harmonis dengan popup
                    Box(
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(50.dp), spotColor = Color(0x40E11D48))
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF43F5E))
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Answer: $correctAnswer",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * Kompatibilitas alias untuk pemanggilan QuizTimeUpOverlay
 */
@Composable
fun QuizTimeUpOverlay(
    correctAnswer: String,
    modifier: Modifier = Modifier
) {
    TimesUpPopupBanner(
        correctAnswer = correctAnswer,
        modifier = modifier
    )
}

/**
 * Komponen tombol NEXT menggunakan asset nextt.png asli.
 * Ukuran proporsional (~78% lebar layar, aspect ratio 2170:725),
 * ContentScale.Fit, tidak stretch, dan tampil 100% utuh tanpa terpotong.
 */
@Composable
fun QuizNextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "nextBtnScale"
    )

    Image(
        painter = painterResource(id = R.drawable.nextt),
        contentDescription = "Next",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxWidth(0.55f)
            .aspectRatio(2170f / 725f)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}