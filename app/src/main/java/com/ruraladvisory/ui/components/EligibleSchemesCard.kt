package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.finance.engine.GovernmentSchemeDirectory
import com.ruraladvisory.finance.model.EligibleScheme
import com.ruraladvisory.ui.theme.AgriTealLight
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.AmberLight
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SchemeMicroGreenText
import com.ruraladvisory.ui.theme.SchemeTermTealBg
import com.ruraladvisory.ui.theme.SchemeTermTealText
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground

/**
 * Eligible Government Schemes Card positioned immediately before the Quarterly Amortization Schedule.
 * Displays all statutory and sector-specific concessional credit and subsidy schemes
 * that the entrepreneur is eligible for based on margin capital, project cost, and trade category.
 */
@Composable
fun EligibleSchemesCard(
    category: BusinessCategory,
    marginCapital: Double,
    projectCost: Double,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    val schemes = remember(category, marginCapital, projectCost) {
        GovernmentSchemeDirectory.getEligibleSchemes(category, marginCapital, projectCost)
    }

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
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Compact Title & Scheme Count Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SchemeMicroGreenBg)
                            .border(1.dp, ForestGreenLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "Government Schemes",
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.title_eligible_schemes),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 15.sp
                    )
                }

                // Scheme Count Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SchemeMicroGreenBg)
                        .border(1.dp, ForestGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    val countText = if (language == LanguageCode.HI) {
                        "${schemes.size} योजनाएं पात्र"
                    } else {
                        "${schemes.size} Matched"
                    }
                    Text(
                        text = countText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        fontSize = 11.sp
                    )
                }
            }

            HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

            // Render each eligible scheme card
            schemes.forEach { scheme ->
                SchemeDetailItem(
                    scheme = scheme,
                    language = language
                )
            }
        }
    }
}

/**
 * Individual scheme card component displaying its status badge, nodal agency,
 * key parameters (interest, ceiling, subsidy, tenure), and eligibility rationale.
 */
@Composable
private fun SchemeDetailItem(
    scheme: EligibleScheme,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    // Badge styling depending on scheme classification
    val (badgeBg, badgeText, badgeBorder, badgeLabelRes) = when {
        scheme.isPrimaryRouted -> Quadruple(
            SchemeMicroGreenBg,
            SchemeMicroGreenText,
            ForestGreenLight,
            R.string.tag_primary_active
        )
        scheme.id.startsWith("SECTOR_") -> Quadruple(
            SchemeTermTealBg,
            SchemeTermTealText,
            AgriTealLight,
            R.string.tag_sector_scheme
        )
        else -> Quadruple(
            AmberLight,
            AmberAccent,
            AmberAccent.copy(alpha = 0.6f),
            R.string.tag_national_scheme
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, if (scheme.isPrimaryRouted) ForestGreenLight else OutlineLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Scheme Category Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (scheme.isPrimaryRouted) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = badgeText,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = stringResource(badgeLabelRes),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = badgeText,
                            fontSize = 10.sp
                        )
                    }
                }

                if (scheme.isPrimaryRouted) {
                    Text(
                        text = if (language == LanguageCode.HI) "ऋण में लागू" else "Active in Loan",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        fontSize = 11.sp
                    )
                }
            }

            // Scheme Name
            Text(
                text = scheme.name(language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextHighContrast,
                fontSize = 15.sp
            )

            // Nodal Agency
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${stringResource(R.string.label_scheme_authority)}: ${scheme.authority(language)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMediumContrast,
                    fontSize = 12.sp
                )
            }

            // 4-Quadrant Metric Chips: Interest, Ceiling, Subsidy, Tenure
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SchemeMetricPill(
                    icon = Icons.Default.TrendingUp,
                    label = stringResource(R.string.label_scheme_interest),
                    value = scheme.interestRate(language),
                    modifier = Modifier.weight(1f)
                )
                SchemeMetricPill(
                    icon = Icons.Default.CurrencyRupee,
                    label = stringResource(R.string.label_scheme_ceiling),
                    value = scheme.maxCeiling(language),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SchemeMetricPill(
                    icon = Icons.Default.CheckCircle,
                    label = stringResource(R.string.label_scheme_subsidy),
                    value = scheme.subsidyBenefit(language),
                    modifier = Modifier.weight(1f)
                )
                SchemeMetricPill(
                    icon = Icons.Default.CalendarMonth,
                    label = stringResource(R.string.label_scheme_tenure),
                    value = scheme.tenureMoratorium(language),
                    modifier = Modifier.weight(1f)
                )
            }

            // Eligibility Advisory Note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmCreamBackground)
                    .border(1.dp, OutlineLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Eligibility",
                        tint = ForestGreenPrimary,
                        modifier = Modifier
                            .size(15.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = scheme.eligibilityNote(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumContrast,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // Expandable: Required Documents & Application Steps (Section 7)
            var isDocExpanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { isDocExpanded = !isDocExpanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = AgriTealSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = stringResource(if (isDocExpanded) R.string.btn_hide_documents else R.string.btn_view_documents),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTealSecondary,
                        fontSize = 11.sp
                    )
                }

                Icon(
                    imageVector = if (isDocExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AgriTealSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (isDocExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCream)
                        .border(1.dp, OutlineLight, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Documents checklist
                    Text(
                        text = stringResource(R.string.title_required_documents),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 11.sp
                    )

                    val docs = scheme.requiredDocuments(language)
                    docs.forEach { doc ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier
                                    .size(13.dp)
                                    .padding(top = 2.dp)
                            )
                            Text(
                                text = doc,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMediumContrast,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    if (scheme.applicationProcess(language).isNotBlank()) {
                        HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))
                        Text(
                            text = stringResource(R.string.title_application_process),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 11.sp
                        )
                        Text(
                            text = scheme.applicationProcess(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMediumContrast,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    if (scheme.portalUrl.isNotBlank()) {
                        Text(
                            text = "${stringResource(R.string.label_official_portal)}: ${scheme.portalUrl}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestGreenPrimary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SchemeMetricPill(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCream)
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
                    fontSize = 10.sp
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = TextHighContrast,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Helper container for 4-tuple badge attributes.
 */
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
