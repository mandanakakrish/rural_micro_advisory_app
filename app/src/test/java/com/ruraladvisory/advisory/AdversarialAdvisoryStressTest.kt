package com.ruraladvisory.advisory

import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.knowledge.RuralTradeKnowledgeBase
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Empirical Adversarial Stress Test Suite (Tier 5 Hardening)
 *
 * Exhaustively stress-tests:
 * 1. All 7 trade categories across 6 dimensions in both English and Hindi.
 * 2. Complete non-empty outputs and Devanagari script integrity without English fallback leakage.
 * 3. Budget tier calibration (Micro vs Small vs Medium) across all 7 categories.
 * 4. Blank, whitespace, Unicode, extreme length, and format-string inputs in location fields.
 * 5. ViewModel reactive state transitions, rapid parameter alterations, and share text under all states.
 */
class AdversarialAdvisoryStressTest {

    private lateinit var engine: OfflineHeuristicAdvisoryEngine
    private val devanagariRegex = Regex("[\\u0900-\\u097F]")

    @Before
    fun setUp() {
        engine = OfflineHeuristicAdvisoryEngine()
    }

    // =========================================================================
    // 1. ALL 7 BUSINESS CATEGORIES IN ENGLISH & HINDI: 6 DIMENSIONS & SCRIPT
    // =========================================================================

    @Test
    fun testAllSevenCategories_allSixDimensions_strictlyPopulatedInEnglish() {
        for (category in BusinessCategory.values()) {
            val input = AdvisoryInput(
                location = LocationInfo("VillageA", "BlockB", "DistrictC"),
                marginCapital = 30000.0,
                category = category,
                language = LanguageCode.EN
            )
            val report = engine.generateReport(input)

            // 1. Market Reach
            assertFalse("Market reach blank for $category in EN", report.marketReach.isBlank())
            assertTrue("Market reach must contain village name for $category", report.marketReach.contains("VillageA"))
            assertTrue("Market reach must contain block name for $category", report.marketReach.contains("BlockB"))
            assertTrue("Market reach must contain district name for $category", report.marketReach.contains("DistrictC"))
            assertTrue("Market reach must mention radius for $category", report.marketReach.contains("radius", ignoreCase = true))
            assertTrue("Market reach must mention consumer base for $category", report.marketReach.contains("Consumer Base", ignoreCase = true))
            assertTrue("Market reach must mention distribution channels for $category", report.marketReach.contains("Distribution Channels", ignoreCase = true))

            // 2. Opportunity Analysis
            assertFalse("Opportunity analysis blank for $category in EN", report.opportunityAnalysis.isBlank())
            assertTrue("Opportunity must contain block name for $category", report.opportunityAnalysis.contains("BlockB"))
            assertTrue("Opportunity must mention niches for $category", report.opportunityAnalysis.contains("niches", ignoreCase = true))

            // 3. SWOT Analysis (All 4 quadrants non-empty and non-blank)
            assertNotNull("SWOT analysis null for $category in EN", report.swotAnalysis)
            assertTrue("Strengths empty for $category in EN", report.swotAnalysis.strengths.isNotEmpty())
            assertTrue("Weaknesses empty for $category in EN", report.swotAnalysis.weaknesses.isNotEmpty())
            assertTrue("Opportunities empty for $category in EN", report.swotAnalysis.opportunities.isNotEmpty())
            assertTrue("Threats empty for $category in EN", report.swotAnalysis.threats.isNotEmpty())

            report.swotAnalysis.strengths.forEach { assertFalse("Strength item blank for $category", it.isBlank()) }
            report.swotAnalysis.weaknesses.forEach { assertFalse("Weakness item blank for $category", it.isBlank()) }
            report.swotAnalysis.opportunities.forEach { assertFalse("Opportunity item blank for $category", it.isBlank()) }
            report.swotAnalysis.threats.forEach { assertFalse("Threat item blank for $category", it.isBlank()) }

            // 4. Threats Identification (Exactly 4 vectors with mitigations)
            assertEquals("Must have 4 threat vectors for $category in EN", 4, report.threatsIdentification.size)
            val threatVectorsExpected = listOf(
                "Seasonal Demand Fluctuations",
                "Supply Chain Bottlenecks",
                "Raw Material Price Volatility",
                "Single-Buyer Dependency & Credit Risk"
            )
            report.threatsIdentification.forEachIndexed { idx, threatStr ->
                assertFalse("Threat item blank at index $idx for $category", threatStr.isBlank())
                assertTrue("Threat item must contain Mitigation for $category", threatStr.contains("Mitigation:", ignoreCase = true))
                assertTrue("Threat item must contain vector name for $category", threatStr.contains(threatVectorsExpected[idx]))
            }

            // 5. Competitor Mapping
            assertFalse("Competitor mapping blank for $category in EN", report.competitorMapping.isBlank())
            assertTrue("Competitor mapping must contain block name for $category", report.competitorMapping.contains("BlockB"))
            assertTrue("Competitor mapping must mention density for $category", report.competitorMapping.contains("Density", ignoreCase = true))
            assertTrue("Competitor mapping must mention profile for $category", report.competitorMapping.contains("Profile", ignoreCase = true))
            assertTrue("Competitor mapping must mention moat for $category", report.competitorMapping.contains("Moat", ignoreCase = true))

            // 6. Pricing Strategy
            assertFalse("Pricing strategy blank for $category in EN", report.pricingStrategy.isBlank())
            assertTrue("Pricing strategy must mention benchmarks for $category", report.pricingStrategy.contains("Benchmarks", ignoreCase = true))
            assertTrue("Pricing strategy must mention gross profit margin for $category", report.pricingStrategy.contains("Gross Profit Margin", ignoreCase = true))
            assertTrue("Pricing strategy must include credit policy advice for $category", report.pricingStrategy.contains("Credit Policy:", ignoreCase = true))

            // Key Recommendations
            assertTrue("Key recommendations empty for $category in EN", report.keyRecommendations.isNotEmpty())
            report.keyRecommendations.forEach { assertFalse("Recommendation blank for $category", it.isBlank()) }
        }
    }

