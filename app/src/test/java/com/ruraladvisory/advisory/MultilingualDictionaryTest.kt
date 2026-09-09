package com.ruraladvisory.advisory

import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.knowledge.RuralTradeKnowledgeBase
import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MultilingualDictionaryTest {

    private val devanagariRegex = Regex("[\\u0900-\\u097F]")

    @Test
    fun testAllCategoriesHaveBilingualTitlesAndDescriptions() {
        val categories = MultilingualDictionary.getAllCategories()
        assertEquals("Must cover all 7 business categories", 7, categories.size)

        for (cat in BusinessCategory.values()) {
            val titleEn = MultilingualDictionary.getCategoryTitle(cat, LanguageCode.EN)
            val titleHi = MultilingualDictionary.getCategoryTitle(cat, LanguageCode.HI)

            assertFalse("English title for $cat must not be blank", titleEn.isBlank())
            assertFalse("Hindi title for $cat must not be blank", titleHi.isBlank())
            assertNotEquals("Hindi title must differ from English title for $cat", titleEn, titleHi)
            assertTrue("Hindi title must contain Devanagari for $cat", devanagariRegex.containsMatchIn(titleHi))

            val descEn = MultilingualDictionary.getCategoryDescription(cat, LanguageCode.EN)
            val descHi = MultilingualDictionary.getCategoryDescription(cat, LanguageCode.HI)

            assertFalse("English desc for $cat must not be blank", descEn.isBlank())
            assertFalse("Hindi desc for $cat must not be blank", descHi.isBlank())
            assertNotEquals("Hindi desc must differ from English desc for $cat", descEn, descHi)
            assertTrue("Hindi desc must contain Devanagari for $cat", devanagariRegex.containsMatchIn(descHi))
        }
    }

    @Test
    fun testAllSixDimensionsHaveBilingualHeadings() {
        val dimensionKeys = MultilingualDictionary.getAllDimensionKeys()
        assertEquals("Must cover all 6 feasibility dimensions", 6, dimensionKeys.size)

        for (dimKey in dimensionKeys) {
            val titleEn = MultilingualDictionary.getDimensionTitle(dimKey, LanguageCode.EN)
            val titleHi = MultilingualDictionary.getDimensionTitle(dimKey, LanguageCode.HI)

            assertFalse("Dimension EN title must not be blank for $dimKey", titleEn.isBlank())
            assertFalse("Dimension HI title must not be blank for $dimKey", titleHi.isBlank())
            assertNotEquals("Dimension HI title must differ from EN title for $dimKey", titleEn, titleHi)
            assertTrue("Dimension HI title must contain Devanagari for $dimKey", devanagariRegex.containsMatchIn(titleHi))
        }
    }

    @Test
    fun testAllBudgetTiersHaveBilingualLabels() {
        for (tier in BudgetTier.values()) {
            val labelEn = MultilingualDictionary.getBudgetTierLabel(tier, LanguageCode.EN)
            val labelHi = MultilingualDictionary.getBudgetTierLabel(tier, LanguageCode.HI)

            assertFalse("Budget tier EN label must not be blank for $tier", labelEn.isBlank())
            assertFalse("Budget tier HI label must not be blank for $tier", labelHi.isBlank())
            assertNotEquals("Budget tier HI label must differ from EN label for $tier", labelEn, labelHi)
            assertTrue("Budget tier HI label must contain Devanagari for $tier", devanagariRegex.containsMatchIn(labelHi))
        }
    }

    @Test
    fun testThreatVectorsHaveBilingualNames() {
        val vectors = MultilingualDictionary.getAllThreatVectorKeys()
        assertEquals("Must cover 4 core threat vectors", 4, vectors.size)

        for (vec in vectors) {
            val nameEn = MultilingualDictionary.getThreatVectorName(vec, LanguageCode.EN)
            val nameHi = MultilingualDictionary.getThreatVectorName(vec, LanguageCode.HI)

            assertFalse("Threat vector EN name must not be blank for $vec", nameEn.isBlank())
            assertFalse("Threat vector HI name must not be blank for $vec", nameHi.isBlank())
            assertNotEquals("Threat vector HI name must differ from EN name for $vec", nameEn, nameHi)
            assertTrue("Threat vector HI name must contain Devanagari for $vec", devanagariRegex.containsMatchIn(nameHi))
        }
    }

    @Test
    fun testSwotParityAcrossAllCategoriesAndTiers() {
        for (cat in BusinessCategory.values()) {
            for (tier in BudgetTier.values()) {
                val swotEn = RuralTradeKnowledgeBase.getSwotAnalysis(cat, tier, LanguageCode.EN)
                val swotHi = RuralTradeKnowledgeBase.getSwotAnalysis(cat, tier, LanguageCode.HI)

                // Verify exact parity in quadrant element counts
                assertEquals("Strengths count parity for $cat tier $tier", swotEn.strengths.size, swotHi.strengths.size)
                assertEquals("Weaknesses count parity for $cat tier $tier", swotEn.weaknesses.size, swotHi.weaknesses.size)
                assertEquals("Opportunities count parity for $cat tier $tier", swotEn.opportunities.size, swotHi.opportunities.size)
                assertEquals("Threats count parity for $cat tier $tier", swotEn.threats.size, swotHi.threats.size)

                assertTrue("Strengths count must be > 0 for $cat $tier", swotEn.strengths.isNotEmpty())
                assertTrue("Weaknesses count must be > 0 for $cat $tier", swotEn.weaknesses.isNotEmpty())
                assertTrue("Opportunities count must be > 0 for $cat $tier", swotEn.opportunities.isNotEmpty())
                assertTrue("Threats count must be > 0 for $cat $tier", swotEn.threats.isNotEmpty())

                // Verify Hindi strings contain genuine Devanagari text
                swotHi.strengths.forEach {
                    assertFalse(it.isBlank())
                    assertTrue(devanagariRegex.containsMatchIn(it))
                }
                swotHi.weaknesses.forEach {
                    assertFalse(it.isBlank())
                    assertTrue(devanagariRegex.containsMatchIn(it))
                }
                swotHi.opportunities.forEach {
                    assertFalse(it.isBlank())
                    assertTrue(devanagariRegex.containsMatchIn(it))
                }
                swotHi.threats.forEach {
                    assertFalse(it.isBlank())
                    assertTrue(devanagariRegex.containsMatchIn(it))
                }
            }
        }
    }

    @Test
    fun testThreatMitigationParityAcrossAllCategories() {
        for (cat in BusinessCategory.values()) {
            val threatsEn = RuralTradeKnowledgeBase.getThreatsList(cat, LanguageCode.EN)
            val threatsHi = RuralTradeKnowledgeBase.getThreatsList(cat, LanguageCode.HI)

            assertEquals("Must have 4 threats in EN for $cat", 4, threatsEn.size)
            assertEquals("Must have 4 threats in HI for $cat", 4, threatsHi.size)

            for (i in 0 until 4) {
                assertFalse("EN threat must not be blank", threatsEn[i].isBlank())
                assertFalse("HI threat must not be blank", threatsHi[i].isBlank())
                assertTrue("HI threat must contain Devanagari", devanagariRegex.containsMatchIn(threatsHi[i]))
            }
        }
    }

    @Test
    fun testKeyRecommendationsParityAcrossAllCategoriesAndTiers() {
        for (cat in BusinessCategory.values()) {
            for (tier in BudgetTier.values()) {
                val recsEn = RuralTradeKnowledgeBase.getRecommendations(cat, tier, LanguageCode.EN)
                val recsHi = RuralTradeKnowledgeBase.getRecommendations(cat, tier, LanguageCode.HI)

                assertEquals("Recommendations count parity for $cat $tier", recsEn.size, recsHi.size)
                assertTrue("Recommendations must have at least 2 items for $cat $tier", recsEn.size >= 2)

                for (i in recsEn.indices) {
                    assertFalse(recsEn[i].isBlank())
                    assertFalse(recsHi[i].isBlank())
                    assertTrue(devanagariRegex.containsMatchIn(recsHi[i]))
                }
            }
        }
    }

    @Test
    fun testCreditPolicyAndQuadrantHeadersBilingualParity() {
        val policyEn = MultilingualDictionary.getCreditPolicyAdvice(LanguageCode.EN)
        val policyHi = MultilingualDictionary.getCreditPolicyAdvice(LanguageCode.HI)

        assertFalse(policyEn.isBlank())
        assertFalse(policyHi.isBlank())
        assertTrue("Hindi credit policy must contain Devanagari", devanagariRegex.containsMatchIn(policyHi))

        val quadrants = listOf("STRENGTHS", "WEAKNESSES", "OPPORTUNITIES", "THREATS")
        for (quad in quadrants) {
            val qEn = MultilingualDictionary.getSwotQuadrantTitle(quad, LanguageCode.EN)
            val qHi = MultilingualDictionary.getSwotQuadrantTitle(quad, LanguageCode.HI)

            assertFalse(qEn.isBlank())
            assertFalse(qHi.isBlank())
            assertTrue(devanagariRegex.containsMatchIn(qHi))
        }
    }
}
