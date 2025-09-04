package com.example.degreeplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        val vm: DegreePlannerViewModel by viewModels()
        setContent {
            DegreePlannerTheme {
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
                    DegreePlannerScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = vm
                    )
                }
            }
        }
    }
}

@Composable
fun DegreePlannerScreen(
    modifier: Modifier = Modifier,
    viewModel: DegreePlannerViewModel
) {
    // Collect the UI state as Compose state
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dropdown list containing major
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = uiState.selectedCourse?.name ?: "Select a Major",
                onValueChange = {}, // Read-only
                readOnly = true,
                label = { Text("Available Majors") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        Modifier.clickable { viewModel.onMajorDropdown() }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = uiState.majorDropdownExpanded,
                onDismissRequest = { viewModel.onDismissMajorDropdown() },
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: START HERE - MAJORS NOT SHOWING UP
                uiState.allMajors.forEach { major ->
                    DropdownMenuItem(
                        text = { Text(major) },
                        onClick = {
                            viewModel.onMajorSelected(major)
                        }
                    )
                }
            }
        }

        // Dropdown list containing unselected courses
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = uiState.selectedCourse?.name ?: "Select a Course",
                onValueChange = {}, // Read-only
                readOnly = true,
                label = { Text("Available Courses") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        Modifier.clickable { viewModel.onCourseDropdown() }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = uiState.courseDropdownExpanded,
                onDismissRequest = { viewModel.onDismissCourseDropdown() },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Filter available courses based on what's already staged
                val availableCourses = uiState.allCourses.filterNot { course ->
                    uiState.stagedCourses.any { staged -> staged.id == course.id }
                }
                availableCourses.forEach { course ->
                    DropdownMenuItem(
                        enabled = uiState.majorSelected,
                        text = { Text(course.id) },
                        onClick = {
                            viewModel.onCourseSelected(course)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.onAddCourseToPlan() }, // Call ViewModel
            enabled = uiState.selectedCourse != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Plan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Your Current Plan:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (uiState.stagedCourses.isEmpty()) {
            Text("No courses added yet. Select a course and click 'Add to Plan'.")
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                uiState.stagedCourses.forEach { course ->
                    CourseStagingItem(
                        course = course,
                        onRemove = {
                            viewModel.onRemoveCourseFromPlan(course) // Call ViewModel
                        }
                    )
                    HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.prerequisiteWarning?.let {
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
            Text(course.id, style = MaterialTheme.typography.bodyLarge)
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Close, contentDescription = "Remove ${course.name}")
        }
    }
}
