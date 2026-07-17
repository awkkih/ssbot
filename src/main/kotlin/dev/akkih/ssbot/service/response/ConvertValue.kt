package dev.akkih.ssbot.service.response

import kotlinx.serialization.Serializable

@Serializable
data class ConvertValue(
    val skin: SkinInfo,
    val character: CharacterImage,
)