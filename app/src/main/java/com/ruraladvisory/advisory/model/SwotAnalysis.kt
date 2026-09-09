package com.ruraladvisory.advisory.model

/**
 * 4-quadrant SWOT matrix (Strengths, Weaknesses, Opportunities, Threats)
 * calibrated to the entrepreneur's budget tier and local business sector.
 */
data class SwotAnalysis(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val opportunities: List<String>,
    val threats: List<String>
)
