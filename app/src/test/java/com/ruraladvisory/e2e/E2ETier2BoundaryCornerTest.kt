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
 * Tier 2 E2E Test Suite: Boundary & Corner Cases.
 * Evaluates extreme limits, statutory cap transitions, input validation rejections,
 * floating-point threshold precision, whitespace sanitization, and Unicode handling.
 *
 * Adheres strictly to requirements in ORIGINAL_REQUEST.md (§R2.1, §R2.2, §R4.1) and PROJECT.md.
 */
class E2ETier2BoundaryCornerTest {

    private lateinit var financialEngine: DefaultFinancialCalculatorEngine
    private lateinit var advisoryEngine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        financialEngine = DefaultFinancialCalculatorEngine()
        advisoryEngine = OfflineHeuristicAdvisoryEngine()
    }

    // =========================================================================
    // 1. Minimum Threshold Boundaries (₹1,000 margin / ₹10,000 project cost)
    // =========================================================================

    @Test
    fun testBoundary_Margin1000_ExactMinimumPermissible() {
        val result = financialEngine.calculate(1000.0)
        assertTrue("Margin ₹1,000 must succeed", result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(10000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(9000.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
    }

    @Test
    fun testBoundary_Margin999_99_RejectionBelowMinimum() {
        val result = financialEngine.calculate(999.99)
        assertTrue("Margin ₹999.99 must be rejected", result is FinancialCalculationResult.BelowMinimumThreshold)
        val failure = result as FinancialCalculationResult.BelowMinimumThreshold
        assertEquals(999.99, failure.enteredMargin, 0.001)
        assertEquals(1000.0, failure.minimumAllowedMargin, 0.001)
        assertEquals(10000.0, failure.minimumAllowedProjectCost, 0.001)
    }

    @Test
    fun testBoundary_Margin500_RejectionBelowMinimum() {
        val result = financialEngine.calculate(500.0)
        assertTrue("Margin ₹500 must be rejected", result is FinancialCalculationResult.BelowMinimumThreshold)
        val failure = result as FinancialCalculationResult.BelowMinimumThreshold
        assertEquals(500.0, failure.enteredMargin, 0.001)
    }

    // =========================================================================
    // 2. Micro Finance Standard & Upper Boundary (₹10k, ₹14k Capped)
    // =========================================================================

    @Test
    fun testBoundary_Margin10000_StandardMicroFinance() {
        val result = financialEngine.calculate(10000.0)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(100000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(90000.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
    }

    @Test
    fun testBoundary_Margin14000_UpperMicroFinance_StrictlyCappedAt125k() {
        // At Margin ₹14,000 -> Project Cost ₹1,40,000 (Upper limit of Micro Finance)
        // 90% Uncapped = ₹1,26,000. Scheme statutory cap = ₹1,25,000!
        val result = financialEngine.calculate(14000.0)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(140000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(126000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.actualLoanAmount, 0.001)
        assertTrue("Loan must be flagged as capped", success.structure.isCapped)
        assertEquals(15000.0, success.structure.effectivePromoterEquity, 0.001)
    }

    // =========================================================================
    // 3. Micro Finance to Term Loan Scheme Transition (₹14,000.01, ₹14,001, ₹15k)
    // =========================================================================

    @Test
    fun testBoundary_Margin14000_01_InfinitesimalCrossoverToTermLoan() {
        // Margin ₹14,000.01 -> Cost ₹1,40,000.10 (> ₹1.40L threshold)
        // Must immediately route to Term Loan Scheme!
        val result = financialEngine.calculate(14000.01)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(140000.10, success.structure.totalProjectCost, 0.001)
        assertEquals(126000.09, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(126000.09, success.structure.actualLoanAmount, 0.001)
        assertFalse("Term loan cap is 45L, so 1.26L is not capped", success.structure.isCapped)
        assertEquals(28, success.scheme.tenureQuarters)
        assertEquals(2, success.scheme.moratoriumQuarters)
    }

    @Test
    fun testBoundary_Margin14001_WholeRupeeStepIntoTermLoan() {
        val result = financialEngine.calculate(14001.0)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(140010.0, success.structure.totalProjectCost, 0.001)
        assertEquals(126009.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
    }

    @Test
    fun testBoundary_Margin15000_StandardTermLoanLowerBoundary() {
        // Margin ₹15,000 -> Project Cost ₹1,50,000 -> Term Loan
        val result = financialEngine.calculate(15000.0)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(150000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(135000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(0.080, success.scheme.annualInterestRate, 0.0001)
        assertEquals(28, success.scheme.tenureQuarters)
    }

    // =========================================================================
    // 4. Budget Tier Boundaries (₹25,000 and ₹1,50,000)
    // =========================================================================

    @Test
    fun testBoundary_Margin24999_99_UpperBoundaryOfMicroBudgetTier() {
        assertEquals(BudgetTier.MICRO, BudgetTier.fromMargin(24999.99))
    }

    @Test
    fun testBoundary_Margin25000_LowerBoundaryOfSmallBudgetTier() {
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(25000.00))
    }

    @Test
    fun testBoundary_Margin150000_UpperBoundaryOfSmallBudgetTier() {
        assertEquals(BudgetTier.SMALL, BudgetTier.fromMargin(150000.00))
    }

    @Test
    fun testBoundary_Margin150000_01_LowerBoundaryOfMediumBudgetTier() {
        assertEquals(BudgetTier.MEDIUM, BudgetTier.fromMargin(150000.01))
    }

    // =========================================================================
    // 5. Maximum Permissible Ceiling Boundaries (₹5,00,000 / ₹50 Lakh Cost)
    // =========================================================================

    @Test
    fun testBoundary_Margin499999_99_JustBelowMaximumPermissibleMargin() {
        val result = financialEngine.calculate(499999.99)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(4999999.90, success.structure.totalProjectCost, 0.001)
    }

    @Test
    fun testBoundary_Margin500000_ExactMaximumPermissibleMargin_MaxCapEnforced() {
        // Margin ₹5,00,000 -> Project Cost ₹50,00,000
        // 90% Loan = ₹45,00,000 (Exact statutory cap of Term Loan)
        val result = financialEngine.calculate(500000.0)
        assertTrue(result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(5000000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(4500000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(4500000.0, success.structure.schemeMaxCap, 0.001)
        assertEquals(500000.0, success.structure.effectivePromoterEquity, 0.001)
    }

    @Test
    fun testBoundary_Margin500000_01_RejectionExceedsMaximumThreshold() {
        val result = financialEngine.calculate(500000.01)
        assertTrue("Margin ₹500,000.01 must be rejected", result is FinancialCalculationResult.ExceedsMaximumThreshold)
        val failure = result as FinancialCalculationResult.ExceedsMaximumThreshold
        assertEquals(500000.01, failure.enteredMargin, 0.001)
        assertEquals(500000.0, failure.maximumAllowedMargin, 0.001)
        assertEquals(5000000.0, failure.maximumAllowedProjectCost, 0.001)
    }

    @Test
    fun testBoundary_Margin500001_RejectionExceedsMaximumThreshold() {
        val result = financialEngine.calculate(500001.0)
        assertTrue("Margin ₹500,001 must be rejected", result is FinancialCalculationResult.ExceedsMaximumThreshold)
        val failure = result as FinancialCalculationResult.ExceedsMaximumThreshold
        assertEquals(500001.0, failure.enteredMargin, 0.001)
    }

    // =========================================================================
    // 6. Corner & Invalid Numerical Inputs
    // =========================================================================

    @Test
    fun testCorner_ZeroMargin_ReturnsInvalidInput() {
        val result = financialEngine.calculate(0.0)
        assertTrue(result is FinancialCalculationResult.InvalidInput)
    }

    @Test
    fun testCorner_NegativeMargin_ReturnsInvalidInput() {
        val result = financialEngine.calculate(-1000.0)
        assertTrue(result is FinancialCalculationResult.InvalidInput)
    }

    @Test
    fun testCorner_DoubleNaN_ReturnsInvalidInput() {
        val result = financialEngine.calculate(Double.NaN)
        assertTrue(result is FinancialCalculationResult.InvalidInput)
    }

    @Test
    fun testCorner_DoublePositiveInfinity_ReturnsInvalidInput() {
        val result = financialEngine.calculate(Double.POSITIVE_INFINITY)
        assertTrue(result is FinancialCalculationResult.InvalidInput)
    }

    @Test
    fun testCorner_DoubleNegativeInfinity_ReturnsInvalidInput() {
        val result = financialEngine.calculate(Double.NEGATIVE_INFINITY)
        assertTrue(result is FinancialCalculationResult.InvalidInput)
    }

    // =========================================================================
    // 7. Location String Sanitization & Unicode Handling
    // =========================================================================

    @Test
    fun testCorner_LocationEmptyStrings_EnglishSanitizationFallback() {
        val input = AdvisoryInput(
            location = LocationInfo("", "", ""),
            marginCapital = 20000.0,
            category = BusinessCategory.RETAIL,
            language = LanguageCode.EN
        )
        val report = advisoryEngine.generateReport(input)
        assertEquals("Local Village", report.location.village)
        assertEquals("Local Block", report.location.block)
        assertEquals("Local District", report.location.district)
        assertFalse("Report must not have literal blank location", report.marketReach.contains("null"))
    }

    @Test
    fun testCorner_LocationWhitespaceAndTabs_HindiSanitizationFallback() {
        val input = AdvisoryInput(
            location = LocationInfo("   \t", " \n ", "   "),
            marginCapital = 20000.0,
            category = BusinessCategory.RETAIL,
            language = LanguageCode.HI
        )
        val report = advisoryEngine.generateReport(input)
        assertEquals("स्थानीय गाँव", report.location.village)
        assertEquals("स्थानीय प्रखंड", report.location.block)
        assertEquals("स्थानीय जिला", report.location.district)
    }

    @Test
    fun testCorner_LocationMixedBlankFields_SanitizesOnlyBlanks() {
        val input = AdvisoryInput(
            location = LocationInfo("Kothapalli", "  ", "Warangal"),
            marginCapital = 30000.0,
            category = BusinessCategory.TEXTILES,
            language = LanguageCode.EN
        )
        val report = advisoryEngine.generateReport(input)
        assertEquals("Kothapalli", report.location.village)
        assertEquals("Local Block", report.location.block)
        assertEquals("Warangal", report.location.district)
    }

    @Test
    fun testCorner_LocationDevanagariUnicodeInput_PreservedFaithfully() {
        val input = AdvisoryInput(
            location = LocationInfo("बड़ोदरा गाँव", "वाघोडिया", "वडोदरा"),
            marginCapital = 40000.0,
            category = BusinessCategory.DAIRY,
            language = LanguageCode.HI
        )
        val report = advisoryEngine.generateReport(input)
        assertEquals("बड़ोदरा गाँव", report.location.village)
        assertEquals("वाघोडिया", report.location.block)
        assertEquals("वडोदरा", report.location.district)
        assertTrue(report.marketReach.contains("बड़ोदरा गाँव"))
    }

    @Test
    fun testCorner_LocationSpecialCharacters_PreservedWithoutEscapingCorruption() {
        val input = AdvisoryInput(
            location = LocationInfo("St. Mary's-Puram", "Sector-9/B", "Hubli-Dharwad"),
            marginCapital = 25000.0,
            category = BusinessCategory.FOOD_PROCESSING,
            language = LanguageCode.EN
        )
        val report = advisoryEngine.generateReport(input)
        assertEquals("St. Mary's-Puram", report.location.village)
        assertEquals("Sector-9/B", report.location.block)
        assertEquals("Hubli-Dharwad", report.location.district)
        assertTrue(report.marketReach.contains("St. Mary's-Puram"))
    }
}
