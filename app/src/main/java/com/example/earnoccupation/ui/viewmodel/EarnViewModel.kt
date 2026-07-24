package com.example.earnoccupation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.earnoccupation.data.api.GeminiService
import com.example.earnoccupation.data.local.AppDatabase
import com.example.earnoccupation.data.local.ChatMessage
import com.example.earnoccupation.data.local.JobItem
import com.example.earnoccupation.data.local.UserProfile
import com.example.earnoccupation.data.repository.JobRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenState {
    WELCOME,
    AUTH,
    USER_DETAILS,
    ANALYZING,
    MAIN_DASHBOARD
}

data class MatchedJob(
    val job: JobItem,
    val matchScore: Int, // 0 to 100%
    val matchReason: String
)

class EarnViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JobRepository

    private val _currentScreen = MutableStateFlow(ScreenState.WELCOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _selectedBranchFilter = MutableStateFlow("ALL")
    val selectedBranchFilter: StateFlow<String> = _selectedBranchFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Jobs, 1: Saved, 2: Applications & Messages, 3: Profile & Skills
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _analyzingProgress = MutableStateFlow(0f)
    val analyzingProgress: StateFlow<Float> = _analyzingProgress.asStateFlow()

    private val _analyzingStatusText = MutableStateFlow("Extracting skill vectors...")
    val analyzingStatusText: StateFlow<String> = _analyzingStatusText.asStateFlow()

    // Cover Letter state
    private val _isGeneratingCoverLetter = MutableStateFlow(false)
    val isGeneratingCoverLetter: StateFlow<Boolean> = _isGeneratingCoverLetter.asStateFlow()

    private val _coverLetterResult = MutableStateFlow<String?>(null)
    val coverLetterResult: StateFlow<String?> = _coverLetterResult.asStateFlow()

    private val _coverLetterJob = MutableStateFlow<JobItem?>(null)
    val coverLetterJob: StateFlow<JobItem?> = _coverLetterJob.asStateFlow()

    // Profile Audit state
    private val _isAuditingProfile = MutableStateFlow(false)
    val isAuditingProfile: StateFlow<Boolean> = _isAuditingProfile.asStateFlow()

    private val _profileAuditResult = MutableStateFlow<String?>(null)
    val profileAuditResult: StateFlow<String?> = _profileAuditResult.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = JobRepository(database.appDao())
        viewModelScope.launch {
            repository.seedInitialJobsIfEmpty()
        }
    }

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allRawJobs: StateFlow<List<JobItem>> = repository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedJobs: StateFlow<List<JobItem>> = repository.savedJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appliedJobs: StateFlow<List<JobItem>> = repository.appliedJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Matched Jobs pipeline
    val matchedJobs: StateFlow<List<MatchedJob>> = combine(
        allRawJobs,
        userProfile,
        selectedBranchFilter,
        searchQuery
    ) { jobs, profile, branch, query ->
        val user = profile ?: UserProfile()
        jobs.map { job ->
            val score = calculateMatchScore(job, user)
            val reason = generateMatchReason(job, user, score)
            MatchedJob(job, score, reason)
        }.filter { item ->
            val matchesBranch = if (branch == "ALL") true else item.job.branch.equals(branch, ignoreCase = true)
            val matchesQuery = query.isBlank() || 
                    item.job.title.contains(query, ignoreCase = true) ||
                    item.job.company.contains(query, ignoreCase = true) ||
                    item.job.requiredSkills.contains(query, ignoreCase = true) ||
                    item.job.state.contains(query, ignoreCase = true) ||
                    item.job.city.contains(query, ignoreCase = true)
            matchesBranch && matchesQuery
        }.sortedByDescending { it.matchScore }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun calculateMatchScore(job: JobItem, user: UserProfile): Int {
        var score = 50 // Base match score

        // Branch match
        if (job.branch.equals(user.branch, ignoreCase = true) || job.branch == "ALL") {
            score += 25
        } else {
            score -= 15
        }

        // Skills match
        val userSkillList = user.skills.split(",").map { it.trim().lowercase() }
        val jobSkillList = job.requiredSkills.split(",").map { it.trim().lowercase() }
        val matchedSkills = userSkillList.count { skill ->
            jobSkillList.any { it.contains(skill) || skill.contains(it) }
        }
        score += (matchedSkills * 8).coerceAtMost(20)

        // Location match
        if (job.state.equals(user.preferredState, ignoreCase = true) || job.city.equals(user.preferredCity, ignoreCase = true)) {
            score += 10
        }

        // Salary match
        if (job.salaryNumber >= user.minSalaryLpa) {
            score += 5
        }

        return score.coerceIn(35, 99)
    }

    private fun generateMatchReason(job: JobItem, user: UserProfile, score: Int): String {
        return when {
            score >= 85 -> "Top Match! Strong overlap in ${user.branch} branch and your skills (${user.skills.take(20)}...)"
            score >= 70 -> "High Compatibility for ${job.branch} roles in ${job.city}, ${job.state}"
            else -> "Relevant opportunity matching your educational profile"
        }
    }

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun setBranchFilter(branch: String) {
        _selectedBranchFilter.value = branch
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun saveUserProfileDetails(
        username: String,
        email: String,
        education: String,
        qualificationDetails: String,
        branch: String,
        age: Int,
        skills: String,
        state: String,
        city: String,
        minSalary: Double
    ) {
        viewModelScope.launch {
            val updated = UserProfile(
                id = 1,
                username = username,
                email = email,
                education = education,
                qualificationDetails = qualificationDetails,
                branch = branch,
                age = age,
                skills = skills,
                preferredState = state,
                preferredCity = city,
                minSalaryLpa = minSalary,
                isLoggedIn = true
            )
            repository.saveProfile(updated)
            startAnalyzingProcess()
        }
    }

    private fun startAnalyzingProcess() {
        _currentScreen.value = ScreenState.ANALYZING
        viewModelScope.launch {
            _analyzingProgress.value = 0.1f
            _analyzingStatusText.value = "Reading education & branch specifications..."
            delay(600)
            _analyzingProgress.value = 0.4f
            _analyzingStatusText.value = "Searching across LinkedIn, Unstop, Indeed & Portals..."
            delay(700)
            _analyzingProgress.value = 0.75f
            _analyzingStatusText.value = "Calculating salary fit & match scores..."
            delay(600)
            _analyzingProgress.value = 1.0f
            _analyzingStatusText.value = "Analysis complete! Generating tailored job matches..."
            delay(400)
            _currentScreen.value = ScreenState.MAIN_DASHBOARD
        }
    }

    fun toggleSaveJob(jobId: String, currentSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSaveJob(jobId, currentSaved)
        }
    }

    fun applyJob(jobId: String) {
        viewModelScope.launch {
            repository.applyForJob(jobId)
        }
    }

    fun getChatForJob(jobId: String) = repository.getChatForJob(jobId)

    fun sendUserMessageToCompany(job: JobItem, messageText: String, userName: String) {
        viewModelScope.launch {
            // Save user message to Room DB
            repository.sendChatMessage(
                jobId = job.id,
                companyName = job.company,
                text = messageText,
                isFromUser = true,
                senderName = userName
            )

            val currentProfile = userProfile.value ?: UserProfile(username = userName)
            val currentChatList = repository.getChatForJob(job.id).firstOrNull() ?: emptyList()
            val chatHistoryPairs = currentChatList.map { Pair(it.senderName, it.messageText) }

            // Get real Gemini AI response as Recruiter
            val aiReply = GeminiService.getAIRecruiterResponse(
                userMessage = messageText,
                user = currentProfile,
                job = job,
                chatHistory = chatHistoryPairs
            )

            repository.sendChatMessage(
                jobId = job.id,
                companyName = job.company,
                text = aiReply,
                isFromUser = false,
                senderName = job.recruiterName
            )
        }
    }

    fun generateCoverLetterForJob(job: JobItem) {
        _coverLetterJob.value = job
        _isGeneratingCoverLetter.value = true
        _coverLetterResult.value = null
        viewModelScope.launch {
            val profile = userProfile.value ?: UserProfile()
            val result = GeminiService.generateCoverLetter(profile, job)
            _coverLetterResult.value = result.getOrDefault("")
            _isGeneratingCoverLetter.value = false
        }
    }

    fun clearCoverLetterDialog() {
        _coverLetterResult.value = null
        _coverLetterJob.value = null
        _isGeneratingCoverLetter.value = false
    }

    fun performProfileAudit() {
        _isAuditingProfile.value = true
        _profileAuditResult.value = null
        viewModelScope.launch {
            val profile = userProfile.value ?: UserProfile()
            val result = GeminiService.analyzeResumeAndSkills(profile)
            _profileAuditResult.value = result.getOrDefault("")
            _isAuditingProfile.value = false
        }
    }

    fun clearProfileAuditDialog() {
        _profileAuditResult.value = null
        _isAuditingProfile.value = false
    }
}

