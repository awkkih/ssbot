package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.Serializable

@Serializable
data class Background(
    val mode: BackgroundMode = BackgroundMode.TRANSPARENT,
    val color: String = "#FFFFFF",
)