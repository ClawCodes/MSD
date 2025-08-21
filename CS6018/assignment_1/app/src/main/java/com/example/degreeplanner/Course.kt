package com.example.degreeplanner
import com.example.degreeplanner.Requirements

data class Course(
    val abbr: String,
    val num: Int,
    val prereqs: Requirements?
)