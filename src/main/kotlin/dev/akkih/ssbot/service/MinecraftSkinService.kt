package dev.akkih.ssbot.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class MinecraftSkinService(private val client: HttpClient) {
    suspend fun fetchSkin(username: String): ByteArray {
        val response = client.get("https://api.mcheads.org/skin/$username")

        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Failed to fetch skin: $username. API is probably down.")
        }

        val contentType = response.contentType()
        if (contentType == null || !contentType.match(ContentType.Image.PNG)) {
            throw IllegalArgumentException("Unexpected response fetching skin for: $username")
        }

        return response.body()
    }
}