package com.ruraladvisory.advisory.model

/**
 * Geographic administrative location for hyper-local advisory targeting.
 */
data class LocationInfo(
    val village: String,
    val block: String,
    val district: String,
    val state: String = "Uttar Pradesh"
) {
    /**
     * Returns sanitized location info with empty/whitespace fields replaced
     * by respectful regional defaults based on the chosen language.
     */
    fun sanitized(language: LanguageCode = LanguageCode.EN): LocationInfo {
        val defaultVillage = if (language == LanguageCode.HI) "स्थानीय गाँव" else "Local Village"
        val defaultBlock = if (language == LanguageCode.HI) "स्थानीय प्रखंड" else "Local Block"
        val defaultDistrict = if (language == LanguageCode.HI) "स्थानीय जिला" else "Local District"
        val defaultState = if (language == LanguageCode.HI) "उत्तर प्रदेश" else "Uttar Pradesh"

        return LocationInfo(
            village = village.trim().ifBlank { defaultVillage },
            block = block.trim().ifBlank { defaultBlock },
            district = district.trim().ifBlank { defaultDistrict },
            state = state.trim().ifBlank { defaultState }
        )
    }

    /**
     * Formats location as standard 3-tier administrative hierarchy: "Village, Block, District".
     */
    fun formatted(language: LanguageCode = LanguageCode.EN): String {
        val clean = sanitized(language)
        return "${clean.village}, ${clean.block}, ${clean.district}"
    }

    /**
     * Formats location with full administrative hierarchy including state: "Village, Block, District, State".
     */
    fun formattedWithState(language: LanguageCode = LanguageCode.EN): String {
        val clean = sanitized(language)
        return "${clean.village}, ${clean.block}, ${clean.district}, ${clean.state}"
    }
}
