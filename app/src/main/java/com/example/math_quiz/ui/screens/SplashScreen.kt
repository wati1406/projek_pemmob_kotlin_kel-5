package com.example.math_quiz.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_quiz.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Layar 1: SplashScreen
 * Menampilkan maskot buku biru 3D kawaii, judul "Math Quiz",
 * tagline "Challenge Your Math Skills!", dan elemen matematika mengambang interaktif.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit = {}
) {
    // Animasi timer splash screen (misal 2.5 detik)
    LaunchedEffect(Unit) {
        delay(2500L)
        onTimeout()
    }

    // Infinite transition untuk animasi mengambang (floating elements)
    val infiniteTransition = rememberInfiniteTransition(label = "floatingAnim")

    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBgLight)
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // 1. Organic Pastel Blobs di 4 Sudut Layar (sesuai mockup)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Top-left: Soft Sky Blue
            drawCircle(
                color = BlobBlue.copy(alpha = 0.65f),
                center = Offset(w * 0.05f, h * 0.02f),
                radius = w * 0.42f
            )

            // Top-right: Soft Pastel Yellow
            drawCircle(
                color = BlobYellow.copy(alpha = 0.7f),
                center = Offset(w * 0.95f, h * 0.04f),
                radius = w * 0.46f
            )

            // Bottom-left: Soft Pastel Pink
            drawCircle(
                color = BlobPink.copy(alpha = 0.7f),
                center = Offset(w * 0.02f, h * 0.96f),
                radius = w * 0.48f
            )

            // Bottom-right: Soft Pastel Purple
            drawCircle(
                color = BlobPurple.copy(alpha = 0.65f),
                center = Offset(w * 0.98f, h * 0.97f),
                radius = w * 0.46f
            )
        }

        // 2. Elemen Matematika Mengambang (Floating Decorative Elements)
        // Kiri Atas: Angka "1" (Coral/Merah Muda)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 42.dp, top = 80.dp)
                .offset(y = floatOffset1.dp),
            content = {
                Text(
                    text = "1",
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizCoral,
                    style = MaterialTheme.typography.displayLarge
                )
            }
        )

        // Kiri Atas-Tengah: Simbol "+" (Biru Muda)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 260.dp)
                .offset(y = floatOffset2.dp)
                .rotate(-10f),
            content = {
                Text(
                    text = "+",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizBlueLight
                )
            }
        )

        // Kanan Atas: Simbol "+" kecil (Kuning)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 85.dp, top = 110.dp)
                .offset(y = floatOffset2.dp),
            content = {
                Text(
                    text = "+",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizYellow
                )
            }
        )

        // Kanan Atas-Tengah: Angka "2" (Hijau Pastel Segar)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 36.dp, top = 210.dp)
                .offset(y = floatOffset1.dp)
                .rotate(8f),
            content = {
                Text(
                    text = "2",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizGreen
                )
            }
        )

        // Titik-titik aksen dekoratif kecil
        DecorativeDot(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 140.dp, top = 85.dp),
            color = QuizPurple.copy(alpha = 0.4f),
            size = 14.dp
        )

        DecorativeDot(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 45.dp, top = 345.dp),
            color = QuizPurple.copy(alpha = 0.35f),
            size = 16.dp
        )

        // Kiri Bawah: Segitiga Geometri 3D (Kuning)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 40.dp, bottom = 175.dp)
                .offset(y = floatOffset1.dp),
            content = {
                PlayfulTriangle(
                    size = 46.dp,
                    color = QuizYellow
                )
            }
        )

        // Kiri Bawah: Simbol "+" kecil (Kuning)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 145.dp, bottom = 120.dp)
                .offset(y = floatOffset2.dp),
            content = {
                Text(
                    text = "+",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizYellow.copy(alpha = 0.85f)
                )
            }
        )

        // Kiri Bawah: Dot Pink kecil
        DecorativeDot(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 28.dp, bottom = 260.dp),
            color = QuizPink.copy(alpha = 0.5f),
            size = 12.dp
        )

        // Kanan Bawah: Kotak Ungu Rounded (Cube 3D)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 210.dp)
                .offset(y = floatOffset2.dp)
                .rotate(18f),
            content = {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp), spotColor = QuizPurple.copy(alpha = 0.4f))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(QuizPurple.copy(alpha = 0.75f), QuizPurple)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        )

        // Kanan Bawah: Angka "3" (Ungu Playful)
        FloatingItem(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 65.dp, bottom = 95.dp)
                .offset(y = floatOffset1.dp)
                .rotate(-6f),
            content = {
                Text(
                    text = "3",
                    fontSize = 62.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizPurple
                )
            }
        )

        // Kanan Bawah: Dot Biru kecil
        DecorativeDot(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 140.dp, bottom = 170.dp),
            color = QuizBlueLight.copy(alpha = 0.6f),
            size = 14.dp
        )

        // 3. Konten Utama di Tengah Layar: Maskot Buku & Judul
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Maskot Karakter Buku Biru Lucu dengan Halo Kuning & Efek Spark
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(230.dp)
                    .offset(y = (floatOffset1 * 0.4f).dp)
            ) {
                // Background Halo Kuning Hangat
                Canvas(modifier = Modifier.size(190.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                QuizYellowHalo,
                                QuizYellowHalo.copy(alpha = 0.85f),
                                QuizYellow.copy(alpha = 0.15f)
                            )
                        )
                    )
                }

                // Efek Percikan/Sparks Kuning di atas kepala buku
                Canvas(modifier = Modifier.size(220.dp)) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    // Spark ray 1
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(cx + 35f, cy - 88f),
                        size = Size(10f, 22f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    // Spark ray 2
                    drawRoundRect(
                        color = QuizYellow,
                        topLeft = Offset(cx + 56f, cy - 72f),
                        size = Size(10f, 18f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                }

                // Maskot Buku 3D Lucu
                CuteBookMascot(
                    modifier = Modifier.size(160.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Judul Aplikasi: "Math Quiz" (Gaya Timbul Playful 3D)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Kata "Math" (Biru Bergradasi dengan Shadow)
                Text(
                    text = "Math",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = QuizBlue,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = QuizBlueDark.copy(alpha = 0.35f)
                        )
                )

                // Kata "Quiz" (Oranye/Kuning Cerah dengan Shadow)
                Text(
                    text = "Quiz",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = QuizOrange,
                    modifier = Modifier.shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        spotColor = QuizOrangeDark.copy(alpha = 0.35f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tagline: "Challenge Your Math Skills!"
            Text(
                text = "Challenge Your Math Skills!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextDark.copy(alpha = 0.82f),
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Indikator Loading Modern yang Halus di Bawah
            SplashLoadingIndicator()
        }
    }
}

/**
 * Maskot Karakter Buku Biru Lucu (Cute Book Mascot)
 * Menggunakan aset R.drawable.buku (buku.png)
 */
@Composable
fun CuteBookMascot(
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Image(
        painter = androidx.compose.ui.res.painterResource(id = com.example.math_quiz.R.drawable.buku),
        contentDescription = "Cute Book Mascot",
        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
    )
}

// Helper untuk menggambar simbol Plus (+) pada Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMathSymbolPlus(
    center: Offset,
    size: Float,
    color: Color
) {
    val barThickness = size * 0.32f
    // Horizontal bar
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - size / 2, center.y - barThickness / 2),
        size = Size(size, barThickness),
        cornerRadius = CornerRadius(barThickness / 2, barThickness / 2)
    )
    // Vertical bar
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - barThickness / 2, center.y - size / 2),
        size = Size(barThickness, size),
        cornerRadius = CornerRadius(barThickness / 2, barThickness / 2)
    )
}

