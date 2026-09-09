package com.ruraladvisory.advisory

import com.ruraladvisory.advisory.engine.GeminiAdvisoryService
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GeminiAdvisoryService]:
 * - Parameterized prebuilt prompt generation
 * - Candidate JSON extraction
 * - JSON response parsing into [FeasibilityReport] & [FinancialViabilityEstimate]
 */
class GeminiAdvisoryServiceTest {

    private lateinit var service: GeminiAdvisoryService

    @Before
    fun setUp() {
        service = GeminiAdvisoryService()
    }

    @Test
    fun buildPrebuiltPrompt_interpolatesAllVariablesCorrectly() {
        val input = AdvisoryInput(
            location = LocationInfo(village = "Sultanpur", block = "Chail", district = "Kaushambi"),
            marginCapital = 14000.0,
            category = BusinessCategory.FOOD_PROCESSING,
            customCategory = "Cold-Pressed Mustard Oil Mill",
            businessDetails = "Purchase oil expeller to extract 50kg oil per day for local households.",
            language = LanguageCode.EN
        )

        val prompt = service.buildPrebuiltPrompt(
            input = input,
            projectCost = 140000.0,
            loanAmount = 125000.0,
            schemeName = "Micro Finance Scheme",
            promoterEquity = 15000.0,
            annualInterestRate = 0.065,
            tenureYears = 3,
            moratoriumMonths = 3
        )

        // Verify all required prompt variables are properly interpolated
        assertTrue("Contains village", prompt.contains("Sultanpur"))
        assertTrue("Contains block", prompt.contains("Chail"))
        assertTrue("Contains district", prompt.contains("Kaushambi"))
        assertTrue("Contains margin capital", prompt.contains("14000.00"))
        assertTrue("Contains project cost", prompt.contains("140000.00"))
        assertTrue("Contains loan amount", prompt.contains("125000.00"))
        assertTrue("Contains scheme name", prompt.contains("Micro Finance Scheme"))
        assertTrue("Contains interest rate", prompt.contains("6.5%"))
        assertTrue("Contains custom category", prompt.contains("Cold-Pressed Mustard Oil Mill"))
        assertTrue("Contains business details", prompt.contains("Purchase oil expeller"))
        assertTrue("Specifies JSON schema", prompt.contains("executiveSummary"))
        assertTrue("Specifies SWOT in schema", prompt.contains("swotAnalysis"))
        assertTrue("Specifies financial viability in schema", prompt.contains("financialViability"))
    }

    @Test
    fun buildPrebuiltPrompt_hindiLanguage_specifiesHindiTarget() {
        val input = AdvisoryInput(
            location = LocationInfo(village = "रामपुर", block = "मंझनपुर", district = "कौशाम्बी"),
            marginCapital = 25000.0,
            category = BusinessCategory.DAIRY,
            customCategory = "डेयरी व दुग्ध संकलन केंद्र",
            businessDetails = "2 गाय खरीदकर दूध संकलन केंद्र शुरू करने की योजना",
            language = LanguageCode.HI
        )

        val prompt = service.buildPrebuiltPrompt(
            input = input,
            projectCost = 250000.0,
            loanAmount = 225000.0,
            schemeName = "Term Loan Scheme",
            promoterEquity = 25000.0,
            annualInterestRate = 0.08,
            tenureYears = 7,
            moratoriumMonths = 6
        )

        assertTrue(prompt.contains("Hindi (हिंदी)"))
        assertTrue(prompt.contains("डेयरी व दुग्ध संकलन केंद्र"))
    }

    @Test
    fun extractCandidateText_validGeminiResponse_extractsText() {
        val geminiJson = """
        {
          "candidates": [
            {
              "content": {
                "parts": [
                  {
                    "text": "{\"executiveSummary\": \"Highly viable rural trade.\"}"
                  }
                ]
              }
            }
          ]
        }
        """.trimIndent()

        val text = service.extractCandidateText(geminiJson)
        assertNotNull(text)
        assertTrue(text!!.contains("Highly viable rural trade"))
    }

    @Test
    fun extractCandidateText_emptyCandidates_returnsNull() {
        val geminiJson = """{ "candidates": [] }"""
        val text = service.extractCandidateText(geminiJson)
        assertNull(text)
    }

