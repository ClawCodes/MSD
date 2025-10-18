package com.example.routing

import com.example.model.User
import com.example.routing.request.UserRequest
import com.example.routing.response.UserResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userService: UserService) {

    // create a new user
    post {
        val userRequest = call.receive<UserRequest>()
        println("user request: ${userRequest}")
        val createdUser = userService.save(
            user = userRequest
        ) ?: return@post call.respond(HttpStatusCode.BadRequest)

        call.response.header(
            name = "username",
            value = createdUser.username
        )
        call.respond(
            message = HttpStatusCode.Created
        )
    }
}

private fun UserRequest.toModel(): User =
    User(
        username = this.username,
    )

private fun User.toResponse(): UserResponse =
    UserResponse(
        username = this.username
    )


