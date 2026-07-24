package com.example.earnoccupation.data.repository

import com.example.earnoccupation.data.local.AppDao
import com.example.earnoccupation.data.local.ChatMessage
import com.example.earnoccupation.data.local.JobItem
import com.example.earnoccupation.data.local.UserProfile
import com.example.earnoccupation.data.seed.InitialJobData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class JobRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfile?> = appDao.getUserProfile()
    val allJobs: Flow<List<JobItem>> = appDao.getAllJobs()
    val savedJobs: Flow<List<JobItem>> = appDao.getSavedJobs()
    val appliedJobs: Flow<List<JobItem>> = appDao.getAppliedJobs()

    suspend fun seedInitialJobsIfEmpty() {
        val existing = appDao.getAllJobs().firstOrNull()
        if (existing.isNullOrEmpty()) {
            appDao.insertJobs(InitialJobData.SAMPLE_JOBS)
        }
        val existingProfile = appDao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            appDao.saveUserProfile(UserProfile())
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        appDao.saveUserProfile(profile)
    }

    suspend fun toggleSaveJob(jobId: String, currentSaved: Boolean) {
        appDao.setJobSaved(jobId, !currentSaved)
    }

    suspend fun applyForJob(jobId: String) {
        appDao.setJobApplied(jobId)
    }

    fun getChatForJob(jobId: String): Flow<List<ChatMessage>> {
        return appDao.getChatForJob(jobId)
    }

    suspend fun sendChatMessage(jobId: String, companyName: String, text: String, isFromUser: Boolean, senderName: String) {
        val message = ChatMessage(
            jobId = jobId,
            companyName = companyName,
            senderName = senderName,
            isFromUser = isFromUser,
            messageText = text
        )
        appDao.insertChatMessage(message)
    }
}
