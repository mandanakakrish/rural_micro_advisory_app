package com.ruraladvisory.advisory

import com.ruraladvisory.advisory.engine.FeasibilityAdvisoryEngine
import com.ruraladvisory.advisory.engine.HybridAdvisoryEngine
import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.advisory.model.SwotAnalysis
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FeasibilityEngineTest {

    private lateinit var engine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        engine = OfflineHeuristicAdvisoryEngine()
    }

    @Test
    fun testAllSevenCategoriesProduceCompleteReportsInEnglish() {
        for (category in BusinessCategory.values()) {
            val input = AdvisoryInput(
                location = LocationInfo("Rampur", "Mohanlalganj", "Lucknow"),
                marginCapital = 50000.0,
                category = category,
                language = LanguageCode.EN
            )
            val report = engine.generateReport(input)

            assertNotNull("Report must not be null for $category", report)
            assertEquals("Category must match", category, report.category)
            assertEquals("Budget tier must match", BudgetTier.SMALL.name, report.budgetTier)
            assertTrue("isOfflineGenerated must be true", report.isOfflineGenerated)

            // 1. Market Reach
            assertFalse("Market Reach must not be blank", report.marketReach.isBlank())
            assertTrue("Market Reach should mention radius", report.marketReach.contains("radius", ignoreCase = true))

            // 2. Opportunity Analysis
            assertFalse("Opportunity Analysis must not be blank", report.opportunityAnalysis.isBlank())

            // 3. SWOT Analysis (all 4 quadrants non-empty)
            assertNotNull("SWOT must not be null", report.swotAnalysis)
            assertTrue("Strengths must not be empty for $category", report.swotAnalysis.strengths.isNotEmpty())
            assertTrue("Weaknesses must not be empty for $category", report.swotAnalysis.weaknesses.isNotEmpty())
            assertTrue("Opportunities must not be empty for $category", report.swotAnalysis.opportunities.isNotEmpty())
            assertTrue("Threats must not be empty for $category", report.swotAnalysis.threats.isNotEmpty())

            // 4. Threats Identification (4 vectors with mitigations)
            assertEquals("Must have 4 threat items for $category", 4, report.threatsIdentification.size)
            for (threatStr in report.threatsIdentification) {
                assertFalse("Threat string must not be blank", threatStr.isBlank())
                assertTrue("Threat string must contain mitigation", threatStr.contains("Mitigation", ignoreCase = true))
            }

            // 5. Competitor Mapping
            assertFalse("Competitor Mapping must not be blank", report.competitorMapping.isBlank())
            assertTrue("Competitor Mapping must mention density", report.competitorMapping.contains("Density", ignoreCase = true))

            // 6. Pricing Strategy
            assertFalse("Pricing Strategy must not be blank", report.pricingStrategy.isBlank())
            assertTrue("Pricing Strategy must contain credit policy", report.pricingStrategy.contains("Credit Policy", ignoreCase = true))

            // Key Recommendations
            assertTrue("Key Recommendations must not be empty for $category", report.keyRecommendations.isNotEmpty())
        }
    }

    @Test
    fun testAllSevenCategoriesProduceCompleteReportsInHindi() {
        for (category in BusinessCategory.values()) {
            val input = AdvisoryInput(
                location = LocationInfo("रामपुर", "मोहनलालगंज", "लखनऊ"),
                marginCapital = 30000.0,
                category = category,
                language = LanguageCode.HI
            )
            val report = engine.generateReport(input)

            assertNotNull("Hindi report must not be null for $category", report)
            assertEquals(category, report.category)
            assertTrue(report.isOfflineGenerated)

            // Verify Hindi content presence (Devanagari Unicode range: \u0900-\u097F)
            val devanagariRegex = Regex("[\\u0900-\\u097F]")
            assertTrue("Market reach in Hindi must contain Devanagari", devanagariRegex.containsMatchIn(report.marketReach))
            assertTrue("Opportunity in Hindi must contain Devanagari", devanagariRegex.containsMatchIn(report.opportunityAnalysis))
            assertTrue("Competitor in Hindi must contain Devanagari", devanagariRegex.containsMatchIn(report.competitorMapping))
            assertTrue("Pricing in Hindi must contain Devanagari", devanagariRegex.containsMatchIn(report.pricingStrategy))

            // Check SWOT in Hindi
            assertTrue(report.swotAnalysis.strengths.all { devanagariRegex.containsMatchIn(it) })
            assertTrue(report.swotAnalysis.weaknesses.all { devanagariRegex.containsMatchIn(it) })
            assertTrue(report.swotAnalysis.opportunities.all { devanagariRegex.containsMatchIn(it) })
            assertTrue(report.swotAnalysis.threats.all { devanagariRegex.containsMatchIn(it) })

            // Check threats in Hindi
            assertEquals(4, report.threatsIdentification.size)
            assertTrue(report.threatsIdentification.all { it.contains("समाधान") })

            // Check recommendations in Hindi
            assertTrue(report.keyRecommendations.isNotEmpty())
            assertTrue(report.keyRecommendations.all { devanagariRegex.containsMatchIn(it) })
        }
    }

    @Test
    fun testBudgetTierCalibrationDifferences() {
        val location = LocationInfo("Peepli", "Chomu", "Jaipur")
        val category = BusinessCategory.DAIRY

        val microInput = AdvisoryInput(location, 15000.0, category, LanguageCode.EN)
        val smallInput = AdvisoryInput(location, 80000.0, category, LanguageCode.EN)
        val mediumInput = AdvisoryInput(location, 250000.0, category, LanguageCode.EN)

        val microReport = engine.generateReport(microInput)
        val smallReport = engine.generateReport(smallInput)
        val mediumReport = engine.generateReport(mediumInput)

        assertEquals(BudgetTier.MICRO.name, microReport.budgetTier)
        assertEquals(BudgetTier.SMALL.name, smallReport.budgetTier)
        assertEquals(BudgetTier.MEDIUM.name, mediumReport.budgetTier)

        // Verify that SWOT differs across budget tiers
        assertNotEquals(
            "Micro strengths must differ from Small strengths",
            microReport.swotAnalysis.strengths,
            smallReport.swotAnalysis.strengths
        )
        assertNotEquals(
            "Small strengths must differ from Medium strengths",
            smallReport.swotAnalysis.strengths,
            mediumReport.swotAnalysis.strengths
        )

        // Verify recommendations differ across budget tiers
        assertNotEquals(
            "Micro recommendations must differ from Small recommendations",
            microReport.keyRecommendations,
            smallReport.keyRecommendations
        )
        assertNotEquals(
            "Small recommendations must differ from Medium recommendations",
            smallReport.keyRecommendations,
            mediumReport.keyRecommendations
        )
    }

    @Test
    fun testBlankLocationSanitization() {
        val blankInputEn = AdvisoryInput(
            location = LocationInfo("   ", "", " \t "),
            marginCapital = 20000.0,
            category = BusinessCategory.RETAIL,
            language = LanguageCode.EN
        )
        val reportEn = engine.generateReport(blankInputEn)
        assertFalse("Report location must not contain 'null'", reportEn.location.village.contains("null"))
        assertEquals("Local Village", reportEn.location.village)
        assertEquals("Local Block", reportEn.location.block)
        assertEquals("Local District", reportEn.location.district)
        assertTrue(reportEn.marketReach.contains("Local Village"))

        val blankInputHi = AdvisoryInput(
            location = LocationInfo("  ", "", " "),
            marginCapital = 20000.0,
            category = BusinessCategory.RETAIL,
            language = LanguageCode.HI
        )
        val reportHi = engine.generateReport(blankInputHi)
        assertEquals("स्थानीय गाँव", reportHi.location.village)
        assertEquals("स्थानीय प्रखंड", reportHi.location.block)
        assertEquals("स्थानीय जिला", reportHi.location.district)
        assertTrue(reportHi.marketReach.contains("स्थानीय गाँव"))
    }

    @Test
    fun testOfflineResilienceExecutionSpeed() {
        val input = AdvisoryInput(
            location = LocationInfo("Barabanki", "Haidergarh", "Barabanki"),
            marginCapital = 100000.0,
            category = BusinessCategory.AGRO_SERVICES,
            language = LanguageCode.EN
        )

        // Warm up run
        engine.generateReport(input)

        // Measure execution time over 20 iterations
        val iterations = 20
        val startTime = System.nanoTime()
        for (i in 1..iterations) {
            val report = engine.generateReport(input)
            assertNotNull(report)
        }
        val totalTimeNanos = System.nanoTime() - startTime
        val avgTimeMillis = (totalTimeNanos / iterations) / 1_000_000.0

        assertTrue(
            "Average report generation must be sub-50ms (was ${avgTimeMillis}ms)",
            avgTimeMillis < 50.0
        )
    }

    @Test
    fun testHybridEngineFallbackWhenUnconfigured() {
        val hybrid = HybridAdvisoryEngine(apiKey = null)
        val input = AdvisoryInput(
            location = LocationInfo("Patan", "Sidhpur", "Patan"),
            marginCapital = 50000.0,
            category = BusinessCategory.TEXTILES,
            language = LanguageCode.EN
        )
        val report = hybrid.generateReport(input)

        assertNotNull(report)
        assertTrue("Report must fall back to offline engine when unconfigured", report.isOfflineGenerated)
        assertEquals(BusinessCategory.TEXTILES, report.category)
        assertFalse(report.opportunityAnalysis.isBlank())
    }

    @Test
    fun testHybridEngineFallbackWhenRemoteFails() {
        val failingHybrid = HybridAdvisoryEngine(
            apiKey = "mock-key",
            remoteProvider = { throw RuntimeException("Network timeout simulation") }
        )
        val input = AdvisoryInput(
            location = LocationInfo("Patan", "Sidhpur", "Patan"),
            marginCapital = 50000.0,
            category = BusinessCategory.POULTRY,
            language = LanguageCode.EN
        )

        val report = failingHybrid.generateReport(input)
        assertNotNull(report)
        assertTrue("Must silently fall back to offline engine on exception", report.isOfflineGenerated)
        assertEquals(BusinessCategory.POULTRY, report.category)
    }

    @Test
    fun testHybridEngineReturnsRemoteWhenSuccessful() {
        val mockRemoteReport = FeasibilityReport(
            category = BusinessCategory.HANDICRAFTS,
            location = LocationInfo("VillageA", "BlockB", "DistrictC"),
            budgetTier = "MICRO",
            marketReach = "Remote enhanced reach",
            opportunityAnalysis = "Remote enhanced opportunity",
            swotAnalysis = SwotAnalysis(listOf("S"), listOf("W"), listOf("O"), listOf("T")),
            threatsIdentification = listOf("Threat 1"),
            competitorMapping = "Remote mapping",
            pricingStrategy = "Remote pricing",
            keyRecommendations = listOf("Rec 1"),
            isOfflineGenerated = true // Will be set to false by Hybrid engine
        )

        val successfulHybrid = HybridAdvisoryEngine(
            apiKey = "valid-key",
            remoteProvider = { mockRemoteReport }
        )
        val input = AdvisoryInput(
            location = LocationInfo("VillageA", "BlockB", "DistrictC"),
            marginCapital = 10000.0,
            category = BusinessCategory.HANDICRAFTS,
            language = LanguageCode.EN
        )

        val report = successfulHybrid.generateReport(input)
        assertNotNull(report)
        assertFalse("Successful remote report must have isOfflineGenerated = false", report.isOfflineGenerated)
        assertEquals("Remote enhanced reach", report.marketReach)
    }
}
