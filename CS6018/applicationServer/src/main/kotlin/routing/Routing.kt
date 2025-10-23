package com.example.routing

import com.example.database.ImageService
import com.example.repository.NotesRepository
import com.example.service.JwtService
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun Application.configureRouting(
  jwtService: JwtService,
  userService: UserService,
  imageService: ImageService
) {
  routing {

    route("/api/auth") {
      authRoute(jwtService)
    }

    route("/api/user") {
      userRoute(userService)
    }

    authenticate {
      route("/api/upload"){
        uploadRoute()
      }

      route("/api/images"){
        imageRoute(imageService)
      }
    }

  }
}

fun extractPrincipalUsername(call: ApplicationCall): String? =
  call.principal<JWTPrincipal>()
    ?.payload
    ?.getClaim("username")
    ?.asString()