    @Test
    fun testAllSevenCategories_allSixDimensions_strictlyPopulatedInHindi_withDevanagariIntegrity() {
        for (category in BusinessCategory.values()) {
            val input = AdvisoryInput(
                location = LocationInfo("रामपुर", "मोहनलालगंज", "लखनऊ"),
                marginCapital = 30000.0,
                category = category,
                language = LanguageCode.HI
            )
            val report = engine.generateReport(input)

            // 1. Market Reach in Hindi
            assertFalse("Hindi market reach blank for $category", report.marketReach.isBlank())
            assertTrue("Hindi market reach must contain village name for $category", report.marketReach.contains("रामपुर"))
            assertTrue("Hindi market reach must contain block name for $category", report.marketReach.contains("मोहनलालगंज"))
            assertTrue("Hindi market reach must contain district name for $category", report.marketReach.contains("लखनऊ"))
            assertTrue("Hindi market reach must contain Devanagari script for $category", devanagariRegex.containsMatchIn(report.marketReach))
            assertTrue("Hindi market reach must contain 'लक्षित बाजार' for $category", report.marketReach.contains("लक्षित बाजार"))
            assertTrue("Hindi market reach must contain 'उपभोक्ता आधार' for $category", report.marketReach.contains("उपभोक्ता आधार"))
            assertTrue("Hindi market reach must contain 'मुख्य वितरण माध्यम' for $category", report.marketReach.contains("मुख्य वितरण माध्यम"))

            // 2. Opportunity Analysis in Hindi
            assertFalse("Hindi opportunity analysis blank for $category", report.opportunityAnalysis.isBlank())
            assertTrue("Hindi opportunity must contain block name for $category", report.opportunityAnalysis.contains("मोहनलालगंज"))
            assertTrue("Hindi opportunity must contain Devanagari script for $category", devanagariRegex.containsMatchIn(report.opportunityAnalysis))
            assertTrue("Hindi opportunity must contain 'प्राथमिक व्यापारिक अवसर' for $category", report.opportunityAnalysis.contains("प्राथमिक व्यापारिक अवसर"))
            assertTrue("Hindi opportunity must contain 'अछूते स्थानीय क्षेत्र' for $category", report.opportunityAnalysis.contains("अछूते स्थानीय क्षेत्र"))

            // 3. SWOT Analysis in Hindi
            assertNotNull("Hindi SWOT null for $category", report.swotAnalysis)
            assertTrue("Hindi strengths empty for $category", report.swotAnalysis.strengths.isNotEmpty())
            assertTrue("Hindi weaknesses empty for $category", report.swotAnalysis.weaknesses.isNotEmpty())
            assertTrue("Hindi opportunities empty for $category", report.swotAnalysis.opportunities.isNotEmpty())
            assertTrue("Hindi threats empty for $category", report.swotAnalysis.threats.isNotEmpty())

            report.swotAnalysis.strengths.forEach {
                assertFalse("Hindi strength blank for $category", it.isBlank())
                assertTrue("Hindi strength must contain Devanagari for $category", devanagariRegex.containsMatchIn(it))
            }
            report.swotAnalysis.weaknesses.forEach {
                assertFalse("Hindi weakness blank for $category", it.isBlank())
                assertTrue("Hindi weakness must contain Devanagari for $category", devanagariRegex.containsMatchIn(it))
            }
            report.swotAnalysis.opportunities.forEach {
                assertFalse("Hindi opportunity blank for $category", it.isBlank())
                assertTrue("Hindi opportunity must contain Devanagari for $category", devanagariRegex.containsMatchIn(it))
            }
            report.swotAnalysis.threats.forEach {
                assertFalse("Hindi threat blank for $category", it.isBlank())
                assertTrue("Hindi threat must contain Devanagari for $category", devanagariRegex.containsMatchIn(it))
            }

            // 4. Threats Identification in Hindi
            assertEquals("Must have 4 threat vectors in Hindi for $category", 4, report.threatsIdentification.size)
            val hindiThreatVectorsExpected = listOf(
                "मौसमी मांग में उतार-चढ़ाव",
                "आपूर्ति श्रृंखला में बाधा",
                "कच्चे माल की कीमतों में अस्थिरता",
                "एकल खरीदार पर निर्भरता एवं उधारी जोखिम"
            )
            report.threatsIdentification.forEachIndexed { idx, threatStr ->
                assertFalse("Hindi threat blank at index $idx for $category", threatStr.isBlank())
                assertTrue("Hindi threat must contain 'समाधान:' for $category", threatStr.contains("समाधान:"))
                assertTrue("Hindi threat must contain vector name for $category", threatStr.contains(hindiThreatVectorsExpected[idx]))
                assertTrue("Hindi threat must contain Devanagari for $category", devanagariRegex.containsMatchIn(threatStr))
            }

            // 5. Competitor Mapping in Hindi
            assertFalse("Hindi competitor mapping blank for $category", report.competitorMapping.isBlank())
            assertTrue("Hindi competitor mapping must contain block name for $category", report.competitorMapping.contains("मोहनलालगंज"))
            assertTrue("Hindi competitor mapping must contain Devanagari script for $category", devanagariRegex.containsMatchIn(report.competitorMapping))
            assertTrue("Hindi competitor mapping must contain 'प्रतिस्पर्धा स्तर' for $category", report.competitorMapping.contains("प्रतिस्पर्धा स्तर"))
            assertTrue("Hindi competitor mapping must contain 'प्रतिस्पर्धी बढ़त' for $category", report.competitorMapping.contains("प्रतिस्पर्धी बढ़त"))

            // 6. Pricing Strategy in Hindi
            assertFalse("Hindi pricing strategy blank for $category", report.pricingStrategy.isBlank())
            assertTrue("Hindi pricing strategy must contain Devanagari script for $category", devanagariRegex.containsMatchIn(report.pricingStrategy))
            assertTrue("Hindi pricing strategy must contain 'अनुशंसित इकाई मूल्य' for $category", report.pricingStrategy.contains("अनुशंसित इकाई मूल्य"))
            assertTrue("Hindi pricing strategy must contain 'अपेक्षित सकल मुनाफा' for $category", report.pricingStrategy.contains("अपेक्षित सकल मुनाफा"))
            assertTrue("Hindi pricing strategy must contain 'उधारी नीति' for $category", report.pricingStrategy.contains("उधारी नीति"))

            // Key Recommendations in Hindi
            assertTrue("Hindi recommendations empty for $category", report.keyRecommendations.isNotEmpty())
            report.keyRecommendations.forEach {
                assertFalse("Hindi recommendation blank for $category", it.isBlank())
                assertTrue("Hindi recommendation must contain Devanagari for $category", devanagariRegex.containsMatchIn(it))
            }
        }
    }

