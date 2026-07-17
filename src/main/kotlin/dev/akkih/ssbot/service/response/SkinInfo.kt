package dev.akkih.ssbot.service.response

import kotlinx.serialization.Serializable

@Serializable
data class SkinInfo(
    val width: Int,
    val height: Int,
    val detectedModel: String,
)