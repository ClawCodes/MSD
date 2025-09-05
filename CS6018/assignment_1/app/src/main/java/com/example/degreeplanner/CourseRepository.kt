package com.example.degreeplanner

import android.annotation.SuppressLint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PlanList(
    val plans: List<Plan>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Plan(
    val name: String,
    val path: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PlanCourseList(
    val name: String,
    val requirements: List<Requirement>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Requirement(
    val type: String,
    val course: PlanCourse? = null,
    val courses: List<PlanCourse>? = null,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PlanCourse(
    val department: String,
    val number: String
)

class CourseRepository(val scope: CoroutineScope) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "https://msd2025.github.io/degreePlans/"
    private var plans = MutableStateFlow<List<Plan>>(emptyList())
    val plansFlow = plans.asStateFlow()
    private var courses = MutableStateFlow<List<Course>>(emptyList())
    val coursesFlow = courses.asStateFlow()

    private suspend fun _fetchPlans(): PlanList {
        return client.get(baseUrl + "degreePlans.json").body()
    }

    fun fetchPlans() {
        scope.launch {
            plans.value = _fetchPlans().plans
        }
    }

    private suspend fun _fetchCourses(path: String): PlanCourseList {
        return client.get(baseUrl + path).body()
    }

    fun getCourses(major: String) {
        for (plan in plans.value) {
            if (plan.name == major) {
                scope.launch {
                    val planCourses = _fetchCourses(plan.path)
                    var newCourses = convertCourseFormat(planCourses)
                    if (planCourses.name == "Computer Science") {
                        newCourses = newCourses + CourseList.courses
                    }
                    courses.value = newCourses
                }
                break
            }
        }
    }

    private fun convertCourseFormat(courseList: PlanCourseList): List<Course> {
        var baseReqs: List<Course> = emptyList()
        var optional: List<Course> = emptyList()

        courseList.requirements?.forEach { req ->
            when (req.type) {
                "requiredCourse" -> {
                    req.course?.let {
                        baseReqs = baseReqs.plus(Course(it.department + it.number))
                    }
                }
                "oneOf" -> {
                    req.courses?.forEach { planCourse ->
                        val currentBaseReqIds = baseReqs.map { it.id }
                        optional = optional.plus(
                            Course(
                                planCourse.department + planCourse.number,
                                prerequisites = currentBaseReqIds
                            )
                        )
                    }
                }
            }
        }
        return baseReqs + optional
    }
}