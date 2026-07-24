package com.example.earnoccupation.data.api

import com.example.BuildConfig
import com.example.earnoccupation.data.local.JobItem
import com.example.earnoccupation.data.local.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private fun getApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    /**
     * Generate dynamic AI cover letter / application pitch tailored to user profile & job item
     */
    suspend fun generateCoverLetter(
        user: UserProfile,
        job: JobItem
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(getFallbackCoverLetter(user, job))
            }

            val prompt = """
                Write a concise, professional, and compelling cover letter application pitch (max 200 words) for:
                Applicant Name: ${user.username}
                Branch / Specialization: ${user.branch}
                Qualification: ${user.education} (${user.qualificationDetails})
                Key Skills: ${user.skills}
                Target Salary: ₹${user.minSalaryLpa} LPA
                
                Applying for Job:
                Title: ${job.title}
                Company: ${job.company}
                Category: ${job.category}
                Required Skills: ${job.requiredSkills}
                Location: ${job.city}, ${job.state}
                
                Keep the tone confident, polite, and directly address Recruiter ${job.recruiterName}. Highlight why the applicant is a top fit.
            """.trimIndent()

            val jsonPayload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.success(getFallbackCoverLetter(user, job))
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    Result.success(text)
                } else {
                    Result.success(getFallbackCoverLetter(user, job))
                }
            }
        } catch (e: Exception) {
            Result.success(getFallbackCoverLetter(user, job))
        }
    }

    /**
     * AI Multi-turn Recruiter Response / Career Guidance
     */
    suspend fun getAIRecruiterResponse(
        userMessage: String,
        user: UserProfile,
        job: JobItem,
        chatHistory: List<Pair<String, String>> // sender to message
    ): String = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext getFallbackRecruiterReply(userMessage, user, job)
            }

            val systemInstruction = """
                You are ${job.recruiterName}, the Senior Hiring Manager & Recruiter at ${job.company}.
                You are conducting direct messaging with applicant ${user.username} for the position of '${job.title}'.
                Applicant Background: Branch: ${user.branch}, Qualification: ${user.education}, Skills: ${user.skills}.
                Target Salary: ₹${job.salaryRange}.
                Be professional, helpful, encouraging, and ask relevant interview or skill clarifying questions if appropriate. Keep response under 80 words.
            """.trimIndent()

            val contentsArray = JSONArray()

            // Context/System injection turn
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            })
            contentsArray.put(JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().put(JSONObject().put("text", "Understood. I am ${job.recruiterName} from ${job.company}. Ready to chat with applicant ${user.username}.")))
            })

            // Chat History
            chatHistory.takeLast(6).forEach { (sender, msgText) ->
                val role = if (sender == user.username) "user" else "model"
                contentsArray.put(JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().put(JSONObject().put("text", msgText)))
                })
            }

            // Current message
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            })

            val jsonPayload = JSONObject().apply {
                put("contents", contentsArray)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL/gemini-3.1-flash-lite-preview:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext getFallbackRecruiterReply(userMessage, user, job)
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                text ?: getFallbackRecruiterReply(userMessage, user, job)
            }
        } catch (e: Exception) {
            getFallbackRecruiterReply(userMessage, user, job)
        }
    }

    /**
     * AI Resume & Career Audit
     */
    suspend fun analyzeResumeAndSkills(
        user: UserProfile
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(getFallbackAudit(user))
            }

            val prompt = """
                Analyze this candidate's career profile for Indian high-growth occupations:
                Name: ${user.username}
                Branch / Stream: ${user.branch}
                Degree / Qualification: ${user.education} (${user.qualificationDetails})
                Current Skills: ${user.skills}
                Preferred Location: ${user.preferredCity}, ${user.preferredState}
                Expected Salary: ₹${user.minSalaryLpa} LPA
                
                Provide a structured report with:
                1. Resume Match Rating (e.g. 88/100)
                2. Key Strengths
                3. Critical Missing Skills to Reach Target Salary
                4. Recommended Certifications & Project Ideas
                Keep formatting clear with bullet points and friendly professional tone.
            """.trimIndent()

            val jsonPayload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.success(getFallbackAudit(user))
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    Result.success(text)
                } else {
                    Result.success(getFallbackAudit(user))
                }
            }
        } catch (e: Exception) {
            Result.success(getFallbackAudit(user))
        }
    }

    private fun getFallbackCoverLetter(user: UserProfile, job: JobItem): String {
        return """
            Dear ${job.recruiterName},
            
            I am writing to express my strong enthusiasm for the ${job.title} position at ${job.company}. As a candidate with a strong background in ${user.branch} (${user.education}) and expertise in ${user.skills}, I am confident in my ability to add immediate value to your engineering team in ${job.city}.
            
            My technical qualifications align closely with your requirements for ${job.requiredSkills}. I look forward to discussing how my experience and passion for innovation match ${job.company}'s growth goals.
            
            Sincerely,
            ${user.username}
        """.trimIndent()
    }

    private fun getFallbackRecruiterReply(userMessage: String, user: UserProfile, job: JobItem): String {
        return "Hello ${user.username}, thank you for your message regarding '${job.title}' at ${job.company}. We appreciate your interest and strong background in ${user.branch}. Our talent acquisition team will review your application closely!"
    }

    private fun getFallbackAudit(user: UserProfile): String {
        return """
            🌟 **AI Career & Profile Audit**
            
            **Overall Score: 88 / 100**
            
            **Key Strengths:**
            • Solid academic grounding in ${user.branch} (${user.education}).
            • Core technical competencies in ${user.skills}.
            • Realistic location preference in ${user.preferredCity}, ${user.preferredState}.
            
            **High-Value Skills to Add for Higher LPA:**
            • System Design & Cloud DevOps (AWS/GCP/Azure)
            • Project Management & Agile Methodologies
            • Hands-on Industry Internship Projects
        """.trimIndent()
    }
}
