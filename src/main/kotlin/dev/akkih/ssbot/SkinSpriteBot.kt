package dev.akkih.ssbot

import dev.akkih.ssbot.command.InfoCommand
import dev.akkih.ssbot.command.SpriteCommand
import dev.akkih.ssbot.command.StatusCommand
import dev.akkih.ssbot.service.CloudflareImageService
import dev.akkih.ssbot.service.HealthCheckService
import dev.akkih.ssbot.service.MinecraftSkinService
import dev.akkih.ssbot.service.SkinSpriteService
import dev.akkih.ssbot.util.Config
import dev.minn.jda.ktx.interactions.commands.*
import dev.minn.jda.ktx.jdabuilder.default
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object Bot {
    val config = Config()

    val client: JDA = default(config.DISCORD_TOKEN)
    val log = LoggerFactory.getLogger(this::class.java) as Logger

    private val customizationOptions: List<OptionData>

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 5_000
            socketTimeoutMillis = 10_000
        }
    }

    val spriteService = SkinSpriteService(httpClient)
    val minecraftService = MinecraftSkinService(httpClient)
    val cloudflareService = CloudflareImageService(httpClient)
    val healthCheckService = HealthCheckService(httpClient)

    init {
        customizationOptions = generateSpriteCustomizationOptions()

        registerCommands()
        registerInteractions()
    }

    private fun generateSpriteCustomizationOptions(): List<OptionData> {
        return listOf(
            Option<String>("arm-model", "The arm model you want your avatar to have. Default: 'Auto'") {
                choice("Auto", "auto")
                choice("Wide", "wide")
                choice("Slim", "slim")
            },
            Option<String>("direction", "The direction you want your avatar to be facing. Default: 'Facing right'") {
                choice("Facing right", "right")
                choice("Facing left", "left")
            },
            Option<String>("pose", "The pose you want your avatar to be. Default: 'Standing'") {
                choice("Standing", "standing")
                choice("Both arms up", "both-up")
                choice("One arm up", "one-up")
                choice("Crouching", "crouching")
                choice("Head only", "head-only")
            },
            Option<String>("head-overlay", "The hat layer you want your avatar to have. Default: '3D'") {
                choice("3D", "outer")
                choice("Flat", "flat")
                choice("Off", "off")
            },
            Option<String>(
                "background-mode",
                "The background mode you want your avatar to be on. Default: 'Transparent'"
            ) {
                choice("Circle", "circle")
                choice("Solid", "solid")
                choice("Transparent", "transparent")
            },
            Option<String>(
                "background-color",
                "Hex color for solid background, e.g. #F8F7F2. Only used when Background Mode is 'Solid'."
            ) {
                setMinLength(4)
                setMaxLength(7)
            },
            Option<Int>("outline", "The outline you want your avatar to have. Default: '0px'") {
                choice("0px", 0)
                choice("1px", 1)
                choice("2px", 2)
            },
            Option<Int>("shadow", "The shadow strength (0-100). Default: '100'") {
                setMinValue(0)
                setMaxValue(100)
            },
        )
    }

    private fun registerCommands() {
        client.updateCommands()
            .slash("sprite", "Convert a Minecraft skin to a cute sprite!") {
                subcommand("file", "Convert a skin from an uploaded PNG.") {
                    option<File>("skin", "The skin in .png format (64x64 or 128x128).", required = true)
                    addOptions(customizationOptions)
                }

                subcommand("username", "Convert a skin by Minecraft username.") {
                    option<String>("username", "The player username. You can put a '.' before the username to fetch from Bedrock.", required = true)
                    addOptions(customizationOptions)
                }
            }
            .slash("status", "Checks the status for the used APIs.")
            .slash("info", "Information about the bot.")
            .queue()
    }

    private fun registerInteractions() {
        SpriteCommand()
        StatusCommand()
        InfoCommand()
    }
}

fun main() {
    Bot
}