package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.SwotAnalysis
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotOpportunityBlueBg
import com.ruraladvisory.ui.theme.SwotOpportunityBlueBorder
import com.ruraladvisory.ui.theme.SwotOpportunityBlueText
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.SwotStrengthGreenBorder
import com.ruraladvisory.ui.theme.SwotStrengthGreenText
import com.ruraladvisory.ui.theme.SwotThreatRedBg
import com.ruraladvisory.ui.theme.SwotThreatRedBorder
import com.ruraladvisory.ui.theme.SwotThreatRedText
import com.ruraladvisory.ui.theme.SwotWeaknessAmberBg
import com.ruraladvisory.ui.theme.SwotWeaknessAmberBorder
import com.ruraladvisory.ui.theme.SwotWeaknessAmberText
import com.ruraladvisory.ui.theme.TextHighContrast

/**
 * 2x2 color-coded SWOT grid tailored for rural micro-entrepreneurs.
 * Displays Strengths (Green), Weaknesses (Amber), Opportunities (Blue), and Threats (Red).
 */
@Composable
fun SwotMatrixGrid(
    swotAnalysis: SwotAnalysis?,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(0.5.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    tint = ForestGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.title_swot),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
            }

            if (swotAnalysis != null) {
                // Top Row: Strengths & Weaknesses
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SwotQuadrantCard(
                        title = MultilingualDictionary.getSwotQuadrantTitle("STRENGTHS", language),
                        icon = Icons.Default.CheckCircle,
                        items = swotAnalysis.strengths,
                        bgColor = SwotStrengthGreenBg,
                        borderColor = SwotStrengthGreenBorder,
                        titleColor = SwotStrengthGreenText,
                        bulletSymbol = "+",
                        modifier = Modifier.weight(1f)
                    )

                    SwotQuadrantCard(
                        title = MultilingualDictionary.getSwotQuadrantTitle("WEAKNESSES", language),
                        icon = Icons.Default.Warning,
                        items = swotAnalysis.weaknesses,
                        bgColor = SwotWeaknessAmberBg,
                        borderColor = SwotWeaknessAmberBorder,
                        titleColor = SwotWeaknessAmberText,
                        bulletSymbol = "-",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Bottom Row: Opportunities & Threats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SwotQuadrantCard(
                        title = MultilingualDictionary.getSwotQuadrantTitle("OPPORTUNITIES", language),
                        icon = Icons.Default.Lightbulb,
                        items = swotAnalysis.opportunities,
                        bgColor = SwotOpportunityBlueBg,
                        borderColor = SwotOpportunityBlueBorder,
                        titleColor = SwotOpportunityBlueText,
                        bulletSymbol = "*",
                        modifier = Modifier.weight(1f)
                    )

                    SwotQuadrantCard(
                        title = MultilingualDictionary.getSwotQuadrantTitle("THREATS", language),
                        icon = Icons.Default.Shield,
                        items = swotAnalysis.threats,
                        bgColor = SwotThreatRedBg,
                        borderColor = SwotThreatRedBorder,
                        titleColor = SwotThreatRedText,
                        bulletSymbol = "!",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SwotQuadrantCard(
    title: String,
    icon: ImageVector,
    items: List<String>,
    bgColor: Color,
    borderColor: Color,
    titleColor: Color,
    bulletSymbol: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(0.5.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = titleColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = titleColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            items.forEach { item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = bulletSymbol,
                        style = MaterialTheme.typography.bodySmall,
                        color = titleColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHighContrast
                    )
                }
            }
        }
    }
}
