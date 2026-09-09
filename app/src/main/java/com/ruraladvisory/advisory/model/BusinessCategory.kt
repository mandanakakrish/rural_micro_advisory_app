package com.ruraladvisory.advisory.model

import com.ruraladvisory.advisory.knowledge.MultilingualDictionary

/**
 * Proposed rural micro-enterprise business categories.
 */
enum class BusinessCategory {
    DAIRY,
    RETAIL,
    FOOD_PROCESSING,
    TEXTILES,
    POULTRY,
    HANDICRAFTS,
    AGRO_SERVICES;

    fun displayName(lang: LanguageCode): String = MultilingualDictionary.getCategoryTitle(this, lang)

    companion object {
        fun fromCustomText(text: String): BusinessCategory {
            val lower = text.lowercase()
            return when {
                lower.contains("dairy") || lower.contains("milk") || lower.contains("cow") || lower.contains("buffalo") || lower.contains("cattle") || lower.contains("ghee") || lower.contains("डेयरी") || lower.contains("दूध") || lower.contains("पशु") -> DAIRY
                lower.contains("poultry") || lower.contains("chicken") || lower.contains("egg") || lower.contains("broiler") || lower.contains("layer") || lower.contains("मुर्गी") || lower.contains("अंडा") || lower.contains("कुक्कुट") -> POULTRY
                lower.contains("tailor") || lower.contains("textile") || lower.contains("apparel") || lower.contains("handloom") || lower.contains("cloth") || lower.contains("garment") || lower.contains("sewing") || lower.contains("सिलाई") || lower.contains("कपड़ा") || lower.contains("वस्त्र") || lower.contains("हथकरघा") -> TEXTILES
                lower.contains("food") || lower.contains("grain") || lower.contains("flour") || lower.contains("oil") || lower.contains("spice") || lower.contains("pickle") || lower.contains("mill") || lower.contains("atta") || lower.contains("dal") || lower.contains("खाद्य") || lower.contains("अनाज") || lower.contains("तेल") || lower.contains("मसाला") || lower.contains("आटा") || lower.contains("चक्की") -> FOOD_PROCESSING
                lower.contains("agri") || lower.contains("agro") || lower.contains("farm") || lower.contains("seed") || lower.contains("tractor") || lower.contains("pump") || lower.contains("rental") || lower.contains("fertilizer") || lower.contains("कृषि") || lower.contains("बीज") || lower.contains("ट्रैक्टर") || lower.contains("उपकरण") -> AGRO_SERVICES
                lower.contains("handicraft") || lower.contains("artisanal") || lower.contains("pottery") || lower.contains("clay") || lower.contains("bamboo") || lower.contains("craft") || lower.contains("terracotta") || lower.contains("eco-product") || lower.contains("हस्तशिल्प") || lower.contains("मिट्टी") || lower.contains("बांस") || lower.contains("पत्तल") -> HANDICRAFTS
                else -> RETAIL
            }
        }
    }
}
