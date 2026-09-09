package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.RepaymentQuarter
import com.ruraladvisory.finance.model.RepaymentSchedule
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SchemeCappedAmberBg
import com.ruraladvisory.ui.theme.SchemeCappedAmberText
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted

/**
 * Expandable Amortization Schedule Table displaying quarterly interest accrual,
 * moratorium treatment, principal repayment, and total quarterly outflow.
 */
@Composable
fun AmortizationScheduleTable(
    schedule: RepaymentSchedule?,
    isExpanded: Boolean,
    language: LanguageCode,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (schedule == null) return

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
            // Header with Calendar Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SchemeMicroGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.title_amortization),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHighContrast,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${schedule.quarters.size} ${stringResource(R.string.quarters_suffix)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreenDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Summary Outflow Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryMetricPill(
                    label = if (language == LanguageCode.HI) "कुल मूलधन" else "Total Principal",
                    amount = schedule.totalPrincipalPaid,
                    modifier = Modifier.weight(1f)
                )
                SummaryMetricPill(
                    label = if (language == LanguageCode.HI) "कुल ब्याज" else "Total Interest",
                    amount = schedule.totalInterestPaid,
                    modifier = Modifier.weight(1f)
                )
                SummaryMetricPill(
                    label = if (language == LanguageCode.HI) "कुल अदायगी" else "Total Outflow",
                    amount = schedule.totalOutflow,
                    isHighlight = true,
                    modifier = Modifier.weight(1f)
                )
            }

            // Expand/Collapse Button (56dp touch target)
            OutlinedButton(
                onClick = onToggleExpand,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ForestGreenPrimary
                ),
                border = BorderStroke(1.5.dp, ForestGreenPrimary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isExpanded) {
                            stringResource(R.string.btn_hide_schedule)
                        } else {
                            stringResource(R.string.btn_show_schedule)
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Full Table when expanded
            if (isExpanded) {
                HorizontalDivider(color = OutlineLight)

                val horizontalScroll = rememberScrollState()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, OutlineLight, RoundedCornerShape(8.dp))
                        .horizontalScroll(horizontalScroll)
                ) {
                    Column {
                        // Table Header Row
                        TableHeaderRow(language)

                        HorizontalDivider(color = OutlineLight, thickness = 1.dp)

                        // Table Data Rows
                        schedule.quarters.forEachIndexed { index, quarter ->
                            TableDataRow(quarter, index, language)
                            if (index < schedule.quarters.size - 1) {
                                HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryMetricPill(
    label: String,
    amount: Double,
    isHighlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isHighlight) SchemeMicroGreenBg else SurfaceCard)
            .border(1.dp, if (isHighlight) ForestGreenPrimary else OutlineLight, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "₹${TextToSpeechHelper.formatIndianCurrency(amount)}",
                style = MaterialTheme.typography.titleSmall,
                color = if (isHighlight) ForestGreenDark else TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TableHeaderRow(language: LanguageCode) {
    Row(
        modifier = Modifier
            .background(SurfaceCard)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(text = stringResource(R.string.col_quarter), width = 60.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_phase), width = 110.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_opening), width = 110.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_principal), width = 100.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_interest), width = 90.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_outflow), width = 110.dp, isHeader = true)
        TableCell(text = stringResource(R.string.col_closing), width = 110.dp, isHeader = true)
    }
}

@Composable
private fun TableDataRow(
    quarter: RepaymentQuarter,
    rowIndex: Int,
    language: LanguageCode
) {
    val rowBg = if (quarter.isMoratorium) {
        SchemeCappedAmberBg.copy(alpha = 0.5f)
    } else if (rowIndex % 2 == 0) {
        SurfaceCream
    } else {
        SurfaceCard
    }

    Row(
        modifier = Modifier
            .background(rowBg)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(text = "Q${quarter.quarterIndex}", width = 60.dp, isBold = true)

        // Phase tag
        Box(
            modifier = Modifier
                .width(110.dp)
                .padding(end = 8.dp)
        ) {
            if (quarter.isMoratorium) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SchemeCappedAmberBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (language == LanguageCode.HI) "छूट (मोराटोरियम)" else "Moratorium",
                        style = MaterialTheme.typography.labelSmall,
                        color = SchemeCappedAmberText,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = if (language == LanguageCode.HI) "अदायगी" else "Repayment",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMediumContrast
                )
            }
        }

        TableCell(text = "₹${TextToSpeechHelper.formatIndianCurrency(quarter.openingPrincipal)}", width = 110.dp)
        TableCell(
            text = "₹${TextToSpeechHelper.formatIndianCurrency(quarter.principalRepayment)}",
            width = 100.dp,
            isBold = !quarter.isMoratorium
        )
        TableCell(text = "₹${TextToSpeechHelper.formatIndianCurrency(quarter.interestAccrual)}", width = 90.dp)
        TableCell(
            text = "₹${TextToSpeechHelper.formatIndianCurrency(quarter.totalQuarterlyOutflow)}",
            width = 110.dp,
            isBold = true,
            color = ForestGreenPrimary
        )
        TableCell(text = "₹${TextToSpeechHelper.formatIndianCurrency(quarter.closingPrincipal)}", width = 110.dp)
    }
}

@Composable
private fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    isHeader: Boolean = false,
    isBold: Boolean = false,
    color: androidx.compose.ui.graphics.Color = TextHighContrast
) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        style = if (isHeader) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
        fontWeight = if (isHeader || isBold) FontWeight.Bold else FontWeight.Normal,
        color = if (isHeader) TextHighContrast else color
    )
}
