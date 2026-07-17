package dev.akkih.ssbot.command

import dev.akkih.ssbot.Bot.client
import dev.akkih.ssbot.util.Colors
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.components.link
import dev.minn.jda.ktx.interactions.components.row
import dev.minn.jda.ktx.messages.Embed

class InfoCommand {
    init {
        client.onCommand("info") { event ->
            event.replyEmbeds(
                Embed {
                    title = "Skin Sprite Bot"
                    description = "Turns Minecraft skins into rendered character sprites."
                    color = Colors.INFO

                    field {
                        name = "🖼️ /sprite file"
                        value = "Upload a `.png` skin (64x64 or 128x128, max 1MB) and get a rendered sprite back."
                        inline = false
                    }

                    field {
                        name = "🎮 /sprite username"
                        value = "Give a Minecraft username and the bot fetches the skin automatically."
                        inline = false
                    }

                    field {
                        name = "📡 /status"
                        value = "Check whether the external services this bot relies on are online."
                        inline = false
                    }

                    field {
                        name = "ℹ️ Credits"
                        value = "Built by **akkih**!\nPowered by [Skin Sprite Studio](https://sss.1m3.jp/) and [MCHeads](https://mcheads.org/)."
                        inline = false
                    }

                    footer {
                        name = "Thanks for using the bot!"
                        iconUrl = event.user.effectiveAvatarUrl
                    }
                }
            ).addComponents(
                row(
                    link("https://sss.1m3.jp/", "Original Website"),
                    link("https://github.com/akkih/ssbot", "GitHub Repository"),
                )
            ).queue()
        }
    }
}