package com.ruraladvisory.e2e

import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tier 1 E2E Test Suite: Feature Coverage in Isolation.
 * Verifies every core capability across the financial engine, scheme router,
 * feasibility dimensions, all 7 trade sectors, and bilingual dictionary parity.
 *
 * Adheres strictly to requirements in ORIGINAL_REQUEST.md (§R1, §R2) and PROJECT.md.
 */
class E2ETier1FeatureCoverageTest {

    private lateinit var financialEngine: DefaultFinancialCalculatorEngine
    private lateinit var advisoryEngine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        financialEngine = DefaultFinancialCalculatorEngine()
        advisoryEngine = OfflineHeuristicAdvisoryEngine()
    }

    // =========================================================================
    // 1. Project Cost Scaling (10x Margin)
    // =========================================================================

    @Test
    fun testProjectCostScaling_Margin1000_ProducesCost10k() {
        val result = financialEngine.calculate(1000.0) as FinancialCalculationResult.Success
        assertEquals(10000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(1000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testProjectCostScaling_Margin5000_ProducesCost50k() {
        val result = financialEngine.calculate(5000.0) as FinancialCalculationResult.Success
        assertEquals(50000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(5000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testProjectCostScaling_Margin10000_ProducesCost100k() {
        val result = financialEngine.calculate(10000.0) as FinancialCalculationResult.Success
        assertEquals(100000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(10000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testProjectCostScaling_Margin25000_ProducesCost250k() {
        val result = financialEngine.calculate(25000.0) as FinancialCalculationResult.Success
        assertEquals(250000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(25000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testProjectCostScaling_Margin100000_ProducesCost1000k() {
        val result = financialEngine.calculate(100000.0) as FinancialCalculationResult.Success
        assertEquals(1000000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(100000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testProjectCostScaling_Margin500000_ProducesCost5000k() {
        val result = financialEngine.calculate(500000.0) as FinancialCalculationResult.Success
        assertEquals(5000000.0, result.structure.totalProjectCost, 0.001)
        assertEquals(500000.0 * 10.0, result.structure.totalProjectCost, 0.001)
    }

    // =========================================================================
    // 2. Uncapped Loan Structuring (90% Loan to Cost)
    // =========================================================================

    @Test
    fun testUncappedLoanCalculation_StrictlyNinetyPercentOfCost() {
        val margins = doubleArrayOf(1000.0, 5000.0, 10000.0, 12000.0, 20000.0, 50000.0)
        for (margin in margins) {
            val result = financialEngine.calculate(margin) as FinancialCalculationResult.Success
            val expectedCost = margin * 10.0
            val expectedUncapped = expectedCost * 0.90
            assertEquals(expectedUncapped, result.structure.uncappedLoanAmount, 0.001)
        }
    }

    // =========================================================================
    // 3. Scheme Routing & Parameter Verification
    // =========================================================================

    @Test
    fun testMicroFinanceSchemeRouting_TenureAndRates() {
        val result = financialEngine.calculate(10000.0) as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, result.scheme)
        assertEquals(0.065, result.scheme.annualInterestRate, 0.0001)
        assertEquals(12, result.scheme.tenureQuarters)
        assertEquals(3, result.scheme.tenureYears)
        assertEquals(1, result.scheme.moratoriumQuarters)
        assertEquals(3, result.scheme.moratoriumMonths)
        assertEquals(11, result.scheme.repaymentQuarters)
        assertEquals(125000.0, result.scheme.maxLoanCap, 0.001)
    }

    @Test
    fun testTermLoanSchemeRouting_TenureAndRates() {
        val result = financialEngine.calculate(20000.0) as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, result.scheme)
        assertEquals(0.080, result.scheme.annualInterestRate, 0.0001)
        assertEquals(28, result.scheme.tenureQuarters)
        assertEquals(7, result.scheme.tenureYears)
        assertEquals(2, result.scheme.moratoriumQuarters)
        assertEquals(6, result.scheme.moratoriumMonths)
        assertEquals(26, result.scheme.repaymentQuarters)
        assertEquals(4500000.0, result.scheme.maxLoanCap, 0.001)
    }

    // =========================================================================
    // 4. Moratorium Simple Interest Math
    // =========================================================================

    @Test
    fun testMoratoriumInterestAccrual_MicroFinanceQuarter1() {
        // Loan = ₹90,000; Rate = 6.5% / 4 = 1.625%
        // Moratorium Interest = 90,000 * 0.01625 = ₹1,462.50
        val result = financialEngine.calculate(10000.0) as FinancialCalculationResult.Success
        val q1 = result.schedule.quarters[0]
        assertTrue("Quarter 1 must be moratorium", q1.isMoratorium)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(1462.50, q1.interestAccrual, 0.001)
        assertEquals(1462.50, q1.totalQuarterlyOutflow, 0.001)
        assertEquals(90000.0, q1.closingPrincipal, 0.001)
        assertEquals(1462.50, result.schedule.totalMoratoriumInterest, 0.001)
    }

    @Test
    fun testMoratoriumInterestAccrual_TermLoanQuarters1And2() {
        // Loan = ₹1,80,000; Rate = 8.0% / 4 = 2.0%
        // Moratorium Interest = 180,000 * 0.02 = ₹3,600.00 each quarter
        val result = financialEngine.calculate(20000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        assertEquals(2, schedule.moratoriumQuartersCount)

        val q1 = schedule.quarters[0]
        val q2 = schedule.quarters[1]

        assertTrue(q1.isMoratorium)
        assertTrue(q2.isMoratorium)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(0.0, q2.principalRepayment, 0.001)
        assertEquals(3600.0, q1.interestAccrual, 0.001)
        assertEquals(3600.0, q2.interestAccrual, 0.001)
        assertEquals(7200.0, schedule.totalMoratoriumInterest, 0.001)
    }

    // =========================================================================
    // 5. Straight-Line Principal Amortization & Penny Balancing
    // =========================================================================

    @Test
    fun testStraightLinePrincipalAmortization_MicroFinance11Installments() {
        val result = financialEngine.calculate(10000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        assertEquals(11, schedule.repaymentQuartersCount)

        // Loan = 90,000 / 11 = 8181.8181... -> 8181.82 base installment
        for (qIdx in 1..10) {
            val q = schedule.quarters[qIdx]
            assertFalse(q.isMoratorium)
            assertEquals(8181.82, q.principalRepayment, 0.001)
        }
    }

    @Test
    fun testStraightLinePrincipalAmortization_TermLoan26Installments() {
        val result = financialEngine.calculate(20000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        assertEquals(26, schedule.repaymentQuartersCount)

        // Loan = 180,000 / 26 = 6923.0769... -> 6923.08 base installment
        for (qIdx in 2..26) {
            val q = schedule.quarters[qIdx]
            assertFalse(q.isMoratorium)
            assertEquals(6923.08, q.principalRepayment, 0.001)
        }
    }

    @Test
    fun testPennyBalancing_GuaranteesZeroClosingBalance() {
        val result = financialEngine.calculate(10000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        val lastQuarter = schedule.quarters.last()

        assertEquals(0.0, lastQuarter.closingPrincipal, 0.0001)
        assertEquals(schedule.quarters[10].closingPrincipal, lastQuarter.principalRepayment, 0.001)
        assertEquals(result.structure.actualLoanAmount, schedule.totalPrincipalPaid, 0.001)
    }

    // =========================================================================
    // 6. Cost & OPEX Breakdown Calculations
    // =========================================================================

    @Test
    fun testCostBreakdown_Capex70Percent() {
        val result = financialEngine.calculate(50000.0) as FinancialCalculationResult.Success
        val cost = result.structure.totalProjectCost // 500,000
        assertEquals(350000.0, result.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(cost * 0.70, result.costBreakdown.fixedAssetsCapex, 0.001)
    }

    @Test
    fun testCostBreakdown_WorkingCapital25Percent() {
        val result = financialEngine.calculate(50000.0) as FinancialCalculationResult.Success
        val cost = result.structure.totalProjectCost // 500,000
        assertEquals(125000.0, result.costBreakdown.workingCapital, 0.001)
        assertEquals(cost * 0.25, result.costBreakdown.workingCapital, 0.001)
    }

    @Test
    fun testCostBreakdown_Contingency5Percent() {
        val result = financialEngine.calculate(50000.0) as FinancialCalculationResult.Success
        val cost = result.structure.totalProjectCost // 500,000
        assertEquals(25000.0, result.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(cost * 0.05, result.costBreakdown.contingencyBuffer, 0.001)
    }

    @Test
    fun testCostBreakdown_OpexSubAllocationsSumToWorkingCapital() {
        val result = financialEngine.calculate(50000.0) as FinancialCalculationResult.Success
        val cb = result.costBreakdown
        val wc = cb.workingCapital // 125,000

        assertEquals(wc * 0.55, cb.rawMaterialsOpex, 0.001)
        assertEquals(wc * 0.25, cb.laborWagesOpex, 0.001)
        assertEquals(wc * 0.12, cb.utilitiesLogisticsOpex, 0.001)
        assertEquals(wc * 0.08, cb.maintenanceSundryOpex, 0.001)

        val subTotal = cb.rawMaterialsOpex + cb.laborWagesOpex + cb.utilitiesLogisticsOpex + cb.maintenanceSundryOpex
        assertEquals(wc, subTotal, 0.01)
    }

    // =========================================================================
    // 7. All 7 Trade Sectors Advisory Synthesis
    // =========================================================================

    private fun generateReportForCategory(category: BusinessCategory) =
        advisoryEngine.generateReport(
            AdvisoryInput(
                location = LocationInfo("VillageA", "BlockB", "DistrictC"),
                marginCapital = 30000.0,
                category = category,
                language = LanguageCode.EN
            )
        )

    @Test
    fun testAdvisorySector_Dairy() {
        val report = generateReportForCategory(BusinessCategory.DAIRY)
        assertEquals(BusinessCategory.DAIRY, report.category)
        assertTrue(report.marketReach.contains("milk", ignoreCase = true) || report.marketReach.contains("dairy", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_Retail() {
        val report = generateReportForCategory(BusinessCategory.RETAIL)
        assertEquals(BusinessCategory.RETAIL, report.category)
        assertTrue(report.marketReach.contains("kirana", ignoreCase = true) || report.marketReach.contains("retail", ignoreCase = true) || report.marketReach.contains("haat", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_FoodProcessing() {
        val report = generateReportForCategory(BusinessCategory.FOOD_PROCESSING)
        assertEquals(BusinessCategory.FOOD_PROCESSING, report.category)
        assertTrue(report.opportunityAnalysis.contains("processing", ignoreCase = true) || report.opportunityAnalysis.contains("value", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_Textiles() {
        val report = generateReportForCategory(BusinessCategory.TEXTILES)
        assertEquals(BusinessCategory.TEXTILES, report.category)
        assertTrue(report.opportunityAnalysis.contains("tailor", ignoreCase = true) || report.opportunityAnalysis.contains("uniform", ignoreCase = true) || report.opportunityAnalysis.contains("kurti", ignoreCase = true) || report.opportunityAnalysis.contains("clothing", ignoreCase = true) || report.opportunityAnalysis.contains("textile", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_Poultry() {
        val report = generateReportForCategory(BusinessCategory.POULTRY)
        assertEquals(BusinessCategory.POULTRY, report.category)
        assertTrue(report.pricingStrategy.contains("bird", ignoreCase = true) || report.pricingStrategy.contains("poultry", ignoreCase = true) || report.pricingStrategy.contains("credit", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_Handicrafts() {
        val report = generateReportForCategory(BusinessCategory.HANDICRAFTS)
        assertEquals(BusinessCategory.HANDICRAFTS, report.category)
        assertTrue(report.marketReach.contains("radius", ignoreCase = true) || report.marketReach.contains("mela", ignoreCase = true) || report.marketReach.contains("pilgrimage", ignoreCase = true) || report.marketReach.contains("caterer", ignoreCase = true) || report.marketReach.contains("craft", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    @Test
    fun testAdvisorySector_AgroServices() {
        val report = generateReportForCategory(BusinessCategory.AGRO_SERVICES)
        assertEquals(BusinessCategory.AGRO_SERVICES, report.category)
        assertTrue(report.competitorMapping.contains("machinery", ignoreCase = true) || report.competitorMapping.contains("tiller", ignoreCase = true) || report.competitorMapping.contains("service", ignoreCase = true) || report.competitorMapping.contains("density", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    // =========================================================================
    // 8. All 6 Feasibility Dimensions Verification
    // =========================================================================

    private val sampleReport by lazy {
        advisoryEngine.generateReport(
            AdvisoryInput(
                location = LocationInfo("Rampur", "Bilaspur", "Rampur"),
                marginCapital = 50000.0,
                category = BusinessCategory.DAIRY,
                language = LanguageCode.EN
            )
        )
    }

    @Test
    fun testFeasibilityDimension_MarketReach() {
        assertFalse(sampleReport.marketReach.isBlank())
        assertTrue("Market reach must state radius coverage", sampleReport.marketReach.contains("radius", ignoreCase = true))
    }

    @Test
    fun testFeasibilityDimension_OpportunityAnalysis() {
        assertFalse(sampleReport.opportunityAnalysis.isBlank())
    }

    @Test
    fun testFeasibilityDimension_SwotAnalysis() {
        assertNotNull(sampleReport.swotAnalysis)
        assertTrue("Strengths must be populated", sampleReport.swotAnalysis.strengths.isNotEmpty())
        assertTrue("Weaknesses must be populated", sampleReport.swotAnalysis.weaknesses.isNotEmpty())
        assertTrue("Opportunities must be populated", sampleReport.swotAnalysis.opportunities.isNotEmpty())
        assertTrue("Threats must be populated", sampleReport.swotAnalysis.threats.isNotEmpty())
    }

    @Test
    fun testFeasibilityDimension_ThreatsIdentification() {
        assertEquals("Must contain exactly 4 localized risk vectors", 4, sampleReport.threatsIdentification.size)
        sampleReport.threatsIdentification.forEach { threat ->
            assertFalse(threat.isBlank())
            assertTrue("Threat must include mitigation strategy", threat.contains("Mitigation", ignoreCase = true))
        }
    }

    @Test
    fun testFeasibilityDimension_CompetitorMapping() {
        assertFalse(sampleReport.competitorMapping.isBlank())
        assertTrue("Competitor mapping must estimate density", sampleReport.competitorMapping.contains("Density", ignoreCase = true))
    }

    @Test
    fun testFeasibilityDimension_PricingStrategy() {
        assertFalse(sampleReport.pricingStrategy.isBlank())
        assertTrue("Pricing must include credit policy", sampleReport.pricingStrategy.contains("Credit Policy", ignoreCase = true))
    }

    // =========================================================================
    // 9. Multilingual Dictionary Parity (English & Hindi)
    // =========================================================================

    @Test
    fun testBilingualParity_AllCategoryTitles() {
        for (cat in BusinessCategory.values()) {
            val en = MultilingualDictionary.getCategoryTitle(cat, LanguageCode.EN)
            val hi = MultilingualDictionary.getCategoryTitle(cat, LanguageCode.HI)
            assertFalse("English title must not be blank for $cat", en.isBlank())
            assertFalse("Hindi title must not be blank for $cat", hi.isBlank())
            assertTrue("Hindi title must contain Devanagari characters", hi.any { it in '\u0900'..'\u097F' })
        }
    }

    @Test
    fun testBilingualParity_AllCategoryDescriptions() {
        for (cat in BusinessCategory.values()) {
            val en = MultilingualDictionary.getCategoryDescription(cat, LanguageCode.EN)
            val hi = MultilingualDictionary.getCategoryDescription(cat, LanguageCode.HI)
            assertFalse("English description must not be blank for $cat", en.isBlank())
            assertFalse("Hindi description must not be blank for $cat", hi.isBlank())
            assertTrue("Hindi description must contain Devanagari characters", hi.any { it in '\u0900'..'\u097F' })
        }
    }

    @Test
    fun testBilingualParity_AllDimensionHeaders() {
        val dimensions = listOf(
            MultilingualDictionary.DIMENSION_MARKET_REACH,
            MultilingualDictionary.DIMENSION_OPPORTUNITY,
            MultilingualDictionary.DIMENSION_SWOT,
            MultilingualDictionary.DIMENSION_THREATS,
            MultilingualDictionary.DIMENSION_COMPETITOR,
            MultilingualDictionary.DIMENSION_PRICING
        )
        for (dim in dimensions) {
            val en = MultilingualDictionary.getDimensionTitle(dim, LanguageCode.EN)
            val hi = MultilingualDictionary.getDimensionTitle(dim, LanguageCode.HI)
            assertFalse("English header must not be blank for $dim", en.isBlank())
            assertFalse("Hindi header must not be blank for $dim", hi.isBlank())
            assertTrue("Hindi header must contain Devanagari", hi.any { it in '\u0900'..'\u097F' })
        }
    }

    @Test
    fun testBilingualParity_AllBudgetTierTitles() {
        for (tier in BudgetTier.values()) {
            val en = MultilingualDictionary.getBudgetTierLabel(tier, LanguageCode.EN)
            val hi = MultilingualDictionary.getBudgetTierLabel(tier, LanguageCode.HI)
            assertFalse("English tier title must not be blank for $tier", en.isBlank())
            assertFalse("Hindi tier title must not be blank for $tier", hi.isBlank())
            assertTrue("Hindi tier title must contain Devanagari", hi.any { it in '\u0900'..'\u097F' })
        }
    }

    @Test
    fun testBilingualParity_CreditPolicyAdvice() {
        val en = MultilingualDictionary.getCreditPolicyAdvice(LanguageCode.EN)
        val hi = MultilingualDictionary.getCreditPolicyAdvice(LanguageCode.HI)
        assertFalse(en.isBlank())
        assertFalse(hi.isBlank())
        assertTrue("Hindi credit advice must contain Devanagari", hi.any { it in '\u0900'..'\u097F' })
    }

    @Test
    fun testBilingualParity_SwotQuadrantHeaders() {
        val quadrants = listOf("strengths", "weaknesses", "opportunities", "threats")
        for (q in quadrants) {
            val en = MultilingualDictionary.getSwotQuadrantTitle(q, LanguageCode.EN)
            val hi = MultilingualDictionary.getSwotQuadrantTitle(q, LanguageCode.HI)
            assertFalse(en.isBlank())
            assertFalse(hi.isBlank())
            assertTrue("Hindi quadrant header must contain Devanagari", hi.any { it in '\u0900'..'\u097F' })
        }
    }

    // =========================================================================
    // 10. Budget Tier Classification Verification
    // =========================================================================

    @Test
    fun testBudgetTierClassification_MicroTier() {
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(1000.0))
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(12000.0))
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(24999.0))
    }

    @Test
    fun testBudgetTierClassification_SmallTier() {
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(25000.0))
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(80000.0))
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(150000.0))
    }

    @Test
    fun testBudgetTierClassification_MediumTier() {
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(150000.01))
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(300000.0))
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(500000.0))
    }
}
