package com.example.service

import com.example.model.User
import com.example.repository.UserRepository
import com.example.routing.request.LoginRequest
import com.example.routing.request.UserRequest

class UserService(
    private val userRepository: UserRepository
) {
    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    fun save(user: UserRequest): User? {
        return if (userRepository.save(user)) User(user.username) else null
    }

    fun ldapAuth(loginRequest: LoginRequest) = userRepository.ldapAuth(loginRequest)

}