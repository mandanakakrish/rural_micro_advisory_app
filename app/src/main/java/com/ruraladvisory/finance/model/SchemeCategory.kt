package com.ruraladvisory.finance.model

/**
 * Government credit scheme categories calibrated for rural and semi-urban micro-enterprises.
 */
enum class SchemeCategory(
    val displayNameRes: String,
    val maxProjectCost: Double,
    val maxLoanCap: Double,
    val annualInterestRate: Double,
    val tenureYears: Int,
    val tenureQuarters: Int,
    val moratoriumMonths: Int,
    val moratoriumQuarters: Int
) {
    MICRO_FINANCE(
        displayNameRes = "Micro Finance Scheme",
        maxProjectCost = 140000.0,
        maxLoanCap = 125000.0,
        annualInterestRate = 0.065,
        tenureYears = 3,
        tenureQuarters = 12,
        moratoriumMonths = 3,
        moratoriumQuarters = 1
    ),
    TERM_LOAN(
        displayNameRes = "Term Loan Scheme",
        maxProjectCost = 5000000.0,
        maxLoanCap = 4500000.0,
        annualInterestRate = 0.080,
        tenureYears = 7,
        tenureQuarters = 28,
        moratoriumMonths = 6,
        moratoriumQuarters = 2
    );

    val quarterlyInterestRate: Double get() = annualInterestRate / 4.0
    val repaymentQuarters: Int get() = tenureQuarters - moratoriumQuarters
}
