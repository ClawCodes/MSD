package com.example.degreeplanner // !! IMPORTANT: Replace with your actual package name !!

object CourseList {
    val courses: List<Course> = listOf(
        Course(
            id = "CS101",
            name = "Introduction to Computer Science",
            credits = 3,
            prerequisites = emptyList()
        ),
        Course(
            id = "CS102",
            name = "Data Structures",
            credits = 4,
            prerequisites = listOf("CS101")
        ),
        Course(
            id = "MA101",
            name = "Calculus I",
            credits = 4,
            prerequisites = emptyList()
        ),
        Course(
            id = "CS201",
            name = "Algorithms",
            credits = 3,
            prerequisites = listOf("CS102", "MA101")
        ),
        Course(
            id = "CS202",
            name = "Discrete Mathematics",
            credits = 3,
            prerequisites = listOf("MA101")
        ),
        Course(
            id = "CS301",
            name = "Operating Systems",
            credits = 3,
            prerequisites = listOf("CS201") // Corrected from CS201, CS202 for a simpler example
        ),
        Course(
            id = "PH101",
            name = "Physics I",
            credits = 4,
            prerequisites = listOf("MA101")
        ),
        Course(
            id = "CS310",
            name = "Compiler Design",
            credits = 3,
            prerequisites = listOf("CS201", "CS202")
        )
        // Add more courses here
    )
}