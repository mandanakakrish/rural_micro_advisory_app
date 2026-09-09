package com.ruraladvisory.advisory.engine

import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.FeasibilityReport

/**
 * Common contract for rural feasibility advisory synthesis engines.
 */
interface FeasibilityAdvisoryEngine {
    /**
     * Synthesizes a comprehensive 6-dimension feasibility report from input parameters.
     */
    fun generateReport(input: AdvisoryInput): FeasibilityReport
}
