package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ArmModelOption {
    @SerialName("auto")
    AUTO,

    @SerialName("wide")
    WIDE,

    @SerialName("slim")
    SLIM,
}