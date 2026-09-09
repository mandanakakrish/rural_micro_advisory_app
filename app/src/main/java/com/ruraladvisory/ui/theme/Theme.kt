package com.ruraladvisory.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RuralLightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = SurfaceCream,
    primaryContainer = ForestGreenLight,
    onPrimaryContainer = ForestGreenDark,
    secondary = AgriTealSecondary,
    onSecondary = SurfaceCream,
    secondaryContainer = AgriTealLight,
    onSecondaryContainer = AgriTealSecondary,
    tertiary = AmberAccent,
    onTertiary = SurfaceCream,
    background = WarmCreamBackground,
    onBackground = TextHighContrast,
    surface = SurfaceCream,
    onSurface = TextHighContrast,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = TextMediumContrast,
    outline = OutlineWarm,
    outlineVariant = OutlineLight,
    error = ErrorRed,
    onError = SurfaceCream,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed
)

private val RuralDarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = ForestGreenDark,
    secondary = AgriTealLight,
    onSecondary = AgriTealSecondary,
    tertiary = GoldAccent,
    background = Color(0xFF121411),
    onBackground = Color(0xFFE2E3DE),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DE),
    surfaceVariant = Color(0xFF424940),
    onSurfaceVariant = Color(0xFFC2C9BD),
    outline = Color(0xFF8C9388),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun RuralAdvisoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For rural outdoor readability, we prioritize the high-contrast light scheme
    val colorScheme = if (darkTheme) RuralDarkColorScheme else RuralLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                // Dark icons on light background (WarmCream) for maximum contrast
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RuralTypography,
        content = content
    )
}
