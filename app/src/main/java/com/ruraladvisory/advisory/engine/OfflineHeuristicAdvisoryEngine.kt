package com.ruraladvisory.advisory.engine

import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.knowledge.RuralTradeKnowledgeBase
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.FeasibilityReport

/**
 * High-performance, 100% offline heuristic advisory engine.
 * Synthesizes complete 6-dimension rural feasibility reports in < 5ms
 * using verified agro-economic domain heuristics without network dependencies.
 */
class OfflineHeuristicAdvisoryEngine : FeasibilityAdvisoryEngine {

    override fun generateReport(input: AdvisoryInput): FeasibilityReport {
        val language = input.language
        val cleanLocation = input.location.sanitized(language)
        val budgetTier = input.budgetTier
        val effectiveCategory = input.category

        val marketReach = RuralTradeKnowledgeBase.buildMarketReachNarrative(
            category = effectiveCategory,
            budgetTier = budgetTier,
            location = cleanLocation,
            language = language
        )

        val opportunity = RuralTradeKnowledgeBase.buildOpportunityNarrative(
            category = effectiveCategory,
            budgetTier = budgetTier,
            location = cleanLocation,
            language = language
        )

        val swot = RuralTradeKnowledgeBase.getSwotAnalysis(
            category = effectiveCategory,
            budgetTier = budgetTier,
            language = language
        )

        val threats = RuralTradeKnowledgeBase.getThreatsList(
            category = effectiveCategory,
            language = language
        )

        val competitor = RuralTradeKnowledgeBase.buildCompetitorMappingNarrative(
            category = effectiveCategory,
            location = cleanLocation,
            language = language
        )

        val pricing = RuralTradeKnowledgeBase.buildPricingStrategyNarrative(
            category = effectiveCategory,
            language = language
        )

        val recommendations = RuralTradeKnowledgeBase.getRecommendations(
            category = effectiveCategory,
            budgetTier = budgetTier,
            language = language
        )

        val tierDisplay = budgetTier.name

        return FeasibilityReport(
            category = effectiveCategory,
            location = cleanLocation,
            budgetTier = tierDisplay,
            marketReach = marketReach,
            opportunityAnalysis = opportunity,
            swotAnalysis = swot,
            threatsIdentification = threats,
            competitorMapping = competitor,
            pricingStrategy = pricing,
            keyRecommendations = recommendations,
            isOfflineGenerated = true,
            customCategory = input.customCategory,
            businessDetails = input.businessDetails
        )
    }
}
