package com.ruraladvisory.finance.engine

import com.ruraladvisory.finance.model.BusinessHealthFlag
import com.ruraladvisory.finance.model.BusinessHealthReport
import com.ruraladvisory.finance.model.CashFlowStatus
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Intelligent Business Intelligence Engine converting raw operational data
 * (Revenue -> Expenses -> Debt EMI -> Net Profit -> Cash Flow)
 * into actionable business health scores and decision guidance.
 */
object BusinessHealthEngine {

    fun assess(
        inputRevenue: Double?,
        inputOperatingExpenses: Double?,
        projectCost: Double,
        quarterlyLoanPayment: Double?
    ): BusinessHealthReport {
        // Baseline heuristics if user has not yet specified custom monthly cash flows
        val revenue = if (inputRevenue != null && inputRevenue > 0.0) {
            inputRevenue
        } else {
            // Rural micro-enterprise average monthly turnover is ~20% of total capex/project cost
            max(20000.0, projectCost * 0.20)
        }

        val opex = if (inputOperatingExpenses != null && inputOperatingExpenses > 0.0) {
            inputOperatingExpenses
        } else {
            // Operating expenses (materials, power, labor, transit) typically 68% of gross turnover
            revenue * 0.68
        }

        // Monthly debt service from quarterly amortization repayment
        val monthlyDebtService = if (quarterlyLoanPayment != null && quarterlyLoanPayment > 0.0) {
            quarterlyLoanPayment / 3.0
        } else {
            0.0
        }

        val totalOutflow = opex + monthlyDebtService
        val netProfit = revenue - totalOutflow
        val netMarginPct = if (revenue > 0.0) (netProfit / revenue) * 100.0 else 0.0
        val operatingSurplus = revenue - opex
        val dscr = if (monthlyDebtService > 0.0) {
            operatingSurplus / monthlyDebtService
        } else {
            99.0
        }

        val breakEvenRevenue = totalOutflow

        // Calculate 0-100 Health Score
        var score = 50
        if (netMarginPct >= 22.0) score += 25
        else if (netMarginPct >= 12.0) score += 15
        else if (netMarginPct >= 5.0) score += 5
        else if (netMarginPct < 0.0) score -= 25

        if (dscr >= 2.0) score += 25
        else if (dscr >= 1.4) score += 15
        else if (dscr >= 1.0) score += 5
        else if (dscr < 1.0 && monthlyDebtService > 0.0) score -= 25

        val finalScore = max(10, min(100, score))

        val status = when {
            netProfit > 0.0 && dscr >= 1.4 && finalScore >= 70 -> CashFlowStatus.HEALTHY
            netProfit >= 0.0 && dscr >= 1.0 -> CashFlowStatus.MODERATE
            else -> CashFlowStatus.AT_RISK
        }

        val flags = mutableListOf<BusinessHealthFlag>()

        // 1. Profit Margin Diagnostics
        if (netMarginPct >= 20.0) {
            flags.add(
                BusinessHealthFlag(
                    id = "FLAG_HEALTHY_MARGIN",
                    titleEn = "Strong Operating Margin",
                    titleHi = "मजबूत परिचालन मार्जिन",
                    descriptionEn = "Your projected net profit margin is healthy (>20%), providing an excellent cash buffer for expansion.",
                    descriptionHi = "आपका शुद्ध लाभ मार्जिन 20% से अधिक है, जो व्यापार विस्तार व आकस्मिक खर्चों हेतु मजबूत आधार है।",
                    isWarning = false
                )
            )
        } else if (netMarginPct < 12.0 && netMarginPct >= 0.0) {
            flags.add(
                BusinessHealthFlag(
                    id = "FLAG_LOW_MARGIN",
                    titleEn = "Thin Margin Alert",
                    titleHi = "कम लाभ मार्जिन चेतावनी",
                    descriptionEn = "Estimated margin is tight (<12%). Consider bulk raw material purchasing or direct selling to increase spread.",
                    descriptionHi = "अनुमानित मार्जिन 12% से कम है। थोक में कच्चा माल खरीदने अथवा सीधे ग्राहकों को बेचने पर विचार करें।",
                    isWarning = true
                )
            )
        } else if (netMarginPct < 0.0) {
            flags.add(
                BusinessHealthFlag(
                    id = "FLAG_NET_DEFICIT",
                    titleEn = "Operating Deficit Alert",
                    titleHi = "परिचालन घाटा चेतावनी",
                    descriptionEn = "Projected expenses and loan EMI exceed monthly sales. Immediate pricing re-calibration or capacity increase required.",
                    descriptionHi = "मासिक खर्च व ऋण किश्त कुल बिक्री से अधिक हैं। तुरंत उत्पाद मूल्य बढ़ाने या उत्पादन क्षमता बढ़ाने की आवश्यकता है।",
                    isWarning = true
                )
            )
        }

        // 2. Debt Service Coverage Diagnostics
        if (monthlyDebtService > 0.0) {
            if (dscr >= 2.0) {
                flags.add(
                    BusinessHealthFlag(
                        id = "FLAG_STRONG_DSCR",
                        titleEn = "Comfortable Debt Repayment",
                        titleHi = "सुगम ऋण अदायगी क्षमता",
                        descriptionEn = "Operating profit covers loan EMI by over 2x. High qualification confidence for bank credit sanction.",
                        descriptionHi = "परिचालन लाभ ऋण किश्त (EMI) से 2 गुना से अधिक है। बैंक ऋण स्वीकृति हेतु अत्यंत अनुकूल स्थिति।",
                        isWarning = false
                    )
                )
            } else if (dscr < 1.3) {
                flags.add(
                    BusinessHealthFlag(
                        id = "FLAG_DEBT_BURDEN",
                        titleEn = "Debt Repayment Pressure",
                        titleHi = "ऋण किश्त का अधिक दबाव",
                        descriptionEn = "Loan repayment consumes over 75% of your operating surplus. Ensure a 3-month moratorium is requested.",
                        descriptionHi = "ऋण किश्त परिचालन बचत का 75% से अधिक हिस्सा ले रही है। कम से कम 3-6 महीने की मोराटोरियम छूट अवश्य लें।",
                        isWarning = true
                    )
                )
            }
        }

        // 3. Working Capital & Growth Opportunities
        flags.add(
            BusinessHealthFlag(
                id = "FLAG_WORKING_CAPITAL_RESERVE",
                titleEn = "Working Capital Cushion",
                titleHi = "कार्यशील पूंजी सुरक्षा कोष",
                descriptionEn = "Maintain at least ₹${(opex * 1.5).roundToInt()} (45 days OpEx) in cash or liquid bank balance for uninterrupted operations.",
                descriptionHi = "निरंतर उत्पादन हेतु कम से कम 45 दिनों के खर्च (लगभग ₹${(opex * 1.5).roundToInt()}) का लिक्विड बैलेंस सुरक्षित रखें।",
                isWarning = false
            )
        )

        return BusinessHealthReport(
            estimatedMonthlyRevenue = revenue,
            estimatedMonthlyOperatingExpenses = opex,
            monthlyDebtService = monthlyDebtService,
            totalMonthlyOutflow = totalOutflow,
            netMonthlyProfit = netProfit,
            netProfitMarginPercentage = netMarginPct,
            debtServiceCoverageRatio = dscr,
            cashFlowStatus = status,
            healthScore = finalScore,
            breakEvenRevenue = breakEvenRevenue,
            flags = flags
        )
    }
}
