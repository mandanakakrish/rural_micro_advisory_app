package com.ruraladvisory.finance

import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.finance.engine.DefaultFinancialCalculatorEngine
import com.ruraladvisory.finance.engine.FinancialCalculatorEngine
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Random

/**
 * Tier 5 Adversarial Stress & Correctness Verification Suite.
 *
 * Exercises extreme boundary transitions, floating point precision drift,
 * penny-rounding invariants, amortization conservation, and string/Unicode
 * resilience across 1,000+ margin points.
 */
class Tier5AdversarialStressTest {

    private lateinit var financeEngine: FinancialCalculatorEngine
    private lateinit var advisoryEngine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        financeEngine = DefaultFinancialCalculatorEngine()
        advisoryEngine = OfflineHeuristicAdvisoryEngine()
    }

    // ------------------------------------------------------------------------
    // CHALLENGE 1: Floating-Point Precision & Rounding across 1000+ Samples
    // ------------------------------------------------------------------------

    @Test
    fun testEmpiricalAmortizationInvariants_Across1000RandomMargins() {
        val random = Random(42_1337)
        val sampleCount = 1000

        var microFinanceCount = 0
        var termLoanCount = 0
        var cappedCount = 0

        for (i in 1..sampleCount) {
            // Generate random margin between ₹1,000.00 and ₹500,000.00 with arbitrary 2-digit cents
            val rawMargin = 1000.0 + random.nextDouble() * (500000.0 - 1000.0)
            val margin = BigDecimal.valueOf(rawMargin).setScale(2, RoundingMode.HALF_UP).toDouble()

            val result = financeEngine.calculate(margin)
            assertTrue("Expected Success for margin $margin (sample $i)", result is FinancialCalculationResult.Success)

            val success = result as FinancialCalculationResult.Success
            val structure = success.structure
            val schedule = success.schedule
            val quarters = schedule.quarters

            if (success.scheme == SchemeCategory.MICRO_FINANCE) {
                microFinanceCount++
                assertEquals("Micro Finance tenure must be 12 quarters", 12, quarters.size)
                assertEquals(1, schedule.moratoriumQuartersCount)
                assertEquals(11, schedule.repaymentQuartersCount)
            } else {
                termLoanCount++
                assertEquals("Term Loan tenure must be 28 quarters", 28, quarters.size)
                assertEquals(2, schedule.moratoriumQuartersCount)
                assertEquals(26, schedule.repaymentQuartersCount)
            }

            if (structure.isCapped) {
                cappedCount++
            }

            // Invariant A: Sum of principal repayments strictly equals actualLoanAmount
            var manualPrincipalSum = 0.0
            var manualInterestSum = 0.0

            for (qIdx in quarters.indices) {
                val q = quarters[qIdx]
                manualPrincipalSum = BigDecimal.valueOf(manualPrincipalSum)
                    .add(BigDecimal.valueOf(q.principalRepayment))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()

                manualInterestSum = BigDecimal.valueOf(manualInterestSum)
                    .add(BigDecimal.valueOf(q.interestAccrual))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()

                // Quarter outflow invariant: Outflow == Principal + Interest
                val expectedOutflow = BigDecimal.valueOf(q.principalRepayment)
                    .add(BigDecimal.valueOf(q.interestAccrual))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
                assertEquals(
                    "Quarter ${q.quarterIndex} outflow must match principal + interest at margin $margin",
                    expectedOutflow,
                    q.totalQuarterlyOutflow,
                    0.0001
                )

                // Continuity invariant: Opening of Q(n) == Closing of Q(n-1)
                if (qIdx > 0) {
                    val prevQ = quarters[qIdx - 1]
                    assertEquals(
                        "Quarter ${q.quarterIndex} opening must equal Q${prevQ.quarterIndex} closing at margin $margin",
                        prevQ.closingPrincipal,
                        q.openingPrincipal,
                        0.0001
                    )
                }

                // Moratorium invariant: Principal = 0 during moratorium
                if (q.isMoratorium) {
                    assertEquals(
                        "Principal repayment during moratorium must be 0.0 at Q${q.quarterIndex}, margin $margin",
                        0.0,
                        q.principalRepayment,
                        0.0001
                    )
                    assertEquals(
                        "Closing principal during moratorium must equal opening principal at Q${q.quarterIndex}, margin $margin",
                        q.openingPrincipal,
                        q.closingPrincipal,
                        0.0001
                    )
                }
            }

            // Invariant B: Sum of principal repaid strictly equals actualLoanAmount
            assertEquals(
                "Sum of principal repaid must strictly equal actualLoanAmount for margin $margin",
                structure.actualLoanAmount,
                manualPrincipalSum,
                0.0001
            )
            assertEquals(
                "schedule.totalPrincipalPaid must strictly equal actualLoanAmount for margin $margin",
                structure.actualLoanAmount,
                schedule.totalPrincipalPaid,
                0.0001
            )

            // Invariant C: Final closing principal must equal exactly 0.00
            val finalQuarter = quarters.last()
            assertEquals(
                "Final closing principal must be exactly 0.00 for margin $margin",
                0.0,
                finalQuarter.closingPrincipal,
                0.0000001
            )

            // Invariant D: Total outflow equals totalPrincipalPaid + totalInterestPaid
            val expectedTotalOutflow = BigDecimal.valueOf(schedule.totalPrincipalPaid)
                .add(BigDecimal.valueOf(schedule.totalInterestPaid))
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
            assertEquals(
                "Schedule totalOutflow must equal totalPrincipalPaid + totalInterestPaid for margin $margin",
                expectedTotalOutflow,
                schedule.totalOutflow,
                0.0001
            )
        }

        assertTrue("Should have tested multiple Micro Finance cases", microFinanceCount > 0)
        assertTrue("Should have tested multiple Term Loan cases", termLoanCount > 0)
    }

    // ------------------------------------------------------------------------
    // CHALLENGE 2: High-Density Precision Grid Around Critical Transitions
    // ------------------------------------------------------------------------

    @Test
    fun testDensePrecisionGrid_LowerThreshold() {
        // High-density sweep around ₹1,000 limit with 1-cent steps
        val margins = listOf(
            1000.00, 1000.01, 1000.02, 1000.05, 1000.10,
            1000.25, 1000.33, 1000.50, 1000.77, 1000.99,
            1001.00, 1005.55, 1010.10, 1111.11, 1234.56
        )

        for (margin in margins) {
            val result = financeEngine.calculate(margin) as FinancialCalculationResult.Success
            assertEquals(SchemeCategory.MICRO_FINANCE, result.scheme)
            val schedule = result.schedule
            assertEquals(0.0, schedule.quarters.last().closingPrincipal, 0.0)
            assertEquals(result.structure.actualLoanAmount, schedule.totalPrincipalPaid, 0.0001)
        }
    }

    @Test
    fun testDensePrecisionGrid_SchemeBoundaryTransition() {
        // High density sweep between ₹13,995.00 and ₹14,005.00
        val boundaryMargins = listOf(
            13995.00, 13998.50, 13999.00, 13999.50, 13999.90, 13999.99,
            14000.00, // Capped Micro Finance
            14000.01, // First Term Loan
            14000.02, 14000.05, 14000.10, 14000.50, 14000.99, 14001.00,
            14005.00, 14010.00
        )

        for (margin in boundaryMargins) {
            val result = financeEngine.calculate(margin) as FinancialCalculationResult.Success
            val structure = result.structure
            val schedule = result.schedule

            // Schedule invariant checks
            assertEquals(0.0, schedule.quarters.last().closingPrincipal, 0.0)
            assertEquals(structure.actualLoanAmount, schedule.totalPrincipalPaid, 0.0001)

            if (margin <= 14000.00) {
                assertEquals("Margin $margin must be MICRO_FINANCE", SchemeCategory.MICRO_FINANCE, result.scheme)
                assertEquals(12, schedule.quarters.size)
                if (margin == 14000.00) {
                    assertTrue("Margin 14000.00 must be capped", structure.isCapped)
                    assertEquals(125000.00, structure.actualLoanAmount, 0.0001)
                    assertEquals(126000.00, structure.uncappedLoanAmount, 0.0001)
                    assertEquals(15000.00, structure.effectivePromoterEquity, 0.0001)
                }
            } else {
                assertEquals("Margin $margin must be TERM_LOAN", SchemeCategory.TERM_LOAN, result.scheme)
                assertEquals(28, schedule.quarters.size)
                assertFalse("Margin $margin just above boundary should not be capped", structure.isCapped)
                if (margin == 14000.01) {
                    assertEquals(140000.10, structure.totalProjectCost, 0.0001)
                    assertEquals(126000.09, structure.uncappedLoanAmount, 0.0001)
                    assertEquals(126000.09, structure.actualLoanAmount, 0.0001)
                    assertEquals(14000.01, structure.effectivePromoterEquity, 0.0001)
                }
            }
        }
    }

    @Test
    fun testDensePrecisionGrid_UpperCeilingTransition() {
        val ceilingMargins = listOf(
            499990.00, 499995.50, 499999.00, 499999.90, 499999.99,
            500000.00 // Upper ceiling
        )

        for (margin in ceilingMargins) {
            val result = financeEngine.calculate(margin) as FinancialCalculationResult.Success
            assertEquals(SchemeCategory.TERM_LOAN, result.scheme)
            val structure = result.structure
            val schedule = result.schedule

            assertEquals(0.0, schedule.quarters.last().closingPrincipal, 0.0)
            assertEquals(structure.actualLoanAmount, schedule.totalPrincipalPaid, 0.0001)

            if (margin == 500000.00) {
                assertEquals(5000000.00, structure.totalProjectCost, 0.0001)
                assertEquals(4500000.00, structure.uncappedLoanAmount, 0.0001)
                assertEquals(4500000.00, structure.actualLoanAmount, 0.0001)
                assertEquals(500000.00, structure.effectivePromoterEquity, 0.0001)
            }
        }
    }

    // ------------------------------------------------------------------------
    // CHALLENGE 3: Loan Caps, Infinitesimal Step & Threshold Boundaries
    // ------------------------------------------------------------------------

    @Test
    fun testExactMicroFinanceCap_Margin14kVs14k01() {
        val res14k = financeEngine.calculate(14000.00) as FinancialCalculationResult.Success
        val res14k01 = financeEngine.calculate(14000.01) as FinancialCalculationResult.Success

        // Margin 14000.00 (Micro Finance Capped)
        assertEquals(SchemeCategory.MICRO_FINANCE, res14k.scheme)
        assertEquals(140000.00, res14k.structure.totalProjectCost, 0.0001)
        assertEquals(126000.00, res14k.structure.uncappedLoanAmount, 0.0001)
        assertEquals(125000.00, res14k.structure.actualLoanAmount, 0.0001)
        assertTrue(res14k.structure.isCapped)
        assertEquals(15000.00, res14k.structure.effectivePromoterEquity, 0.0001)
        assertEquals(12, res14k.schedule.quarters.size)
        assertEquals(1, res14k.schedule.moratoriumQuartersCount)
        assertEquals(11, res14k.schedule.repaymentQuartersCount)
        assertEquals(0.0, res14k.schedule.quarters.last().closingPrincipal, 0.0)
        assertEquals(125000.00, res14k.schedule.totalPrincipalPaid, 0.0001)

        // Margin 14000.01 (Term Loan Uncapped)
        assertEquals(SchemeCategory.TERM_LOAN, res14k01.scheme)
        assertEquals(140000.10, res14k01.structure.totalProjectCost, 0.0001)
        assertEquals(126000.09, res14k01.structure.uncappedLoanAmount, 0.0001)
        assertEquals(126000.09, res14k01.structure.actualLoanAmount, 0.0001)
        assertFalse(res14k01.structure.isCapped)
        assertEquals(14000.01, res14k01.structure.effectivePromoterEquity, 0.0001)
        assertEquals(28, res14k01.schedule.quarters.size)
        assertEquals(2, res14k01.schedule.moratoriumQuartersCount)
        assertEquals(26, res14k01.schedule.repaymentQuartersCount)
        assertEquals(0.0, res14k01.schedule.quarters.last().closingPrincipal, 0.0)
        assertEquals(126000.09, res14k01.schedule.totalPrincipalPaid, 0.0001)
    }

    @Test
    fun testRejectionsAcrossBoundaryPerimeter() {
        // Immediately below 1000
        assertTrue(financeEngine.calculate(999.999) is FinancialCalculationResult.BelowMinimumThreshold)
        assertTrue(financeEngine.calculate(999.99) is FinancialCalculationResult.BelowMinimumThreshold)
        assertTrue(financeEngine.calculate(999.00) is FinancialCalculationResult.BelowMinimumThreshold)
        assertTrue(financeEngine.calculate(1.00) is FinancialCalculationResult.BelowMinimumThreshold)

        // Immediately above 500,000
        assertTrue(financeEngine.calculate(500000.01) is FinancialCalculationResult.ExceedsMaximumThreshold)
        assertTrue(financeEngine.calculate(500000.10) is FinancialCalculationResult.ExceedsMaximumThreshold)
        assertTrue(financeEngine.calculate(500001.00) is FinancialCalculationResult.ExceedsMaximumThreshold)
        assertTrue(financeEngine.calculate(1000000.00) is FinancialCalculationResult.ExceedsMaximumThreshold)

        // Malformed / Non-positive
        assertTrue(financeEngine.calculate(0.0) is FinancialCalculationResult.InvalidInput)
        assertTrue(financeEngine.calculate(-0.001) is FinancialCalculationResult.InvalidInput)
        assertTrue(financeEngine.calculate(-50000.0) is FinancialCalculationResult.InvalidInput)
        assertTrue(financeEngine.calculate(Double.NaN) is FinancialCalculationResult.InvalidInput)
        assertTrue(financeEngine.calculate(Double.POSITIVE_INFINITY) is FinancialCalculationResult.InvalidInput)
        assertTrue(financeEngine.calculate(Double.NEGATIVE_INFINITY) is FinancialCalculationResult.InvalidInput)
    }

    // ------------------------------------------------------------------------
    // CHALLENGE 4: Advisory Engine Resilience against Extreme Strings & Unicode
    // ------------------------------------------------------------------------

    @Test
    fun testAdvisoryStress_ExtremeInputsAndUnicodeFidelity() {
        val longString = "A".repeat(5000)
        val whitespaceString = "   \t\n   "
        val unicodeDevanagari = "आनंद नगर, वडताल रोड, ब्लॉक १२"
        val specialChars = "!@#$%^&*()_+{}[]|\":;<>?,./~`-="
        val emojiString = "🌾🌾🚜🚜🐄🐄💰💰"

        val testLocations = listOf(
            LocationInfo(village = longString, block = "Block A", district = "District B"),
            LocationInfo(village = whitespaceString, block = whitespaceString, district = whitespaceString),
            LocationInfo(village = unicodeDevanagari, block = "चाणक्य ब्लॉक", district = "पटना"),
            LocationInfo(village = specialChars, block = specialChars, district = specialChars),
            LocationInfo(village = emojiString, block = emojiString, district = emojiString)
        )

        for (loc in testLocations) {
            for (cat in BusinessCategory.values()) {
                for (lang in LanguageCode.values()) {
                    val input = AdvisoryInput(
                        location = loc,
                        marginCapital = 50000.0,
                        category = cat,
                        language = lang
                    )
                    val report = advisoryEngine.generateReport(input)

                    // Assertions: engine must not crash and all dimensions must be populated
                    assertNotNull("Report must not be null", report)
                    assertTrue("Market reach must not be blank", report.marketReach.isNotBlank())
                    assertTrue("Opportunity analysis must not be blank", report.opportunityAnalysis.isNotBlank())
                    assertTrue("Strengths must not be empty", report.swotAnalysis.strengths.isNotEmpty())
                    assertTrue("Weaknesses must not be empty", report.swotAnalysis.weaknesses.isNotEmpty())
                    assertTrue("Opportunities must not be empty", report.swotAnalysis.opportunities.isNotEmpty())
                    assertTrue("Threats must not be empty", report.swotAnalysis.threats.isNotEmpty())
                    assertTrue("Threats identification must not be empty", report.threatsIdentification.isNotEmpty())
                    assertTrue("Competitor mapping must not be blank", report.competitorMapping.isNotBlank())
                    assertTrue("Pricing strategy must not be blank", report.pricingStrategy.isNotBlank())
                    assertTrue("Key recommendations must not be empty", report.keyRecommendations.isNotEmpty())
                    assertTrue("Report must be offline generated", report.isOfflineGenerated)
                }
            }
        }
    }
}
