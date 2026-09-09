package com.ruraladvisory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.components.AmortizationScheduleTable
import com.ruraladvisory.ui.components.BusinessHealthCard
import com.ruraladvisory.ui.components.FinancialDashboardCard
import com.ruraladvisory.ui.navigation.AppTab
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

/**
 * Screen 2: Financial Calculator, Smart Structuring & Amortization
 * Reference: WEB/src/components/FinancialCalculatorView.tsx
 */
@Composable
fun FinancialCalculatorScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isHindi = uiState.language == LanguageCode.HI

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
                    text = if (isHindi) "वित्तीय संरचना एवं ऋण मूल्यांकन" else "Credit Appraisal & Financial Structuring",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Text(
                    text = if (isHindi) "10% मार्जिन, 90% रियायती ऋण व किश्त तालिका" else "10% margin, 90% concessional debt & repayment schedule",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // 1. Financial Structuring Dashboard
        FinancialDashboardCard(
            financialResult = uiState.financialResult,
            language = uiState.language
        )

        // 2. Business Intelligence & Cash-Flow Diagnostic (What-If simulation)
        uiState.businessHealthReport?.let { healthReport ->
            BusinessHealthCard(
                report = healthReport,
                language = uiState.language,
                monthlyRevenue = uiState.monthlyRevenue,
                monthlyExpenses = uiState.monthlyExpenses,
                onRevenueChange = { viewModel.onMonthlyRevenueChanged(it) },
                onExpensesChange = { viewModel.onMonthlyExpensesChanged(it) }
            )
        }

        // 3. Quarterly Amortization Schedule
        uiState.successfulFinance?.let { success ->
            AmortizationScheduleTable(
                schedule = success.schedule,
                isExpanded = uiState.isAmortizationExpanded,
                language = uiState.language,
                onToggleExpand = { viewModel.toggleAmortizationExpanded() }
            )
        }

        // 4. Primary Forward Action Button
        Button(
            onClick = { viewModel.selectTab(AppTab.FEASIBILITY) },
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
                    text = if (isHindi) "व्यवहार्यता अध्ययन एवं बाजार विश्लेषण" else "Proceed to Feasibility Study",
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
