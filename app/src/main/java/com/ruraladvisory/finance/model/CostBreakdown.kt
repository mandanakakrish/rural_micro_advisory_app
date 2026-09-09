package com.ruraladvisory.finance.model

/**
 * Breakdown of project cost across Capital Expenditure (CAPEX), Working Capital,
 * Contingency buffer, and operational expenditure categories.
 */
data class CostBreakdown(
    val totalProjectCost: Double,
    val fixedAssetsCapex: Double,
    val workingCapital: Double,
    val contingencyBuffer: Double,
    val estimatedMonthlyOpex: Double,
    val estimatedQuarterlyOpex: Double,
    val rawMaterialsOpex: Double,
    val laborWagesOpex: Double,
    val utilitiesLogisticsOpex: Double,
    val maintenanceSundryOpex: Double
)
