package dev.akkih.ssbot.service

import dev.akkih.ssbot.Bot.config
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.*

class HealthCheckService(private val client: HttpClient) {
    suspend fun mcHeadsStatus(): Boolean {
        return try {
            val response = client.get("https://api.mcheads.org/health")

            response.status.value < 400
        } catch (ex: CancellationException) {
            throw ex
        } catch (_: Exception) {
            false
        }
    }

    suspend fun skinSpriteStudioStatus(): Boolean {
        return try {
            val response = client.post("https://sss.1m3.jp/api/convert")

            response.status.value in 400..499
        } catch (ex: CancellationException) {
            throw ex
        } catch (_: Exception) {
            false
        }
    }

    suspend fun cloudflareWorkersStatus(): Boolean {
        return try {
            val response = client.get(config.WORKER_BASE_URL)

            response.status.value == 200
        } catch (ex: CancellationException) {
            throw ex
        } catch (_: Exception) {
            false
        }
    }
}