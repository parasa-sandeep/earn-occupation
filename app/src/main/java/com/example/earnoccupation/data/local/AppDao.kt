package com.example.earnoccupation.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // User Profile
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    // User Accounts
    @Query("SELECT * FROM user_accounts WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserAccountDirect(email: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAccount(account: UserAccount)

    @Query("SELECT COUNT(*) FROM user_accounts")
    suspend fun getUserAccountCount(): Int

    // Job Items
    @Query("SELECT * FROM job_items")
    fun getAllJobs(): Flow<List<JobItem>>

    @Query("SELECT * FROM job_items WHERE isSaved = 1")
    fun getSavedJobs(): Flow<List<JobItem>>

    @Query("SELECT * FROM job_items WHERE isApplied = 1")
    fun getAppliedJobs(): Flow<List<JobItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobItem>)

    @Update
    suspend fun updateJob(job: JobItem)

    @Query("UPDATE job_items SET isSaved = :isSaved WHERE id = :jobId")
    suspend fun setJobSaved(jobId: String, isSaved: Boolean)

    @Query("UPDATE job_items SET isApplied = 1, applicationStatus = 'Applied' WHERE id = :jobId")
    suspend fun setJobApplied(jobId: String)

    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE jobId = :jobId ORDER BY timestamp ASC")
    fun getChatForJob(jobId: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}
