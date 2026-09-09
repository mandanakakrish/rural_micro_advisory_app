package com.ruraladvisory.finance

import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.finance.engine.BusinessHealthEngine
import com.ruraladvisory.finance.model.CashFlowStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [BusinessHealthEngine] testing revenue-expense-profit-cashflow diagnostics,
 * DSCR thresholds, break-even calculation, and health scoring.
 */
class BusinessHealthEngineTest {

    @Test
    fun testHealthyBusinessAssessment() {
        val report = BusinessHealthEngine.assess(
            inputRevenue = 60000.0,
            inputOperatingExpenses = 35000.0,
            projectCost = 100000.0,
            quarterlyLoanPayment = 9000.0 // monthly EMI = 3000
        )

        assertEquals(60000.0, report.estimatedMonthlyRevenue, 0.01)
        assertEquals(35000.0, report.estimatedMonthlyOperatingExpenses, 0.01)
        assertEquals(3000.0, report.monthlyDebtService, 0.01)
        assertEquals(38000.0, report.totalMonthlyOutflow, 0.01)
        assertEquals(22000.0, report.netMonthlyProfit, 0.01)
        assertTrue("Margin should be > 30%", report.netProfitMarginPercentage > 30.0)
        assertEquals(CashFlowStatus.HEALTHY, report.cashFlowStatus)
        assertTrue(report.healthScore >= 80)
        assertTrue(report.flags.isNotEmpty())
    }

    @Test
    fun testDeficitBusinessAssessment() {
        val report = BusinessHealthEngine.assess(
            inputRevenue = 20000.0,
            inputOperatingExpenses = 22000.0,
            projectCost = 100000.0,
            quarterlyLoanPayment = 9000.0
        )

        assertTrue(report.netMonthlyProfit < 0.0)
        assertEquals(CashFlowStatus.AT_RISK, report.cashFlowStatus)
        val warningFlag = report.flags.firstOrNull { it.isWarning }
        assertNotNull("Should contain warning flag for deficit", warningFlag)
    }

    @Test
    fun testDefaultHeuristicsWhenInputsEmpty() {
        val report = BusinessHealthEngine.assess(
            inputRevenue = null,
            inputOperatingExpenses = null,
            projectCost = 200000.0,
            quarterlyLoanPayment = null
        )

        assertTrue(report.estimatedMonthlyRevenue > 0.0)
        assertTrue(report.estimatedMonthlyOperatingExpenses > 0.0)
        assertEquals(0.0, report.monthlyDebtService, 0.01)
        assertTrue(report.healthScore in 10..100)
    }

    @Test
    fun testLocalizationAccessors() {
        val report = BusinessHealthEngine.assess(
            inputRevenue = 50000.0,
            inputOperatingExpenses = 30000.0,
            projectCost = 150000.0,
            quarterlyLoanPayment = 6000.0
        )

        val flag = report.flags.first()
        assertEquals(flag.titleEn, flag.title(LanguageCode.EN))
        assertEquals(flag.titleHi, flag.title(LanguageCode.HI))
        assertEquals(flag.descriptionEn, flag.description(LanguageCode.EN))
        assertEquals(flag.descriptionHi, flag.description(LanguageCode.HI))
    }
}