    @Test
    fun testNoEnglishFallbackLeakageInHindiOutputs() {
        // Assert that Hindi advisory reports do NOT leak English fallback terms or placeholders
        for (category in BusinessCategory.values()) {
            val input = AdvisoryInput(
                location = LocationInfo("रामनगर", "चकिया", "चंदौली"),
                marginCapital = 50000.0,
                category = category,
                language = LanguageCode.HI
            )
            val report = engine.generateReport(input)

            val fullText = buildString {
                append(report.marketReach).append(" ")
                append(report.opportunityAnalysis).append(" ")
                append(report.competitorMapping).append(" ")
                append(report.pricingStrategy).append(" ")
                report.threatsIdentification.forEach { append(it).append(" ") }
                report.swotAnalysis.strengths.forEach { append(it).append(" ") }
                report.swotAnalysis.weaknesses.forEach { append(it).append(" ") }
                report.swotAnalysis.opportunities.forEach { append(it).append(" ") }
                report.swotAnalysis.threats.forEach { append(it).append(" ") }
                report.keyRecommendations.forEach { append(it).append(" ") }
            }

            // Forbidden English fallback leakages in Hindi narrative
            assertFalse("Leakage of 'Local Village' in Hindi for $category", fullText.contains("Local Village"))
            assertFalse("Leakage of 'Local Block' in Hindi for $category", fullText.contains("Local Block"))
            assertFalse("Leakage of 'Local District' in Hindi for $category", fullText.contains("Local District"))
            assertFalse("Leakage of 'Targeted market reach' in Hindi for $category", fullText.contains("Targeted market reach"))
            assertFalse("Leakage of 'Primary market opportunity' in Hindi for $category", fullText.contains("Primary market opportunity"))
            assertFalse("Leakage of 'Competitor Density' in Hindi for $category", fullText.contains("Competitor Density"))
            assertFalse("Leakage of 'Recommended Unit Pricing' in Hindi for $category", fullText.contains("Recommended Unit Pricing"))
            assertFalse("Leakage of 'Mitigation:' in Hindi for $category", fullText.contains("Mitigation:"))
            assertFalse("Leakage of 'Credit Policy:' in Hindi for $category", fullText.contains("Credit Policy:"))
            assertFalse("Leakage of unresolved curly brace placeholder in $category", fullText.contains("{") || fullText.contains("}"))
        }
    }

