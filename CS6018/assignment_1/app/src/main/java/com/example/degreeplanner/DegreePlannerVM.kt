package com.example.degreeplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class DegreePlannerUiState(
    val selectedMajor: String? = null,
    val majorSelected: Boolean = false,
    val majorDropdownExpanded: Boolean = false,
    val allCourses: List<Course> = CourseList.courses,
    val allMajors: List<String> = emptyList(),
    val selectedCourse: Course? = null,
    val stagedCourses: List<Course> = emptyList(),
    val prerequisiteWarning: String? = null,
    val courseDropdownExpanded: Boolean = false
)

class DegreePlannerViewModel : ViewModel() {
    val courseRepo = CourseRepository(scope = CoroutineScope(SupervisorJob()))

    private val _uiState = MutableStateFlow(DegreePlannerUiState())
    val uiState: StateFlow<DegreePlannerUiState> = _uiState.asStateFlow()

    init {
        courseRepo.fetchPlans()
        viewModelScope.launch {
            courseRepo.plansFlow.collect { plans ->
                _uiState.update { currentState ->
                    currentState.copy(
                        allMajors = plans.map { it.name }
                    )
                }
            }
        }
        viewModelScope.launch {
            courseRepo.coursesFlow.collect { courses ->
                _uiState.update { currentState ->
                    currentState.copy(
                        allCourses = courses
                    )
                }
            }
        }
    }

    fun onMajorSelected(major: String) {
        courseRepo.getCourses(major)
        _uiState.update { currentState ->
            currentState.copy(
                selectedMajor = major,
                majorSelected = true,
                majorDropdownExpanded = false,
                stagedCourses = emptyList(),
            )
        }
    }

    fun onCourseSelected(course: Course) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCourse = course,
                courseDropdownExpanded = false,
                prerequisiteWarning = null
            )
        }
    }

    fun onAddCourseToPlan() {
        _uiState.value.selectedCourse?.let { courseToAdd ->
            val updatedStagedCourses = _uiState.value.stagedCourses + courseToAdd
            _uiState.update { currentState ->
                currentState.copy(
                    stagedCourses = updatedStagedCourses,
                    selectedCourse = null,
                    prerequisiteWarning = checkPrerequisites(
                        updatedStagedCourses,
                        currentState.allCourses
                    )
                )
            }
        }
    }

    fun onRemoveCourseFromPlan(course: Course) {
        val updatedStagedCourses = _uiState.value.stagedCourses - course
        _uiState.update { currentState ->
            currentState.copy(
                stagedCourses = updatedStagedCourses,
                prerequisiteWarning = checkPrerequisites(
                    updatedStagedCourses,
                    currentState.allCourses
                )
            )
        }
    }

    fun onMajorDropdown() {
        _uiState.update { currentState ->
            currentState.copy(majorDropdownExpanded = !currentState.majorDropdownExpanded)
        }
    }

    fun onDismissMajorDropdown() {
        _uiState.update { currentState ->
            currentState.copy(majorDropdownExpanded = false)
        }
    }

    fun onCourseDropdown() {
        _uiState.update { currentState ->
            currentState.copy(courseDropdownExpanded = !currentState.courseDropdownExpanded)
        }
    }

    fun onDismissCourseDropdown() {
        _uiState.update { currentState ->
            currentState.copy(courseDropdownExpanded = false)
        }
    }

    private fun checkPrerequisites(stagedCourses: List<Course>, allCourses: List<Course>): String? {
        for (stagedCourse in stagedCourses) {
            for (prereqId in stagedCourse.prerequisites) {
                if (stagedCourses.none { it.id == prereqId }) {
                    val prereqCourseid =
                        allCourses.find { it.id == prereqId }?.id ?: "Unknown Course"
                    return "Warning: ${stagedCourse.id} is missing prerequisite: $prereqCourseid"
                }
            }
        }
        return null
    }
}
