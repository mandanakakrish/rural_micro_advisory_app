package com.ruraladvisory.advisory.knowledge

import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode

/**
 * Multilingual taxonomy and 1:1 bilingual dictionary for English and Hindi.
 * Provides complete localized parity across categories, feasibility dimensions,
 * budget tiers, threat vectors, and advisory headers.
 */
object MultilingualDictionary {

    data class CategoryMetadata(
        val englishTitle: String,
        val hindiTitle: String,
        val englishDescription: String,
        val hindiDescription: String
    )

    private val categoryMap: Map<BusinessCategory, CategoryMetadata> = mapOf(
        BusinessCategory.DAIRY to CategoryMetadata(
            englishTitle = "Dairy Farming & Milk Value Chain",
            hindiTitle = "डेयरी एवं दुग्ध मूल्य श्रृंखला",
            englishDescription = "Milk production, chilling, collection, and value-added dairy products (ghee, paneer, curd).",
            hindiDescription = "दूध उत्पादन, संकलन, शीतलन एवं मूल्य संवर्धित उत्पाद (घी, पनीर, दही)।"
        ),
        BusinessCategory.RETAIL to CategoryMetadata(
            englishTitle = "Rural Grocery & Daily Needs (Kirana)",
            hindiTitle = "ग्रामीण किराना एवं दैनिक आवश्यकता भंडार",
            englishDescription = "Daily household staples, fast-moving consumer goods (FMCG), and essential provisions.",
            hindiDescription = "दैनिक घरेलू खाद्य सामग्री, एफएमसीजी वस्तुएं एवं आवश्यक किराना सामान।"
        ),
        BusinessCategory.FOOD_PROCESSING to CategoryMetadata(
            englishTitle = "Food & Grain Processing",
            hindiTitle = "खाद्य एवं अनाज प्रसंस्करण",
            englishDescription = "Flour milling (atta chakki), pulse de-husking (dal mill), spice pulverizing, and oil expelling.",
            hindiDescription = "आटा चक्की, दाल प्रसंस्करण, मसाला पिसाई एवं सरसों तेल निष्कर्षण इकाई।"
        ),
        BusinessCategory.TEXTILES to CategoryMetadata(
            englishTitle = "Apparel, Tailoring & Handloom",
            hindiTitle = "वस्त्र, सिलाई एवं हथकरघा",
            englishDescription = "Bespoke tailoring, garment alteration, school uniform supply, and ready-to-wear apparel.",
            hindiDescription = "सिलाई केंद्र, कपड़े की मरम्मत, स्कूल यूनिफॉर्म निर्माण एवं रेडीमेड वस्त्र।"
        ),
        BusinessCategory.POULTRY to CategoryMetadata(
            englishTitle = "Poultry & Egg Farming",
            hindiTitle = "कुक्कुट / मुर्गी एवं अंडा पालन",
            englishDescription = "Commercial broiler chicken rearing, native desi poultry, and high-yield layer egg production.",
            hindiDescription = "व्यावसायिक ब्रायलर पालन, देशी मुर्गी पालन एवं पौष्टिक अंडा उत्पादन।"
        ),
        BusinessCategory.HANDICRAFTS to CategoryMetadata(
            englishTitle = "Artisanal Handicrafts & Eco-Products",
            hindiTitle = "पारंपरिक हस्तशिल्प एवं पर्यावरण-अनुकूल उत्पाद",
            englishDescription = "Bamboo utility crafts, terracotta pottery, and biodegradable leaf tableware (pattal-dona).",
            hindiDescription = "बांस शिल्प, मिट्टी के बर्तन एवं पर्यावरण-अनुकूल पत्तल-दोना निर्माण।"
        ),
        BusinessCategory.AGRO_SERVICES to CategoryMetadata(
            englishTitle = "Agri-Services & Equipment Rental",
            hindiTitle = "कृषि सेवा केंद्र एवं उपकरण किराया",
            englishDescription = "Custom hiring centre (CHC) for farm machinery, power tillers, sprayers, and crop processing.",
            hindiDescription = "ट्रैक्टर, पावर टिलर, स्प्रेयर एवं कृषि यंत्रों का कस्टम हायरिंग सेवा केंद्र।"
        )
    )

