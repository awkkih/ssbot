package dev.akkih.ssbot.service.models.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PoseOption {
    @SerialName("standing")
    STANDING,

    @SerialName("both-up")
    BOTH_ARMS_UP,

    @SerialName("one-up")
    ONE_ARM_UP,

    @SerialName("crouching")
    CROUCHING,

    @SerialName("head-only")
    HEAD_ONLY,
}