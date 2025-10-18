package com.example.routing.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
  //@Serializable(with = UUIDSerializer::class)
  //val id: UUID,
  val username: String,
)