// Helper untuk menggambar simbol Cross (×) pada Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMathSymbolCross(
    center: Offset,
    size: Float,
    color: Color
) {
    val barThickness = size * 0.32f
    val halfSize = size * 0.42f
    // Garis miring 1
    drawLine(
        color = color,
        start = Offset(center.x - halfSize, center.y - halfSize),
        end = Offset(center.x + halfSize, center.y + halfSize),
        strokeWidth = barThickness,
        cap = androidx.compose.ui.graphics.StrokeCap.Round
    )
    // Garis miring 2
    drawLine(
        color = color,
        start = Offset(center.x + halfSize, center.y - halfSize),
        end = Offset(center.x - halfSize, center.y + halfSize),
        strokeWidth = barThickness,
        cap = androidx.compose.ui.graphics.StrokeCap.Round
    )
}

// Helper untuk menggambar simbol Divide (÷) pada Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMathSymbolDivide(
    center: Offset,
    size: Float,
    color: Color
) {
    val barThickness = size * 0.28f
    val dotRadius = size * 0.16f
    // Garis tengah
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - size / 2, center.y - barThickness / 2),
        size = Size(size, barThickness),
        cornerRadius = CornerRadius(barThickness / 2, barThickness / 2)
    )
    // Titik atas
    drawCircle(
        color = color,
        radius = dotRadius,
        center = Offset(center.x, center.y - size * 0.42f)
    )
    // Titik bawah
    drawCircle(
        color = color,
        radius = dotRadius,
        center = Offset(center.x, center.y + size * 0.42f)
    )
}

/**
 * Komponen Segitiga Kuning Geometri 3D Lembut
 */
@Composable
private fun PlayfulTriangle(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = QuizYellow
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val trianglePath = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            lineTo(w * 0.92f, h * 0.88f)
            lineTo(w * 0.08f, h * 0.88f)
            close()
        }
        // Shadow/stroke outline rounded style
        drawPath(
            path = trianglePath,
            color = color,
            style = Stroke(
                width = 14f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
    }
}

/**
 * Wrapper dekoratif untuk elemen mengambang
 */
@Composable
private fun FloatingItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Titik bulat dekoratif pastel
 */
@Composable
private fun DecorativeDot(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = color, shape = CircleShape)
    )
}

/**
 * 3 Titik Indikator Animasi Loading Bouncing Playful
 */
@Composable
private fun SplashLoadingIndicator(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val dot1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .offset(y = dot1Offset.dp)
                .size(10.dp)
                .background(QuizBlue, CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(y = dot2Offset.dp)
                .size(10.dp)
                .background(QuizGreen, CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(y = dot3Offset.dp)
                .size(10.dp)
                .background(QuizOrange, CircleShape)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SplashScreenPreview() {
    MathquizTheme {
        SplashScreen()
    }
}
