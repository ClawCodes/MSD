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

class CourseRepository (val scope: CoroutineScope) {
    private val client = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "https://msd2025.github.io/degreePlans/"
    var plans = mutableListOf<Plan>()

    private suspend fun _fetchPlans(): PlanList {
        try {
            return client.get(baseUrl + "degreePlans.json").body()
        } catch (e: Exception) {
            throw Exception("Network request failed")
            e.printStackTrace()
        }
    }

    fun fetchPlans(){
        scope.launch {
            plans = _fetchPlans().plans.toMutableList()
        }
    }
}