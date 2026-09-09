package com.ruraladvisory.advisory.model

/**
 * Statutory and operational enterprise scale of the rural business.
 * Calibrates credit thresholds, scheme eligibility (e.g. Mudra Shishu/Kishore vs PMEGP vs CGTMSE),
 * and operational recommendations.
 */
enum class BusinessScale {
    MICRO,
    SMALL,
    MEDIUM;

    fun displayName(lang: LanguageCode): String = when (this) {
        MICRO -> if (lang == LanguageCode.HI) "सूक्ष्म (Micro)" else "Micro (सूक्ष्म)"
        SMALL -> if (lang == LanguageCode.HI) "लघु (Small)" else "Small (लघु)"
        MEDIUM -> if (lang == LanguageCode.HI) "मध्यम (Medium)" else "Medium (मध्यम)"
    }

    fun description(lang: LanguageCode): String = when (this) {
        MICRO -> if (lang == LanguageCode.HI) "निवेश ₹1 करोड़ तक / टर्नओवर ₹5 करोड़ तक" else "Investment up to ₹1 Cr / Turnover up to ₹5 Cr"
        SMALL -> if (lang == LanguageCode.HI) "निवेश ₹10 करोड़ तक / टर्नओवर ₹50 करोड़ तक" else "Investment up to ₹10 Cr / Turnover up to ₹50 Cr"
        MEDIUM -> if (lang == LanguageCode.HI) "निवेश ₹50 करोड़ तक / टर्नओवर ₹250 करोड़ तक" else "Investment up to ₹50 Cr / Turnover up to ₹250 Cr"
    }
}
