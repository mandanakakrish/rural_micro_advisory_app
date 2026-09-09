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
 * Tier 4 E2E Test Suite: Real-World Micro-Enterprise Application Scenarios.
 * Models 5 end-to-end user journeys representing distinct geographies, enterprise sectors,
 * budget scales, and concessional loan structures across rural and semi-urban India.
 *
 * Scenarios:
 * 1. Smallholder Dairy Farmer in Anand, Gujarat (Margin ₹12,000 -> Micro Finance, 6.5%, 3-yr, 1-qtr moratorium)
 * 2. Handloom Weaver in Pochampally, Telangana (Margin ₹14,000 -> Micro Finance, capped at ₹1,25,000)
 * 3. Spice & Pickle Processing SHG in Coorg, Karnataka (Margin ₹1,50,000 -> Term Loan, 8.0%, 7-yr, 2-qtr moratorium)
 * 4. Rural Poultry Broiler Unit in Namakkal, Tamil Nadu (Margin ₹35,000 -> Term Loan, ₹3.15L loan)
 * 5. Custom Hiring Agro-Services Center in Bhatinda, Punjab (Margin ₹5,00,000 -> Term Loan max cap ₹45L)
 */
class E2ETier4RealWorldScenarioTest {

    private lateinit var financialEngine: DefaultFinancialCalculatorEngine
    private lateinit var advisoryEngine: OfflineHeuristicAdvisoryEngine

    @Before
    fun setUp() {
        financialEngine = DefaultFinancialCalculatorEngine()
        advisoryEngine = OfflineHeuristicAdvisoryEngine()
    }

