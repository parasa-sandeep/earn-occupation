package com.example.earnoccupation.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.earnoccupation.ui.components.Interactive3DCareerCube
import com.example.earnoccupation.ui.components.Interactive3DTiltCard
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassAccentGradient
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardBorderGlow
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo200
import com.example.ui.theme.Indigo300
import com.example.ui.theme.MeshGradientBrush
import com.example.ui.theme.Purple300

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "3d_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshGradientBrush)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Header Row with Glass Logo Container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF6366F1))
                            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF818CF8))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_earn_logo_1784874017860),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Earn Occupation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.testTag("app_title_text")
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(GlassCardBg)
                        .border(1.dp, GlassCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👤", fontSize = 18.sp)
                }
            }

            // Hero Frosted Glass Banner
            Box(
                modifier = Modifier
                    .graphicsLayer { translationY = floatOffset }
                    .fillMaxWidth()
            ) {
                // Background Ambient Glow
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(12.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0x556366F1), Color.Transparent)
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                )

                Interactive3DTiltCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    maxTiltDegrees = 12f
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassCardBorderGlow, RoundedCornerShape(28.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Interactive 3D Spinning & Draggable Career Wireframe Cube
                            Interactive3DCareerCube(
                                modifier = Modifier
                                    .size(110.dp)
                                    .padding(bottom = 12.dp),
                                cubeSize = 75.dp
                            )

                            Text(
                                text = "Welcome to",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = "The Future.",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                style = TextStyle(brush = GlassAccentGradient),
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Swipe or tilt in 3D to explore skills, top Indian pay scales in CSE, ECE, Civil, Mech & Medical with direct portal access.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Indigo200,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2-Column Grid Frosted Highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📊", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ANALYZE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo300,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Skills & Pay",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🌐", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CONNECT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo300,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Unstop & LI",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Branch Selection Frosted Pill Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(GlassCardBg)
                    .border(1.dp, Color(0x33818CF8), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BRANCH SELECTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo200,
                        letterSpacing = 1.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x666366F1))
                                .border(0.5.dp, Color(0x66A5B4FC), CircleShape)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "CSE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Medical", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "ECE", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feature Highlights in Frosted Glass Cards
            Text(
                text = "Key Portal Capabilities",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            FeatureCardFrosted(
                icon = Icons.Default.School,
                iconColor = CyanAccent,
                title = "All Engineering & Healthcare Branches",
                subtitle = "Custom opportunities for CSE, ECE, Mechanical, Civil, EEE & Medical graduates."
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureCardFrosted(
                icon = Icons.Default.Work,
                iconColor = Indigo300,
                title = "Direct Apply on Real Platforms",
                subtitle = "Seamless redirection to LinkedIn, Unstop & Official Company career portals."
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureCardFrosted(
                icon = Icons.Default.Chat,
                iconColor = EmeraldGreen,
                title = "Direct Company Communication",
                subtitle = "In-app direct recruiter messaging and instant application response tracking."
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureCardFrosted(
                icon = Icons.Default.Business,
                iconColor = Purple300,
                title = "State, City & Salary Match Analysis",
                subtitle = "AI filtering by expected salary, state preferences, and skill matrix."
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Frosted Action Button (High Contrast Crisp White Button with Indigo text)
            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F172A)
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("start_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Start",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "FIND INTERNSHIP • SECURE SALARY • REAL-TIME APPLICATION",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Indigo300.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun FeatureCardFrosted(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GlassCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f))
                    .border(0.5.dp, iconColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Indigo200,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

