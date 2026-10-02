package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Romantic Color Palette for Cherry AI:
 * - Soft Pinks (Blush, Petal, Silk Rose)
 * - Deep Reds (Ruby, Crimson, Bordeaux)
 * - Warm Neutrals (Warm Ivory, Linen, Alabaster, Cashmere Taupe)
 */
object RomanticPalette {
    // Soft Pinks
    val SoftPink = Color(0xFFF8BBD0)
    val PetalBlush = Color(0xFFFFD9E2)
    val SilkRose = Color(0xFFFCE4EC)
    val RoseWater = Color(0xFFFFF0F3)
    val RomanticPeach = Color(0xFFFFDAD9)

    // Romantic Reds & Crimsons
    val RubyRed = Color(0xFFB81D4C)
    val CrimsonRose = Color(0xFFC2185B)
    val DeepVelvet = Color(0xFF880E4F)
    val BordeauxWine = Color(0xFF650025)
    val CoralRose = Color(0xFFE53935)

    // Warm Neutrals
    val WarmIvory = Color(0xFFFFFBF9)
    val WarmAlabaster = Color(0xFFFFF8F7)
    val WarmLinen = Color(0xFFFBF0EF)
    val WarmCashmere = Color(0xFFF3DDE0)
    val CashmereTaupe = Color(0xFF847375)
    val WarmMocha = Color(0xFF524345)
    val WarmEspresso = Color(0xFF23191B)
    val VelvetNoir = Color(0xFF1E1416)
}

// Material 3 Light Romantic Scheme Tokens
val RomanticPrimaryLight = RomanticPalette.RubyRed
val RomanticOnPrimaryLight = Color(0xFFFFFFFF)
val RomanticPrimaryContainerLight = RomanticPalette.PetalBlush
val RomanticOnPrimaryContainerLight = Color(0xFF3F0013)

val RomanticSecondaryLight = Color(0xFF984061)
val RomanticOnSecondaryLight = Color(0xFFFFFFFF)
val RomanticSecondaryContainerLight = Color(0xFFFFD9E2)
val RomanticOnSecondaryContainerLight = Color(0xFF3E001F)

val RomanticTertiaryLight = Color(0xFF9E4343)
val RomanticOnTertiaryLight = Color(0xFFFFFFFF)
val RomanticTertiaryContainerLight = RomanticPalette.RomanticPeach
val RomanticOnTertiaryContainerLight = Color(0xFF410005)

val RomanticErrorLight = Color(0xFFBA1A1A)
val RomanticOnErrorLight = Color(0xFFFFFFFF)
val RomanticErrorContainerLight = Color(0xFFFFDAD6)
val RomanticOnErrorContainerLight = Color(0xFF410002)

val RomanticBackgroundLight = RomanticPalette.WarmAlabaster
val RomanticOnBackgroundLight = RomanticPalette.WarmEspresso
val RomanticSurfaceLight = RomanticPalette.WarmAlabaster
val RomanticOnSurfaceLight = RomanticPalette.WarmEspresso
val RomanticSurfaceVariantLight = RomanticPalette.WarmCashmere
val RomanticOnSurfaceVariantLight = RomanticPalette.WarmMocha
val RomanticOutlineLight = RomanticPalette.CashmereTaupe
val RomanticOutlineVariantLight = Color(0xFFD6C1C4)

// Material 3 Dark Romantic Scheme Tokens
val RomanticPrimaryDark = Color(0xFFFFB2BF)
val RomanticOnPrimaryDark = Color(0xFF650025)
val RomanticPrimaryContainerDark = Color(0xFF8F0038)
val RomanticOnPrimaryContainerDark = RomanticPalette.PetalBlush

val RomanticSecondaryDark = Color(0xFFFFAEB9)
val RomanticOnSecondaryDark = Color(0xFF5D1133)
val RomanticSecondaryContainerDark = Color(0xFF7A2949)
val RomanticOnSecondaryContainerDark = Color(0xFFFFD9E2)

val RomanticTertiaryDark = Color(0xFFFFB3B3)
val RomanticOnTertiaryDark = Color(0xFF5F1319)
val RomanticTertiaryContainerDark = Color(0xFF7F2B30)
val RomanticOnTertiaryContainerDark = Color(0xFFFFDAD9)

val RomanticErrorDark = Color(0xFFFFB4AB)
val RomanticOnErrorDark = Color(0xFF690005)
val RomanticErrorContainerDark = Color(0xFF93000A)
val RomanticOnErrorContainerDark = Color(0xFFFFDAD6)

val RomanticBackgroundDark = RomanticPalette.VelvetNoir
val RomanticOnBackgroundDark = Color(0xFFEFE0E1)
val RomanticSurfaceDark = RomanticPalette.VelvetNoir
val RomanticOnSurfaceDark = Color(0xFFEFE0E1)
val RomanticSurfaceVariantDark = RomanticPalette.WarmMocha
val RomanticOnSurfaceVariantDark = Color(0xFFD6C1C4)
val RomanticOutlineDark = Color(0xFF9F8C8F)
val RomanticOutlineVariantDark = RomanticPalette.WarmMocha

// Backwards-compatible aliases
val CherryPrimaryLight = RomanticPrimaryLight
val CherryOnPrimaryLight = RomanticOnPrimaryLight
val CherryPrimaryContainerLight = RomanticPrimaryContainerLight
val CherryOnPrimaryContainerLight = RomanticOnPrimaryContainerLight

val CherrySecondaryLight = RomanticSecondaryLight
val CherryOnSecondaryLight = RomanticOnSecondaryLight
val CherrySecondaryContainerLight = RomanticSecondaryContainerLight
val CherryOnSecondaryContainerLight = RomanticOnSecondaryContainerLight

val CherryTertiaryLight = RomanticTertiaryLight
val CherryOnTertiaryLight = RomanticOnTertiaryLight
val CherryTertiaryContainerLight = RomanticTertiaryContainerLight
val CherryOnTertiaryContainerLight = RomanticOnTertiaryContainerLight

val CherryBackgroundLight = RomanticBackgroundLight
val CherryOnBackgroundLight = RomanticOnBackgroundLight
val CherrySurfaceLight = RomanticSurfaceLight
val CherryOnSurfaceLight = RomanticOnSurfaceLight
val CherrySurfaceVariantLight = RomanticSurfaceVariantLight
val CherryOnSurfaceVariantLight = RomanticOnSurfaceVariantLight

val CherryPrimaryDark = RomanticPrimaryDark
val CherryOnPrimaryDark = RomanticOnPrimaryDark
val CherryPrimaryContainerDark = RomanticPrimaryContainerDark
val CherryOnPrimaryContainerDark = RomanticOnPrimaryContainerDark

val CherrySecondaryDark = RomanticSecondaryDark
val CherryOnSecondaryDark = RomanticOnSecondaryDark
val CherrySecondaryContainerDark = RomanticSecondaryContainerDark
val CherryOnSecondaryContainerDark = RomanticOnSecondaryContainerDark

val CherryBackgroundDark = RomanticBackgroundDark
val CherryOnBackgroundDark = RomanticOnBackgroundDark
val CherrySurfaceDark = RomanticSurfaceDark
val CherryOnSurfaceDark = RomanticOnSurfaceDark
val CherrySurfaceVariantDark = RomanticSurfaceVariantDark
val CherryOnSurfaceVariantDark = RomanticOnSurfaceVariantDark
