package com.ruraladvisory.finance.engine

import com.ruraladvisory.finance.model.CostBreakdown
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.FinancialStructure
import com.ruraladvisory.finance.model.RepaymentQuarter
import com.ruraladvisory.finance.model.RepaymentSchedule
import com.ruraladvisory.finance.model.SchemeCategory
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Standard implementation of [FinancialCalculatorEngine] delivering concession credit
 * calculations, scheme routing, loan capping, straight-line principal amortization with
 * penny-balancing, and CAPEX/OPEX cost breakdowns for rural micro-enterprises.
 */
class DefaultFinancialCalculatorEngine : FinancialCalculatorEngine {

    companion object : FinancialCalculatorEngine {
        private val defaultInstance = DefaultFinancialCalculatorEngine()

        override fun calculate(marginCapital: Double): FinancialCalculationResult =
            defaultInstance.calculate(marginCapital)
    }

    override fun calculate(marginCapital: Double): FinancialCalculationResult {
        // 1. Sanitize floating-point and non-positive inputs
        if (marginCapital.isNaN() || marginCapital.isInfinite() || marginCapital <= 0.0) {
            return FinancialCalculationResult.InvalidInput(
                "Margin capital must be a finite, strictly positive value."
            )
        }

        // 2. Enforce minimum margin threshold (₹1,000 margin / ₹10,000 project cost)
        if (marginCapital < 1000.0) {
            return FinancialCalculationResult.BelowMinimumThreshold(enteredMargin = marginCapital)
        }

        // 3. Scale project cost at 10x available margin
        val totalProjectCost = round2(marginCapital * 10.0)

        // 4. Enforce maximum project cost ceiling (₹50.00 Lakh)
        if (totalProjectCost > 5000000.0) {
            return FinancialCalculationResult.ExceedsMaximumThreshold(enteredMargin = marginCapital)
        }

        // 5. Route scheme based on project cost threshold (₹1.40 Lakh)
        val scheme = if (totalProjectCost <= SchemeCategory.MICRO_FINANCE.maxProjectCost) {
            SchemeCategory.MICRO_FINANCE
        } else {
            SchemeCategory.TERM_LOAN
        }

        // 6. Compute debt structuring with statutory scheme loan caps
        val uncappedLoanAmount = round2(totalProjectCost * 0.90)
        val schemeMaxCap = scheme.maxLoanCap
        val isCapped = uncappedLoanAmount > schemeMaxCap
        val actualLoanAmount = if (isCapped) schemeMaxCap else uncappedLoanAmount
        val effectivePromoterEquity = round2(totalProjectCost - actualLoanAmount)
        val loanToCostRatio = actualLoanAmount / totalProjectCost

        val structure = FinancialStructure(
            marginCapital = marginCapital,
            totalProjectCost = totalProjectCost,
            uncappedLoanAmount = uncappedLoanAmount,
            actualLoanAmount = actualLoanAmount,
            schemeMaxCap = schemeMaxCap,
            isCapped = isCapped,
            effectivePromoterEquity = effectivePromoterEquity,
            loanToCostRatio = loanToCostRatio
        )

        // 7. Generate quarterly amortization schedule with moratorium simple interest
        val schedule = generateAmortizationSchedule(actualLoanAmount, scheme)

        // 8. Generate CAPEX and OPEX cost breakdown
        val costBreakdown = generateCostBreakdown(totalProjectCost)

        return FinancialCalculationResult.Success(
            scheme = scheme,
            structure = structure,
            schedule = schedule,
            costBreakdown = costBreakdown
        )
    }