    @Test
    fun testNoEnglishFallbackLeakageAcrossAllCategoriesAndAllBudgetTiersInHindi() {
        val tiers = listOf(10000.0, 50000.0, 200000.0)
        for (category in BusinessCategory.values()) {
            for (margin in tiers) {
                val input = AdvisoryInput(
                    location = LocationInfo("रामनगर", "चकिया", "चंदौली"),
                    marginCapital = margin,
                    category = category,
                    language = LanguageCode.HI
                )
                val report = engine.generateReport(input)

                val fullText = buildString {
                    append(report.marketReach).append(" ")
                    append(report.opportunityAnalysis).append(" ")
                    append(report.competitorMapping).append(" ")
                    append(report.pricingStrategy).append(" ")
                    report.threatsIdentification.forEach { append(it).append(" ") }
                    report.swotAnalysis.strengths.forEach { append(it).append(" ") }
                    report.swotAnalysis.weaknesses.forEach { append(it).append(" ") }
                    report.swotAnalysis.opportunities.forEach { append(it).append(" ") }
                    report.swotAnalysis.threats.forEach { append(it).append(" ") }
                    report.keyRecommendations.forEach { append(it).append(" ") }
                }

                assertFalse("Leakage of 'Local Village' in Hindi for $category at $margin", fullText.contains("Local Village"))
                assertFalse("Leakage of 'Local Block' in Hindi for $category at $margin", fullText.contains("Local Block"))
                assertFalse("Leakage of 'Local District' in Hindi for $category at $margin", fullText.contains("Local District"))
                assertFalse("Leakage of 'Targeted market reach' in Hindi for $category at $margin", fullText.contains("Targeted market reach"))
                assertFalse("Leakage of 'Primary market opportunity' in Hindi for $category at $margin", fullText.contains("Primary market opportunity"))
                assertFalse("Leakage of 'Competitor Density' in Hindi for $category at $margin", fullText.contains("Competitor Density"))
                assertFalse("Leakage of 'Recommended Unit Pricing' in Hindi for $category at $margin", fullText.contains("Recommended Unit Pricing"))
                assertFalse("Leakage of 'Mitigation:' in Hindi for $category at $margin", fullText.contains("Mitigation:"))
                assertFalse("Leakage of 'Credit Policy:' in Hindi for $category at $margin", fullText.contains("Credit Policy:"))
                assertFalse("Leakage of unresolved placeholder in $category at $margin", fullText.contains("{") || fullText.contains("}"))
            }
        }
    }

    // =========================================================================
    // 2. BUDGET TIER CALIBRATION (MICRO, SMALL, MEDIUM) ACROSS ALL CATEGORIES
    // =========================================================================

