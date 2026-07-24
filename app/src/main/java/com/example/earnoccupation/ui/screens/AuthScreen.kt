package com.example.earnoccupation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.earnoccupation.ui.viewmodel.AuthResult
import com.example.earnoccupation.ui.viewmodel.EarnViewModel
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.GlassAccentGradient
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardBorderGlow
import com.example.ui.theme.Indigo200
import com.example.ui.theme.Indigo300
import com.example.ui.theme.MeshGradientBrush
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: EarnViewModel,
    onAuthSuccess: (username: String, email: String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Login, 1: Sign Up
    var emailInput by remember { mutableStateOf("sandeep@earnoccupation.com") }
    var usernameInput by remember { mutableStateOf("Sandeep Parasa") }
    var passwordInput by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var emailErrorMsg by remember { mutableStateOf<String?>(null) }
    var passwordErrorMsg by remember { mutableStateOf<String?>(null) }
    var usernameErrorMsg by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var resetSuccessMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val emailRegex = remember { Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") }

    fun clearErrors() {
        errorMessage = null
        emailErrorMsg = null
        passwordErrorMsg = null
        usernameErrorMsg = null
    }

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
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Earn Occupation Portal",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(brush = GlassAccentGradient),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Secure portal login & personal job matchmaking",
                fontSize = 13.sp,
                color = Indigo200,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
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
                                onClick = {
                                    selectedTab = 0
                                    clearErrors()
                                },
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
                                onClick = {
                                    selectedTab = 1
                                    clearErrors()
                                },
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

                        // Error Banner Alert
                        errorMessage?.let { errorMsg ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x44FF334B)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = "Error",
                                        tint = Color(0xFFFF5252),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = errorMsg,
                                        color = Color(0xFFFFECEC),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        // Email Field (Used in both LOGIN & SIGN UP)
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = {
                                emailInput = it
                                emailErrorMsg = null
                                errorMessage = null
                            },
                            label = { Text("Email Address", color = Indigo200) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyanAccent) },
                            isError = emailErrorMsg != null,
                            supportingText = {
                                emailErrorMsg?.let {
                                    Text(it, color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                errorBorderColor = Color(0xFFFF5252),
                                focusedLabelColor = Indigo300,
                                unfocusedLabelColor = Indigo200,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Username / Full Name Field (SIGN UP ONLY)
                        if (selectedTab == 1) {
                            OutlinedTextField(
                                value = usernameInput,
                                onValueChange = {
                                    usernameInput = it
                                    usernameErrorMsg = null
                                    errorMessage = null
                                },
                                label = { Text("Full Name / Username", color = Indigo200) },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanAccent) },
                                isError = usernameErrorMsg != null,
                                supportingText = {
                                    usernameErrorMsg?.let {
                                        Text(it, color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Indigo300,
                                    unfocusedBorderColor = GlassCardBorder,
                                    errorBorderColor = Color(0xFFFF5252),
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

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Password Field
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                passwordErrorMsg = null
                                errorMessage = null
                            },
                            label = { Text("Password", color = Indigo200) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyanAccent) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = Indigo200
                                    )
                                }
                            },
                            isError = passwordErrorMsg != null,
                            supportingText = {
                                passwordErrorMsg?.let {
                                    Text(it, color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                errorBorderColor = Color(0xFFFF5252),
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
                                    .padding(top = 6.dp),
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                clearErrors()
                                var hasValidationError = false

                                val cleanEmail = emailInput.trim()
                                val cleanPassword = passwordInput.trim()
                                val cleanUsername = usernameInput.trim()

                                // Strict Email Format Check
                                if (cleanEmail.isBlank() || !emailRegex.matches(cleanEmail)) {
                                    emailErrorMsg = "Enter a valid email (e.g., name@domain.com)"
                                    hasValidationError = true
                                }

                                // Password Length Check (Must be >= 6 chars)
                                if (cleanPassword.length < 6) {
                                    passwordErrorMsg = "Password must be at least 6 characters long"
                                    hasValidationError = true
                                }

                                // Username Check for Sign Up
                                if (selectedTab == 1 && (cleanUsername.isBlank() || cleanUsername.length < 2)) {
                                    usernameErrorMsg = "Name must be at least 2 characters long"
                                    hasValidationError = true
                                }

                                if (hasValidationError) {
                                    errorMessage = "Please enter correct Email and Password details."
                                    return@Button
                                }

                                isLoading = true
                                coroutineScope.launch {
                                    if (selectedTab == 0) {
                                        // LOGIN ACTION
                                        val res = viewModel.authenticateLogin(cleanEmail, cleanPassword)
                                        isLoading = false
                                        when (res) {
                                            is AuthResult.Success -> {
                                                onAuthSuccess(res.username, res.email)
                                            }
                                            is AuthResult.Error -> {
                                                errorMessage = res.message
                                                if (res.message.contains("email", ignoreCase = true)) {
                                                    emailErrorMsg = "Email check failed"
                                                }
                                                if (res.message.contains("password", ignoreCase = true)) {
                                                    passwordErrorMsg = "Incorrect Password"
                                                }
                                            }
                                        }
                                    } else {
                                        // SIGN UP ACTION
                                        val res = viewModel.registerNewAccount(cleanEmail, cleanUsername, cleanPassword)
                                        isLoading = false
                                        when (res) {
                                            is AuthResult.Success -> {
                                                onAuthSuccess(res.username, res.email)
                                            }
                                            is AuthResult.Error -> {
                                                errorMessage = res.message
                                                if (res.message.contains("email", ignoreCase = true)) {
                                                    emailErrorMsg = "Email already registered"
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            enabled = !isLoading,
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
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color(0xFF0F172A),
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = if (selectedTab == 0) "LOGIN TO CONTINUE" else "CREATE ACCOUNT",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Demo credentials hint card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0x1100E5FF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 Demo Credentials: sandeep@earnoccupation.com / password123 (or switch to Sign Up)",
                                fontSize = 11.sp,
                                color = Indigo200,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { onAuthSuccess("Guest User", "guest@earnoccupation.com") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Skip for now (Continue as Guest)",
                                color = Indigo200.copy(alpha = 0.7f),
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
                        val cleanReset = resetEmailInput.trim()
                        if (cleanReset.isNotBlank() && emailRegex.matches(cleanReset)) {
                            resetSuccessMessage = "Reset link sent to $cleanReset!"
                        } else {
                            resetSuccessMessage = "Please enter a valid email address."
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


