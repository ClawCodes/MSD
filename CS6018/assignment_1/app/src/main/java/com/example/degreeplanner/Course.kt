package com.example.degreeplanner

data class Course(
    val id: String, // Unique identifier for the course (e.g., "CS101")
    val name: String? = null, // Full name of the course
    val prerequisites: List<String> = emptyList() // List of course IDs that are prerequisites
)