package dev.akkih.ssbot.command

import dev.akkih.ssbot.Bot.client
import dev.akkih.ssbot.Bot.cloudflareService
import dev.akkih.ssbot.Bot.log
import dev.akkih.ssbot.Bot.minecraftService
import dev.akkih.ssbot.Bot.spriteService
import dev.akkih.ssbot.service.models.ConvertOptions
import dev.akkih.ssbot.util.*
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.components.link
import dev.minn.jda.ktx.jdabuilder.scope
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.into
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

class SpriteCommand {
    init {
        client.onCommand("sprite") { event ->
            event.deferReply().queue()

            val start = System.nanoTime()

            client.scope.launch {
                try {
                    val skinBytes = generateSkinBytes(event)

                    val dimensionError = validateSkinDimensions(skinBytes)
                    if (dimensionError != null) {
                        userError(dimensionError)
                    }

                    val customizationOptions = ConvertOptions.of(event)

                    val serviceResponse = spriteService.convert(skinBytes, customizationOptions)
                    val characterImage = decodeBase64Image(serviceResponse.character.base64)
                    val resizedImageBytes = renderPreview(characterImage, customizationOptions)

                    val cloudflareDownloadUrl = cloudflareService.uploadFile(resizedImageBytes)
                    val discordUpload = FileUpload.fromData(resizedImageBytes, "resized.png")

                    val took = (System.nanoTime() - start) / 1_000_000_000.0

                    val embed = Embed {
                        title = "Avatar generated!"
                        image = "attachment://resized.png"
                        color = Colors.OK

                        footer {
                            name = "Rendered in ${"%.2f".format(took)} s - 800x800"
                            iconUrl = event.user.effectiveAvatarUrl
                        }
                    }

                    event.hook.editOriginalEmbeds(embed)
                        .setAttachments(discordUpload)
                        .setComponents(link(cloudflareDownloadUrl, "Download").into())
                        .queue()
                }
                catch (_: CancellationException) {}
                catch (ex: UserFacingError) {
                    event.hook.editOriginalEmbeds(ErrorEmbed(ex.message ?: "Unknown error.")).queue()
                }
                catch (ex: Exception) {
                    log.error("Failed to convert skin", ex)
                    event.hook.editOriginalEmbeds(ErrorEmbed(ex.message ?: "Unknown error.")).queue()
                }
            }
        }
    }

    // Utility
    private suspend fun generateSkinBytes(event: GenericCommandInteractionEvent): ByteArray {
        when (event.subcommandName) {
            "file" -> {
                val attachment = event.getOption("skin")?.asAttachment!!

                if (!attachment.fileExtension.equals("png", ignoreCase = true)) {
                    userError("Please upload a .png file.")
                }

                if (attachment.size > 1_000_000) {
                    userError("File is too large, max 1MB.")
                }

                return attachment.proxy.download().await().use { it.readBytes() }
            }

            "username" -> {
                val username = event.getOption("username")?.asString!!
                return minecraftService.fetchSkin(username)
            }

            else -> {
                userError("Unhandled command: ${event.subcommandName}")
            }
        }
    }

    private suspend fun validateSkinDimensions(skinBytes: ByteArray): String? {
        return withContext(Dispatchers.IO) {
            val image = try {
                ImageIO.read(ByteArrayInputStream(skinBytes))
            } catch (ex: Exception) {
                log.warn("Failed to read PNG for the dimension check.", ex)
                null
            } ?: return@withContext "Couldn't read that file as a PNG image."

            val width = image.width
            val height = image.height

            val isValid = (width == 64 && height == 64) || (width == 128 && height == 128)

            return@withContext if (!isValid) "Invalid skin size: ${width}x${height}. Please upload a 64x64 or 128x128 .png skin." else null
        }
    }
}