    data class DimensionMetadata(
        val key: String,
        val englishTitle: String,
        val hindiTitle: String
    )

    const val DIMENSION_MARKET_REACH = "MARKET_REACH"
    const val DIMENSION_OPPORTUNITY = "OPPORTUNITY"
    const val DIMENSION_SWOT = "SWOT"
    const val DIMENSION_THREATS = "THREATS"
    const val DIMENSION_COMPETITOR = "COMPETITOR"
    const val DIMENSION_PRICING = "PRICING"

    private val dimensionMap: Map<String, DimensionMetadata> = mapOf(
        DIMENSION_MARKET_REACH to DimensionMetadata(
            key = DIMENSION_MARKET_REACH,
            englishTitle = "Market Reach & Distribution Channels",
            hindiTitle = "बाजार पहुंच एवं स्थानीय वितरण माध्यम"
        ),
        DIMENSION_OPPORTUNITY to DimensionMetadata(
            key = DIMENSION_OPPORTUNITY,
            englishTitle = "Opportunity & Unserved Niche Analysis",
            hindiTitle = "व्यापारिक अवसर एवं नए क्षेत्र का विश्लेषण"
        ),
        DIMENSION_SWOT to DimensionMetadata(
            key = DIMENSION_SWOT,
            englishTitle = "SWOT Analysis (Strengths, Weaknesses, Opportunities, Threats)",
            hindiTitle = "स्वॉट विश्लेषण (ताकत, कमजोरी, अवसर, जोखिम)"
        ),
        DIMENSION_THREATS to DimensionMetadata(
            key = DIMENSION_THREATS,
            englishTitle = "Threats Identification & Risk Mitigation",
            hindiTitle = "जोखिम पहचान एवं सुरक्षा के उपाय"
        ),
        DIMENSION_COMPETITOR to DimensionMetadata(
            key = DIMENSION_COMPETITOR,
            englishTitle = "Competitor Mapping & Density",
            hindiTitle = "प्रतियोगी विश्लेषण एवं स्थानीय प्रतिस्पर्धा"
        ),
        DIMENSION_PRICING to DimensionMetadata(
            key = DIMENSION_PRICING,
            englishTitle = "Product Valuation & Pricing Strategy",
            hindiTitle = "उत्पाद मूल्य एवं सही मूल्य-निर्धारण रणनीति"
        )
    )

    data class BudgetTierMetadata(
        val englishLabel: String,
        val hindiLabel: String
    )

    private val budgetTierMap: Map<BudgetTier, BudgetTierMetadata> = mapOf(
        BudgetTier.MICRO to BudgetTierMetadata(
            englishLabel = "Micro Enterprise (< ₹25k margin)",
            hindiLabel = "सूक्ष्म उद्यम (मार्जिन < ₹25,000)"
        ),
        BudgetTier.SMALL to BudgetTierMetadata(
            englishLabel = "Small Enterprise (₹25k - ₹1.5L margin)",
            hindiLabel = "लघु उद्यम (मार्जिन ₹25,000 - ₹1.5 लाख)"
        ),
        BudgetTier.MEDIUM to BudgetTierMetadata(
            englishLabel = "Medium Enterprise (> ₹1.5L margin)",
            hindiLabel = "मध्यम उद्यम (मार्जिन > ₹1.5 लाख)"
        )
    )

    data class ThreatVectorMetadata(
        val key: String,
        val englishName: String,
        val hindiName: String
    )

    const val VECTOR_SEASONAL = "SEASONAL_FLUCTUATION"
    const val VECTOR_SUPPLY = "SUPPLY_CHAIN_BOTTLENECK"
    const val VECTOR_RAW_MATERIAL = "RAW_MATERIAL_VOLATILITY"
    const val VECTOR_SINGLE_BUYER = "SINGLE_BUYER_DEPENDENCY"

