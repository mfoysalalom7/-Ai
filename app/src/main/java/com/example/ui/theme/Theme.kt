package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Romantic Material 3 Color Schemes:
 * - Soft pinks (blush, petal pink, rose water)
 * - Deep & vibrant reds (ruby red, crimson, velvet wine)
 * - Warm neutrals (warm ivory, alabaster, cashmere taupe, linen)
 */
val RomanticLightColorScheme = lightColorScheme(
    primary = RomanticPrimaryLight,
    onPrimary = RomanticOnPrimaryLight,
    primaryContainer = RomanticPrimaryContainerLight,
    onPrimaryContainer = RomanticOnPrimaryContainerLight,
    secondary = RomanticSecondaryLight,
    onSecondary = RomanticOnSecondaryLight,
    secondaryContainer = RomanticSecondaryContainerLight,
    onSecondaryContainer = RomanticOnSecondaryContainerLight,
    tertiary = RomanticTertiaryLight,
    onTertiary = RomanticOnTertiaryLight,
    tertiaryContainer = RomanticTertiaryContainerLight,
    onTertiaryContainer = RomanticOnTertiaryContainerLight,
    error = RomanticErrorLight,
    onError = RomanticOnErrorLight,
    errorContainer = RomanticErrorContainerLight,
    onErrorContainer = RomanticOnErrorContainerLight,
    background = RomanticBackgroundLight,
    onBackground = RomanticOnBackgroundLight,
    surface = RomanticSurfaceLight,
    onSurface = RomanticOnSurfaceLight,
    surfaceVariant = RomanticSurfaceVariantLight,
    onSurfaceVariant = RomanticOnSurfaceVariantLight,
    outline = RomanticOutlineLight,
    outlineVariant = RomanticOutlineVariantLight
)

val RomanticDarkColorScheme = darkColorScheme(
    primary = RomanticPrimaryDark,
    onPrimary = RomanticOnPrimaryDark,
    primaryContainer = RomanticPrimaryContainerDark,
    onPrimaryContainer = RomanticOnPrimaryContainerDark,
    secondary = RomanticSecondaryDark,
    onSecondary = RomanticOnSecondaryDark,
    secondaryContainer = RomanticSecondaryContainerDark,
    onSecondaryContainer = RomanticOnSecondaryContainerDark,
    tertiary = RomanticTertiaryDark,
    onTertiary = RomanticOnTertiaryDark,
    tertiaryContainer = RomanticTertiaryContainerDark,
    onTertiaryContainer = RomanticOnTertiaryContainerDark,
    error = RomanticErrorDark,
    onError = RomanticOnErrorDark,
    errorContainer = RomanticErrorContainerDark,
    onErrorContainer = RomanticOnErrorContainerDark,
    background = RomanticBackgroundDark,
    onBackground = RomanticOnBackgroundDark,
    surface = RomanticSurfaceDark,
    onSurface = RomanticOnSurfaceDark,
    surfaceVariant = RomanticSurfaceVariantDark,
    onSurfaceVariant = RomanticOnSurfaceVariantDark,
    outline = RomanticOutlineDark,
    outlineVariant = RomanticOutlineVariantDark
)

@Composable
fun RomanticTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep the romantic palette consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> RomanticDarkColorScheme
        else -> RomanticLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RomanticShapes,
        content = content
    )
}

@Composable
fun CherryAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = RomanticTheme(darkTheme, dynamicColor, content)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = RomanticTheme(darkTheme, dynamicColor, content)
