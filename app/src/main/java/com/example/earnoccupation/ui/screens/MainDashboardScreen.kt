package com.example.earnoccupation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.earnoccupation.data.local.JobItem
import com.example.earnoccupation.ui.components.Interactive3DTiltCard
import com.example.earnoccupation.data.local.UserProfile
import com.example.earnoccupation.ui.viewmodel.EarnViewModel
import com.example.earnoccupation.ui.viewmodel.MatchedJob
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassAccentGradient
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardBorderGlow
import com.example.ui.theme.Indigo200
import com.example.ui.theme.Indigo300
import com.example.ui.theme.MeshGradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    viewModel: EarnViewModel,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val matchedJobs by viewModel.matchedJobs.collectAsState()
    val savedJobs by viewModel.savedJobs.collectAsState()
    val appliedJobs by viewModel.appliedJobs.collectAsState()

    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedBranch by viewModel.selectedBranchFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isGeneratingCoverLetter by viewModel.isGeneratingCoverLetter.collectAsState()
    val coverLetterResult by viewModel.coverLetterResult.collectAsState()
    val coverLetterJob by viewModel.coverLetterJob.collectAsState()

    val isAuditingProfile by viewModel.isAuditingProfile.collectAsState()
    val profileAuditResult by viewModel.profileAuditResult.collectAsState()

    var activeChatJob by remember { mutableStateOf<JobItem?>(null) }
    val clipboardManager = LocalClipboardManager.current


    val branchesList = listOf("ALL", "CSE", "ECE", "MECHANICAL", "CIVIL", "MEDICAL", "ELECTRICAL")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xEB0F172A),
                contentColor = Indigo300,
                tonalElevation = 12.dp,
                modifier = Modifier.border(1.dp, GlassCardBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
                    label = { Text("Jobs") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Indigo300,
                        selectedTextColor = Indigo300,
                        indicatorColor = Color(0x336366F1),
                        unselectedIconColor = Indigo200.copy(alpha = 0.5f),
                        unselectedTextColor = Indigo200.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("nav_jobs_tab")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    icon = {
                        BadgedBox(badge = {
                            if (savedJobs.isNotEmpty()) {
                                Badge(containerColor = Indigo300, contentColor = Color(0xFF0F172A)) {
                                    Text(savedJobs.size.toString(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }) {
                            Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                        }
                    },
                    label = { Text("Saved") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Indigo300,
                        selectedTextColor = Indigo300,
                        indicatorColor = Color(0x336366F1),
                        unselectedIconColor = Indigo200.copy(alpha = 0.5f),
                        unselectedTextColor = Indigo200.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("nav_saved_tab")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.setSelectedTab(2) },
                    icon = {
                        BadgedBox(badge = {
                            if (appliedJobs.isNotEmpty()) {
                                Badge(containerColor = EmeraldGreen, contentColor = Color.White) {
                                    Text(appliedJobs.size.toString(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }) {
                            Icon(Icons.Default.Chat, contentDescription = "Messages")
                        }
                    },
                    label = { Text("Chat & Apps") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Indigo300,
                        selectedTextColor = Indigo300,
                        indicatorColor = Color(0x336366F1),
                        unselectedIconColor = Indigo200.copy(alpha = 0.5f),
                        unselectedTextColor = Indigo200.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("nav_chat_tab")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { viewModel.setSelectedTab(3) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Indigo300,
                        selectedTextColor = Indigo300,
                        indicatorColor = Color(0x336366F1),
                        unselectedIconColor = Indigo200.copy(alpha = 0.5f),
                        unselectedTextColor = Indigo200.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("nav_profile_tab")
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MeshGradientBrush)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GlassCardBg)
                        .border(0.5.dp, GlassCardBorder)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Welcome, ${userProfile?.username ?: "User"}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "${userProfile?.branch ?: "CSE"} • ${userProfile?.education ?: "Degree"}",
                            fontSize = 12.sp,
                            color = CyanAccent
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x336366F1))
                            .border(1.dp, Indigo300, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Branch: ${userProfile?.branch ?: "CSE"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo300
                        )
                    }
                }

                if (selectedTab == 0 || selectedTab == 1) {
                    // Search & Filter Header
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search by title, skill, city...", color = Indigo200.copy(alpha = 0.6f)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanAccent) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = GlassCardBg,
                                unfocusedContainerColor = GlassCardBg
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dashboard_search_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Branch Selector Chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(branchesList) { branch ->
                                val isSelected = selectedBranch == branch
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setBranchFilter(branch) },
                                    label = {
                                        Text(
                                            text = if (branch == "ALL") "All Branches" else branch,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else Indigo200
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0x666366F1),
                                        containerColor = GlassCardBg
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) Indigo300 else GlassCardBorder
                                    ),
                                    modifier = Modifier.testTag("filter_chip_$branch")
                                )
                            }
                        }
                    }
                }

                // Tab View Body
                when (selectedTab) {
                    0 -> {
                        // All Jobs Feed
                        if (matchedJobs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No matching jobs found for selected branch/search.\nTry selecting 'All Branches' or resetting search.",
                                    color = Indigo200,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(matchedJobs, key = { it.job.id }) { matchedItem ->
                                    JobCard(
                                        matchedJob = matchedItem,
                                        onSaveToggle = { viewModel.toggleSaveJob(matchedItem.job.id, matchedItem.job.isSaved) },
                                        onApplyClick = {
                                            viewModel.applyJob(matchedItem.job.id)
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(matchedItem.job.applyUrl))
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                        },
                                        onMessageClick = {
                                            activeChatJob = matchedItem.job
                                        },
                                        onCoverLetterClick = {
                                            viewModel.generateCoverLetterForJob(matchedItem.job)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        // Saved Jobs
                        if (savedJobs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Indigo200, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("No saved jobs yet.", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text("Bookmark jobs from the Jobs tab to view them here.", color = Indigo200, fontSize = 13.sp)
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(savedJobs, key = { it.id }) { savedJob ->
                                    val score = 90
                                    val reason = "Bookmarked Opportunity"
                                    JobCard(
                                        matchedJob = MatchedJob(savedJob, score, reason),
                                        onSaveToggle = { viewModel.toggleSaveJob(savedJob.id, savedJob.isSaved) },
                                        onApplyClick = {
                                            viewModel.applyJob(savedJob.id)
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(savedJob.applyUrl))
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                        },
                                        onMessageClick = {
                                            activeChatJob = savedJob
                                        },
                                        onCoverLetterClick = {
                                            viewModel.generateCoverLetterForJob(savedJob)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        // Chat & Recruiter Communication
                        if (activeChatJob != null) {
                            RecruiterChatView(
                                job = activeChatJob!!,
                                viewModel = viewModel,
                                userName = userProfile?.username ?: "User",
                                onBackToJobs = { activeChatJob = null }
                            )
                        } else if (appliedJobs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("No Company Messages Yet", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text("Apply to jobs or tap 'Direct Message' on any job card to chat with recruiters directly!", color = Indigo200, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 32.dp))
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Text(
                                        text = "Applied Applications & Company Messages",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(brush = GlassAccentGradient),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                items(appliedJobs, key = { it.id }) { appliedJob ->
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { activeChatJob = appliedJob }
                                            .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(appliedJob.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                Text(appliedJob.company, color = CyanAccent, fontSize = 13.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("Recruiter: ${appliedJob.recruiterName} (${appliedJob.recruiterRole})", color = Indigo200, fontSize = 12.sp)
                                            }

                                            Button(
                                                onClick = { activeChatJob = appliedJob },
                                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Chat")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // Profile & Skill Roadmap
                        ProfileTab(
                            profile = userProfile ?: UserProfile(),
                            onEditProfileClick = onEditProfileClick,
                            onAuditProfileClick = { viewModel.performProfileAudit() }
                        )
                    }
                }
            }

            // AI Cover Letter Dialog
            if (isGeneratingCoverLetter || coverLetterResult != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearCoverLetterDialog() },
                    containerColor = Color(0xFF0F172A),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini AI Application Pitch",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    text = {
                        if (isGeneratingCoverLetter) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = CyanAccent)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Writing tailored application pitch for ${coverLetterJob?.company ?: "company"}...", color = Indigo200, fontSize = 14.sp)
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Tailored for ${coverLetterJob?.title} at ${coverLetterJob?.company}:",
                                    fontSize = 12.sp,
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                                    modifier = Modifier.border(1.dp, GlassCardBorder, RoundedCornerShape(12.dp))
                                ) {
                                    Text(
                                        text = coverLetterResult ?: "",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        if (!isGeneratingCoverLetter && coverLetterResult != null) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(coverLetterResult ?: ""))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color(0xFF0F172A))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy Pitch", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.clearCoverLetterDialog() }) {
                            Text("Close", color = Indigo200)
                        }
                    }
                )
            }

            // AI Resume & Career Audit Dialog
            if (isAuditingProfile || profileAuditResult != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearProfileAuditDialog() },
                    containerColor = Color(0xFF0F172A),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = EmeraldGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini AI Profile Audit",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    text = {
                        if (isAuditingProfile) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = EmeraldGreen)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Analyzing skills, LPA target & branch metrics...", color = Indigo200, fontSize = 14.sp)
                            }
                        } else {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                                modifier = Modifier.border(1.dp, GlassCardBorderGlow, RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = profileAuditResult ?: "",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        if (!isAuditingProfile && profileAuditResult != null) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(profileAuditResult ?: ""))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy Audit", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.clearProfileAuditDialog() }) {
                            Text("Close", color = Indigo200)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun JobCard(
    matchedJob: MatchedJob,
    onSaveToggle: () -> Unit,
    onApplyClick: () -> Unit,
    onMessageClick: () -> Unit,
    onCoverLetterClick: () -> Unit
) {
    val job = matchedJob.job

    Interactive3DTiltCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        maxTiltDegrees = 12f
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = GlassCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassCardBorder, RoundedCornerShape(22.dp))
                .testTag("job_card_${job.id}")
        ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Match score badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (matchedJob.matchScore >= 80) EmeraldGreen.copy(alpha = 0.25f) else CyanAccent.copy(alpha = 0.25f)
                        )
                        .border(
                            1.dp,
                            if (matchedJob.matchScore >= 80) EmeraldGreen else CyanAccent,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Indigo300,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${matchedJob.matchScore}% Skill Match",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                IconButton(onClick = onSaveToggle) {
                    Icon(
                        imageVector = if (job.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Job",
                        tint = if (job.isSaved) Indigo300 else Indigo200
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = job.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${job.company} • ${job.category} (${job.platform})",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = Indigo300, modifier = Modifier.size(16.dp))
                Text(
                    text = job.salaryRange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo300
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Indigo200, modifier = Modifier.size(16.dp))
                Text(
                    text = "${job.city}, ${job.state}",
                    fontSize = 13.sp,
                    color = Indigo200
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Required Skills: ${job.requiredSkills}",
                fontSize = 12.sp,
                color = Indigo200.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(13.dp))
                    }
                }

                Button(
                    onClick = onCoverLetterClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x3300E5FF), contentColor = CyanAccent),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.border(1.dp, CyanAccent.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("AI Pitch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = onMessageClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x336366F1), contentColor = EmeraldGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.border(1.dp, EmeraldGreen, RoundedCornerShape(14.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
}

@Composable
private fun RecruiterChatView(
    job: JobItem,
    viewModel: EarnViewModel,
    userName: String,
    onBackToJobs: () -> Unit
) {
    val chatMessages by viewModel.getChatForJob(job.id).collectAsState(initial = emptyList())
    var messageInputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Chat Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onBackToJobs) {
                Text("< Back to Jobs", color = CyanAccent)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(job.company, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                Text("Recruiter: ${job.recruiterName}", color = Indigo300, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(GlassCardBg, RoundedCornerShape(20.dp))
                .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Direct Channel with ${job.company} (${job.title})",
                        color = Indigo200,
                        fontSize = 11.sp
                    )
                }
            }

            items(chatMessages) { msg ->
                val isUser = msg.isFromUser
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) Color.White else Color(0xFF1E1B4B)
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.senderName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUser) Color(0xFF0F172A) else CyanAccent
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.messageText,
                                fontSize = 13.sp,
                                color = if (isUser) Color(0xFF0F172A) else Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Message Input Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = messageInputText,
                onValueChange = { messageInputText = it },
                placeholder = { Text("Type direct message to recruiter...", color = Indigo200.copy(alpha = 0.6f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = GlassCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = GlassCardBg,
                    unfocusedContainerColor = GlassCardBg
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageInputText.isNotBlank()) {
                        viewModel.sendUserMessageToCompany(job, messageInputText.trim(), userName)
                        messageInputText = ""
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(EmeraldGreen)
                    .size(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
private fun ProfileTab(
    profile: UserProfile,
    onEditProfileClick: () -> Unit,
    onAuditProfileClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Interactive3DTiltCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                maxTiltDegrees = 10f
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassCardBorderGlow, RoundedCornerShape(22.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(profile.username, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, style = TextStyle(brush = GlassAccentGradient))
                                Text(profile.email, fontSize = 13.sp, color = Indigo200)
                            }

                            IconButton(onClick = onEditProfileClick) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = CyanAccent)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileItemRow("Education", profile.education)
                        ProfileItemRow("Branch", profile.branch)
                        ProfileItemRow("Age", "${profile.age} Years")
                        ProfileItemRow("Skills", profile.skills)
                        ProfileItemRow("Location Target", "${profile.preferredCity}, ${profile.preferredState}")
                        ProfileItemRow("Expected Salary", "≥ ₹${profile.minSalaryLpa} LPA")

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onAuditProfileClick,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_profile_audit_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gemini AI Resume & Skill Audit", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("Branch Skill Booster Recommendations (${profile.branch})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Indigo300)
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val recommendations = when (profile.branch.uppercase()) {
                        "CSE" -> listOf("System Design & Architecture", "Generative AI & LLM Prompting", "Docker & Kubernetes")
                        "ECE" -> listOf("Embedded C / RTOS", "SystemVerilog for VLSI", "Raspberry Pi / IoT Protocols")
                        "MECHANICAL" -> listOf("ANSYS FEA Simulation", "CATIA V5 Automotive Design", "PLC Industrial Automation")
                        "CIVIL" -> listOf("STAAD Pro Structural Analysis", "Revit Civil 3D BIM", "Quantity Surveying & Estimation")
                        "MEDICAL" -> listOf("Clinical Research Compliance", "Bio-statistical Analysis", "Good Clinical Practice (GCP)")
                        else -> listOf("Data Analytics", "Project Management (Agile)", "Technical Communication")
                    }

                    recommendations.forEach { rec ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(rec, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileItemRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, color = Indigo200)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}
