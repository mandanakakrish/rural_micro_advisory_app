package com.ruraladvisory.finance.model

import com.ruraladvisory.advisory.model.LanguageCode

/**
 * Diagnostic status representing the financial viability and cash flow solvency of the enterprise.
 */
enum class CashFlowStatus {
    HEALTHY,
    MODERATE,
    AT_RISK
}

/**
 * An actionable diagnostic alert or insight regarding business operations, margins, or cash flow.
 */
data class BusinessHealthFlag(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val isWarning: Boolean
) {
    fun title(lang: LanguageCode): String = if (lang == LanguageCode.HI) titleHi else titleEn
    fun description(lang: LanguageCode): String = if (lang == LanguageCode.HI) descriptionHi else descriptionEn
}

/**
 * Comprehensive Business Intelligence Report converting Revenue, Operating Expenses,
 * and Debt Service into understandable decisions.
 */
data class BusinessHealthReport(
    val estimatedMonthlyRevenue: Double,
    val estimatedMonthlyOperatingExpenses: Double,
    val monthlyDebtService: Double,
    val totalMonthlyOutflow: Double,
    val netMonthlyProfit: Double,
    val netProfitMarginPercentage: Double,
    val debtServiceCoverageRatio: Double,
    val cashFlowStatus: CashFlowStatus,
    val healthScore: Int,
    val breakEvenRevenue: Double,
    val flags: List<BusinessHealthFlag>
)
