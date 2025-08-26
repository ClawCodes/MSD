package com.example.degreeplanner

data class Course(
    val id: String, // Unique identifier for the course (e.g., "CS101")
    val name: String, // Full name of the course (e.g., "Introduction to Computer Science")
    val description: String? = null, // Optional description
    val credits: Int? = null, // Optional credit hours
    val prerequisites: List<String> = emptyList() // List of course IDs that are prerequisites
)