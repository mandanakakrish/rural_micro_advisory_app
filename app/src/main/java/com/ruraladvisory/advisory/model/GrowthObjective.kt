package com.ruraladvisory.advisory.model

/**
 * Proposed growth ambition of the rural entrepreneur.
 * Informs AI advisory whether to focus on initial setup, working capital expansion,
 * machinery modernization, or direct-to-consumer distribution.
 */
enum class GrowthObjective {
    NEW_VENTURE,
    EXPANSION,
    MODERNIZATION,
    MARKET_REACH;

    fun displayName(lang: LanguageCode): String = when (this) {
        NEW_VENTURE -> if (lang == LanguageCode.HI) "नया उद्यम" else "New Venture"
        EXPANSION -> if (lang == LanguageCode.HI) "व्यापार विस्तार" else "Expansion"
        MODERNIZATION -> if (lang == LanguageCode.HI) "मशीनरी व आधुनिकीकरण" else "Modernization"
        MARKET_REACH -> if (lang == LanguageCode.HI) "बाजार पहुंच व विक्रय" else "Market Reach"
    }
}

/**
 * Actionable step in the "What Should I Do Next?" personalized implementation roadmap.
 */
data class ActionPlanStep(
    val stepNumber: Int,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val timelineEn: String,
    val timelineHi: String,
    val priorityEn: String,
    val priorityHi: String
) {
    fun title(lang: LanguageCode): String = if (lang == LanguageCode.HI) titleHi else titleEn
    fun description(lang: LanguageCode): String = if (lang == LanguageCode.HI) descriptionHi else descriptionEn
    fun timeline(lang: LanguageCode): String = if (lang == LanguageCode.HI) timelineHi else timelineEn
    fun priority(lang: LanguageCode): String = if (lang == LanguageCode.HI) priorityHi else priorityEn
}
