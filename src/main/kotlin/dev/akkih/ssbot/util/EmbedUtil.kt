package dev.akkih.ssbot.util

import dev.minn.jda.ktx.messages.Embed

fun ErrorEmbed(message: String) = Embed {
    title = "An error occurred!"
    description = message
    color = Colors.ERROR
}