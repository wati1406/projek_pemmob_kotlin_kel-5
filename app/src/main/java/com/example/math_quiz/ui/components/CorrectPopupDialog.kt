package com.example.math_quiz.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.math_quiz.R

/**
 * Popup "Correct!" yang muncul ketika user menjawab soal dengan benar.
 *
 * Sesuai referensi desain:
 * - Menggunakan drawable `asset_correct` sebagai container utama
 * - Tombol centang hijau (GreenCheckCircle) di lingkaran sebelah kiri asset_correct
 * - Teks "Correct!" di bagian tengah banner
 * - Badge "+10" (kuning) menempel di bagian bawah banner
 * - Efek percikan (sparks) kuning di kiri & kanan
 */
@Composable
fun CorrectPopupDialog(
    pointsEarned: Int = 10,
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
                .scale(scale.value),
            contentAlignment = Alignment.Center
        ) {
            CorrectPopupContent(pointsEarned = pointsEarned)
        }
    }
}

/**
 * Visual utama Popup Correct (banner asset_correct + checkmark + text + +10 badge + sparks).
 */
@Composable
fun CorrectPopupContent(
    pointsEarned: Int = 10,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(360.dp)
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        // Percikan sinar/sparks kuning di samping kiri dan kanan
        YellowSparksDecoration(
            modifier = Modifier.fillMaxSize()
        )

        // Banner Utama menggunakan asset_correct
        Box(
            modifier = Modifier
                .width(290.dp)
                .height(95.dp),
            contentAlignment = Alignment.Center
        ) {
            val isPreview = LocalInspectionMode.current
            
            // Render gambar asset_correct dengan fallback jika preview belum ter-sync
            if (isPreview) {
                // Safe Preview Background agar Preview di Android Studio tidak pernah "Render Issues"
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFE8FCDA), Color(0xFFDCFCE7))
                            )
                        )
                        .border(
                            width = 3.dp,
                            color = Color(0xFF4ADE80),
                            shape = RoundedCornerShape(50.dp)
                        )
                )
            }

            // Load drawable asset_correct
            Image(
                painter = painterResource(id = R.drawable.asset_correct),
                contentDescription = "Correct!",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Baris Konten: Checkmark di kiri, Teks "Correct!" di tengah/kanan
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Centang Hijau nempel pas di bulatan kiri asset_correct
                Box(
                    modifier = Modifier.size(76.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GreenCheckCircle(size = 72.dp)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Teks "Correct!" hijau bold nempel di area tengah banner
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Correct!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF15803D),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Badge "+10" Kuning nempel di bawah banner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = 28.dp, y = 18.dp)
            ) {
                YellowPointsBadge(pointsEarned = pointsEarned)
            }
        }
    }
}

/**
 * Lingkaran hijau 3D dengan tanda centang putih presisi.
 */
@Composable
fun GreenCheckCircle(size: Dp = 72.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val r = this.size.minDimension / 2f
        val centerOffset = Offset(r, r)

        // Shadow / Ring luar hijau tua
        drawCircle(
            color = Color(0xFF14532D),
            radius = r,
            center = centerOffset
        )

        // Gradien hijau 3D
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF4ADE80), Color(0xFF15803D)),
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

        // Tanda Centang Putih (Checkmark)
        val strokeWidth = r * 0.22f
        val checkPath = Path().apply {
            moveTo(this@Canvas.size.width * 0.27f, this@Canvas.size.height * 0.52f)
            lineTo(this@Canvas.size.width * 0.44f, this@Canvas.size.height * 0.68f)
            lineTo(this@Canvas.size.width * 0.72f, this@Canvas.size.height * 0.36f)
        }
        drawPath(
            path = checkPath,
            color = Color.White,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * Badge Tombol Kuning "+10" dengan gaya 3D glossy.
 */
@Composable
fun YellowPointsBadge(
    pointsEarned: Int = 10,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(50.dp), spotColor = Color(0x88D97706))
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFEF08A), // Kuning terang atas
                        Color(0xFFFDE047), // Kuning pertengahan
                        Color(0xFFEAB308)  // Kuning emas bawah
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color(0xFFFEF08A),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 22.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+$pointsEarned",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF15803D)
        )
    }
}

/**
 * Percikan 6 sinar kuning (3 kiri, 3 kanan) menyebar di sekitar popup.
 */
@Composable
private fun YellowSparksDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val sparkColor = Color(0xFFFFD026)

        // Definisi posisi & sudut ray (3 di kiri, 3 di kanan)
        val leftSparks = listOf(
            Triple(w * 0.04f, h * 0.24f, -40f),
            Triple(w * 0.01f, h * 0.50f, 0f),
            Triple(w * 0.04f, h * 0.76f, 40f)
        )
        val rightSparks = listOf(
            Triple(w * 0.96f, h * 0.24f, 40f),
            Triple(w * 0.99f, h * 0.50f, 0f),
            Triple(w * 0.96f, h * 0.76f, -40f)
        )

        val rayWidth = 8.dp.toPx()
        val rayHeight = 20.dp.toPx()
        val cornerRadius = CornerRadius(rayWidth / 2f, rayWidth / 2f)

        (leftSparks + rightSparks).forEach { (cx, cy, angle) ->
            rotate(degrees = angle, pivot = Offset(cx, cy)) {
                drawRoundRect(
                    color = sparkColor,
                    topLeft = Offset(cx - rayWidth / 2f, cy - rayHeight / 2f),
                    size = Size(rayWidth, rayHeight),
                    cornerRadius = cornerRadius
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// PREVIEW SINGLE (Hanya memunculkan Popup Correct tanpa background putih ekstra)
// ─────────────────────────────────────────────

@androidx.compose.ui.tooling.preview.Preview(
    name = "Correct Popup Preview",
    showBackground = true,
    backgroundColor = 0xFFBAE6FD,
    widthDp = 390,
    heightDp = 220
)
@Composable
fun CorrectPopupPreview() {
    com.example.math_quiz.ui.theme.MathquizTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFBAE6FD)),
            contentAlignment = Alignment.Center
        ) {
            CorrectPopupContent(pointsEarned = 10)
        }
    }
}

/**
 * State layar quiz saat benar - digunakan oleh MainActivity jika diperlukan.
 * (Disimpan tanpa tag @Preview agar tidak memicu view putih ke-2 di Android Studio Preview)
 */
@Composable
fun QuizScreenCorrectState(
    onNextQuestion: () -> Unit = {}
) {
    var showCorrectPopup by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2FE)),
        contentAlignment = Alignment.Center
    ) {
        if (showCorrectPopup) {
            CorrectPopupDialog(
                pointsEarned = 10,
                onDismiss = { showCorrectPopup = false }
            )
        }
    }
}
