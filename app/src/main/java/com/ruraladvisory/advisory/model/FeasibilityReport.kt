package com.ruraladvisory.advisory.model

/**
 * Comprehensive feasibility advisory report covering all 6 analytical dimensions:
 * 1. Market Reach (radius, consumer base, distribution channels)
 * 2. Opportunity Analysis (underserved niches in rural economy)
 * 3. SWOT Analysis (budget-calibrated 4 quadrants)
 * 4. Threats Identification (4 rural risk vectors with mitigations)
 * 5. Competitor Mapping (block-level density and competitive moat)
 * 6. Pricing Strategy (optimal unit pricing and credit policy)
 *
 * Along with key recommendations and offline execution indicator.
 */
data class FinancialViabilityEstimate(
    val expectedMonthlyRevenue: String,
    val expectedMonthlyNetProfit: String,
    val breakevenTimeline: String,
    val roiPercentage: String
)

data class FeasibilityReport(
    val category: BusinessCategory,
    val location: LocationInfo,
    val budgetTier: String,
    val marketReach: String,
    val opportunityAnalysis: String,
    val swotAnalysis: SwotAnalysis,
    val threatsIdentification: List<String>,
    val competitorMapping: String,
    val pricingStrategy: String,
    val keyRecommendations: List<String>,
    val isOfflineGenerated: Boolean,
    val customCategory: String = "",
    val businessDetails: String = "",
    val executiveSummary: String = "",
    val financialViability: FinancialViabilityEstimate? = null,
    val isAiGenerated: Boolean = false,
    val aiModelName: String? = null
)
