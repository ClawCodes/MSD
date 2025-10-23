package com.example.model
import kotlinx.serialization.Serializable

@Serializable
data class ImageFile (val id: Int, val username: String,
                      val pw_hash: String,
    )

@Serializable
data class ImageBytes (val id: String, val bytesBase64: String, val contentType: String)