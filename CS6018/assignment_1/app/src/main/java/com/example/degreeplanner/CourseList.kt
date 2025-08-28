package com.example.degreeplanner

val cs101 =  Course(
    id = "CS101",
    name = "Introduction to Computer Science",
)
val cs102 = Course(
    id = "CS102",
    name = "Data Structures and Algorithms",
    prerequisites = listOf("CS101")
)
val cs103 = Course(
    id = "CS103",
    name = "Software Engineering",
    prerequisites = listOf("CS101", "CS102")
)
val ma101 = Course(
    id = "MA101",
    name = "Calculus I",
)
val ma202 = Course(
    id = "MA202",
    name = "Discrete Mathematics",
    prerequisites = listOf("MA101")
)
val cs203 = Course(
    id = "CS203",
    name = "Linear Algebra",
    prerequisites = listOf("MA101")
)
val cs201 = Course(
    id = "CS201",
    name = "Algorithms",
    prerequisites = listOf("CS102", "MA202")
)
val cs301 = Course(
    id = "CS301",
    name = "Systems I",
    prerequisites = listOf("CS201")
)
val cs302 = Course(
    id = "CS310",
    name = "Distributed Systems",
    prerequisites = listOf("CS201", "CS301")
)

object CourseList {
    val courses: List<Course> = listOf(
        cs101, cs102, cs103, ma101, ma202, cs203, cs201, cs301, cs302
    )
}

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
    return null
}