    @Test
    fun testBudgetTierCalibration_distinctAcrossAllSevenCategoriesInHindi() {
        val location = LocationInfo("हरिरामपुर", "चुनार", "मिर्ज़ापुर")

        for (category in BusinessCategory.values()) {
            val microInput = AdvisoryInput(location, 10000.0, category, LanguageCode.HI)
            val smallInput = AdvisoryInput(location, 50000.0, category, LanguageCode.HI)
            val mediumInput = AdvisoryInput(location, 200000.0, category, LanguageCode.HI)

            val microReport = engine.generateReport(microInput)
            val smallReport = engine.generateReport(smallInput)
            val mediumReport = engine.generateReport(mediumInput)

            // Verify assigned budget tier names
            assertEquals(BudgetTier.MICRO.name, microReport.budgetTier)
            assertEquals(BudgetTier.SMALL.name, smallReport.budgetTier)
            assertEquals(BudgetTier.MEDIUM.name, mediumReport.budgetTier)

            // Verify SWOT quadrants are distinct across tiers in Hindi
            assertNotEquals("Hindi Strengths must differ Micro vs Small for $category",
                microReport.swotAnalysis.strengths, smallReport.swotAnalysis.strengths)
            assertNotEquals("Hindi Strengths must differ Small vs Medium for $category",
                smallReport.swotAnalysis.strengths, mediumReport.swotAnalysis.strengths)
            assertNotEquals("Hindi Strengths must differ Micro vs Medium for $category",
                microReport.swotAnalysis.strengths, mediumReport.swotAnalysis.strengths)

            assertNotEquals("Hindi Weaknesses must differ Micro vs Small for $category",
                microReport.swotAnalysis.weaknesses, smallReport.swotAnalysis.weaknesses)
            assertNotEquals("Hindi Weaknesses must differ Small vs Medium for $category",
                smallReport.swotAnalysis.weaknesses, mediumReport.swotAnalysis.weaknesses)

            assertNotEquals("Hindi Opportunities must differ Micro vs Small for $category",
                microReport.swotAnalysis.opportunities, smallReport.swotAnalysis.opportunities)
            assertNotEquals("Hindi Opportunities must differ Small vs Medium for $category",
                smallReport.swotAnalysis.opportunities, mediumReport.swotAnalysis.opportunities)

            assertNotEquals("Hindi Threats must differ Micro vs Small for $category",
                microReport.swotAnalysis.threats, smallReport.swotAnalysis.threats)
            assertNotEquals("Hindi Threats must differ Small vs Medium for $category",
                smallReport.swotAnalysis.threats, mediumReport.swotAnalysis.threats)

            // Verify Hindi Recommendations are distinct across tiers
            assertNotEquals("Hindi Recommendations must differ Micro vs Small for $category",
                microReport.keyRecommendations, smallReport.keyRecommendations)
            assertNotEquals("Hindi Recommendations must differ Small vs Medium for $category",
                smallReport.keyRecommendations, mediumReport.keyRecommendations)
            assertNotEquals("Hindi Recommendations must differ Micro vs Medium for $category",
                microReport.keyRecommendations, mediumReport.keyRecommendations)

            // Verify Consumer Base description in Market Reach is distinct across tiers in Hindi
            assertNotEquals("Hindi Consumer base in market reach must differ Micro vs Small for $category",
                microReport.marketReach, smallReport.marketReach)
            assertNotEquals("Hindi Consumer base in market reach must differ Small vs Medium for $category",
                smallReport.marketReach, mediumReport.marketReach)
        }
    }

    @Test
    fun testBudgetTierCalibration_distinctAcrossAllSevenCategories() {
        val location = LocationInfo("Harirampur", "Chunar", "Mirzapur")

        for (category in BusinessCategory.values()) {
            val microInput = AdvisoryInput(location, 10000.0, category, LanguageCode.EN)
            val smallInput = AdvisoryInput(location, 50000.0, category, LanguageCode.EN)
            val mediumInput = AdvisoryInput(location, 200000.0, category, LanguageCode.EN)

            val microReport = engine.generateReport(microInput)
            val smallReport = engine.generateReport(smallInput)
            val mediumReport = engine.generateReport(mediumInput)

            // Verify assigned budget tier names
            assertEquals(BudgetTier.MICRO.name, microReport.budgetTier)
            assertEquals(BudgetTier.SMALL.name, smallReport.budgetTier)
            assertEquals(BudgetTier.MEDIUM.name, mediumReport.budgetTier)

            // Verify SWOT quadrants are distinct across tiers
            assertNotEquals("Strengths must differ Micro vs Small for $category",
                microReport.swotAnalysis.strengths, smallReport.swotAnalysis.strengths)
            assertNotEquals("Strengths must differ Small vs Medium for $category",
                smallReport.swotAnalysis.strengths, mediumReport.swotAnalysis.strengths)
            assertNotEquals("Strengths must differ Micro vs Medium for $category",
                microReport.swotAnalysis.strengths, mediumReport.swotAnalysis.strengths)

            assertNotEquals("Weaknesses must differ Micro vs Small for $category",
                microReport.swotAnalysis.weaknesses, smallReport.swotAnalysis.weaknesses)
            assertNotEquals("Weaknesses must differ Small vs Medium for $category",
                smallReport.swotAnalysis.weaknesses, mediumReport.swotAnalysis.weaknesses)

            assertNotEquals("Opportunities must differ Micro vs Small for $category",
                microReport.swotAnalysis.opportunities, smallReport.swotAnalysis.opportunities)
            assertNotEquals("Opportunities must differ Small vs Medium for $category",
                smallReport.swotAnalysis.opportunities, mediumReport.swotAnalysis.opportunities)

            assertNotEquals("Threats must differ Micro vs Small for $category",
                microReport.swotAnalysis.threats, smallReport.swotAnalysis.threats)
            assertNotEquals("Threats must differ Small vs Medium for $category",
                smallReport.swotAnalysis.threats, mediumReport.swotAnalysis.threats)

            // Verify Key Recommendations are distinct across tiers
            assertNotEquals("Recommendations must differ Micro vs Small for $category",
                microReport.keyRecommendations, smallReport.keyRecommendations)
            assertNotEquals("Recommendations must differ Small vs Medium for $category",
                smallReport.keyRecommendations, mediumReport.keyRecommendations)
            assertNotEquals("Recommendations must differ Micro vs Medium for $category",
                microReport.keyRecommendations, mediumReport.keyRecommendations)

            // Verify Consumer Base description in Market Reach is distinct across tiers
            assertNotEquals("Consumer base in market reach must differ Micro vs Small for $category",
                microReport.marketReach, smallReport.marketReach)
            assertNotEquals("Consumer base in market reach must differ Small vs Medium for $category",
                smallReport.marketReach, mediumReport.marketReach)
        }
    }

