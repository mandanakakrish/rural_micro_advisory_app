package com.ruraladvisory.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.SchemeCategory
import com.ruraladvisory.ui.navigation.AppTab
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

/**
 * Screen 4: Bankable Detailed Project Report (DPR)
 * Reference: WEB/src/components/DprView.tsx & DprDocumentModal.tsx
 */
@Composable
fun DprDocumentScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    onShareDpr: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isHindi = uiState.language == LanguageCode.HI
    val success = uiState.successfulFinance
    val struct = success?.structure
    val scheme = success?.scheme

    val isMicro = scheme == SchemeCategory.MICRO_FINANCE
    val schemeName = if (isMicro) {
        if (isHindi) "माइक्रो फाइनेंस रियायती योजना" else "Micro Finance Scheme"
    } else {
        if (isHindi) "टर्म लोन रियायती योजना" else "Term Loan Scheme"
    }

    val projectCost = struct?.totalProjectCost ?: (uiState.marginCapital * 10.0)
    val loanAmount = struct?.actualLoanAmount ?: (uiState.marginCapital * 9.0)
    val promoterEquity = struct?.effectivePromoterEquity ?: uiState.marginCapital
    val interestRate = scheme?.annualInterestRate?.let { "${(it * 100).toInt()}% p.a." } ?: "6.5% - 8.0% p.a."
    val tenure = scheme?.tenureYears?.let { "$it Years / वर्ष" } ?: "3-7 Years"
    val moratorium = scheme?.moratoriumMonths?.let { "$it Months / माह" } ?: "3-6 Months"

    // 70/25/5 allocation figures
    val capexMachinery = (projectCost * 0.70).toLong()
    val workingCapital = (projectCost * 0.25).toLong()
    val contingencyBuffer = (projectCost * 0.05).toLong()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header: Screen Context
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isHindi) "विस्तृत परियोजना रिपोर्ट (DPR)" else "Bankable Detailed Project Report (DPR)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Text(
                    text = if (isHindi) "SCA, PMEGP, मुद्रा व बैंक ऋण स्वीकृति हेतु आधिकारिक दस्तावेज" else "Official credit appraisal dossier for SCA, PMEGP & commercial banks",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Official Bankable DPR Sheet
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, OutlineLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Official Document Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isHindi)
                            "राज्य चैनलाइजिंग एजेंसी (SCA) रियायती योजना मूल्यांकन"
                        else
                            "STATE CHANNELIZING AGENCY (SCA) CONCESSIONAL SCHEME APPRAISAL",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = ForestGreenDark,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isHindi) "विस्तृत परियोजना रिपोर्ट (DPR)" else "DETAILED PROJECT REPORT (DPR)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = TextHighContrast,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Ref: SCA/DPR/${(uiState.district.ifBlank { "REG" }).take(3).uppercase()}-${uiState.marginCapital.toLong()} • ABHYUDAY Verified",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                HorizontalDivider(color = OutlineLight, thickness = 1.5.dp)

                // Section 1: Entrepreneur Profile
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DprSectionHeader(
                        number = "1",
                        title = if (isHindi) "उद्यमी एवं उद्यम प्रोफाइल" else "Entrepreneur & Enterprise Profile",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.openUserDetails() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                        border = BorderStroke(1.dp, ForestGreenPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "संपादित करें" else "Edit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                val notSpec = if (isHindi) "दर्ज नहीं" else "Not Specified"
                DprKeyValRow(if (isHindi) "उद्यमी का नाम" else "Entrepreneur Name", uiState.activeProfile.name.ifBlank { notSpec })
                DprKeyValRow(if (isHindi) "संपर्क नंबर" else "Phone Contact", uiState.activeProfile.phoneNumber.ifBlank { notSpec })
                if (uiState.activeProfile.email.isNotBlank()) {
                    DprKeyValRow(if (isHindi) "ईमेल पता" else "Email Address", uiState.activeProfile.email)
                }
                val ageText = if (uiState.activeProfile.age > 0) ", ${uiState.activeProfile.age} yrs / वर्ष" else ""
                DprKeyValRow(if (isHindi) "लिंग एवं आयु" else "Gender & Age", "${uiState.activeProfile.gender}$ageText")
                val promCat = if (uiState.promoterCategory.isNotBlank()) uiState.promoterCategory else notSpec
                DprKeyValRow(if (isHindi) "सामाजिक श्रेणी" else "Promoter Category", "$promCat (${uiState.activeProfile.specialCategory})")
                val eduText = if (uiState.activeProfile.educationQualification.isNotBlank()) uiState.activeProfile.educationQualification else notSpec
                DprKeyValRow(if (isHindi) "शैक्षणिक योग्यता" else "Education & EDP", "$eduText • EDP: ${if (uiState.activeProfile.hasEdpTraining) "Certified" else "Pending"}")
                val locList = listOf(uiState.village, uiState.block, uiState.district).filter { it.isNotBlank() }
                val locStr = if (locList.isNotEmpty()) locList.joinToString(", ") else notSpec
                DprKeyValRow(if (isHindi) "स्थान एवं क्षेत्र" else "Location & Area", "$locStr (${uiState.activeProfile.areaType})")
                DprKeyValRow(if (isHindi) "उद्यम श्रेणी" else "Business Category", uiState.customCategory.ifBlank { uiState.selectedCategory.displayName(uiState.language) })
                DprKeyValRow(if (isHindi) "व्यापार पैमाना (Scale)" else "MSME Scale", uiState.scale.displayName(uiState.language))
                if (uiState.productionCapacity.isNotBlank()) {
                    DprKeyValRow(if (isHindi) "उत्पादन क्षमता" else "Operational Capacity", uiState.productionCapacity)
                }

                HorizontalDivider(color = OutlineLight)

                // Section 2: Statutory Scheme & Financial Structure
                DprSectionHeader(
                    number = "2",
                    title = if (isHindi) "रियायती योजना एवं वित्तीय संरचना" else "Concessional Scheme & Financial Structure"
                )
                DprKeyValRow(if (isHindi) "चयनित ऋण योजना" else "Selected Loan Scheme", schemeName)
                DprKeyValRow(if (isHindi) "कुल परियोजना लागत" else "Total Project Cost", "₹" + TextToSpeechHelper.formatIndianCurrency(projectCost))
                DprKeyValRow(if (isHindi) "प्रमोटर अंशदान (10%)" else "Promoter Margin (10%)", "₹" + TextToSpeechHelper.formatIndianCurrency(promoterEquity))
                DprKeyValRow(if (isHindi) "रियायती ऋण स्वीकृति (90%)" else "Concessional Debt (90%)", "₹" + TextToSpeechHelper.formatIndianCurrency(loanAmount))
                DprKeyValRow(if (isHindi) "रियायती ब्याज दर" else "Concessional Interest", interestRate)
                DprKeyValRow(if (isHindi) "ऋण अदायगी अवधि" else "Repayment Tenure", tenure)
                DprKeyValRow(if (isHindi) "मोरेटोरियम (छूट अवधि)" else "Moratorium Grace Period", moratorium)

                HorizontalDivider(color = OutlineLight)

                // Section 3: Means of Finance & Capital Allocation
                DprSectionHeader(
                    number = "3",
                    title = if (isHindi) "पूंजी आवंटन एवं संपत्ति विभाजन (70/25/5)" else "CAPEX & Means of Finance (70/25/5)"
                )
                DprKeyValRow(if (isHindi) "संयंत्र, मशीनरी एवं उपकरण (70%)" else "Plant, Machinery & CAPEX (70%)", "₹" + TextToSpeechHelper.formatIndianCurrency(capexMachinery.toDouble()))
                DprKeyValRow(if (isHindi) "कच्चा माल व कार्यशील पूंजी (25%)" else "Working Capital Buffer (25%)", "₹" + TextToSpeechHelper.formatIndianCurrency(workingCapital.toDouble()))
                DprKeyValRow(if (isHindi) "आकस्मिक व लाइसेंसिंग रिज़र्व (5%)" else "Contingency & Licensing (5%)", "₹" + TextToSpeechHelper.formatIndianCurrency(contingencyBuffer.toDouble()))

                HorizontalDivider(color = OutlineLight)

                // Section 4: Operating Viability & Cash Flow Diagnostic
                DprSectionHeader(
                    number = "4",
                    title = if (isHindi) "परिचालन व्यवहार्यता एवं ऋण शोधन क्षमता" else "Operating Viability & Solvency"
                )
                uiState.businessHealthReport?.let { health ->
                    DprKeyValRow(if (isHindi) "अनुमानित मासिक बिक्री" else "Est. Monthly Sales", "₹" + TextToSpeechHelper.formatIndianCurrency(health.estimatedMonthlyRevenue))
                    DprKeyValRow(if (isHindi) "अनुमानित मासिक खर्च (OpEx)" else "Est. Monthly OpEx", "₹" + TextToSpeechHelper.formatIndianCurrency(health.estimatedMonthlyOperatingExpenses))
                    DprKeyValRow(if (isHindi) "मासिक ऋण किश्त (EMI)" else "Monthly Debt Service (EMI)", "₹" + TextToSpeechHelper.formatIndianCurrency(health.monthlyDebtService))
                    DprKeyValRow(if (isHindi) "शुद्ध मासिक लाभ" else "Net Monthly Profit", "₹" + TextToSpeechHelper.formatIndianCurrency(health.netMonthlyProfit) + " (${String.format(java.util.Locale.US, "%.1f", health.netProfitMarginPercentage)}%)")
                    DprKeyValRow(if (isHindi) "ऋण सेवा कवरेज (DSCR)" else "Debt Coverage (DSCR)", "${String.format(java.util.Locale.US, "%.2f", health.debtServiceCoverageRatio)}x")
                }

                HorizontalDivider(color = OutlineLight)

                // Section 5: Statutory SCA Official Appraisal Checklist
                DprSectionHeader(
                    number = "5",
                    title = if (isHindi) "अधिकारी सत्यापन एवं स्वीकृति चेकलिस्ट" else "Official SCA Appraisal Checklist"
                )
                val checkItems = if (isHindi) {
                    listOf(
                        "10% प्रमोटर मार्जिन बैंक खाते में उपलब्ध एवं सत्यापित।",
                        "मशीनरी एवं उपकरणों के वैध GST कोटेशन संलग्न।",
                        "ग्राम पंचायत अथवा स्थानीय प्राधिकारी से व्यापार अनापत्ति (NOC)।",
                        "तकनीकी व आर्थिक व्यवहार्यता (TEV) दिशानिर्देशों के अनुरूप प्रमाणित।"
                    )
                } else {
                    listOf(
                        "10% promoter equity verified in operative savings bank account.",
                        "Competitive machinery quotations with GST numbers obtained.",
                        "Gram Panchayat / local authority trade consent confirmed.",
                        "Techno-Economic Viability (TEV) verified in compliance with SCA guidelines."
                    )
                }

                checkItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMediumContrast,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Share / Export Action Button
        OutlinedButton(
            onClick = onShareDpr,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreenPrimary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenDark)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "डीपीआर सारांश शेयर करें (Share DPR)" else "Share Bankable DPR Summary",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Primary Forward Action Button
        Button(
            onClick = { viewModel.selectTab(AppTab.ADVISOR) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreenPrimary,
                contentColor = SurfaceCream
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isHindi) "उद्यम सलाहकार से चर्चा करें" else "Consult Enterprise Advisor (Sahayak)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DprSectionHeader(number: String, title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ForestGreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SurfaceCream,
                fontSize = 11.sp
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = ForestGreenDark
        )
    }
}

@Composable
private fun DprKeyValRow(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.bodySmall,
            color = TextMediumContrast,
            fontSize = 12.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = TextHighContrast,
            fontSize = 12.sp
        )
    }
}