    private fun generateAmortizationSchedule(
        loanAmount: Double,
        scheme: SchemeCategory
    ): RepaymentSchedule {
        val tenureQuarters = scheme.tenureQuarters
        val moratoriumQuarters = scheme.moratoriumQuarters
        val repaymentQuarters = scheme.repaymentQuarters
        val quarterlyRate = scheme.quarterlyInterestRate

        val basePrincipalInstallment = round2(loanAmount / repaymentQuarters)

        val quarters = ArrayList<RepaymentQuarter>(tenureQuarters)
        var currentOpening = loanAmount
        var totalPrincipalPaid = 0.0
        var totalInterestPaid = 0.0
        var totalMoratoriumInterest = 0.0

        for (q in 1..tenureQuarters) {
            val isMoratorium = q <= moratoriumQuarters

            if (isMoratorium) {
                val opening = currentOpening
                val principal = 0.0
                val interest = round2(opening * quarterlyRate)
                val outflow = interest
                val closing = opening

                quarters.add(
                    RepaymentQuarter(
                        quarterIndex = q,
                        isMoratorium = true,
                        openingPrincipal = opening,
                        principalRepayment = principal,
                        interestAccrual = interest,
                        totalQuarterlyOutflow = outflow,
                        closingPrincipal = closing
                    )
                )

                totalMoratoriumInterest = round2(totalMoratoriumInterest + interest)
                totalInterestPaid = round2(totalInterestPaid + interest)
                currentOpening = closing
            } else {
                val opening = currentOpening
                val isFinalQuarter = (q == tenureQuarters)
                val principal = if (isFinalQuarter) {
                    // Penny balancing: final quarter takes exact remaining balance down to 0.00
                    opening
                } else {
                    minOf(basePrincipalInstallment, opening)
                }

                val interest = round2(opening * quarterlyRate)
                val outflow = round2(principal + interest)
                val closing = if (isFinalQuarter) 0.0 else round2(opening - principal)

                quarters.add(
                    RepaymentQuarter(
                        quarterIndex = q,
                        isMoratorium = false,
                        openingPrincipal = opening,
                        principalRepayment = principal,
                        interestAccrual = interest,
                        totalQuarterlyOutflow = outflow,
                        closingPrincipal = closing
                    )
                )

                totalPrincipalPaid = round2(totalPrincipalPaid + principal)
                totalInterestPaid = round2(totalInterestPaid + interest)
                currentOpening = closing
            }
        }

        val totalOutflow = round2(totalPrincipalPaid + totalInterestPaid)

        return RepaymentSchedule(
            quarters = quarters,
            totalPrincipalPaid = totalPrincipalPaid,
            totalInterestPaid = totalInterestPaid,
            totalOutflow = totalOutflow,
            totalMoratoriumInterest = totalMoratoriumInterest,
            moratoriumQuartersCount = moratoriumQuarters,
            repaymentQuartersCount = repaymentQuarters
        )
    }

    private fun generateCostBreakdown(totalProjectCost: Double): CostBreakdown {
        val fixedAssetsCapex = round2(totalProjectCost * 0.70)
        val workingCapital = round2(totalProjectCost * 0.25)
        val contingencyBuffer = round2(totalProjectCost * 0.05)
        val estimatedMonthlyOpex = round2(workingCapital / 3.0)
        val estimatedQuarterlyOpex = workingCapital
        val rawMaterialsOpex = round2(workingCapital * 0.55)
        val laborWagesOpex = round2(workingCapital * 0.25)
        val utilitiesLogisticsOpex = round2(workingCapital * 0.12)
        val maintenanceSundryOpex = round2(workingCapital * 0.08)

        return CostBreakdown(
            totalProjectCost = totalProjectCost,
            fixedAssetsCapex = fixedAssetsCapex,
            workingCapital = workingCapital,
            contingencyBuffer = contingencyBuffer,
            estimatedMonthlyOpex = estimatedMonthlyOpex,
            estimatedQuarterlyOpex = estimatedQuarterlyOpex,
            rawMaterialsOpex = rawMaterialsOpex,
            laborWagesOpex = laborWagesOpex,
            utilitiesLogisticsOpex = utilitiesLogisticsOpex,
            maintenanceSundryOpex = maintenanceSundryOpex
        )
    }

    private fun round2(value: Double): Double {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}
