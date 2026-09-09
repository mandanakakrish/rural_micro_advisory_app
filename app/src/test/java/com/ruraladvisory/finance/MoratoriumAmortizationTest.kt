package com.ruraladvisory.finance

import com.ruraladvisory.finance.engine.DefaultFinancialCalculatorEngine
import com.ruraladvisory.finance.engine.FinancialCalculatorEngine
import com.ruraladvisory.finance.model.FinancialCalculationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Rigorous unit tests validating quarterly interest accrual, moratorium grace periods,
 * straight-line principal amortization, and penny balancing.
 */
class MoratoriumAmortizationTest {

    private lateinit var engine: FinancialCalculatorEngine

    @Before
    fun setUp() {
        engine = DefaultFinancialCalculatorEngine()
    }

    @Test
    fun testMoratoriumAndAmortization_Margin10k_MicroFinance() {
        val result = engine.calculate(10000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        val quarters = schedule.quarters

        assertEquals(12, quarters.size)
        assertEquals(1, schedule.moratoriumQuartersCount)
        assertEquals(11, schedule.repaymentQuartersCount)

        // Q1 Moratorium
        val q1 = quarters[0]
        assertEquals(1, q1.quarterIndex)
        assertTrue(q1.isMoratorium)
        assertEquals(90000.0, q1.openingPrincipal, 0.001)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(1462.50, q1.interestAccrual, 0.001)
        assertEquals(1462.50, q1.totalQuarterlyOutflow, 0.001)
        assertEquals(90000.0, q1.closingPrincipal, 0.001)

        // Q2 Repayment Start
        val q2 = quarters[1]
        assertEquals(2, q2.quarterIndex)
        assertFalse(q2.isMoratorium)
        assertEquals(90000.0, q2.openingPrincipal, 0.001)
        assertEquals(8181.82, q2.principalRepayment, 0.001)
        assertEquals(1462.50, q2.interestAccrual, 0.001)
        assertEquals(9644.32, q2.totalQuarterlyOutflow, 0.001)
        assertEquals(81818.18, q2.closingPrincipal, 0.001)

        // Q12 Final Repayment with Penny Balancing
        val q12 = quarters[11]
        assertEquals(12, q12.quarterIndex)
        assertFalse(q12.isMoratorium)
        assertEquals(8181.80, q12.openingPrincipal, 0.001)
        assertEquals(8181.80, q12.principalRepayment, 0.001) // Exact match to remaining
        assertEquals(132.95, q12.interestAccrual, 0.001)
        assertEquals(8314.75, q12.totalQuarterlyOutflow, 0.001)
        assertEquals(0.0, q12.closingPrincipal, 0.001) // Strictly 0.00

        // Overall Totals
        assertEquals(90000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(10237.50, schedule.totalInterestPaid, 0.001)
        assertEquals(100237.50, schedule.totalOutflow, 0.001)
        assertEquals(1462.50, schedule.totalMoratoriumInterest, 0.001)
    }

    @Test
    fun testMoratoriumAndAmortization_Margin14k_MicroFinanceCapped() {
        val result = engine.calculate(14000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        val quarters = schedule.quarters

        assertEquals(12, quarters.size)

        // Q1 Moratorium for ₹1,25,000 loan
        val q1 = quarters[0]
        assertTrue(q1.isMoratorium)
        assertEquals(125000.0, q1.openingPrincipal, 0.001)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(2031.25, q1.interestAccrual, 0.001)
        assertEquals(2031.25, q1.totalQuarterlyOutflow, 0.001)
        assertEquals(125000.0, q1.closingPrincipal, 0.001)

        // Final quarter balances strictly to zero
        val q12 = quarters[11]
        assertEquals(11363.60, q12.principalRepayment, 0.001)
        assertEquals(0.0, q12.closingPrincipal, 0.001)

        assertEquals(125000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(14218.75, schedule.totalInterestPaid, 0.001)
        assertEquals(139218.75, schedule.totalOutflow, 0.001)
        assertEquals(2031.25, schedule.totalMoratoriumInterest, 0.001)
    }

    @Test
    fun testMoratoriumAndAmortization_Margin15k_TermLoan() {
        val result = engine.calculate(15000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        val quarters = schedule.quarters

        assertEquals(28, quarters.size)
        assertEquals(2, schedule.moratoriumQuartersCount)
        assertEquals(26, schedule.repaymentQuartersCount)

        // Q1 Moratorium
        val q1 = quarters[0]
        assertTrue(q1.isMoratorium)
        assertEquals(135000.0, q1.openingPrincipal, 0.001)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(2700.0, q1.interestAccrual, 0.001)
        assertEquals(2700.0, q1.totalQuarterlyOutflow, 0.001)
        assertEquals(135000.0, q1.closingPrincipal, 0.001)

        // Q2 Moratorium
        val q2 = quarters[1]
        assertTrue(q2.isMoratorium)
        assertEquals(135000.0, q2.openingPrincipal, 0.001)
        assertEquals(0.0, q2.principalRepayment, 0.001)
        assertEquals(2700.0, q2.interestAccrual, 0.001)
        assertEquals(2700.0, q2.totalQuarterlyOutflow, 0.001)
        assertEquals(135000.0, q2.closingPrincipal, 0.001)

        // Q3 Repayment start
        val q3 = quarters[2]
        assertFalse(q3.isMoratorium)
        assertEquals(135000.0, q3.openingPrincipal, 0.001)
        assertEquals(5192.31, q3.principalRepayment, 0.001)
        assertEquals(2700.0, q3.interestAccrual, 0.001)
        assertEquals(7892.31, q3.totalQuarterlyOutflow, 0.001)

        // Q28 Final quarter
        val q28 = quarters[27]
        assertEquals(0.0, q28.closingPrincipal, 0.001)

        assertEquals(135000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(5400.0, schedule.totalMoratoriumInterest, 0.001)
    }

    @Test
    fun testMoratoriumAndAmortization_Margin500k_TermLoanMaxCap() {
        val result = engine.calculate(500000.0) as FinancialCalculationResult.Success
        val schedule = result.schedule
        val quarters = schedule.quarters

        assertEquals(28, quarters.size)
        assertEquals(2, schedule.moratoriumQuartersCount)
        assertEquals(26, schedule.repaymentQuartersCount)

        // Q1 and Q2 Moratorium
        for (i in 0..1) {
            val q = quarters[i]
            assertTrue(q.isMoratorium)
            assertEquals(4500000.0, q.openingPrincipal, 0.001)
            assertEquals(0.0, q.principalRepayment, 0.001)
            assertEquals(90000.0, q.interestAccrual, 0.001)
            assertEquals(90000.0, q.totalQuarterlyOutflow, 0.001)
            assertEquals(4500000.0, q.closingPrincipal, 0.001)
        }

        // Q28 Final quarter
        val q28 = quarters[27]
        assertEquals(0.0, q28.closingPrincipal, 0.001)

        assertEquals(4500000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(180000.0, schedule.totalMoratoriumInterest, 0.001)
    }

    @Test
    fun testScheduleInvariantsAcrossVariedMargins() {
        val testMargins = doubleArrayOf(
            1000.0, 1500.0, 5000.0, 10000.0, 14000.0, 14001.0,
            15000.0, 25000.0, 50000.0, 100000.0, 250000.0, 499999.0, 500000.0
        )

        for (margin in testMargins) {
            val result = engine.calculate(margin)
            assertTrue("Expected success for margin $margin", result is FinancialCalculationResult.Success)
            val success = result as FinancialCalculationResult.Success
            val schedule = success.schedule
            val quarters = schedule.quarters

            // Invariant 1: Total principal paid strictly equals actual loan amount
            assertEquals(
                "Total principal must match actual loan for margin $margin",
                success.structure.actualLoanAmount,
                schedule.totalPrincipalPaid,
                0.01
            )

            // Invariant 2: Total outflow equals principal + interest
            assertEquals(
                "Total outflow must equal principal + interest for margin $margin",
                schedule.totalPrincipalPaid + schedule.totalInterestPaid,
                schedule.totalOutflow,
                0.01
            )

            // Invariant 3: Final closing principal must strictly be 0.00
            val finalQuarter = quarters.last()
            assertEquals(
                "Final quarter closing principal must be 0.00 for margin $margin",
                0.0,
                finalQuarter.closingPrincipal,
                0.0001
            )

            // Invariant 4: Continuity across quarters
            for (i in quarters.indices) {
                val q = quarters[i]
                if (i > 0) {
                    val prev = quarters[i - 1]
                    assertEquals(
                        "Quarter ${q.quarterIndex} opening must equal quarter ${prev.quarterIndex} closing",
                        prev.closingPrincipal,
                        q.openingPrincipal,
                        0.01
                    )
                }

                assertEquals(
                    "Quarter ${q.quarterIndex} outflow must equal principal + interest",
                    q.principalRepayment + q.interestAccrual,
                    q.totalQuarterlyOutflow,
                    0.01
                )
            }
        }
    }
}
