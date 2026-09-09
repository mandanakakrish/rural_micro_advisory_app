package com.ruraladvisory.finance.model

import com.ruraladvisory.advisory.model.LanguageCode

/**
 * Data model representing a government concessional scheme or subsidy program
 * for which a rural micro-enterprise qualifies.
 */
data class EligibleScheme(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val authorityEn: String,
    val authorityHi: String,
    val interestRateDescriptionEn: String,
    val interestRateDescriptionHi: String,
    val maxCeilingEn: String,
    val maxCeilingHi: String,
    val subsidyBenefitEn: String,
    val subsidyBenefitHi: String,
    val tenureMoratoriumEn: String,
    val tenureMoratoriumHi: String,
    val isPrimaryRouted: Boolean,
    val isEligible: Boolean,
    val eligibilityNoteEn: String,
    val eligibilityNoteHi: String,
    val requiredDocumentsEn: List<String> = emptyList(),
    val requiredDocumentsHi: List<String> = emptyList(),
    val applicationProcessEn: String = "",
    val applicationProcessHi: String = "",
    val portalUrl: String = ""
) {
    fun name(lang: LanguageCode): String = if (lang == LanguageCode.HI) nameHi else nameEn
    fun authority(lang: LanguageCode): String = if (lang == LanguageCode.HI) authorityHi else authorityEn
    fun interestRate(lang: LanguageCode): String = if (lang == LanguageCode.HI) interestRateDescriptionHi else interestRateDescriptionEn
    fun maxCeiling(lang: LanguageCode): String = if (lang == LanguageCode.HI) maxCeilingHi else maxCeilingEn
    fun subsidyBenefit(lang: LanguageCode): String = if (lang == LanguageCode.HI) subsidyBenefitHi else subsidyBenefitEn
    fun tenureMoratorium(lang: LanguageCode): String = if (lang == LanguageCode.HI) tenureMoratoriumHi else tenureMoratoriumEn
    fun eligibilityNote(lang: LanguageCode): String = if (lang == LanguageCode.HI) eligibilityNoteHi else eligibilityNoteEn
    fun requiredDocuments(lang: LanguageCode): List<String> = if (lang == LanguageCode.HI) requiredDocumentsHi else requiredDocumentsEn
    fun applicationProcess(lang: LanguageCode): String = if (lang == LanguageCode.HI) applicationProcessHi else applicationProcessEn
}
