package com.example.math_quiz.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.math_quiz.R

@Composable
fun QuizResultDialog(
    score: Int = 850,
    onNextLevel: () -> Unit = {},
    onPlayAgain: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val roundedFont = FontFamily.SansSerif

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Trofi di atas card (zIndex agar menimpa card)
            Image(
                painter = painterResource(id = R.drawable.piala),
                contentDescription = "Gold Trophy",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-60).dp)
                    .zIndex(1f)
            )

            // Card utama
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 75.dp) // beri ruang untuk trofi
                    .shadow(10.dp, RoundedCornerShape(32.dp), spotColor = Color(0x33000000))
                    .background(Color.White, RoundedCornerShape(32.dp))
                    .padding(top = 16.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // 1. Judul Kuis Selesai
                    Text(
                        text = "Quiz Complete!",
                        fontFamily = roundedFont,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E3A8A)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Score Section
                    Text(
                        text = "Score",
                        fontFamily = roundedFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kiri),
                            contentDescription = "Left Accessory",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "$score",
                            fontFamily = roundedFont,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1E3A8A)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.kanan),
                            contentDescription = "Right Accessory",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. Performance Pill: Great Job!
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "⭐ Great Job!",
                            fontFamily = roundedFont,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF16A34A)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // 4. Tiga Tombol Aksi Vertikal
                    ResultMenuButton(
                        imageResId = R.drawable.btn_next_level,
                        textFallback = "NEXT LEVEL",
                        backgroundColor = Color(0xFF22C55E),
                        onClick = onNextLevel
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ResultMenuButton(
                        imageResId = R.drawable.btn_play_again,
                        textFallback = "PLAY AGAIN",
                        backgroundColor = Color(0xFF3B82F6),
                        onClick = onPlayAgain
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ResultMenuButton(
                        imageResId = R.drawable.btn_home,
                        textFallback = "HOME",
                        backgroundColor = Color.White,
                        isHome = true,
                        onClick = onHome
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultMenuButton(
    imageResId: Int?,
    textFallback: String,
    backgroundColor: Color,
    isHome: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (isPressed) 0.94f else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId != null) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = textFallback,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textFallback,
                    color = if (isHome) Color(0xFF1E3A8A) else Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(name = "Quiz Result Dialog", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun QuizResultPreview() {
    com.example.math_quiz.ui.theme.MathquizTheme {
        QuizResultDialog()
    }
}
