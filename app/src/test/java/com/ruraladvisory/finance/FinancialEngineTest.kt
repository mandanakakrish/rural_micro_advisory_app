package com.ruraladvisory.finance

import com.ruraladvisory.finance.engine.DefaultFinancialCalculatorEngine
import com.ruraladvisory.finance.engine.FinancialCalculatorEngine
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [DefaultFinancialCalculatorEngine] validating debt structuring,
 * scheme routing, statutory loan capping, and threshold validation.
 */
class FinancialEngineTest {

    private lateinit var engine: FinancialCalculatorEngine

    @Before
    fun setUp() {
        engine = DefaultFinancialCalculatorEngine()
    }

    @Test
    fun testMargin10k_MicroFinance_StandardCase() {
        // Margin ₹10,000 -> Project Cost ₹1,00,000 -> Micro Finance (6.5%, 3-yr, 3-mo moratorium)
        val result = engine.calculate(10000.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)

        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(10000.0, success.structure.marginCapital, 0.001)
        assertEquals(100000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(90000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(90000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.schemeMaxCap, 0.001)
        assertFalse(success.structure.isCapped)
        assertEquals(10000.0, success.structure.effectivePromoterEquity, 0.001)
        assertEquals(0.90, success.structure.loanToCostRatio, 0.001)

        // Scheme properties
        assertEquals(0.065, success.scheme.annualInterestRate, 0.0001)
        assertEquals(12, success.scheme.tenureQuarters)
        assertEquals(1, success.scheme.moratoriumQuarters)
        assertEquals(11, success.scheme.repaymentQuarters)

        // Cost breakdown: 70% CAPEX, 25% WC, 5% Contingency
        assertEquals(70000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(25000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(5000.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(8333.33, success.costBreakdown.estimatedMonthlyOpex, 0.01)
        assertEquals(25000.0, success.costBreakdown.estimatedQuarterlyOpex, 0.001)
        assertEquals(13750.0, success.costBreakdown.rawMaterialsOpex, 0.001) // 55% of WC
        assertEquals(6250.0, success.costBreakdown.laborWagesOpex, 0.001)    // 25% of WC
        assertEquals(3000.0, success.costBreakdown.utilitiesLogisticsOpex, 0.001) // 12% of WC
        assertEquals(2000.0, success.costBreakdown.maintenanceSundryOpex, 0.001)  // 8% of WC
    }

    @Test
    fun testMargin14k_MicroFinance_UpperBoundary_StrictlyCapped() {
        // Margin ₹14,000 -> Project Cost ₹1,40,000 -> Micro Finance threshold
        // Uncapped 90% is ₹1,26,000 -> strictly capped to ₹1,25,000!
        val result = engine.calculate(14000.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)

        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(140000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(126000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.schemeMaxCap, 0.001)
        assertTrue("Loan must be flagged as capped", success.structure.isCapped)
        assertEquals(15000.0, success.structure.effectivePromoterEquity, 0.001) // 140k - 125k = 15k
        assertEquals(125000.0 / 140000.0, success.structure.loanToCostRatio, 0.0001)

        // Cost breakdown
        assertEquals(98000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(35000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(7000.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(11666.67, success.costBreakdown.estimatedMonthlyOpex, 0.01)
        assertEquals(35000.0, success.costBreakdown.estimatedQuarterlyOpex, 0.001)
    }

    @Test
    fun testMargin15k_TermLoan_LowerBoundary() {
        // Margin ₹15,000 -> Project Cost ₹1,50,000 -> Term Loan (8.0%, 7-yr, 6-mo moratorium)
        val result = engine.calculate(15000.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)

        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(150000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(135000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(135000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(4500000.0, success.structure.schemeMaxCap, 0.001)
        assertFalse(success.structure.isCapped)
        assertEquals(15000.0, success.structure.effectivePromoterEquity, 0.001)
        assertEquals(0.90, success.structure.loanToCostRatio, 0.001)

        // Scheme properties
        assertEquals(0.080, success.scheme.annualInterestRate, 0.0001)
        assertEquals(28, success.scheme.tenureQuarters)
        assertEquals(2, success.scheme.moratoriumQuarters)
        assertEquals(26, success.scheme.repaymentQuarters)

        // Cost breakdown
        assertEquals(105000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(37500.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(7500.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(12500.0, success.costBreakdown.estimatedMonthlyOpex, 0.01)
    }

    @Test
    fun testMargin500k_TermLoan_UpperBoundary_MaxCap() {
        // Margin ₹5,00,000 -> Project Cost ₹50,00,000 -> Term Loan maximum cap
        val result = engine.calculate(500000.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)

        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(5000000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(4500000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(4500000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(4500000.0, success.structure.schemeMaxCap, 0.001)
        assertFalse(success.structure.isCapped)
        assertEquals(500000.0, success.structure.effectivePromoterEquity, 0.001)
        assertEquals(0.90, success.structure.loanToCostRatio, 0.001)

        // Cost breakdown
        assertEquals(3500000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(1250000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(250000.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(416666.67, success.costBreakdown.estimatedMonthlyOpex, 0.01)
    }

    @Test
    fun testSchemeTransition_RightAboveBoundary() {
        // Margin ₹14,001 -> Project Cost ₹140,010 -> Instantly switches to Term Loan
        val result = engine.calculate(14001.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(140010.0, success.structure.totalProjectCost, 0.001)
        assertEquals(126009.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(126009.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
    }

    @Test
    fun testMargin1000_LowerPermissibleLimit() {
        // Margin ₹1,000 -> Project Cost ₹10,000 -> Micro Finance minimum allowed
        val result = engine.calculate(1000.0)
        assertTrue("Expected Success result", result is FinancialCalculationResult.Success)
        val success = result as FinancialCalculationResult.Success
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(10000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(9000.0, success.structure.actualLoanAmount, 0.001)
    }

    @Test
    fun testBelowMinimumThreshold_RejectsMarginUnder1000() {
        val result = engine.calculate(999.99)
        assertTrue(result is FinancialCalculationResult.BelowMinimumThreshold)
        val failure = result as FinancialCalculationResult.BelowMinimumThreshold
        assertEquals(999.99, failure.enteredMargin, 0.001)
        assertEquals(1000.0, failure.minimumAllowedMargin, 0.001)
        assertEquals(10000.0, failure.minimumAllowedProjectCost, 0.001)

        val resultSmall = engine.calculate(500.0)
        assertTrue(resultSmall is FinancialCalculationResult.BelowMinimumThreshold)
    }

    @Test
    fun testExceedsMaximumThreshold_RejectsCostOver50Lakh() {
        val result = engine.calculate(500001.0)
        assertTrue(result is FinancialCalculationResult.ExceedsMaximumThreshold)
        val failure = result as FinancialCalculationResult.ExceedsMaximumThreshold
        assertEquals(500001.0, failure.enteredMargin, 0.001)
        assertEquals(500000.0, failure.maximumAllowedMargin, 0.001)
        assertEquals(5000000.0, failure.maximumAllowedProjectCost, 0.001)
    }

    @Test
    fun testInvalidInputs_HandledCleanly() {
        assertTrue(engine.calculate(0.0) is FinancialCalculationResult.InvalidInput)
        assertTrue(engine.calculate(-1000.0) is FinancialCalculationResult.InvalidInput)
        assertTrue(engine.calculate(Double.NaN) is FinancialCalculationResult.InvalidInput)
        assertTrue(engine.calculate(Double.POSITIVE_INFINITY) is FinancialCalculationResult.InvalidInput)
        assertTrue(engine.calculate(Double.NEGATIVE_INFINITY) is FinancialCalculationResult.InvalidInput)
    }
}
