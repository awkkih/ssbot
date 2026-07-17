package dev.akkih.ssbot.service.models

import dev.akkih.ssbot.service.models.options.*
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

@Serializable
data class ConvertOptions(
    val model: ArmModelOption = ArmModelOption.AUTO,
    val pose: PoseOption = PoseOption.STANDING,
    val view: DirectionOption = DirectionOption.RIGHT,
    val hatLayer: HatLayerOption = HatLayerOption.OUTER,
    val background: Background = Background(),
    val outline: Int = 0,
    val shading: Int = 100,
) {
    companion object {
        fun of(event: GenericCommandInteractionEvent) = ConvertOptions(
            model = parseArmModel(event.getOption("arm-model")?.asString),
            view = parseView(event.getOption("direction")?.asString),
            pose = parsePose(event.getOption("pose")?.asString),
            hatLayer = parseHatLayer(event.getOption("head-overlay")?.asString),
            background = Background(
                mode = parseBackgroundMode(event.getOption("background-mode")?.asString),
                color = parseBackgroundColor(event.getOption("background-color")?.asString),
            ),
            outline = event.getOption("outline")?.asInt ?: 0,
            shading = event.getOption("shadow")?.asInt ?: 100
        )
    }
}

private fun parseArmModel(value: String?) = when (value) {
    "wide" -> ArmModelOption.WIDE
    "slim" -> ArmModelOption.SLIM
    else -> ArmModelOption.AUTO
}

private fun parseView(value: String?) = when (value) {
    "left" -> DirectionOption.LEFT
    else -> DirectionOption.RIGHT
}

private fun parsePose(value: String?) = when (value) {
    "both-up" -> PoseOption.BOTH_ARMS_UP
    "one-up" -> PoseOption.ONE_ARM_UP
    "crouching" -> PoseOption.CROUCHING
    "head-only" -> PoseOption.HEAD_ONLY
    else -> PoseOption.STANDING
}

private fun parseHatLayer(value: String?) = when (value) {
    "flat" -> HatLayerOption.FLAT
    "off" -> HatLayerOption.OFF
    else -> HatLayerOption.OUTER
}

private fun parseBackgroundMode(value: String?) = when (value) {
    "circle" -> BackgroundMode.CIRCLE
    "solid" -> BackgroundMode.SOLID
    else -> BackgroundMode.TRANSPARENT
}

private val colorRegex = Regex("^#[0-9a-fA-F]{6}$")
private fun parseBackgroundColor(value: String?): String {
    if (value.isNullOrBlank()) return "#F8F7F2"

    val normalized = if (value.startsWith("#")) value else "#$value"

    return if (colorRegex.matches(normalized)) {
        normalized
    } else {
        "#F8F7F2"
    }
}