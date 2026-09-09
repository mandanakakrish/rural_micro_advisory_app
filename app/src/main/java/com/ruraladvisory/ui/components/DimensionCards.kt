package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.ErrorRed
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotOpportunityBlueText
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast

/**
 * Visual Cards displaying the 6 analytical feasibility dimensions
 * and strategic recommendations for rural micro-enterprises.
 */
@Composable
fun DimensionCards(
    report: FeasibilityReport?,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    if (report == null) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        Text(
            text = stringResource(R.string.title_feasibility),
            style = MaterialTheme.typography.titleLarge,
            color = ForestGreenDark,
            fontWeight = FontWeight.Bold
        )

        // 1. Market Reach & Distribution
        SingleDimensionCard(
            title = MultilingualDictionary.getDimensionTitle(MultilingualDictionary.DIMENSION_MARKET_REACH, language),
            content = report.marketReach,
            icon = Icons.Default.ShareLocation,
            iconTint = ForestGreenPrimary
        )

        // 2. Opportunity & Unserved Niches
        SingleDimensionCard(
            title = MultilingualDictionary.getDimensionTitle(MultilingualDictionary.DIMENSION_OPPORTUNITY, language),
            content = report.opportunityAnalysis,
            icon = Icons.Default.Lightbulb,
            iconTint = AmberAccent
        )

        // 3. Competitor Mapping & Density
        SingleDimensionCard(
            title = MultilingualDictionary.getDimensionTitle(MultilingualDictionary.DIMENSION_COMPETITOR, language),
            content = report.competitorMapping,
            icon = Icons.Default.Groups,
            iconTint = AgriTealSecondary
        )

        // 4. Product Valuation & Pricing Strategy
        SingleDimensionCard(
            title = MultilingualDictionary.getDimensionTitle(MultilingualDictionary.DIMENSION_PRICING, language),
            content = report.pricingStrategy,
            icon = Icons.Default.CurrencyRupee,
            iconTint = ForestGreenPrimary
        )

        // 5. Threats Identification & Mitigation
        ThreatsDimensionCard(
            title = MultilingualDictionary.getDimensionTitle(MultilingualDictionary.DIMENSION_THREATS, language),
            threats = report.threatsIdentification
        )

        // 6. Strategic Recommendations
        RecommendationsCard(
            title = MultilingualDictionary.getRecommendationsHeader(language),
            recommendations = report.keyRecommendations
        )
    }
}

@Composable
private fun SingleDimensionCard(
    title: String,
    content: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(0.5.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMediumContrast,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun ThreatsDimensionCard(
    title: String,
    threats: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(0.5.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ErrorRed.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
            }

            threats.forEach { threat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CrisisAlert,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = threat,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHighContrast
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationsCard(
    title: String,
    recommendations: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SwotStrengthGreenBg),
        border = BorderStroke(0.5.dp, ForestGreenPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AssignmentTurnedIn,
                    contentDescription = null,
                    tint = ForestGreenDark,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = ForestGreenDark,
                    fontWeight = FontWeight.Bold
                )
            }

            recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✔",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ForestGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rec,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextHighContrast
                    )
                }
            }
        }
    }
}
