package com.ruraladvisory.finance.model

/**
 * Encapsulates the core debt-equity financial structure for a proposed project.
 */
data class FinancialStructure(
    val marginCapital: Double,
    val totalProjectCost: Double,
    val uncappedLoanAmount: Double,
    val actualLoanAmount: Double,
    val schemeMaxCap: Double,
    val isCapped: Boolean,
    val effectivePromoterEquity: Double,
    val loanToCostRatio: Double
)
