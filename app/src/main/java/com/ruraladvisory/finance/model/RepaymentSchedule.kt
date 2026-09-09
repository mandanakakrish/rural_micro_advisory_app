package com.ruraladvisory.finance.model

/**
 * Encapsulates the complete quarterly amortization schedule over the loan tenure.
 */
data class RepaymentSchedule(
    val quarters: List<RepaymentQuarter>,
    val totalPrincipalPaid: Double,
    val totalInterestPaid: Double,
    val totalOutflow: Double,
    val totalMoratoriumInterest: Double,
    val moratoriumQuartersCount: Int,
    val repaymentQuartersCount: Int
)
