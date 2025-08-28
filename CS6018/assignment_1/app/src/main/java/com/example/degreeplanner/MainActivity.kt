package com.example.degreeplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.degreeplanner.ui.theme.DegreePlannerTheme

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

//@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar and OutlinedTextField in M3
@Composable
fun DegreePlannerScreen(modifier: Modifier = Modifier) {
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var stagedCourses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var prerequisiteWarning by remember { mutableStateOf<String?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

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

        // Dropdown list containing unselected courses
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCourse?.name ?: "Select a Course",
                onValueChange = {}, // Do nothing on change - read only
                readOnly = true,
                label = { Text("Available Courses") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        Modifier.clickable { dropdownExpanded = !dropdownExpanded }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                allCourses.filterNot { stagedCourses.any { staged -> staged.id == it.id } }.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.name) },
                        onClick = {
                            selectedCourse = course
                            dropdownExpanded = false
                            prerequisiteWarning = null // Clear warning when selecting a new course
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                selectedCourse?.let { courseToAdd ->
                    stagedCourses = stagedCourses + courseToAdd
                    prerequisiteWarning = checkPrerequisites(stagedCourses, allCourses)
                    selectedCourse = null // Reset dropdown selection so select course doesn't remain
                }
            },
            enabled = selectedCourse != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Plan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Staging area for selected courses
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
                    HorizontalDivider() // Include divider line between items
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
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Close, contentDescription = "Remove ${course.name}")
        }
    }
}