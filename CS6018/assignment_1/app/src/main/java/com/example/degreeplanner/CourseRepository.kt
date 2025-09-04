package com.example.degreeplanner

import android.annotation.SuppressLint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
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
    var plans = mutableListOf<Plan>()
    var courses: PlanCourseList? = null

    private suspend fun _fetchPlans(): PlanList {
        try {
            return client.get(baseUrl + "degreePlans.json").body()
        } catch (e: Exception) {
            throw Exception("Network request failed")
            e.printStackTrace()
        }
    }

    fun fetchPlans() {
        scope.launch {
            plans = _fetchPlans().plans.toMutableList()
        }
    }

    private suspend fun _fetchCourses(path: String): PlanCourseList {
        return client.get(baseUrl + path).body()
    }

    fun getCourses(major: String) {
        for (plan in plans) {
            if (plan.name == major) {
                scope.launch {
                    courses = _fetchCourses(plan.path)
                }
                return
            }
        }
    }

    fun getCourseList(): List<Course> {
        if (courses == null) {
            return emptyList()
        } else {
            val baseReqs: List<Course> = emptyList()
            val optional: List<Course> = emptyList()
            for (req in courses!!.requirements) {
                if (req.type == "requiredCourse") {
                    baseReqs.plus(Course(req.course!!.department + req.course.number))
                }
                if (req.type == "oneOf") {
                    for (course in req.courses!!) {
                        optional.plus(
                            Course(
                                course.department + course.number,
                                prerequisites = baseReqs.map { it.id })
                        )
                    }
                }
            }
            return baseReqs + optional
        }
    }
}