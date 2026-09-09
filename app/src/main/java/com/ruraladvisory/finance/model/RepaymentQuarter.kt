package com.ruraladvisory.finance.model

/**
 * Represents cash flow and principal/interest amortization for a single quarter.
 */
data class RepaymentQuarter(
    val quarterIndex: Int,
    val isMoratorium: Boolean,
    val openingPrincipal: Double,
    val principalRepayment: Double,
    val interestAccrual: Double,
    val totalQuarterlyOutflow: Double,
    val closingPrincipal: Double
)
