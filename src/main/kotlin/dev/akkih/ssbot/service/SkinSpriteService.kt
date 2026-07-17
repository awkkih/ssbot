package dev.akkih.ssbot.service

import dev.akkih.ssbot.Bot.json
import dev.akkih.ssbot.service.models.ConvertOptions
import dev.akkih.ssbot.service.response.ConvertValue
import dev.akkih.ssbot.service.response.SkinConversionResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class SkinSpriteService(private val client: HttpClient) {
    suspend fun convert(skin: ByteArray, options: ConvertOptions = ConvertOptions()): ConvertValue {
        val response = client.submitFormWithBinaryData(
            url = "https://sss.1m3.jp/api/convert",
            formData = formData {
                append("options", json.encodeToString(options))
                append("skin", skin, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=\"skin.png\"")
                })
            }
        ).body<SkinConversionResponse>()

        if (!response.ok) {
            throw IllegalArgumentException(response.error ?: "Unknown error.")
        }

        return response.value ?: throw IllegalArgumentException("Server returned ok but no image data.")
    }
}