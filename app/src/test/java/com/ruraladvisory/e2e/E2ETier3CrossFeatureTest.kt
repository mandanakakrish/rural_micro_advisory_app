package com.ruraladvisory.e2e

import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.finance.engine.DefaultFinancialCalculatorEngine
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tier 3 E2E Test Suite: Cross-Feature Combinations.
 * Evaluates pairwise combinations across orthogonal dimensions:
 * Category (7 sectors) x Budget Tier (3 tiers) x Scheme (2 schemes) x Language (EN/HI).
 *
 * Ensures holistic integration without cross-module regressions or state leakages.
 */
class E2ETier3CrossFeatureTest {

    private lateinit var financialEngine: DefaultFinancialCalculatorEngine
    private lateinit var advisoryEngine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        financialEngine = DefaultFinancialCalculatorEngine()
        advisoryEngine = OfflineHeuristicAdvisoryEngine()
    }

    private fun verifyE2EPair(
        category: BusinessCategory,
        margin: Double,
        expectedTier: BudgetTier,
        expectedScheme: SchemeCategory,
        language: LanguageCode,
        location: LocationInfo
    ) {
        // 1. Evaluate Financial Engine
        val finResult = financialEngine.calculate(margin)
        assertTrue("Financial calculation must succeed for margin $margin", finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        assertEquals("Scheme must match expected", expectedScheme, success.scheme)
        assertEquals(margin * 10.0, success.structure.totalProjectCost, 0.001)

        val schedule = success.schedule
        val finalQ = schedule.quarters.last()
        assertEquals(0.0, finalQ.closingPrincipal, 0.0001)
        assertEquals(success.structure.actualLoanAmount, schedule.totalPrincipalPaid, 0.01)

        // 2. Evaluate Advisory Engine
        val advInput = AdvisoryInput(
            location = location,
            marginCapital = margin,
            category = category,
            language = language
        )
        val report = advisoryEngine.generateReport(advInput)

        assertEquals(category, report.category)
        assertEquals(expectedTier.name, report.budgetTier)
        assertFalse(report.marketReach.isBlank())
        assertFalse(report.opportunityAnalysis.isBlank())
        assertEquals(4, report.threatsIdentification.size)
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
        assertTrue(report.swotAnalysis.weaknesses.isNotEmpty())
        assertTrue(report.swotAnalysis.opportunities.isNotEmpty())
        assertTrue(report.swotAnalysis.threats.isNotEmpty())
        assertTrue(report.keyRecommendations.isNotEmpty())

        if (language == LanguageCode.HI) {
            val devanagariRegex = Regex("[\\u0900-\\u097F]")
            assertTrue("Hindi report must contain Devanagari in marketReach", devanagariRegex.containsMatchIn(report.marketReach))
            assertTrue("Hindi report must contain Devanagari in opportunity", devanagariRegex.containsMatchIn(report.opportunityAnalysis))
            assertTrue("Hindi report must contain Devanagari in pricing", devanagariRegex.containsMatchIn(report.pricingStrategy))
        }
    }

    // =========================================================================
    // Category x Budget Tier x Scheme x Language Pairwise Combinations
    // =========================================================================

    @Test
    fun testPairwise_Dairy_MicroTier_MicroFinance_English() {
        verifyE2EPair(
            category = BusinessCategory.DAIRY,
            margin = 10000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.EN,
            location = LocationInfo("Verka", "Amritsar-I", "Amritsar")
        )
    }

    @Test
    fun testPairwise_Dairy_SmallTier_TermLoan_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.DAIRY,
            margin = 50000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("सांगवी", "बारामती", "पुणे")
        )
    }

    @Test
    fun testPairwise_Retail_MicroTier_MicroFinance_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.RETAIL,
            margin = 12000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.HI,
            location = LocationInfo("चौक बाज़ार", "हंडिया", "प्रयागराज")
        )
    }

    @Test
    fun testPairwise_Retail_SmallTier_TermLoan_English() {
        verifyE2EPair(
            category = BusinessCategory.RETAIL,
            margin = 80000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.EN,
            location = LocationInfo("Kalyanpur", "Bithoor", "Kanpur")
        )
    }

    @Test
    fun testPairwise_FoodProcessing_MicroTier_TermLoan_English() {
        // Cross-boundary: Margin ₹18,000 -> BudgetTier MICRO (< 25k), but Scheme TERM_LOAN (> 14k)
        verifyE2EPair(
            category = BusinessCategory.FOOD_PROCESSING,
            margin = 18000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.EN,
            location = LocationInfo("Kalyani", "Nadia", "Nadia")
        )
    }

    @Test
    fun testPairwise_FoodProcessing_SmallTier_TermLoan_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.FOOD_PROCESSING,
            margin = 100000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("हाजीपुर", "वैशाली", "वैशाली")
        )
    }

    @Test
    fun testPairwise_FoodProcessing_MediumTier_TermLoan_English() {
        verifyE2EPair(
            category = BusinessCategory.FOOD_PROCESSING,
            margin = 250000.0,
            expectedTier = BudgetTier.MEDIUM,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.EN,
            location = LocationInfo("Ratnagiri", "Chiplun", "Ratnagiri")
        )
    }

    @Test
    fun testPairwise_Textiles_MicroTier_MicroFinance_English_Capped() {
        // Margin ₹14,000 -> Micro Finance strictly capped to ₹1,25,000
        verifyE2EPair(
            category = BusinessCategory.TEXTILES,
            margin = 14000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.EN,
            location = LocationInfo("Chanderi", "Chanderi", "Ashoknagar")
        )
    }

    @Test
    fun testPairwise_Textiles_SmallTier_TermLoan_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.TEXTILES,
            margin = 60000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("महेश्वर", "खरगोन", "खरगोन")
        )
    }

    @Test
    fun testPairwise_Poultry_MicroTier_MicroFinance_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.POULTRY,
            margin = 11000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.HI,
            location = LocationInfo("अमरोहा देहात", "जोया", "अमरोहा")
        )
    }

    @Test
    fun testPairwise_Poultry_SmallTier_TermLoan_English() {
        verifyE2EPair(
            category = BusinessCategory.POULTRY,
            margin = 35000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.EN,
            location = LocationInfo("Anekal", "Anekal", "Bengaluru Rural")
        )
    }

    @Test
    fun testPairwise_Poultry_MediumTier_TermLoan_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.POULTRY,
            margin = 300000.0,
            expectedTier = BudgetTier.MEDIUM,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("करनाल गाँव", "घरौंडा", "करनाल")
        )
    }

    @Test
    fun testPairwise_Handicrafts_MicroTier_MicroFinance_English() {
        verifyE2EPair(
            category = BusinessCategory.HANDICRAFTS,
            margin = 13000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.EN,
            location = LocationInfo("Raghurajpur", "Puri", "Puri")
        )
    }

    @Test
    fun testPairwise_Handicrafts_SmallTier_TermLoan_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.HANDICRAFTS,
            margin = 40000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("कोंडागाँव", "बस्तर", "बस्तर")
        )
    }

    @Test
    fun testPairwise_AgroServices_MicroTier_MicroFinance_Hindi() {
        verifyE2EPair(
            category = BusinessCategory.AGRO_SERVICES,
            margin = 10000.0,
            expectedTier = BudgetTier.MICRO,
            expectedScheme = SchemeCategory.MICRO_FINANCE,
            language = LanguageCode.HI,
            location = LocationInfo("शेखपुरा", "बरबीघा", "शेखपुरा")
        )
    }

    @Test
    fun testPairwise_AgroServices_SmallTier_TermLoan_English() {
        verifyE2EPair(
            category = BusinessCategory.AGRO_SERVICES,
            margin = 120000.0,
            expectedTier = BudgetTier.SMALL,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.EN,
            location = LocationInfo("Karnal", "Nilokheri", "Karnal")
        )
    }

    @Test
    fun testPairwise_AgroServices_MediumTier_TermLoan_Hindi_MaxCap() {
        verifyE2EPair(
            category = BusinessCategory.AGRO_SERVICES,
            margin = 500000.0,
            expectedTier = BudgetTier.MEDIUM,
            expectedScheme = SchemeCategory.TERM_LOAN,
            language = LanguageCode.HI,
            location = LocationInfo("बठिंडा देहात", "तलवंडी साबो", "बठिंडा")
        )
    }

    @Test
    fun testPairwise_LanguageSwitchPreservesIdenticalFinancialMath() {
        val margin = 75000.0
        val finResult = financialEngine.calculate(margin) as FinancialCalculationResult.Success

        val location = LocationInfo("Sanganer", "Sanganer", "Jaipur")
        val enReport = advisoryEngine.generateReport(AdvisoryInput(location, margin, BusinessCategory.TEXTILES, LanguageCode.EN))
        val hiReport = advisoryEngine.generateReport(AdvisoryInput(location, margin, BusinessCategory.TEXTILES, LanguageCode.HI))

        // Financial parameters remain invariant regardless of advisory language
        assertEquals(750000.0, finResult.structure.totalProjectCost, 0.001)
        assertEquals(675000.0, finResult.structure.actualLoanAmount, 0.001)
        assertEquals(enReport.budgetTier, hiReport.budgetTier)
        assertEquals(enReport.category, hiReport.category)
    }
}
