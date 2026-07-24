package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.earnoccupation.ui.screens.AnalyzingScreen
import com.example.earnoccupation.ui.screens.AuthScreen
import com.example.earnoccupation.ui.screens.MainDashboardScreen
import com.example.earnoccupation.ui.screens.UserDetailsScreen
import com.example.earnoccupation.ui.screens.WelcomeScreen
import com.example.earnoccupation.ui.viewmodel.EarnViewModel
import com.example.earnoccupation.ui.viewmodel.ScreenState
import com.example.ui.theme.EarnOccupationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: EarnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EarnOccupationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EarnOccupationApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EarnOccupationApp(
    viewModel: EarnViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val analyzingProgress by viewModel.analyzingProgress.collectAsState()
    val analyzingStatusText by viewModel.analyzingStatusText.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    when (currentScreen) {
        ScreenState.WELCOME -> {
            WelcomeScreen(
                onStartClick = { viewModel.navigateTo(ScreenState.AUTH) }
            )
        }

        ScreenState.AUTH -> {
            AuthScreen(
                onAuthSuccess = { username, email ->
                    viewModel.navigateTo(ScreenState.USER_DETAILS)
                }
            )
        }

        ScreenState.USER_DETAILS -> {
            UserDetailsScreen(
                currentProfile = userProfile,
                onSubmitDetails = { username, email, edu, qual, branch, age, skills, state, city, salary ->
                    viewModel.saveUserProfileDetails(
                        username = username,
                        email = email,
                        education = edu,
                        qualificationDetails = qual,
                        branch = branch,
                        age = age,
                        skills = skills,
                        state = state,
                        city = city,
                        minSalary = salary
                    )
                }
            )
        }

        ScreenState.ANALYZING -> {
            AnalyzingScreen(
                progress = analyzingProgress,
                statusText = analyzingStatusText
            )
        }

        ScreenState.MAIN_DASHBOARD -> {
            MainDashboardScreen(
                viewModel = viewModel,
                onEditProfileClick = { viewModel.navigateTo(ScreenState.USER_DETAILS) }
            )
        }
    }
}
