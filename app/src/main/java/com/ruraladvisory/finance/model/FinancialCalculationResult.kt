package com.ruraladvisory.finance.model

/**
 * Result of financial structuring calculation: either a valid result (Success)
 * or a specific constraint violation / error.
 */
sealed class FinancialCalculationResult {
    data class Success(
        val scheme: SchemeCategory,
        val structure: FinancialStructure,
        val schedule: RepaymentSchedule,
        val costBreakdown: CostBreakdown
    ) : FinancialCalculationResult()

    data class BelowMinimumThreshold(
        val enteredMargin: Double,
        val minimumAllowedMargin: Double = 1000.0,
        val minimumAllowedProjectCost: Double = 10000.0
    ) : FinancialCalculationResult()

    data class ExceedsMaximumThreshold(
        val enteredMargin: Double,
        val maximumAllowedMargin: Double = 500000.0,
        val maximumAllowedProjectCost: Double = 5000000.0
    ) : FinancialCalculationResult()

    data class InvalidInput(val message: String) : FinancialCalculationResult()
}
