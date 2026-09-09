package com.ruraladvisory.advisory.model

/**
 * Enterprise scale tier calibrated by promoter's margin capital.
 * - MICRO: Margin < ₹25,000 (Project Cost < ₹2.5 Lakh)
 * - SMALL: Margin ₹25,000 to ₹1,50,000 (Project Cost ₹2.5 Lakh to ₹15 Lakh)
 * - MEDIUM: Margin > ₹1,50,000 (Project Cost > ₹15 Lakh)
 */
enum class BudgetTier {
    MICRO,
    SMALL,
    MEDIUM;

    companion object {
        const val MICRO_THRESHOLD: Double = 25000.0
        const val SMALL_MAX_THRESHOLD: Double = 150000.0

        fun fromMargin(marginCapital: Double): BudgetTier = when {
            marginCapital < MICRO_THRESHOLD -> MICRO
            marginCapital <= SMALL_MAX_THRESHOLD -> SMALL
            else -> MEDIUM
        }
    }
}
