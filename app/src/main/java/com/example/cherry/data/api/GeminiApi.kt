package com.example.cherry.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class BackendHistory(
    @Json(name = "role") val role: String,
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class ChatBackendRequest(
    @Json(name = "systemInstruction") val systemInstruction: String,
    @Json(name = "userPrompt") val userPrompt: String,
    @Json(name = "history") val history: List<BackendHistory>
)

@JsonClass(generateAdapter = true)
data class ChatBackendResponse(
    @Json(name = "reply") val reply: String?
)

interface ChatBackendService {
    @POST("chat")
    suspend fun chat(
        @Body request: ChatBackendRequest
    ): ChatBackendResponse
}

object GeminiClient {
    private const val BASE_URL =
        "https://YOUR-SERVICE.onrender.com/"

    private val service: ChatBackendService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .build()
            .create(ChatBackendService::class.java)
    }

    suspend fun callGemini(
        systemInstruction: String,
        userPrompt: String,
        history: List<Pair<String, String>> = emptyList()
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = ChatBackendRequest(
                systemInstruction = systemInstruction,
                userPrompt = userPrompt,
                history = history.takeLast(10).map { (sender, text) ->
                    BackendHistory(
                        role = if (sender == "user") "user" else "assistant",
                        text = text
                    )
                }
            )

            val response = service.chat(request)
            val reply = response.reply

            if (!reply.isNullOrBlank()) {
                Result.success(reply)
            } else {
                Result.failure(
                    Exception("Empty response from AI backend")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
