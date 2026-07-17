package dev.akkih.ssbot.service.response

import kotlinx.serialization.Serializable

@Serializable
data class SkinConversionResponse(
    val ok: Boolean,
    val error: String? = null,
    val value: ConvertValue? = null,
)