package com.example.routing


import com.example.auth.UserPrincipal
import com.example.database.Image
import com.example.database.ImageService
import com.example.model.ImageBytes
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import java.util.Base64

suspend fun saveUpload(part: PartData.FileItem, dstFile: File) {
    withContext(Dispatchers.IO) {
        val inChan: ByteReadChannel = part.provider()
        val outChan: ByteWriteChannel = dstFile.writeChannel()
        try {
            inChan.copyAndClose(outChan)
            outChan.flushAndClose()
        } catch (t: Throwable) {
            outChan.cancel(t)
            println("Failed to save image: ${t.message}")
        }
    }
}


fun Route.imageRoute(imageService: ImageService) {
    post {
        val principal = call.principal<UserPrincipal>() ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val multipart = call.receiveMultipart()
        var saved: String? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    val contentType = part.contentType ?: ContentType.Application.OctetStream
                    require(contentType.match(ContentType.Image.Any)) { "Only images allowed" }

                    val imageId = UUID.randomUUID().toString()
                    val ext = when {
                        contentType.match(ContentType.Image.JPEG) -> "jpg"
                        contentType.match(ContentType.Image.PNG)  -> "png"
                        else -> "bin"
                    }
                    val path = File("/data/images/$imageId.$ext")

                    saveUpload(part, path)

                    saved = imageService.create(
                        Image(
                            id = imageId,
                            owner = principal.id,
                            path = path.absolutePath,
                        )
                    )
                }
                else -> { }
            }
            part.dispose()
        }

        if (saved == null) return@post call.respond(HttpStatusCode.BadRequest, "No file part")
        call.respond(HttpStatusCode.Created, mapOf("id" to saved))
    }

    get {
        val principal = call.principal<UserPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)

        val images = imageService.read(principal.id) // fetch all rows for this owner:contentReference[oaicite:1]{index=1}

        val out = images.mapNotNull { img ->
            val file = File(img.path)
            if (!file.exists()) return@mapNotNull null

            val contentType = when (file.extension.lowercase()) {
                "jpg", "jpeg" -> ContentType.Image.JPEG.toString()
                "png"         -> ContentType.Image.PNG.toString()
                else           -> ContentType.Application.OctetStream.toString()
            }

            val bytes = file.readBytes()
            ImageBytes(
                id = img.id,
                bytesBase64 = Base64.getEncoder().encodeToString(bytes),
                contentType = contentType
            )
        }

        call.respond(HttpStatusCode.OK, out)
    }
}