    @Test
    fun parseGeminiJsonResponse_validStructuredJson_returnsPopulatedFeasibilityReport() {
        val structuredJson = """
        {
          "executiveSummary": "Outstanding opportunity in Kaushambi for pure cold-pressed mustard oil with steady local demand.",
          "marketReach": "Direct sales to 8 surrounding villages within 12 km radius and weekly Haat bazaars.",
          "opportunityAnalysis": "Substantial gap in unadulterated edible oil with strong preference over packaged commercial brands.",
          "swotAnalysis": {
            "strengths": ["Zero transport middleman", "Fresh organic appeal", "High oil recovery rate"],
            "weaknesses": ["Power supply dependency", "Seasonal seed price volatility"],
            "opportunities": ["By-product mustard cake (khali) sales to local dairy farmers", "Custom milling service"],
            "threats": ["Erratic village electricity", "Large industrial refined oil competition"]
          },
          "threatsIdentification": [
            "Power outages: Install diesel generator or 5HP solar drive backup",
            "Mustard seed price surge: Procure directly during harvest season"
          ],
          "competitorMapping": "Only two traditional kolhu units in the block with slow turnaround; electric expeller offers 4x throughput.",
          "pricingStrategy": "Retail at ₹175/litre with 28% gross margin; wholesale mustard cake at ₹30/kg to cattle owners.",
          "keyRecommendations": [
            "Procure a certified 9-bolt expeller and 5HP motor",
            "Sign supply tie-ups with 15 local dairy farmers for cattle feed cake",
            "Offer transparent live pressing to build customer trust"
          ],
          "financialViability": {
            "expectedMonthlyRevenue": "₹75,000",
            "expectedMonthlyNetProfit": "₹22,000",
            "breakevenTimeline": "5 months",
            "roiPercentage": "31%"
          }
        }
        """.trimIndent()

        val input = AdvisoryInput(
            location = LocationInfo("Rampur", "Manjhanpur", "Kaushambi"),
            marginCapital = 14000.0,
            category = BusinessCategory.FOOD_PROCESSING,
            customCategory = "Cold-Pressed Mustard Oil",
            businessDetails = "Buy mini expeller",
            language = LanguageCode.EN
        )

        val report = service.parseGeminiJsonResponse(structuredJson, input)
        assertNotNull(report)
        assertTrue(report!!.isAiGenerated)
        assertFalse(report.isOfflineGenerated)
        assertEquals("Gemini 1.5 Flash", report.aiModelName)
        assertEquals("Cold-Pressed Mustard Oil", report.customCategory)
        assertTrue(report.executiveSummary.contains("Outstanding opportunity in Kaushambi"))
        assertEquals(3, report.swotAnalysis.strengths.size)
        assertEquals(2, report.swotAnalysis.weaknesses.size)
        assertEquals(2, report.threatsIdentification.size)
        assertEquals(3, report.keyRecommendations.size)

        val fin = report.financialViability
        assertNotNull(fin)
        assertEquals("₹75,000", fin?.expectedMonthlyRevenue)
        assertEquals("₹22,000", fin?.expectedMonthlyNetProfit)
        assertEquals("5 months", fin?.breakevenTimeline)
        assertEquals("31%", fin?.roiPercentage)
    }

    @Test
    fun parseGeminiJsonResponse_withMarkdownCodeFences_cleansAndParsesSuccessfully() {
        val fencedJson = """
        ```json
        {
          "executiveSummary": "Tailoring boutique has strong festive and school uniform demand.",
          "marketReach": "5 km radius covering 4 villages.",
          "opportunityAnalysis": "School uniform stitching and bridal wear.",
          "swotAnalysis": {
            "strengths": ["Skilled stitching"],
            "weaknesses": ["Single operator"],
            "opportunities": ["Bulk uniforms"],
            "threats": ["Ready-made garments"]
          },
          "threatsIdentification": ["Ready-made clothes: Focus on custom fit"],
          "competitorMapping": "Few local tailors with long delays.",
          "pricingStrategy": "Stitching fee ₹150-₹350 per garment.",
          "keyRecommendations": ["Buy motorized sewing machine"]
        }
        ```
        """.trimIndent()

        val input = AdvisoryInput(
            location = LocationInfo("Rampur", "Manjhanpur", "Kaushambi"),
            marginCapital = 10000.0,
            customCategory = "Tailoring Shop",
            language = LanguageCode.EN
        )

        val report = service.parseGeminiJsonResponse(fencedJson, input)
        assertNotNull(report)
        assertTrue(report!!.isAiGenerated)
        assertTrue(report.executiveSummary.contains("Tailoring boutique"))
        assertEquals(BusinessCategory.TEXTILES, report.category) // Mapped via fromCustomText
    }

    @Test
    fun parseGeminiJsonResponse_malformedJson_returnsNullGracefully() {
        val invalidJson = "{ invalid json content ... "
        val input = AdvisoryInput(
            location = LocationInfo("Rampur", "Manjhanpur", "Kaushambi"),
            marginCapital = 10000.0
        )

        val report = service.parseGeminiJsonResponse(invalidJson, input)
        assertNull(report)
    }
}
