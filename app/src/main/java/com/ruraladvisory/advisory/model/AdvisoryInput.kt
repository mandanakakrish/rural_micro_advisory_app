package com.ruraladvisory.advisory.model

/**
 * Input parameters required to synthesize a hyper-local feasibility advisory report.
 */
data class AdvisoryInput(
    val location: LocationInfo,
    val marginCapital: Double,
    val category: BusinessCategory = BusinessCategory.DAIRY,
    val language: LanguageCode = LanguageCode.EN,
    val customCategory: String = "",
    val businessDetails: String = "",
    val growthObjective: GrowthObjective = GrowthObjective.EXPANSION,
    val scale: BusinessScale = BusinessScale.MICRO,
    val productionCapacity: String = ""
) {
    val budgetTier: BudgetTier
        get() = BudgetTier.fromMargin(marginCapital)
}
