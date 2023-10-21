package org.jetbrains.jewel.bridge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.jewel.ThemeColorPalette
import java.util.TreeMap

private val logger = Logger.getInstance("BridgeThemeColorPalette")

val ThemeColorPalette.windowsPopupBorder
    get() = lookup("windowsPopupBorder")

fun ThemeColorPalette.Companion.readFromLaF(): ThemeColorPalette {
    val grey = readPaletteColors("Grey")
    val blue = readPaletteColors("Blue")
    val green = readPaletteColors("Green")
    val red = readPaletteColors("Red")
    val yellow = readPaletteColors("Yellow")
    val orange = readPaletteColors("Orange")
    val purple = readPaletteColors("Purple")
    val teal = readPaletteColors("Teal")
    val windowsPopupBorder = readPaletteColor("windowsPopupBorder")

    val rawMap = buildMap<String, Color> {
        putAll(grey)
        putAll(blue)
        putAll(green)
        putAll(red)
        putAll(yellow)
        putAll(orange)
        putAll(purple)
        putAll(teal)
        if (windowsPopupBorder.isSpecified) put("windowsPopupBorder", windowsPopupBorder)
    }

    return ThemeColorPalette(
        grey = grey.values.toList(),
        blue = blue.values.toList(),
        green = green.values.toList(),
        red = red.values.toList(),
        yellow = yellow.values.toList(),
        orange = orange.values.toList(),
        purple = purple.values.toList(),
        teal = teal.values.toList(),
        rawMap = rawMap,
    )
}

private fun readPaletteColors(colorName: String): Map<String, Color> {
    val defaults = uiDefaults
    val allKeys = defaults.keys
    val colorNameKeyPrefix = "ColorPalette.$colorName"
    val colorNameKeyPrefixLength = colorNameKeyPrefix.length

    val lastColorIndex = allKeys.asSequence()
        .filterIsInstance(String::class.java)
        .filter { it.startsWith(colorNameKeyPrefix) }
        .mapNotNull {
            val afterName = it.substring(colorNameKeyPrefixLength)
            afterName.toIntOrNull()
        }
        .maxOrNull() ?: return TreeMap()

    return buildMap {
        for (i in 1..lastColorIndex) {
            val key = "$colorNameKeyPrefix$i"
            val value = defaults[key] as? java.awt.Color
            if (value == null) {
                logger.error("Unable to find color value for palette key '$colorNameKeyPrefix$i'")
                continue
            }

            put(key, value.toComposeColor())
        }
    }
}

private fun readPaletteColor(colorName: String): Color {
    val defaults = uiDefaults
    val colorNameKey = "ColorPalette.$colorName"
    return (defaults[colorNameKey] as? java.awt.Color)
        ?.toComposeColor() ?: Color.Unspecified
}
