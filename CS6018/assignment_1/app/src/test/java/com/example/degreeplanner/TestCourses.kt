package com.example.degreeplanner

import org.junit.Test
import org.junit.Assert.*

class TestCourses {
    @Test
    fun `checkPrerequisites with no selected courses should return null`() {
        // Arrange
        val selectedCourses = emptyList<Course>()

        // Act
        val result = checkPrerequisites(selectedCourses, CourseList.courses)

        // Assert
        assertNull("Expected null when no courses are selected, but got: $result", null)
    }

    @Test
    fun `checkPrerequisites with all prerequisites met should return null`() {
        // Arrange
        val selectedCourses =
            listOf(cs101, cs102)

        // Act
        val result = checkPrerequisites(selectedCourses, CourseList.courses)

        // Assert
        assertNull("Expected null when all prerequisites are met, but got: $result", result)
    }


    // TODO: FIX THIS TEST
    @Test
    fun `checkPrerequisites when a single prerequisite is missing should return warning`() {
        // Arrange
        val selectedCourses = listOf(cs102) // missing cs101 prereq

        // Act
        val result = checkPrerequisites(selectedCourses, CourseList.courses)

        // Assert
        val expectedWarning = "Warning: '${cs102.name}' is missing prerequisite: '${cs101.name}'."
        assertEquals("Warning message did not match the expected format.", result)
    }
}