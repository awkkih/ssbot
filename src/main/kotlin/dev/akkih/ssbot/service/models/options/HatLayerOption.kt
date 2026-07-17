package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HatLayerOption {
    @SerialName("outer")
    OUTER,

    @SerialName("flat")
    FLAT,

    @SerialName("off")
    OFF,
}