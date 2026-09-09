package com.ruraladvisory.finance.engine

import com.ruraladvisory.finance.model.FinancialCalculationResult

/**
 * Engine contract for evaluating margin capital inputs and producing financial structuring,
 * scheme routing, amortization schedules, and operational cost breakdowns.
 */
interface FinancialCalculatorEngine {
    fun calculate(marginCapital: Double): FinancialCalculationResult
}
