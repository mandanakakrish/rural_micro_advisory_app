package com.ruraladvisory.advisory.engine

import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.FeasibilityReport

/**
 * Hybrid advisory engine that attempts remote LLM/Gemini enhancement when configured and online,
 * with immediate, silent fallback to [OfflineHeuristicAdvisoryEngine] if unconfigured,
 * offline, timed out, or encountering network errors.
 */
class HybridAdvisoryEngine(
    val apiKey: String? = null,
    val fallbackEngine: FeasibilityAdvisoryEngine = OfflineHeuristicAdvisoryEngine(),
    private val remoteProvider: ((AdvisoryInput) -> FeasibilityReport?)? = null
) : FeasibilityAdvisoryEngine {

    override fun generateReport(input: AdvisoryInput): FeasibilityReport {
        // If unconfigured or no remote provider attached, instantly fall back
        if (apiKey.isNullOrBlank() && remoteProvider == null) {
            return fallbackEngine.generateReport(input)
        }

        return try {
            val remoteResult = remoteProvider?.invoke(input)
            if (remoteResult != null) {
                remoteResult.copy(isOfflineGenerated = false)
            } else {
                fallbackEngine.generateReport(input)
            }
        } catch (_: Throwable) {
            // Silent, instantaneous fallback upon any error, exception, or timeout
            fallbackEngine.generateReport(input)
        }
    }
}
