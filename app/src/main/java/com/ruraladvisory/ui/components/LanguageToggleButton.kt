package com.ruraladvisory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast

/**
 * Accessible in-app vernacular language toggle between English and Hindi.
 * Features large touch targets (48dp+) and high contrast visual feedback.
 */
@Composable
fun LanguageToggleButton(
    currentLanguage: LanguageCode,
    onLanguageSelected: (LanguageCode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(2.dp, ForestGreenPrimary, RoundedCornerShape(24.dp)),
        color = SurfaceCream,
        shadowElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageToggleSegment(
                title = "English",
                isSelected = currentLanguage == LanguageCode.EN,
                onClick = { onLanguageSelected(LanguageCode.EN) }
            )
            LanguageToggleSegment(
                title = "हिंदी",
                isSelected = currentLanguage == LanguageCode.HI,
                onClick = { onLanguageSelected(LanguageCode.HI) }
            )
        }
    }
}

@Composable
private fun LanguageToggleSegment(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) ForestGreenPrimary else Color.Transparent
    val textColor = if (isSelected) SurfaceCream else TextHighContrast
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(bgColor)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = fontWeight
        )
    }
}
