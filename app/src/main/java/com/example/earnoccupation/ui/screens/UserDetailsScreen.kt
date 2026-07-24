package com.example.earnoccupation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.earnoccupation.data.local.UserProfile
import com.example.earnoccupation.ui.components.Interactive3DTiltCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassAccentGradient
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardBorderGlow
import com.example.ui.theme.Indigo200
import com.example.ui.theme.Indigo300
import com.example.ui.theme.MeshGradientBrush
import com.example.ui.theme.Purple300

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserDetailsScreen(
    currentProfile: UserProfile?,
    onSubmitDetails: (
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
    ) -> Unit
) {
    var username by remember { mutableStateOf(currentProfile?.username ?: "Sandeep Parasa") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "sandeep@earnoccupation.com") }

    val educationOptions = listOf(
        "Bachelor's Degree (B.Tech / B.E / MBBS)",
        "Diploma / Polytechnic",
        "Master's Degree (M.Tech / M.Sc / MBA)",
        "Doctorate / PhD",
        "Higher Secondary / High School"
    )
    var selectedEducation by remember { mutableStateOf(currentProfile?.education ?: educationOptions[0]) }
    var eduDropdownExpanded by remember { mutableStateOf(false) }

    var qualificationDetails by remember {
        mutableStateOf(currentProfile?.qualificationDetails ?: "B.Tech Computer Science Engineering")
    }

    // Branches list
    val branchesList = listOf(
        "CSE" to "Computer Science & IT",
        "ECE" to "Electronics & Communication",
        "MECHANICAL" to "Mechanical & Auto",
        "CIVIL" to "Civil & Construction",
        "MEDICAL" to "Medical & Healthcare",
        "ELECTRICAL" to "Electrical & Energy",
        "OTHER" to "Other Engineering / Biotech"
    )
    var selectedBranch by remember { mutableStateOf(currentProfile?.branch ?: "CSE") }

    var ageInput by remember { mutableStateOf(currentProfile?.age?.toString() ?: "22") }

    // Skills
    val skillsList = remember {
        mutableStateListOf<String>().apply {
            val initial = currentProfile?.skills ?: "Kotlin, Java, Python, Problem Solving, Data Analysis"
            addAll(initial.split(",").map { it.trim() }.filter { it.isNotEmpty() })
        }
    }
    var newSkillText by remember { mutableStateOf("") }

    // Location & Salary
    var selectedState by remember { mutableStateOf(currentProfile?.preferredState ?: "Telangana") }
    var selectedCity by remember { mutableStateOf(currentProfile?.preferredCity ?: "Hyderabad") }
    var minSalaryLpa by remember { mutableStateOf(currentProfile?.minSalaryLpa?.toString() ?: "8.0") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshGradientBrush)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Career & Qualification Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(brush = GlassAccentGradient),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Type in your educational qualifications, branch, skills, and location to analyze matching jobs & salaries",
                fontSize = 13.sp,
                color = Indigo200,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 20.dp)
            )

            // Section 1: Education Qualification
            Interactive3DTiltCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                maxTiltDegrees = 10f
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.School, contentDescription = null, tint = CyanAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Educational Qualification",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = eduDropdownExpanded,
                        onExpandedChange = { eduDropdownExpanded = !eduDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedEducation,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Highest Education", color = Indigo200) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eduDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("education_dropdown")
                        )

                        ExposedDropdownMenu(
                            expanded = eduDropdownExpanded,
                            onDismissRequest = { eduDropdownExpanded = false },
                            modifier = Modifier.background(Color(0xFF1E1B4B))
                        ) {
                            educationOptions.forEach { edu ->
                                DropdownMenuItem(
                                    text = { Text(edu, color = Color.White) },
                                    onClick = {
                                        selectedEducation = edu
                                        eduDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = qualificationDetails,
                        onValueChange = { qualificationDetails = it },
                        label = { Text("Degree / Specialization Details", color = Indigo200) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo300,
                            unfocusedBorderColor = GlassCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("qualification_details_input")
                    )
                }
            }
        }

            // Section 2: Branch Selection
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BusinessCenter, contentDescription = null, tint = Indigo300)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select Branch / Field",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    branchesList.forEach { (code, name) ->
                        val isSelected = selectedBranch == code
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) Color(0x666366F1) else Color(0x1AFFFFFF))
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) Indigo300 else GlassCardBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedBranch = code }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("branch_option_$code")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = code,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Indigo300 else Color.White
                                    )
                                    Text(
                                        text = name,
                                        fontSize = 12.sp,
                                        color = Indigo200
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Indigo300,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Present Skills
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Present Skills",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Type skills you possess (e.g. Python, AutoCAD, Patient Care, STAAD Pro, C++)",
                        fontSize = 12.sp,
                        color = Indigo200,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newSkillText,
                            onValueChange = { newSkillText = it },
                            placeholder = { Text("Type skill name...", color = Indigo200.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldGreen,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("add_skill_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newSkillText.isNotBlank()) {
                                    skillsList.add(newSkillText.trim())
                                    newSkillText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("add_skill_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Skill")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        skillsList.forEach { skill ->
                            FilterChip(
                                selected = true,
                                onClick = { skillsList.remove(skill) },
                                label = { Text(skill, color = Color.White, fontSize = 13.sp) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Indigo200,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0x336366F1)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = true,
                                    borderColor = CyanAccent
                                )
                            )
                        }
                    }
                }
            }

            // Section 4: Age, Location & Salary Expectations
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GlassCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Purple300)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Age, Location & Salary Expectations",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = ageInput,
                            onValueChange = { ageInput = it },
                            label = { Text("Your Age", color = Indigo200) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("age_input")
                        )

                        OutlinedTextField(
                            value = minSalaryLpa,
                            onValueChange = { minSalaryLpa = it },
                            label = { Text("Min Salary (LPA)", color = Indigo200) },
                            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = Indigo300) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("salary_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = selectedState,
                            onValueChange = { selectedState = it },
                            label = { Text("Preferred State", color = Indigo200) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("state_input")
                        )

                        OutlinedTextField(
                            value = selectedCity,
                            onValueChange = { selectedCity = it },
                            label = { Text("Preferred City", color = Indigo200) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo300,
                                unfocusedBorderColor = GlassCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("city_input")
                        )
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    val age = ageInput.toIntOrNull() ?: 22
                    val salary = minSalaryLpa.toDoubleOrNull() ?: 8.0
                    val skillsJoined = skillsList.joinToString(", ")
                    onSubmitDetails(
                        username,
                        email,
                        selectedEducation,
                        qualificationDetails,
                        selectedBranch,
                        age,
                        skillsJoined,
                        selectedState,
                        selectedCity,
                        salary
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F172A)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("submit_user_details_button")
            ) {
                Text(
                    text = "SUBMIT & ANALYZE MATCHING JOBS",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

