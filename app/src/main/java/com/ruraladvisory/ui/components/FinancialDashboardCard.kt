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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.ErrorRed
import com.ruraladvisory.ui.theme.ErrorRedLight
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SchemeCappedAmberBg
import com.ruraladvisory.ui.theme.SchemeCappedAmberText
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SchemeMicroGreenText
import com.ruraladvisory.ui.theme.SchemeTermTealBg
import com.ruraladvisory.ui.theme.SchemeTermTealText
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted

/**
 * Visual Financial Dashboard displaying scheme qualification badge,
 * key financial structuring metrics, and cost / OPEX breakdown.
 */
@Composable
fun FinancialDashboardCard(
    financialResult: FinancialCalculationResult?,
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
            when (financialResult) {
                is FinancialCalculationResult.Success -> {
                    // 1. Scheme Qualification Badge
                    SchemeQualificationBadge(
                        scheme = financialResult.scheme,
                        isCapped = financialResult.structure.isCapped,
                        maxCap = financialResult.structure.schemeMaxCap,
                        language = language
                    )

                    // 2. Financial Metrics Grid
                    FinancialMetricsGrid(
                        result = financialResult,
                        language = language
                    )

                    HorizontalDivider(color = OutlineLight, thickness = 0.5.dp)

                    // 3. CAPEX & OPEX Breakdown
                    CostAllocationSection(
                        result = financialResult,
                        language = language
                    )
                }
                is FinancialCalculationResult.BelowMinimumThreshold -> {
                    ErrorThresholdCard(
                        title = if (language == LanguageCode.HI) "न्यूनतम मार्जिन से कम" else "Below Minimum Threshold",
                        description = stringResource(R.string.error_margin_too_low)
                    )
                }
                is FinancialCalculationResult.ExceedsMaximumThreshold -> {
                    ErrorThresholdCard(
                        title = if (language == LanguageCode.HI) "अधिकतम सीमा से अधिक" else "Exceeds Scheme Maximum",
                        description = stringResource(R.string.error_margin_too_high)
                    )
                }
                is FinancialCalculationResult.InvalidInput, null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .border(0.5.dp, OutlineLight, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (language == LanguageCode.HI)
                                    "मार्जिन पूंजी दर्ज करें"
                                else
                                    "Enter Margin Capital",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                            Text(
                                text = if (language == LanguageCode.HI)
                                    "रियायती ऋण संरचना, योजना पात्रता और पुनर्भुगतान अनुसूची देखने के लिए सेटअप में अपनी प्रमोटर मार्जिन पूंजी (न्यूनतम ₹1,000) दर्ज करें।"
                                else
                                    "Enter your available promoter margin capital in the Setup tab (min ₹1,000) to view 90% concessional credit structuring and repayment schedule.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMediumContrast,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SchemeQualificationBadge(
    scheme: SchemeCategory,
    isCapped: Boolean,
    maxCap: Double,
    language: LanguageCode
) {
    val isMicro = scheme == SchemeCategory.MICRO_FINANCE
    val badgeBg = if (isMicro) SchemeMicroGreenBg else SchemeTermTealBg
    val badgeBorder = if (isMicro) ForestGreenPrimary else AgriTealSecondary
    val badgeText = if (isMicro) SchemeMicroGreenText else SchemeTermTealText

    val schemeTitle = if (isMicro) {
        stringResource(R.string.scheme_micro_finance)
    } else {
        stringResource(R.string.scheme_term_loan)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = badgeBorder,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schemeTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = badgeText,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.title_scheme_qualification),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMediumContrast
                )
            }
        }

        // Scheme Key Terms Row (Rate, Tenure, Moratorium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SchemeFeaturePill(
                label = stringResource(R.string.label_interest_rate),
                value = "${String.format(java.util.Locale.US, "%.1f", scheme.annualInterestRate * 100.0)}% p.a.",
                modifier = Modifier.weight(1f)
            )
            SchemeFeaturePill(
                label = stringResource(R.string.label_tenure),
                value = "${scheme.tenureYears} ${stringResource(R.string.years_suffix)}",
                modifier = Modifier.weight(1f)
            )
            SchemeFeaturePill(
                label = stringResource(R.string.label_moratorium),
                value = "${scheme.moratoriumMonths} ${stringResource(R.string.months_suffix)}",
                modifier = Modifier.weight(1f)
            )
        }

        // Warning banner if capped
        if (isCapped) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SchemeCappedAmberBg)
                    .border(1.dp, AmberAccent, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (language == LanguageCode.HI) {
                        "वैधानिक योजना सीमा लागू: पात्र ऋण ₹${TextToSpeechHelper.formatIndianCurrency(maxCap)} पर सीमित है।"
                    } else {
                        "Scheme ceiling enforced: Loan capped at ₹${TextToSpeechHelper.formatIndianCurrency(maxCap)}."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = SchemeCappedAmberText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SchemeFeaturePill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard)
            .border(1.dp, OutlineLight, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FinancialMetricsGrid(
    result: FinancialCalculationResult.Success,
    language: LanguageCode
) {
    val struct = result.structure

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(R.string.title_financial_breakdown),
            style = MaterialTheme.typography.titleSmall,
            color = TextMediumContrast,
            fontWeight = FontWeight.Bold
        )

        // 2-Column Key Financial Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricHighlightCard(
                title = stringResource(R.string.metric_project_cost),
                amount = struct.totalProjectCost,
                highlightColor = ForestGreenPrimary,
                modifier = Modifier.weight(1f)
            )
            MetricHighlightCard(
                title = stringResource(R.string.metric_loan_amount),
                amount = struct.actualLoanAmount,
                highlightColor = AgriTealSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricHighlightCard(
                title = stringResource(R.string.metric_promoter_margin),
                amount = struct.marginCapital,
                highlightColor = AmberAccent,
                modifier = Modifier.weight(1f)
            )
            MetricHighlightCard(
                title = stringResource(R.string.metric_promoter_equity),
                amount = struct.effectivePromoterEquity,
                highlightColor = ForestGreenDark,
                modifier = Modifier.weight(1f)
            )
        }

        // Loan-to-Cost Ratio bar
        val ltcPercent = String.format("%.1f%%", struct.loanToCostRatio * 100.0)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.metric_loan_to_cost),
                style = MaterialTheme.typography.bodySmall,
                color = TextMediumContrast
            )
            Text(
                text = ltcPercent,
                style = MaterialTheme.typography.titleSmall,
                color = ForestGreenPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MetricHighlightCard(
    title: String,
    amount: Double,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(1.dp, OutlineLight, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹${TextToSpeechHelper.formatIndianCurrency(amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = highlightColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CostAllocationSection(
    result: FinancialCalculationResult.Success,
    language: LanguageCode
) {
    val cost = result.costBreakdown

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.title_cost_breakdown),
                style = MaterialTheme.typography.titleSmall,
                color = TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }

        CostBreakdownRow(
            label = stringResource(R.string.cost_capex),
            amount = cost.fixedAssetsCapex
        )
        CostBreakdownRow(
            label = stringResource(R.string.cost_working_capital),
            amount = cost.workingCapital
        )
        CostBreakdownRow(
            label = stringResource(R.string.cost_contingency),
            amount = cost.contingencyBuffer
        )
        CostBreakdownRow(
            label = stringResource(R.string.cost_monthly_opex),
            amount = cost.estimatedMonthlyOpex,
            isHighlighted = true
        )

        // OPEX details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SubCostRow(stringResource(R.string.cost_raw_materials), cost.rawMaterialsOpex)
            SubCostRow(stringResource(R.string.cost_labor), cost.laborWagesOpex)
            SubCostRow(stringResource(R.string.cost_utilities), cost.utilitiesLogisticsOpex)
            SubCostRow(stringResource(R.string.cost_maintenance), cost.maintenanceSundryOpex)
        }
    }
}

@Composable
private fun CostBreakdownRow(
    label: String,
    amount: Double,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlighted) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
            color = if (isHighlighted) ForestGreenDark else TextHighContrast,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "₹${TextToSpeechHelper.formatIndianCurrency(amount)}",
            style = if (isHighlighted) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall,
            color = if (isHighlighted) ForestGreenPrimary else TextHighContrast,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SubCostRow(label: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "• $label",
            style = MaterialTheme.typography.bodySmall,
            color = TextMediumContrast
        )
        Text(
            text = "₹${TextToSpeechHelper.formatIndianCurrency(amount)}",
            style = MaterialTheme.typography.bodySmall,
            color = TextMediumContrast,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ErrorThresholdCard(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ErrorRedLight)
            .border(1.dp, ErrorRed, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = ErrorRed,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextHighContrast
            )
        }
    }
}
