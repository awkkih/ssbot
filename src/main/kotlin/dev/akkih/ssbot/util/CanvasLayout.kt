package dev.akkih.ssbot.util

import dev.akkih.ssbot.service.models.ConvertOptions
import dev.akkih.ssbot.service.models.options.BackgroundMode
import dev.akkih.ssbot.service.models.options.PoseOption
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

data class CanvasLayout(
    val size: Int,
    val contentWidth: Int,
    val contentHeight: Int,
    val offsetX: Int,
    val offsetY: Int,
)

fun calculateLayout(
    width: Int,
    height: Int,
    backgroundMode: String,
    fit: String,
): CanvasLayout {
    val max = if (backgroundMode.equals("transparent", true)) 792 else 660

    val contentWidth =
        if (fit.equals("width", true))
            max
        else
            (width * max.toDouble() / height).roundToInt()

    val contentHeight =
        if (fit.equals("height", true))
            max
        else
            (height * max.toDouble() / width).roundToInt()

    return CanvasLayout(
        size = 800,
        contentWidth = contentWidth,
        contentHeight = contentHeight,
        offsetX = (800 - contentWidth) / 2,
        offsetY = (800 - contentHeight) / 2,
    )
}

private fun paintBackground(graphics: Graphics2D, width: Int, height: Int, mode: BackgroundMode, hexColor: String) {
    when (mode) {
        BackgroundMode.TRANSPARENT -> return
        BackgroundMode.SOLID -> {
            graphics.color = Color.decode(hexColor)
            graphics.fillRect(0, 0, width, height)
        }
        BackgroundMode.CIRCLE -> {
            graphics.color = Color.decode(hexColor)
            graphics.fill(Ellipse2D.Double(0.0, 0.0, width.toDouble(), height.toDouble()))
        }
    }
}

fun renderPreview(character: BufferedImage, options: ConvertOptions): ByteArray {
    val fit = if (options.pose == PoseOption.HEAD_ONLY) "width" else "height"

    val layout = calculateLayout(
        width = character.width,
        height = character.height,
        backgroundMode = options.background.mode.name.lowercase(),
        fit = fit,
    )

    val canvas = BufferedImage(layout.size, layout.size, BufferedImage.TYPE_INT_ARGB)
    val graphics = canvas.createGraphics()

    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)

    paintBackground(graphics, layout.size, layout.size, options.background.mode, options.background.color)

    graphics.drawImage(
        character,
        layout.offsetX, layout.offsetY,
        layout.offsetX + layout.contentWidth, layout.offsetY + layout.contentHeight,
        0, 0, character.width, character.height,
        null
    )
    graphics.dispose()

    val output = ByteArrayOutputStream()
    ImageIO.write(canvas, "png", output)

    return output.toByteArray()
}

fun decodeBase64Image(base64: String): BufferedImage {
    val bytes = Base64.getDecoder().decode(base64)

    return ImageIO.read(ByteArrayInputStream(bytes))
        ?: throw IllegalArgumentException("Failed to decode character image.")
}