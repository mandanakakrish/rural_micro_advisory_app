package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.engine.GeminiConfig
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.FinancialViabilityEstimate
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotOpportunityBlueText
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted

/**
 * Card managing Gemini AI Real-Time Analysis state, triggering live queries,
 * configuring API key, and presenting AI financial viability projections.
 */
@Composable
fun AiAdvisoryStatusCard(
    report: FeasibilityReport?,
    location: LocationInfo,
    marginCapital: Double,
    isAiLoading: Boolean,
    apiKey: String = "",
    language: LanguageCode,
    onGenerateAiClick: () -> Unit,
    onSaveApiKey: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val effectiveKey = if (apiKey.isNotBlank()) apiKey else GeminiConfig.GEMINI_API_KEY
    val hasKey = effectiveKey.isNotBlank()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(0.5.dp, if (report?.isAiGenerated == true) ForestGreenPrimary else OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Title & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (report?.isAiGenerated == true) ForestGreenPrimary else AmberAccent,
                        modifier = Modifier.size(26.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.title_ai_advisory),
                            style = MaterialTheme.typography.titleMedium,
                            color = ForestGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        // Live Status Badge
                        val isLiveAi = report?.isAiGenerated == true
                        val badgeText = if (isLiveAi) {
                            stringResource(R.string.ai_badge_active)
                        } else if (hasKey) {
                            if (language == LanguageCode.HI) "एआई इंजन तैयार है" else "AI Engine Ready"
                        } else {
                            stringResource(R.string.ai_badge_offline)
                        }
                        val badgeBg = if (isLiveAi || hasKey) SwotStrengthGreenBg else SurfaceCard
                        val badgeColor = if (isLiveAi || hasKey) ForestGreenDark else TextMuted
                        val badgeBorder = if (isLiveAi || hasKey) ForestGreenPrimary else OutlineLight

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeBg)
                                .border(0.5.dp, badgeBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Executive Summary (if provided by AI)
            if (!report?.executiveSummary.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SwotStrengthGreenBg.copy(alpha = 0.6f))
                        .border(0.5.dp, ForestGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ForestGreenDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (language == LanguageCode.HI) "AI त्वरित निष्कर्ष (Executive Verdict)" else "AI Executive Verdict",
                                style = MaterialTheme.typography.labelMedium,
                                color = ForestGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = report?.executiveSummary ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextHighContrast,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // AI Financial Viability Projections (if available)
            report?.financialViability?.let { viability ->
                AiFinancialProjectionsGrid(viability = viability, language = language)
            }

            // Generate / Re-analyze Primary Button
            Button(
                onClick = onGenerateAiClick,
                enabled = !isAiLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreenPrimary,
                    contentColor = SurfaceCream,
                    disabledContainerColor = ForestGreenLight.copy(alpha = 0.4f),
                    disabledContentColor = SurfaceCream.copy(alpha = 0.7f)
                )
            ) {
                if (isAiLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = SurfaceCream,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.btn_analyzing_ai),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = stringResource(R.string.btn_generate_ai),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            val locDisplay = listOf(location.village, location.block, location.district, location.state)
                .filter { it.isNotBlank() }
                .joinToString(", ")
                .ifBlank { if (language == LanguageCode.HI) "स्थान अनिर्दिष्ट" else "Not specified" }

            Text(
                text = if (language == LanguageCode.HI) {
                    "स्थान: $locDisplay | मार्जिन: ₹${marginCapital.toLong()} | रियल-टाइम डेटा पर आधारित"
                } else {
                    "Location: $locDisplay | Margin: ₹${marginCapital.toLong()} | Real-time advisory"
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun AiFinancialProjectionsGrid(
    viability: FinancialViabilityEstimate,
    language: LanguageCode
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.title_ai_projections),
            style = MaterialTheme.typography.titleSmall,
            color = ForestGreenDark,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectionMetricBox(
                title = stringResource(R.string.metric_ai_monthly_revenue),
                value = viability.expectedMonthlyRevenue,
                icon = Icons.Default.TrendingUp,
                tint = ForestGreenPrimary,
                modifier = Modifier.weight(1f)
            )
            ProjectionMetricBox(
                title = stringResource(R.string.metric_ai_monthly_profit),
                value = viability.expectedMonthlyNetProfit,
                icon = Icons.Default.ShowChart,
                tint = AgriTealSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectionMetricBox(
                title = stringResource(R.string.metric_ai_breakeven),
                value = viability.breakevenTimeline,
                icon = Icons.Default.QueryBuilder,
                tint = AmberAccent,
                modifier = Modifier.weight(1f)
            )
            ProjectionMetricBox(
                title = stringResource(R.string.metric_ai_roi),
                value = viability.roiPercentage,
                icon = Icons.Default.AutoAwesome,
                tint = ForestGreenPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProjectionMetricBox(
    title: String,
    value: String,
    icon: ImageVector,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(1.dp, OutlineLight, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMediumContrast,
                    maxLines = 1
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = ForestGreenDark,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