    // =========================================================================
    // Scenario 1: Smallholder Dairy Farmer in Anand, Gujarat
    // =========================================================================
    @Test
    fun testScenario1_SmallholderDairyFarmer_AnandGujarat() {
        val location = LocationInfo(village = "Hadgood", block = "Anand", district = "Anand")
        val marginCapital = 12000.0
        val category = BusinessCategory.DAIRY

        // --- Step 1: Financial Structuring ---
        val finResult = financialEngine.calculate(marginCapital)
        assertTrue("Financial calculation must succeed", finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        // Project Cost & Scheme Auto-Selection
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(120000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(108000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(108000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.schemeMaxCap, 0.001)
        assertFalse("Loan must not be capped", success.structure.isCapped)
        assertEquals(12000.0, success.structure.effectivePromoterEquity, 0.001)
        assertEquals(0.90, success.structure.loanToCostRatio, 0.001)

        // Scheme Terms
        assertEquals(0.065, success.scheme.annualInterestRate, 0.0001)
        assertEquals(12, success.scheme.tenureQuarters)
        assertEquals(1, success.scheme.moratoriumQuarters)
        assertEquals(11, success.scheme.repaymentQuarters)

        // Moratorium & Amortization Schedule
        val schedule = success.schedule
        assertEquals(12, schedule.quarters.size)

        // Q1 Moratorium (Simple interest at 6.5% / 4 = 1.625% on ₹1,08,000 = ₹1,755.00)
        val q1 = schedule.quarters[0]
        assertTrue(q1.isMoratorium)
        assertEquals(108000.0, q1.openingPrincipal, 0.001)
        assertEquals(0.0, q1.principalRepayment, 0.001)
        assertEquals(1755.00, q1.interestAccrual, 0.001)
        assertEquals(1755.00, q1.totalQuarterlyOutflow, 0.001)
        assertEquals(108000.0, q1.closingPrincipal, 0.001)

        // Q2 First Principal Repayment (Base installment = 108000 / 11 = 9818.18)
        val q2 = schedule.quarters[1]
        assertFalse(q2.isMoratorium)
        assertEquals(108000.0, q2.openingPrincipal, 0.001)
        assertEquals(9818.18, q2.principalRepayment, 0.001)
        assertEquals(1755.00, q2.interestAccrual, 0.001)
        assertEquals(11573.18, q2.totalQuarterlyOutflow, 0.001)
        assertEquals(98181.82, q2.closingPrincipal, 0.001)

        // Q12 Penny Balancing down to 0.00
        val q12 = schedule.quarters[11]
        assertEquals(0.0, q12.closingPrincipal, 0.0001)
        assertEquals(108000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(1755.00, schedule.totalMoratoriumInterest, 0.001)

        // Cost Breakdown (70% Capex, 25% WC, 5% Contingency)
        assertEquals(84000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(30000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(6000.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(10000.0, success.costBreakdown.estimatedMonthlyOpex, 0.01)

        // --- Step 2: Feasibility Advisory Synthesis (English) ---
        val advInputEn = AdvisoryInput(location, marginCapital, category, LanguageCode.EN)
        val reportEn = advisoryEngine.generateReport(advInputEn)

        assertEquals(category, reportEn.category)
        assertEquals(BudgetTier.MICRO.name, reportEn.budgetTier)
        assertTrue(reportEn.isOfflineGenerated)
        assertTrue(reportEn.location.village.contains("Hadgood"))
        assertTrue(reportEn.location.district.contains("Anand"))
        assertTrue(reportEn.marketReach.contains("radius", ignoreCase = true))
        assertTrue(reportEn.swotAnalysis.strengths.isNotEmpty())
        assertEquals(4, reportEn.threatsIdentification.size)

        // --- Step 3: Feasibility Advisory Synthesis (Hindi) ---
        val advInputHi = AdvisoryInput(location, marginCapital, category, LanguageCode.HI)
        val reportHi = advisoryEngine.generateReport(advInputHi)
        assertTrue(reportHi.marketReach.any { it in '\u0900'..'\u097F' })
    }

    // =========================================================================
    // Scenario 2: Handloom Weaver in Pochampally, Telangana
    // =========================================================================
    @Test
    fun testScenario2_HandloomWeaver_PochampallyTelangana() {
        val location = LocationInfo(village = "Bhoodan Pochampally", block = "Pochampally", district = "Yadadri Bhuvanagiri")
        val marginCapital = 14000.0 // Upper boundary of Micro Finance
        val category = BusinessCategory.TEXTILES

        // --- Step 1: Financial Structuring & Statutory Cap ---
        val finResult = financialEngine.calculate(marginCapital)
        assertTrue(finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        // Project cost is ₹1,40,000 (Micro Finance threshold)
        assertEquals(SchemeCategory.MICRO_FINANCE, success.scheme)
        assertEquals(140000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(126000.0, success.structure.uncappedLoanAmount, 0.001)

        // Strictly capped at statutory maximum ₹1,25,000
        assertEquals(125000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(125000.0, success.structure.schemeMaxCap, 0.001)
        assertTrue("Statutory ceiling must be enforced", success.structure.isCapped)
        assertEquals(15000.0, success.structure.effectivePromoterEquity, 0.001)

        // Repayment Schedule
        val schedule = success.schedule
        assertEquals(12, schedule.quarters.size)

        // Q1 Moratorium on capped ₹1,25,000: 125,000 * 0.01625 = ₹2,031.25
        val q1 = schedule.quarters[0]
        assertTrue(q1.isMoratorium)
        assertEquals(125000.0, q1.openingPrincipal, 0.001)
        assertEquals(2031.25, q1.interestAccrual, 0.001)
        assertEquals(2031.25, q1.totalQuarterlyOutflow, 0.001)

        // Amortization (125,000 / 11 = 11,363.64)
        val q2 = schedule.quarters[1]
        assertEquals(11363.64, q2.principalRepayment, 0.001)

        // Q12 Penny Balancing to 0.00
        val q12 = schedule.quarters[11]
        assertEquals(11363.60, q12.principalRepayment, 0.001)
        assertEquals(0.0, q12.closingPrincipal, 0.0001)
        assertEquals(125000.0, schedule.totalPrincipalPaid, 0.001)
        assertEquals(2031.25, schedule.totalMoratoriumInterest, 0.001)

        // Cost Breakdown
        assertEquals(98000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(35000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(7000.0, success.costBreakdown.contingencyBuffer, 0.001)

        // --- Step 2: Feasibility Advisory Synthesis ---
        val report = advisoryEngine.generateReport(AdvisoryInput(location, marginCapital, category, LanguageCode.EN))
        assertEquals(category, report.category)
        assertEquals(BudgetTier.MICRO.name, report.budgetTier)
        assertTrue(report.opportunityAnalysis.contains("tailor", ignoreCase = true) || report.opportunityAnalysis.contains("uniform", ignoreCase = true) || report.opportunityAnalysis.contains("kurti", ignoreCase = true) || report.opportunityAnalysis.contains("clothing", ignoreCase = true) || report.opportunityAnalysis.contains("textile", ignoreCase = true))
        assertTrue(report.threatsIdentification.any { it.contains("raw material", ignoreCase = true) || it.contains("yarn", ignoreCase = true) || it.contains("Mitigation", ignoreCase = true) })
    }

    // =========================================================================
    // Scenario 3: Spice & Pickle Processing SHG in Coorg, Karnataka
    // =========================================================================
    @Test
    fun testScenario3_SpiceAndPickleSHG_CoorgKarnataka() {
        val location = LocationInfo(village = "Madikeri", block = "Madikeri", district = "Kodagu")
        val marginCapital = 150000.0
        val category = BusinessCategory.FOOD_PROCESSING

        // --- Step 1: Financial Structuring ---
        val finResult = financialEngine.calculate(marginCapital)
        assertTrue(finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        // Project Cost = ₹15,00,000 -> Term Loan Scheme (8.0%, 7 years, 2 quarters moratorium)
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(1500000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(1350000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(1350000.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
        assertEquals(150000.0, success.structure.effectivePromoterEquity, 0.001)
        assertEquals(0.90, success.structure.loanToCostRatio, 0.001)

        assertEquals(28, success.scheme.tenureQuarters)
        assertEquals(2, success.scheme.moratoriumQuarters)
        assertEquals(26, success.scheme.repaymentQuarters)

        // Moratorium Quarters (Rate = 8.0% / 4 = 2.0% on ₹13,50,000 = ₹27,000.00 per quarter)
        val schedule = success.schedule
        assertEquals(28, schedule.quarters.size)

        val q1 = schedule.quarters[0]
        val q2 = schedule.quarters[1]
        assertTrue(q1.isMoratorium)
        assertTrue(q2.isMoratorium)
        assertEquals(27000.0, q1.interestAccrual, 0.001)
        assertEquals(27000.0, q2.interestAccrual, 0.001)
        assertEquals(54000.0, schedule.totalMoratoriumInterest, 0.001)

        // Q3 First Principal Repayment (1,350,000 / 26 = 51,923.08)
        val q3 = schedule.quarters[2]
        assertFalse(q3.isMoratorium)
        assertEquals(51923.08, q3.principalRepayment, 0.001)

        // Final Quarter Q28 Penny Balancing
        val q28 = schedule.quarters[27]
        assertEquals(0.0, q28.closingPrincipal, 0.0001)
        assertEquals(1350000.0, schedule.totalPrincipalPaid, 0.001)

        // Cost Breakdown
        assertEquals(1050000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(375000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(75000.0, success.costBreakdown.contingencyBuffer, 0.001)
        assertEquals(125000.0, success.costBreakdown.estimatedMonthlyOpex, 0.01)

        // --- Step 2: Feasibility Advisory Synthesis ---
        val report = advisoryEngine.generateReport(AdvisoryInput(location, marginCapital, category, LanguageCode.EN))
        assertEquals(category, report.category)
        assertEquals(BudgetTier.SMALL.name, report.budgetTier)
        assertTrue(report.opportunityAnalysis.contains("processing", ignoreCase = true) || report.opportunityAnalysis.contains("value", ignoreCase = true) || report.opportunityAnalysis.contains("spice", ignoreCase = true))
        assertTrue(report.swotAnalysis.strengths.isNotEmpty())
    }

    // =========================================================================
    // Scenario 4: Rural Poultry Broiler Unit in Namakkal, Tamil Nadu
    // =========================================================================
    @Test
    fun testScenario4_RuralPoultryBroilerUnit_NamakkalTamilNadu() {
        val location = LocationInfo(village = "Paramathi", block = "Paramathi-Velur", district = "Namakkal")
        val marginCapital = 35000.0
        val category = BusinessCategory.POULTRY

        // --- Step 1: Financial Structuring ---
        val finResult = financialEngine.calculate(marginCapital)
        assertTrue(finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        // Project Cost = ₹3,50,000 -> Term Loan Scheme (8.0%, 28 quarters, 2 quarters moratorium)
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(350000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(315000.0, success.structure.uncappedLoanAmount, 0.001)
        assertEquals(315000.0, success.structure.actualLoanAmount, 0.001)
        assertFalse(success.structure.isCapped)
        assertEquals(35000.0, success.structure.effectivePromoterEquity, 0.001)

        // Moratorium Interest: 315,000 * 0.02 = ₹6,300.00 per quarter
        val schedule = success.schedule
        val q1 = schedule.quarters[0]
        val q2 = schedule.quarters[1]
        assertEquals(6300.0, q1.interestAccrual, 0.001)
        assertEquals(6300.0, q2.interestAccrual, 0.001)
        assertEquals(12600.0, schedule.totalMoratoriumInterest, 0.001)

        // Repayment (315,000 / 26 = 12,115.38)
        val q3 = schedule.quarters[2]
        assertEquals(12115.38, q3.principalRepayment, 0.001)

        // Penny Balancing
        val q28 = schedule.quarters[27]
        assertEquals(0.0, q28.closingPrincipal, 0.0001)
        assertEquals(315000.0, schedule.totalPrincipalPaid, 0.001)

        // Cost Breakdown
        assertEquals(245000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(87500.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(17500.0, success.costBreakdown.contingencyBuffer, 0.001)

        // --- Step 2: Feasibility Advisory Synthesis ---
        val report = advisoryEngine.generateReport(AdvisoryInput(location, marginCapital, category, LanguageCode.EN))
        assertEquals(category, report.category)
        assertEquals(BudgetTier.SMALL.name, report.budgetTier)
        assertTrue(report.competitorMapping.contains("broiler", ignoreCase = true) || report.competitorMapping.contains("poultry", ignoreCase = true) || report.competitorMapping.contains("Density", ignoreCase = true))
        assertTrue(report.threatsIdentification.size == 4)
    }

    // =========================================================================
    // Scenario 5: Custom Hiring Agro-Services Center in Bhatinda, Punjab
    // =========================================================================
    @Test
    fun testScenario5_CustomHiringAgroServices_BhatindaPunjab() {
        val location = LocationInfo(village = "Talwandi Sabo", block = "Talwandi Sabo", district = "Bhatinda")
        val marginCapital = 500000.0 // Maximum permissible statutory margin
        val category = BusinessCategory.AGRO_SERVICES

        // --- Step 1: Financial Structuring & Statutory Cap Ceiling ---
        val finResult = financialEngine.calculate(marginCapital)
        assertTrue(finResult is FinancialCalculationResult.Success)
        val success = finResult as FinancialCalculationResult.Success

        // Project Cost = ₹50,00,000 (Maximum allowed)
        assertEquals(SchemeCategory.TERM_LOAN, success.scheme)
        assertEquals(5000000.0, success.structure.totalProjectCost, 0.001)
        assertEquals(4500000.0, success.structure.uncappedLoanAmount, 0.001)

        // 90% Loan equals Term Loan maximum ceiling ₹45,00,000
        assertEquals(4500000.0, success.structure.actualLoanAmount, 0.001)
        assertEquals(4500000.0, success.structure.schemeMaxCap, 0.001)
        assertFalse("Uncapped matches cap exactly", success.structure.isCapped)
        assertEquals(500000.0, success.structure.effectivePromoterEquity, 0.001)

        // Moratorium Interest: 4,500,000 * 0.02 = ₹90,000.00 per quarter
        val schedule = success.schedule
        val q1 = schedule.quarters[0]
        val q2 = schedule.quarters[1]
        assertEquals(90000.0, q1.interestAccrual, 0.001)
        assertEquals(90000.0, q2.interestAccrual, 0.001)
        assertEquals(180000.0, schedule.totalMoratoriumInterest, 0.001)

        // Base Repayment: 4,500,000 / 26 = 1,73,076.92
        val q3 = schedule.quarters[2]
        assertEquals(173076.92, q3.principalRepayment, 0.001)

        // Q28 Penny Balancing
        val q28 = schedule.quarters[27]
        assertEquals(0.0, q28.closingPrincipal, 0.0001)
        assertEquals(4500000.0, schedule.totalPrincipalPaid, 0.001)

        // Cost Breakdown
        assertEquals(3500000.0, success.costBreakdown.fixedAssetsCapex, 0.001)
        assertEquals(1250000.0, success.costBreakdown.workingCapital, 0.001)
        assertEquals(250000.0, success.costBreakdown.contingencyBuffer, 0.001)

        // --- Step 2: Feasibility Advisory Synthesis (Medium Tier) ---
        val report = advisoryEngine.generateReport(AdvisoryInput(location, marginCapital, category, LanguageCode.EN))
        assertEquals(category, report.category)
        assertEquals(BudgetTier.MEDIUM.name, report.budgetTier)
        assertTrue(report.opportunityAnalysis.contains("custom hiring", ignoreCase = true) || report.opportunityAnalysis.contains("mechanization", ignoreCase = true) || report.opportunityAnalysis.contains("harvester", ignoreCase = true) || report.opportunityAnalysis.contains("tractor", ignoreCase = true) || report.opportunityAnalysis.contains("tiller", ignoreCase = true))
        assertTrue(report.keyRecommendations.isNotEmpty())
    }
}