    private val threatVectorMap: Map<String, ThreatVectorMetadata> = mapOf(
        VECTOR_SEASONAL to ThreatVectorMetadata(
            key = VECTOR_SEASONAL,
            englishName = "Seasonal Demand Fluctuations",
            hindiName = "मौसमी मांग में उतार-चढ़ाव"
        ),
        VECTOR_SUPPLY to ThreatVectorMetadata(
            key = VECTOR_SUPPLY,
            englishName = "Supply Chain Bottlenecks",
            hindiName = "आपूर्ति श्रृंखला में बाधा"
        ),
        VECTOR_RAW_MATERIAL to ThreatVectorMetadata(
            key = VECTOR_RAW_MATERIAL,
            englishName = "Raw Material Price Volatility",
            hindiName = "कच्चे माल की कीमतों में अस्थिरता"
        ),
        VECTOR_SINGLE_BUYER to ThreatVectorMetadata(
            key = VECTOR_SINGLE_BUYER,
            englishName = "Single-Buyer Dependency & Credit Risk",
            hindiName = "एकल खरीदार पर निर्भरता एवं उधारी जोखिम"
        )
    )

    fun getCategoryTitle(category: BusinessCategory, language: LanguageCode): String {
        val meta = categoryMap[category] ?: return category.name
        return if (language == LanguageCode.HI) meta.hindiTitle else meta.englishTitle
    }

    fun getCategoryDescription(category: BusinessCategory, language: LanguageCode): String {
        val meta = categoryMap[category] ?: return ""
        return if (language == LanguageCode.HI) meta.hindiDescription else meta.englishDescription
    }

    fun getDimensionTitle(key: String, language: LanguageCode): String {
        val meta = dimensionMap[key] ?: return key
        return if (language == LanguageCode.HI) meta.hindiTitle else meta.englishTitle
    }

    fun getBudgetTierLabel(tier: BudgetTier, language: LanguageCode): String {
        val meta = budgetTierMap[tier] ?: return tier.name
        return if (language == LanguageCode.HI) meta.hindiLabel else meta.englishLabel
    }

    fun getThreatVectorName(vectorKey: String, language: LanguageCode): String {
        val meta = threatVectorMap[vectorKey] ?: return vectorKey
        return if (language == LanguageCode.HI) meta.hindiName else meta.englishName
    }

    fun getAllCategories(): List<BusinessCategory> = categoryMap.keys.toList()

    fun getAllDimensionKeys(): List<String> = dimensionMap.keys.toList()

    fun getAllThreatVectorKeys(): List<String> = threatVectorMap.keys.toList()

    fun getAllBudgetTiers(): List<BudgetTier> = budgetTierMap.keys.toList()

    fun getSwotQuadrantTitle(quadrant: String, language: LanguageCode): String = when (quadrant.uppercase()) {
        "STRENGTHS" -> if (language == LanguageCode.HI) "ताकत (Strengths)" else "Strengths"
        "WEAKNESSES" -> if (language == LanguageCode.HI) "कमजोरियां (Weaknesses)" else "Weaknesses"
        "OPPORTUNITIES" -> if (language == LanguageCode.HI) "अवसर (Opportunities)" else "Opportunities"
        "THREATS" -> if (language == LanguageCode.HI) "जोखिम (Threats)" else "Threats"
        else -> quadrant
    }

    fun getRecommendationsHeader(language: LanguageCode): String =
        if (language == LanguageCode.HI) "प्रमुख रणनीतिक सुझाव" else "Key Strategic Recommendations"

    fun getCreditPolicyAdvice(language: LanguageCode): String =
        if (language == LanguageCode.HI) {
            "उधारी नीति: कुल मासिक बिक्री के 10% से 15% से अधिक उधारी न दें। नए माल/सेवा देने से पूर्व 7 से 10 दिनों में पुराना भुगतान अनिवार्य करें।"
        } else {
            "Credit Policy: Restrict informal customer credit (Udhar) to a strict ceiling of 10% to 15% of monthly turnover. Enforce a 7 to 10 day settlement cycle before issuing fresh credit."
        }
}
