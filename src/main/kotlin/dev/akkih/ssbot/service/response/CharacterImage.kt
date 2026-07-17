package dev.akkih.ssbot.service.response

import kotlinx.serialization.Serializable

@Serializable
data class CharacterImage(
    val width: Int,
    val height: Int,
    val base64: String
)