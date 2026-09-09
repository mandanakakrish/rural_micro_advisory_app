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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.BusinessHealthReport
import com.ruraladvisory.finance.model.CashFlowStatus
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.AmberLight
import com.ruraladvisory.ui.theme.ErrorRed
import com.ruraladvisory.ui.theme.ErrorRedLight
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SchemeMicroGreenText
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground

/**
 * Business Intelligence & Cash-Flow Diagnostic Card (Section 8)
 * converting raw operational figures (Revenue -> OpEx -> Loan Outflow -> Net Profit)
 * into understandable decisions, solvency status, and operational health scoring.
 */
@Composable
fun BusinessHealthCard(
    report: BusinessHealthReport,
    language: LanguageCode,
    monthlyRevenue: Double = 0.0,
    monthlyExpenses: Double = 0.0,
    onRevenueChange: ((Double) -> Unit)? = null,
    onExpensesChange: ((Double) -> Unit)? = null,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Title & Health Score Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SchemeMicroGreenBg)
                            .border(1.dp, ForestGreenLight.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Business Health",
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.title_business_health),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                        Text(
                            text = stringResource(R.string.subtitle_business_health),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMediumContrast
                        )
                    }
                }

                // Health Score Badge (e.g. 85/100)
                val (statusColor, statusBg, statusTextRes) = when (report.cashFlowStatus) {
                    CashFlowStatus.HEALTHY -> Triple(ForestGreenPrimary, SchemeMicroGreenBg, R.string.status_healthy)
                    CashFlowStatus.MODERATE -> Triple(AmberAccent, AmberLight, R.string.status_moderate)
                    CashFlowStatus.AT_RISK -> Triple(ErrorRed, ErrorRedLight, R.string.status_at_risk)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(statusBg)
                            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${report.healthScore}/100",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(statusTextRes),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor,
                        fontSize = 9.sp
                    )
                }
            }

            HorizontalDivider(color = OutlineLight.copy(alpha = 0.6f))

            // 4-Quadrant Waterfall Metrics: Revenue, OpEx, Loan EMI, Net Profit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip(
                    label = stringResource(R.string.label_monthly_revenue),
                    value = TextToSpeechHelper.formatIndianCurrency(report.estimatedMonthlyRevenue),
                    icon = Icons.Default.CurrencyRupee,
                    modifier = Modifier.weight(1f)
                )
                MetricChip(
                    label = stringResource(R.string.label_monthly_expenses),
                    value = TextToSpeechHelper.formatIndianCurrency(report.estimatedMonthlyOperatingExpenses),
                    icon = Icons.Default.PriceChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip(
                    label = stringResource(R.string.label_debt_service_burden),
                    value = TextToSpeechHelper.formatIndianCurrency(report.monthlyDebtService),
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
                MetricChip(
                    label = "${stringResource(R.string.label_net_monthly_profit)} (${String.format(java.util.Locale.US, "%.1f", report.netProfitMarginPercentage)}%)",
                    value = TextToSpeechHelper.formatIndianCurrency(report.netMonthlyProfit),
                    icon = Icons.Default.Savings,
                    valueColor = if (report.netMonthlyProfit >= 0.0) ForestGreenPrimary else ErrorRed,
                    modifier = Modifier.weight(1f)
                )
            }

            // Diagnostic Flags & Business Guidance
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCard)
                    .border(1.dp, OutlineLight.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                report.flags.forEach { flag ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (flag.isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (flag.isWarning) AmberAccent else ForestGreenPrimary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Column {
                            Text(
                                text = flag.title(language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (flag.isWarning) AmberAccent else ForestGreenDark,
                                fontSize = 12.sp
                            )
                            Text(
                                text = flag.description(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMediumContrast,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Interactive Cash-Flow Diagnostic Adjuster (Section 8)
            var isAdjusterExpanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(WarmCreamBackground)
                    .border(1.dp, OutlineLight, RoundedCornerShape(10.dp))
                    .clickable { isAdjusterExpanded = !isAdjusterExpanded }
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (language == LanguageCode.HI) "वास्तविक मासिक आय एवं व्यय जांचें (What-If)" else "Customize Monthly Cash Flow (What-If)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                        }

                        Icon(
                            imageVector = if (isAdjusterExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isAdjusterExpanded) "Collapse" else "Expand",
                            tint = ForestGreenPrimary
                        )
                    }

                    if (isAdjusterExpanded) {
                        Text(
                            text = if (language == LanguageCode.HI) {
                                "अपनी वास्तविक मासिक बिक्री और परिचालन लागत दर्ज करके तुरंत लाभप्रदता, ऋण शोधन क्षमता (DSCR) और व्यापार स्वास्थ्य स्कोर का विश्लेषण करें।"
                            } else {
                                "Enter your actual monthly turnover and operating expenses to simulate real-time solvency, DSCR debt coverage, and cash-flow health."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMediumContrast,
                            fontSize = 11.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            var revText by remember(monthlyRevenue) {
                                mutableStateOf(if (monthlyRevenue > 0) monthlyRevenue.toLong().toString() else "")
                            }
                            var expText by remember(monthlyExpenses) {
                                mutableStateOf(if (monthlyExpenses > 0) monthlyExpenses.toLong().toString() else "")
                            }

                            OutlinedTextField(
                                value = revText,
                                onValueChange = { input ->
                                    revText = input
                                    input.toDoubleOrNull()?.let { onRevenueChange?.invoke(it) }
                                },
                                label = { Text(if (language == LanguageCode.HI) "मासिक बिक्री (₹)" else "Monthly Sales (₹)", fontSize = 11.sp) },
                                placeholder = { Text(report.estimatedMonthlyRevenue.toLong().toString(), fontSize = 11.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = expText,
                                onValueChange = { input ->
                                    expText = input
                                    input.toDoubleOrNull()?.let { onExpensesChange?.invoke(it) }
                                },
                                label = { Text(if (language == LanguageCode.HI) "परिचालन व्यय (₹)" else "Monthly OpEx (₹)", fontSize = 11.sp) },
                                placeholder = { Text(report.estimatedMonthlyOperatingExpenses.toLong().toString(), fontSize = 11.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricChip(
    label: String,
    value: String,
    icon: ImageVector,
    valueColor: Color = TextHighContrast,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(WarmCreamBackground)
            .border(1.dp, OutlineLight.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AgriTealSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    maxLines = 2
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                fontSize = 13.sp
            )
        }
    }
}
