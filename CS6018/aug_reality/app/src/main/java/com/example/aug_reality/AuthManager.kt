package com.example.aug_reality

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

fun endPoint(stem: String): String{
    return String.format("http://10.0.2.2:8080/api/%s", stem)
}

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class AuthResponse(val token: String)

@Serializable
data class ImageBytesDto(
    val id: String,
    val bytesBase64: String,
    val contentType: String
)

class AuthManager(val dataStore: DataStore<Preferences>, val client: HttpClient){
    val tokenKey = stringPreferencesKey("token")
    val usernameKey = stringPreferencesKey("username")

    val prefsFlow by lazy { dataStore.data }

    suspend fun login(username: String, password: String){
        val tokenResp =
            try {
                client.post(endPoint("auth")) {
                    contentType(ContentType.Application.Json)
                    setBody(LoginRequest(username, password))
                }
            }
            catch (e: Exception){
                Log.d("ERROR ON LOGIN", "${e}")
                return
            }
        val token: AuthResponse = tokenResp.body()
        dataStore.edit { prefs ->
            prefs[tokenKey] = token.token
            prefs[usernameKey] = username
            prefs
        }
    }

    suspend fun signUp(username: String, password: String){
        try {
            val resp =
                client.post(endPoint("user")) {
                    contentType(ContentType.Application.Json)
                    setBody(LoginRequest(username, password))
                }
            if (resp.status == HttpStatusCode.Created) {
                val tokenResp =
                    client.post(endPoint("auth")) {
                        contentType(ContentType.Application.Json)
                        setBody(LoginRequest(username, password))
                    }
                Log.d("resp", "${tokenResp}")
                val token: AuthResponse = tokenResp.body()
                dataStore.edit { prefs ->
                    prefs[tokenKey] = token.token
                    prefs[usernameKey] = username
                    prefs
                }
            }
        }
        catch (e: Exception){
            Log.d("ERROR ON SIGNUP", "${e}")
        }
    }
}