package com.example.routing

import com.example.database.ImageService
import com.example.service.JwtService
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Route.ping(){
  post {
    call.respondText("pong")
  }
  get {
    call.respondText("pong")
  }
}
fun Application.configureRouting(
  jwtService: JwtService,
  userService: UserService,
  imageService: ImageService
) {
  routing {
    println("ROUTING")
    route("/"){
      println("PING!")
      ping()
    }
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
        println("IMAGE")
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



