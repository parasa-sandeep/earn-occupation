package com.example.earnoccupation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val username: String = "Sandeep Parasa",
    val email: String = "user@earnoccupation.com",
    val education: String = "Bachelor's Degree",
    val qualificationDetails: String = "B.Tech / Specialized Degree",
    val branch: String = "CSE",
    val age: Int = 22,
    val skills: String = "Kotlin, Java, Python, Problem Solving, Data Analysis",
    val preferredState: String = "Telangana",
    val preferredCity: String = "Hyderabad",
    val minSalaryLpa: Double = 8.0,
    val bio: String = "Aspiring professional looking for high-impact roles.",
    val isLoggedIn: Boolean = true
)

@Entity(tableName = "job_items")
data class JobItem(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val branch: String, // CSE, ECE, MECHANICAL, CIVIL, MEDICAL, ELECTRICAL, OTHER
    val category: String, // Job, Internship, Remote
    val requiredSkills: String,
    val minEducation: String,
    val state: String,
    val city: String,
    val salaryRange: String,
    val salaryNumber: Double, // in LPA for sorting
    val applyUrl: String,
    val platform: String, // LinkedIn, Unstop, Indeed, Direct Company Portal
    val description: String,
    val requirements: String,
    val recruiterName: String,
    val recruiterRole: String,
    val isSaved: Boolean = false,
    val isApplied: Boolean = false,
    val applicationStatus: String = "Not Applied" // Not Applied, Applied, Under Review, Shortlisted
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jobId: String,
    val companyName: String,
    val senderName: String,
    val isFromUser: Boolean,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis()
)
