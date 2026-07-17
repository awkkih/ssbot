package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackgroundMode {
    @SerialName("transparent")
    TRANSPARENT,

    @SerialName("solid")
    SOLID,

    @SerialName("circle")
    CIRCLE,
}