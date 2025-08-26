package com.example.degreeplanner // !! IMPORTANT: Use your actual package name !!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.* // Using Material 3 components
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// If Course.kt and CourseList.kt are in the same package, no import needed.
// Otherwise, import them:
// import com.example.degreeplanner.Course
// import com.example.degreeplanner.CourseList
import com.example.degreeplanner.ui.theme.DegreePlannerTheme // Assuming your theme file

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DegreePlannerTheme { // Apply your app's theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Degree Planner") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) { innerPadding ->
                    DegreePlannerScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar and OutlinedTextField in M3
@Composable
fun DegreePlannerScreen(modifier: Modifier = Modifier) {
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var stagedCourses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var prerequisiteWarning by remember { mutableStateOf<String?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }

    val allCourses = CourseList.courses // From CourseList.kt

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Plan Your Degree",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 1. Dropdown list for courses
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCourse?.name ?: "Select a Course",
                onValueChange = { /* Read-only, selection happens via dropdown items */ },
                readOnly = true,
                label = { Text("Available Courses") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        Modifier.clickable { expandedDropdown = !expandedDropdown }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = { expandedDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                allCourses.filterNot { stagedCourses.any { staged -> staged.id == it.id } }.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.name) },
                        onClick = {
                            selectedCourse = course
                            expandedDropdown = false
                            prerequisiteWarning = null // Clear warning when selecting a new course
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Button to add selected course to staging area
        Button(
            onClick = {
                selectedCourse?.let { courseToAdd ->
                    if (!stagedCourses.any { it.id == courseToAdd.id }) {
                        stagedCourses = stagedCourses + courseToAdd
                        prerequisiteWarning = checkPrerequisites(stagedCourses, allCourses)
                    }
                    selectedCourse = null // Reset dropdown selection
                }
            },
            enabled = selectedCourse != null,
            modifier = Modifier.fillMaxWidth(0.7f) // Make button a bit smaller
        ) {
            Text("Add to Plan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Staging area for selected courses
        Text(
            "Your Current Plan:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (stagedCourses.isEmpty()) {
            Text("No courses added yet. Select a course and click 'Add to Plan'.")
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                stagedCourses.forEach { course ->
                    CourseStagingItem(
                        course = course,
                        onRemove = {
                            stagedCourses = stagedCourses - course
                            prerequisiteWarning = checkPrerequisites(stagedCourses, allCourses)
                        }
                    )
                    Divider() // Add a divider between items
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Prerequisite notification area
        prerequisiteWarning?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CourseStagingItem(course: Course, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(course.name, style = MaterialTheme.typography.bodyLarge)
            course.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
        IconButton(onClick = onRemove) { // Using IconButton for better semantics
            Icon(Icons.Filled.Close, contentDescription = "Remove ${course.name}")
        }
    }
}

// Function to check prerequisites (remains the same logic)
fun checkPrerequisites(selectedCourses: List<Course>, allCourses: List<Course>): String? {
    val selectedCourseIds = selectedCourses.map { it.id }.toSet()

    for (course in selectedCourses) {
        for (prerequisiteId in course.prerequisites) {
            if (prerequisiteId !in selectedCourseIds) {
                val missingPrerequisiteCourse = allCourses.find { it.id == prerequisiteId }
                return "Warning: '${course.name}' is missing prerequisite: '${missingPrerequisiteCourse?.name ?: "ID: $prerequisiteId"}'."
            }
        }
    }
    return null // All prerequisites met
}