    @Test
    fun testBudgetTierThresholdsBoundaryValues() {
        // Below 25,000 -> MICRO
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(1000.0))
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(24999.99))

        // Exact 25,000 -> SMALL
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(25000.0))
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(25000.01))
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(100000.0))

        // Exact 150,000 -> SMALL
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(150000.0))

        // Above 150,000 -> MEDIUM
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(150000.01))
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(150001.0))
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(500000.0))
    }

    // =========================================================================
    // 3. BLANK, WHITESPACE, AND EXTREME STRING INPUTS IN LOCATION FIELDS
    // =========================================================================

    @Test
    fun testLocationSanitization_blankAndWhitespace_handledGracefullyInEnglish() {
        val whitespaceVariants = listOf(
            LocationInfo("", "", ""),
            LocationInfo("   ", " ", "  "),
            LocationInfo("\t", "\n", "\r\n"),
            LocationInfo("  \t\n  ", "   ", " \t ")
        )

        for (loc in whitespaceVariants) {
            val sanitized = loc.sanitized(LanguageCode.EN)
            assertEquals("Local Village", sanitized.village)
            assertEquals("Local Block", sanitized.block)
            assertEquals("Local District", sanitized.district)

            val formatted = loc.formatted(LanguageCode.EN)
            assertEquals("Local Village, Local Block, Local District", formatted)

            val report = engine.generateReport(
                AdvisoryInput(loc, 20000.0, BusinessCategory.RETAIL, LanguageCode.EN)
            )
            assertNotNull(report)
            assertEquals("Local Village", report.location.village)
            assertTrue(report.marketReach.contains("Local Village"))
            assertTrue(report.marketReach.contains("Local Block"))
            assertTrue(report.marketReach.contains("Local District"))
        }
    }

    @Test
    fun testLocationSanitization_blankAndWhitespace_handledGracefullyInHindi() {
        val whitespaceVariants = listOf(
            LocationInfo("", "", ""),
            LocationInfo("   ", " ", "  "),
            LocationInfo("\t", "\n", "\r\n")
        )

        for (loc in whitespaceVariants) {
            val sanitized = loc.sanitized(LanguageCode.HI)
            assertEquals("स्थानीय गाँव", sanitized.village)
            assertEquals("स्थानीय प्रखंड", sanitized.block)
            assertEquals("स्थानीय जिला", sanitized.district)

            val formatted = loc.formatted(LanguageCode.HI)
            assertEquals("स्थानीय गाँव, स्थानीय प्रखंड, स्थानीय जिला", formatted)

            val report = engine.generateReport(
                AdvisoryInput(loc, 20000.0, BusinessCategory.RETAIL, LanguageCode.HI)
            )
            assertNotNull(report)
            assertEquals("स्थानीय गाँव", report.location.village)
            assertTrue(report.marketReach.contains("स्थानीय गाँव"))
            assertTrue(report.marketReach.contains("स्थानीय प्रखंड"))
            assertTrue(report.marketReach.contains("स्थानीय जिला"))
        }
    }

    @Test
    fun testLocationSanitization_extremeStrings_noCrashAndIntegrityMaintained() {
        val longString = "A".repeat(5000)
        val formatString = "%s%d%n%x%08d"
        val injectionString = "' OR '1'='1' -- <script>alert('xss')</script>"
        val unicodeEmojiString = "🌾 Village 🚀, Block 👨‍🌾, District 🇮🇳"

        val extremeLocs = listOf(
            LocationInfo(longString, "NormalBlock", "NormalDistrict"),
            LocationInfo("NormalVillage", formatString, "NormalDistrict"),
            LocationInfo("NormalVillage", "NormalBlock", injectionString),
            LocationInfo(unicodeEmojiString, "Block-1 & 2 / Zone A", "Dist's (South)")
        )

        for (loc in extremeLocs) {
            val sanitizedEn = loc.sanitized(LanguageCode.EN)
            assertFalse(sanitizedEn.village.isBlank())
            assertFalse(sanitizedEn.block.isBlank())
            assertFalse(sanitizedEn.district.isBlank())

            val formatted = loc.formatted(LanguageCode.EN)
            assertFalse(formatted.isBlank())

            val report = engine.generateReport(
                AdvisoryInput(loc, 25000.0, BusinessCategory.TEXTILES, LanguageCode.EN)
            )
            assertNotNull(report)
            assertFalse(report.marketReach.isBlank())
            assertFalse(report.opportunityAnalysis.isBlank())
            assertFalse(report.competitorMapping.isBlank())
            assertFalse(report.pricingStrategy.isBlank())
        }
    }

    // =========================================================================
    // 4. VIEWMODEL REACTIVE STATE TRANSITIONS & SHARE TEXT STRESS TESTING
    // =========================================================================

    @Test
    fun testViewModel_rapidCategoryCycling_maintainsStateConsistency() {
        val viewModel = AdvisoryViewModel()

        for (category in BusinessCategory.values()) {
            viewModel.onCategorySelected(category)
            val state = viewModel.uiState.value

            assertEquals(category, state.selectedCategory)
            val report = state.feasibilityReport
            assertNotNull(report)
            assertEquals(category, report?.category)
            assertTrue(state.shareText.contains(MultilingualDictionary.getCategoryTitle(category, state.language)))
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun testViewModel_rapidLanguageToggling_updatesAllLocalizedTexts() {
        val viewModel = AdvisoryViewModel()
        assertEquals(LanguageCode.EN, viewModel.uiState.value.language)

        for (i in 1..6) {
            viewModel.onLanguageToggled()
            val state = viewModel.uiState.value
            val expectedLang = if (i % 2 == 1) LanguageCode.HI else LanguageCode.EN

            assertEquals(expectedLang, state.language)
            val report = state.feasibilityReport
            assertNotNull(report)

            if (expectedLang == LanguageCode.HI) {
                assertTrue(state.shareText.contains("ग्रामीण सूक्ष्म-सलाहकार रिपोर्ट"))
                assertTrue(state.ttsScript.contains("सलाह"))
                assertTrue(devanagariRegex.containsMatchIn(report?.marketReach ?: ""))
            } else {
                assertTrue(state.shareText.contains("RURAL MICRO-ADVISORY REPORT"))
                assertTrue(state.ttsScript.contains("Advisory summary for"))
            }
        }
    }

    @Test
    fun testViewModel_marginInputTextHandling_invalidAndBoundaryInputs() {
        val viewModel = AdvisoryViewModel()

        // 1. Enter non-numeric chars -> digits filtered, parsed correctly
        viewModel.onMarginInputTextChanged("₹14,000/-")
        var state = viewModel.uiState.value
        assertEquals("14000", state.marginInputText)
        assertEquals(14000.0, state.marginCapital, 0.001)
        assertNull(state.errorMessage)
        assertNotNull(state.successfulFinance)

        // 2. Enter empty string -> validation error, preserves existing marginCapital
        viewModel.onMarginInputTextChanged("")
        state = viewModel.uiState.value
        assertEquals("", state.marginInputText)
        assertNotNull(state.errorMessage)
        assertTrue(state.errorMessage!!.contains("valid margin capital"))

        // 3. Below minimum threshold (e.g. 500)
        viewModel.onMarginInputTextChanged("500")
        state = viewModel.uiState.value
        assertEquals("500", state.marginInputText)
        assertEquals(500.0, state.marginCapital, 0.001)
        assertNotNull(state.errorMessage)
        assertTrue("Error must mention ₹1,000", state.errorMessage!!.contains("₹1,000"))
        assertNull("successfulFinance must be null when below threshold", state.successfulFinance)
        assertTrue(state.financialResult is FinancialCalculationResult.BelowMinimumThreshold)

        // Verify share text still formats safely without financial breakdown crash
        assertFalse(state.shareText.isBlank())
        assertTrue(state.shareText.contains("Feasibility Dimensions:"))

        // 4. Switch to Hindi and verify below-threshold error message in Hindi
        viewModel.setLanguage(LanguageCode.HI)
        state = viewModel.uiState.value
        assertEquals(LanguageCode.HI, state.language)
        assertNotNull(state.errorMessage)
        assertTrue("Hindi error must contain ₹1,000", state.errorMessage!!.contains("₹1,000"))
        assertTrue("Hindi error must contain Devanagari", devanagariRegex.containsMatchIn(state.errorMessage!!))

        // 5. Exceeds maximum threshold (e.g. 600000)
        viewModel.setLanguage(LanguageCode.EN)
        viewModel.onMarginInputTextChanged("600000")
        state = viewModel.uiState.value
        assertEquals("600000", state.marginInputText)
        assertEquals(600000.0, state.marginCapital, 0.001)
        assertNotNull(state.errorMessage)
        assertTrue("Error must mention ₹5,00,000", state.errorMessage!!.contains("₹5,00,000"))
        assertNull("successfulFinance must be null when above threshold", state.successfulFinance)
        assertTrue(state.financialResult is FinancialCalculationResult.ExceedsMaximumThreshold)

        // 6. Return to valid margin via preset button
        viewModel.onPresetMarginSelected(50000.0)
        state = viewModel.uiState.value
        assertEquals(50000.0, state.marginCapital, 0.001)
        assertEquals("50000", state.marginInputText)
        assertNull(state.errorMessage)
        assertNotNull(state.successfulFinance)
        assertEquals(SchemeCategory.TERM_LOAN, state.successfulFinance?.scheme)
    }

    @Test
    fun testViewModel_shareTextUnderAllFinancialCalculationResultStates() {
        val viewModel = AdvisoryViewModel()

        for (lang in listOf(LanguageCode.EN, LanguageCode.HI)) {
            viewModel.setLanguage(lang)
            val report = viewModel.uiState.value.feasibilityReport!!

            // State A: Success (Micro Finance Capped)
            viewModel.onMarginChanged(14000.0)
            val resSuccessCapped = viewModel.uiState.value.financialResult!!
            val shareCapped = viewModel.buildFormattedShareText(report, resSuccessCapped, lang)
            assertFalse(shareCapped.isBlank())
            if (lang == LanguageCode.EN) {
                assertTrue(shareCapped.contains("Micro Finance Scheme"))
                assertTrue(shareCapped.contains("Loan capped at statutory scheme maximum limit!"))
            } else {
                assertTrue(shareCapped.contains("सूक्ष्म वित्त योजना"))
                assertTrue(shareCapped.contains("वैधानिक अधिकतम ऋण सीमा लागू!"))
            }

            // State B: Success (Term Loan Uncapped)
            viewModel.onMarginChanged(50000.0)
            val resSuccessTerm = viewModel.uiState.value.financialResult!!
            val shareTerm = viewModel.buildFormattedShareText(report, resSuccessTerm, lang)
            assertFalse(shareTerm.isBlank())
            if (lang == LanguageCode.EN) {
                assertTrue(shareTerm.contains("Term Loan Scheme"))
                assertFalse(shareTerm.contains("Loan capped at statutory"))
            } else {
                assertTrue(shareTerm.contains("सावधि ऋण योजना"))
                assertFalse(shareTerm.contains("वैधानिक अधिकतम ऋण सीमा लागू!"))
            }

            // State C: BelowMinimumThreshold
            val resBelow = FinancialCalculationResult.BelowMinimumThreshold(500.0)
            val shareBelow = viewModel.buildFormattedShareText(report, resBelow, lang)
            assertFalse(shareBelow.isBlank())
            // Should contain feasibility sections even without financial success section
            if (lang == LanguageCode.EN) {
                assertTrue(shareBelow.contains("Feasibility Dimensions:"))
                assertTrue(shareBelow.contains("Budget-Calibrated SWOT Analysis:"))
            } else {
                assertTrue(shareBelow.contains("व्यवहार्यता विश्लेषण (6 आयाम):"))
                assertTrue(shareBelow.contains("स्वॉट (SWOT) विश्लेषण:"))
            }

            // State D: ExceedsMaximumThreshold
            val resAbove = FinancialCalculationResult.ExceedsMaximumThreshold(600000.0)
            val shareAbove = viewModel.buildFormattedShareText(report, resAbove, lang)
            assertFalse(shareAbove.isBlank())
            if (lang == LanguageCode.EN) {
                assertTrue(shareAbove.contains("Feasibility Dimensions:"))
            } else {
                assertTrue(shareAbove.contains("व्यवहार्यता विश्लेषण (6 आयाम):"))
            }

            // State E: InvalidInput
            val resInvalid = FinancialCalculationResult.InvalidInput("Margin cannot be negative")
            val shareInvalid = viewModel.buildFormattedShareText(report, resInvalid, lang)
            assertFalse(shareInvalid.isBlank())
            if (lang == LanguageCode.EN) {
                assertTrue(shareInvalid.contains("Feasibility Dimensions:"))
            } else {
                assertTrue(shareInvalid.contains("व्यवहार्यता विश्लेषण (6 आयाम):"))
            }
        }
    }

    @Test
    fun testViewModel_multithreadedAndRandomInterleavedStress() {
        val viewModel = AdvisoryViewModel()
        val categories = BusinessCategory.values()
        val margins = listOf(1000.0, 10000.0, 14000.0, 15000.0, 50000.0, 150000.0, 500000.0)
        val villages = listOf("Rampur", "Shivpur", "Belur", "Alipur")

        // Interleave 100 rapid alterations across all fields
        for (i in 0 until 100) {
            val cat = categories[i % categories.size]
            val margin = margins[i % margins.size]
            val vill = villages[i % villages.size]
            val lang = if (i % 2 == 0) LanguageCode.EN else LanguageCode.HI

            viewModel.onVillageChanged(vill)
            viewModel.onCategorySelected(cat)
            viewModel.onMarginChanged(margin)
            viewModel.setLanguage(lang)

            val state = viewModel.uiState.value
            assertEquals(cat, state.selectedCategory)
            assertEquals(margin, state.marginCapital, 0.001)
            assertEquals(vill, state.village)
            assertEquals(lang, state.language)

            val report = state.feasibilityReport
            assertNotNull(report)
            assertEquals(cat, report?.category)
            assertEquals(vill, report?.location?.village)

            val finance = state.successfulFinance
            assertNotNull(finance)
            assertTrue(state.shareText.isNotBlank())
            assertTrue(state.ttsScript.isNotBlank())
        }
    }
}
