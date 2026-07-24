package com.example.earnoccupation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.earnoccupation.ui.components.Interactive3DTiltCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.GlassAccentGradient
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardBorderGlow
import com.example.ui.theme.Indigo200
import com.example.ui.theme.Indigo300
import com.example.ui.theme.MeshGradientBrush

@Composable
fun AuthScreen(
    onAuthSuccess: (username: String, email: String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Login, 1: Sign Up
    var usernameInput by remember { mutableStateOf("Sandeep Parasa") }
    var emailInput by remember { mutableStateOf("sandeep@earnoccupation.com") }
    var passwordInput by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var resetSuccessMessage by remember { mutableStateOf<String?>(null) }

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
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Earn Occupation Portal",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(brush = GlassAccentGradient),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Log in or create a new account to personalize your job search",
                fontSize = 13.sp,
                color = Indigo200,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 28.dp)
            )

            // 3D Frosted Glass Card Container
            Interactive3DTiltCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                maxTiltDegrees = 10f
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassCardBorderGlow, RoundedCornerShape(24.dp))
                ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Tab Selector
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0x33000000),
                        contentColor = Indigo300,
                        indicator = { tabPositions ->
                            if (selectedTab < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = Indigo300,
                                    height = 3.dp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassCardBorder, RoundedCornerShape(14.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.testTag("tab_login")
                        ) {
                            Text(
                                text = "LOGIN",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) Indigo300 else Indigo200.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.testTag("tab_signup")
                        ) {
                            Text(
                                text = "SIGN UP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) Indigo300 else Indigo200.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Form Fields
                    if (selectedTab == 1) {
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address", color = Indigo200) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyanAccent) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedLabelColor = Indigo300,
                                unfocusedLabelColor = Indigo200,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("Username", color = Indigo200) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanAccent) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo300,
                            unfocusedBorderColor = GlassCardBorder,
                            focusedLabelColor = Indigo300,
                            unfocusedLabelColor = Indigo200,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Password", color = Indigo200) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyanAccent) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password",
                                    tint = Indigo200
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo300,
                            unfocusedBorderColor = GlassCardBorder,
                            focusedLabelColor = Indigo300,
                            unfocusedLabelColor = Indigo200,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input")
                    )

                    if (selectedTab == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 13.sp,
                                color = CyanAccent,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clickable { showForgotPasswordDialog = true }
                                    .testTag("forgot_password_button")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (usernameInput.isNotBlank()) {
                                onAuthSuccess(usernameInput, emailInput)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("auth_submit_button")
                    ) {
                        Text(
                            text = if (selectedTab == 0) "LOGIN TO CONTINUE" else "CREATE ACCOUNT",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { onAuthSuccess("Guest User", "guest@earnoccupation.com") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Skip for now (Continue as Guest)",
                            color = Indigo200,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showForgotPasswordDialog = false
                resetSuccessMessage = null
            },
            containerColor = Color(0xFF1E1B4B),
            title = {
                Text(
                    text = "Reset Password",
                    color = Indigo300,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address to receive password reset instructions:",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resetEmailInput,
                        onValueChange = { resetEmailInput = it },
                        label = { Text("Email", color = Indigo200) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo300,
                            unfocusedBorderColor = GlassCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    resetSuccessMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = CyanAccent,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (resetEmailInput.isNotBlank()) {
                            resetSuccessMessage = "Reset link sent to $resetEmailInput!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF0F172A))
                ) {
                    Text("Send Reset Link", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Close", color = Indigo200)
                }
            }
        )
    }
}

