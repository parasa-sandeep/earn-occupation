package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Frosted Glass Base Palette
val FrostedDarkBg = Color(0xFF0F172A)
val FrostedIndigoDark = Color(0xFF1E1B4B)
val FrostedIndigoMesh = Color(0xFF312E81)
val FrostedPurpleMesh = Color(0xFF4C1D95)
val FrostedBlueMesh = Color(0xFF1E40AF)
val FrostedPinkMesh = Color(0xFF701A75)

// Glass Surface & Translucent Colors
val GlassCardBg = Color(0x1CFFFFFF) // Translucent white (~11% opacity)
val GlassCardBgHover = Color(0x2EFFFFFF) // Translucent white (~18% opacity)
val GlassCardBorder = Color(0x33FFFFFF) // Translucent subtle border (~20% opacity)
val GlassCardBorderGlow = Color(0x66A5B4FC) // Indigo glowing border (~40% opacity)

// Accents
val Indigo300 = Color(0xFFA5B4FC)
val Indigo200 = Color(0xFFC7D2FE)
val Indigo100 = Color(0xFFE0E7FF)
val Purple300 = Color(0xFFD8B4FE)
val CyanAccent = Color(0xFF38BDF8)
val EmeraldGreen = Color(0xFF34D399)
val AccentGold = Color(0xFFFBBF24)

// Legacy alias mappings for backward compatibility
val Navy900 = FrostedDarkBg
val Navy800 = Color(0xFF1E293B)
val Navy700 = Color(0xFF334155)
val SlateLight = Color(0xFFF8FAFC)
val SlateCard = GlassCardBg
val SlateBorder = GlassCardBorder
val DarkSurface = FrostedDarkBg
val DarkCardSurface = GlassCardBg

// Mesh & Frosted Brushes
val MeshGradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E1B4B),
        Color(0xFF2A085C),
        Color(0xFF0F172A)
    )
)

val GlassAccentGradient = Brush.horizontalGradient(
    colors = listOf(Indigo300, Purple300)
)

