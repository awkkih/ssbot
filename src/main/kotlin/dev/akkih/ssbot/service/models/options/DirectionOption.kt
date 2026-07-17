package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DirectionOption {
    @SerialName("right")
    RIGHT,

    @SerialName("left")
    LEFT,
}