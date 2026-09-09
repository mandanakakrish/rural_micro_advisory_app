package com.ruraladvisory.finance

import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.finance.engine.GovernmentSchemeDirectory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [GovernmentSchemeDirectory] verifying scheme matching,
 * primary routing logic, sector-specific programs, and bilingual localization.
 */
class EligibleSchemesTest {

    @Test
    fun testMicroFinanceRouting_CostUnder140k() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.RETAIL,
            marginCapital = 10000.0,
            projectCost = 100000.0
        )

        assertTrue(schemes.isNotEmpty())
        val primary = schemes.firstOrNull { it.isPrimaryRouted }
        assertNotNull("Primary scheme must exist", primary)
        assertEquals("SCA_MICRO_FINANCE", primary!!.id)
        assertTrue(primary.nameEn.contains("Micro Finance"))
        assertEquals("6.5% p.a. Concessional", primary.interestRateDescriptionEn)

        // Verify MUDRA scheme is also included for RETAIL
        val mudra = schemes.firstOrNull { it.id == "SECTOR_RETAIL_MUDRA" }
        assertNotNull("MUDRA scheme should be eligible for retail", mudra)
    }

    @Test
    fun testTermLoanRouting_CostAbove140k() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.FOOD_PROCESSING,
            marginCapital = 25000.0,
            projectCost = 250000.0
        )

        val primary = schemes.firstOrNull { it.isPrimaryRouted }
        assertNotNull("Primary scheme must exist", primary)
        assertEquals("SCA_TERM_LOAN", primary!!.id)
        assertTrue(primary.nameEn.contains("Term Loan"))
        assertEquals("8.0% p.a. Concessional", primary.interestRateDescriptionEn)

        // Verify PMFME scheme is included for FOOD_PROCESSING
        val pmfme = schemes.firstOrNull { it.id == "SECTOR_FOOD_PMFME" }
        assertNotNull("PMFME should be eligible for food processing", pmfme)
    }

    @Test
    fun testDairyScheme_IncludesAHIDF() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.DAIRY,
            marginCapital = 12000.0,
            projectCost = 120000.0
        )

        val ahidf = schemes.firstOrNull { it.id == "SECTOR_DAIRY_AHIDF" }
        assertNotNull(ahidf)
        assertTrue(ahidf!!.nameEn.contains("AHIDF"))
        assertTrue(ahidf.nameHi.contains("डेयरी"))
    }

    @Test
    fun testPoultryScheme_IncludesNLM() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.POULTRY,
            marginCapital = 12000.0,
            projectCost = 120000.0
        )

        val nlm = schemes.firstOrNull { it.id == "SECTOR_POULTRY_NLM" }
        assertNotNull(nlm)
        assertTrue(nlm!!.nameEn.contains("National Livestock Mission"))
    }

    @Test
    fun testTextileScheme_IncludesPMVishwakarma() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.TEXTILES,
            marginCapital = 10000.0,
            projectCost = 100000.0
        )

        val vishwakarma = schemes.firstOrNull { it.id == "SECTOR_TEXTILE_VISHWAKARMA" }
        assertNotNull(vishwakarma)
        assertTrue(vishwakarma!!.nameEn.contains("PM Vishwakarma"))
    }

    @Test
    fun testAgroServicesScheme_IncludesSMAM() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.AGRO_SERVICES,
            marginCapital = 15000.0,
            projectCost = 150000.0
        )

        val smam = schemes.firstOrNull { it.id == "SECTOR_AGRO_SMAM" }
        assertNotNull(smam)
        assertTrue(smam!!.nameEn.contains("SMAM"))
    }

    @Test
    fun testHandicraftsScheme_IncludesCraftVishwakarma() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.HANDICRAFTS,
            marginCapital = 8000.0,
            projectCost = 80000.0
        )

        val craft = schemes.firstOrNull { it.id == "SECTOR_CRAFT_VISHWAKARMA" }
        assertNotNull(craft)
        assertTrue(craft!!.nameEn.contains("Vishwakarma"))
    }

    @Test
    fun testBilingualLocalizationAccessors() {
        val schemes = GovernmentSchemeDirectory.getEligibleSchemes(
            category = BusinessCategory.RETAIL,
            marginCapital = 10000.0,
            projectCost = 100000.0
        )

        val scheme = schemes.first()
        // English
        assertEquals(scheme.nameEn, scheme.name(LanguageCode.EN))
        assertEquals(scheme.authorityEn, scheme.authority(LanguageCode.EN))
        assertEquals(scheme.interestRateDescriptionEn, scheme.interestRate(LanguageCode.EN))
        assertEquals(scheme.maxCeilingEn, scheme.maxCeiling(LanguageCode.EN))
        assertEquals(scheme.subsidyBenefitEn, scheme.subsidyBenefit(LanguageCode.EN))
        assertEquals(scheme.tenureMoratoriumEn, scheme.tenureMoratorium(LanguageCode.EN))
        assertEquals(scheme.eligibilityNoteEn, scheme.eligibilityNote(LanguageCode.EN))

        // Hindi
        assertEquals(scheme.nameHi, scheme.name(LanguageCode.HI))
        assertEquals(scheme.authorityHi, scheme.authority(LanguageCode.HI))
        assertEquals(scheme.interestRateDescriptionHi, scheme.interestRate(LanguageCode.HI))
        assertEquals(scheme.maxCeilingHi, scheme.maxCeiling(LanguageCode.HI))
        assertEquals(scheme.subsidyBenefitHi, scheme.subsidyBenefit(LanguageCode.HI))
        assertEquals(scheme.tenureMoratoriumHi, scheme.tenureMoratorium(LanguageCode.HI))
        assertEquals(scheme.eligibilityNoteHi, scheme.eligibilityNote(LanguageCode.HI))
    }
}
