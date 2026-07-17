package dev.akkih.ssbot.service

import dev.akkih.ssbot.Bot.config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class CloudflareImageService(private val client: HttpClient) {
    @Serializable
    data class UploadResponse(
        val key: String,
        val downloadUrl: String,
    )

    suspend fun uploadFile(image: ByteArray): String {
        val response = client.post("${config.WORKER_BASE_URL}/upload") {
            header("x-upload-secret", config.WORKER_UPLOAD_SECRET)
            contentType(ContentType.Image.PNG)
            setBody(image)
        }.body<UploadResponse>()

        require(response.downloadUrl.isNotBlank()) { "downloadUrl is missing" }

        return response.downloadUrl
    }
}