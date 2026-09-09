package com.ruraladvisory.advisory.engine

import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.FinancialViabilityEstimate
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.SwotAnalysis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

/**
 * Service orchestrating Gemini AI real-time advisory generation.
 * Formulates parameterized prompts based on real local data, margin capital,
 * and custom business details, then parses structured JSON into [FeasibilityReport].
 */
class GeminiAdvisoryService(
    private val modelEndpoint: String = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"
) {

    /**
     * Constructs an optimized prebuilt prompt parameterized with margin, location,
     * and business concept/details for fast, structured AI inference.
     */
    fun buildPrebuiltPrompt(
        input: AdvisoryInput,
        projectCost: Double,
        loanAmount: Double,
        schemeName: String,
        promoterEquity: Double,
        annualInterestRate: Double,
        tenureYears: Int,
        moratoriumMonths: Int
    ): String {
        val targetLang = if (input.language == LanguageCode.HI) "Hindi (हिंदी)" else "English"
        val village = input.location.village.ifBlank { "Rampur" }
        val block = input.location.block.ifBlank { "Manjhanpur" }
        val district = input.location.district.ifBlank { "Kaushambi" }
        val state = input.location.state.ifBlank { "Uttar Pradesh" }
        val categoryName = input.customCategory.ifBlank { input.category.name }
        val details = input.businessDetails.ifBlank { "Micro rural enterprise startup." }
        val scaleStr = input.scale.displayName(LanguageCode.EN)
        val capacityStr = input.productionCapacity.ifBlank { "Standard rural micro enterprise capacity" }

        return """
You are an expert Rural Economic, Priority Sector Lending & Micro-Enterprise Feasibility Advisor in India.
Analyze the following proposed rural business venture with high realism, hyper-local precision, and practical actionable guidance for a rural entrepreneur.

Venture & Financial Parameters:
- Location: Village: $village, Block: $block, District: $district, State: $state, India
- Statutory Enterprise Scale: $scaleStr (${input.scale.name})
- Operational / Production Capacity: $capacityStr
- Promoter Margin Capital (10%): ₹${String.format(Locale.US, "%.2f", input.marginCapital)}
- Total Feasible Project Cost (10x Margin): ₹${String.format(Locale.US, "%.2f", projectCost)}
- Concessional Debt Component (up to 90%): ₹${String.format(Locale.US, "%.2f", loanAmount)}
- Promoter Total Equity: ₹${String.format(Locale.US, "%.2f", promoterEquity)}
- Recommended Scheme: $schemeName (${String.format(Locale.US, "%.1f", annualInterestRate * 100)}% interest, $tenureYears years tenure, $moratoriumMonths months moratorium)
- Proposed Business Category / Trade: $categoryName
- Entrepreneur Growth Ambition: ${input.growthObjective.displayName(input.language)} (${input.growthObjective.name})
- Business Concept & Operational Details: $details
- Target Output Language: $targetLang (You MUST respond strictly in $targetLang)

Respond ONLY with a VALID JSON object (no markdown fencing, no code blocks, no backticks, pure JSON) adhering to this schema:
{
  "executiveSummary": "2-sentence high-impact viability assessment and rural market potential in this location",
  "marketReach": "Realistic customer catchment radius (5-15 km), target local buyers (villages, haat bazaars, block markets), and distribution channels",
  "opportunityAnalysis": "Specific underserved market opportunities, supply gaps, and local competitive edges for this specific venture",
  "swotAnalysis": {
    "strengths": ["Strength 1", "Strength 2", "Strength 3"],
    "weaknesses": ["Weakness 1", "Weakness 2"],
    "opportunities": ["Opportunity 1", "Opportunity 2"],
    "threats": ["Threat 1", "Threat 2"]
  },
  "threatsIdentification": [
    "Threat 1 with concrete mitigation tailored to rural constraints",
    "Threat 2 with concrete mitigation tailored to rural constraints",
    "Threat 3 with concrete mitigation tailored to rural constraints"
  ],
  "competitorMapping": "Local competition assessment at village and block level, and how to build a competitive customer moat",
  "pricingStrategy": "Clear unit pricing formula, expected gross margins (e.g. 20-35%), and village credit/cash policy",
  "keyRecommendations": [
    "Immediate step to launch with the specified margin",
    "Equipment procurement & local sourcing guidance",
    "Working capital & cashflow management tip"
  ],
  "financialViability": {
    "expectedMonthlyRevenue": "₹...",
    "expectedMonthlyNetProfit": "₹...",
    "breakevenTimeline": "... months",
    "roiPercentage": "...%"
  }
}
""".trimIndent()
    }

    /**
     * Executes asynchronous REST request to Gemini API and parses the structured response.
     * Returns null upon failure, timeout, or invalid API key to trigger immediate fallback.
     */
    suspend fun generateAdvisory(
        input: AdvisoryInput,
        projectCost: Double,
        loanAmount: Double,
        schemeName: String,
        promoterEquity: Double,
        annualInterestRate: Double,
        tenureYears: Int,
        moratoriumMonths: Int,
        apiKey: String
    ): FeasibilityReport? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext null

        try {
            val prompt = buildPrebuiltPrompt(
                input = input,
                projectCost = projectCost,
                loanAmount = loanAmount,
                schemeName = schemeName,
                promoterEquity = promoterEquity,
                annualInterestRate = annualInterestRate,
                tenureYears = tenureYears,
                moratoriumMonths = moratoriumMonths
            )

            val url = URL("$modelEndpoint?key=$apiKey")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 12000
                readTimeout = 20000
                doOutput = true
                doInput = true
            }

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val generationConfig = JSONObject().apply {
                    put("response_mime_type", "application/json")
                    put("temperature", 0.3)
                    put("maxOutputTokens", 2048)
                }
                put("generationConfig", generationConfig)
            }

            OutputStreamWriter(connection.outputStream, "UTF-8").use { writer ->
                writer.write(requestJson.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                return@withContext null
            }

            val responseBody = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8")).use { reader ->
                reader.readText()
            }

            val candidateText = extractCandidateText(responseBody) ?: return@withContext null
            parseGeminiJsonResponse(candidateText, input)
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Extracts text from Gemini's generateContent response JSON.
     */
    fun extractCandidateText(responseJsonString: String): String? {
        return try {
            val root = JSONObject(responseJsonString)
            val candidates = root.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            if (parts.length() == 0) return null
            val part = parts.getJSONObject(0)
            if (part.has("text")) part.getString("text") else null
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Parses structured AI JSON content into a [FeasibilityReport].
     */
    fun parseGeminiJsonResponse(
        jsonString: String,
        input: AdvisoryInput
    ): FeasibilityReport? {
        return try {
            // Strip any markdown code fences if present (e.g. ```json ... ```)
            val cleanJson = jsonString
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(cleanJson)

            val executiveSummary = json.optString("executiveSummary", "")
            val marketReach = json.optString("marketReach", "Local rural market catchment within 5-10 km.")
            val opportunity = json.optString("opportunityAnalysis", "High demand for rural local services.")
            val competitor = json.optString("competitorMapping", "Low direct organized competition in the block.")
            val pricing = json.optString("pricingStrategy", "Affordable local pricing with cash and short credit terms.")

            val swotObj = json.optJSONObject("swotAnalysis")
            val strengths = toStringList(swotObj?.optJSONArray("strengths"))
            val weaknesses = toStringList(swotObj?.optJSONArray("weaknesses"))
            val opportunities = toStringList(swotObj?.optJSONArray("opportunities"))
            val threats = toStringList(swotObj?.optJSONArray("threats"))

            val swotAnalysis = SwotAnalysis(
                strengths = if (strengths.isNotEmpty()) strengths else listOf("Strong local demand"),
                weaknesses = if (weaknesses.isNotEmpty()) weaknesses else listOf("Limited initial capital"),
                opportunities = if (opportunities.isNotEmpty()) opportunities else listOf("Government priority sector support"),
                threats = if (threats.isNotEmpty()) threats else listOf("Seasonal variations")
            )

            val threatsList = toStringList(json.optJSONArray("threatsIdentification"))
            val recommendationsList = toStringList(json.optJSONArray("keyRecommendations"))

            val finObj = json.optJSONObject("financialViability")
            val viability = if (finObj != null) {
                FinancialViabilityEstimate(
                    expectedMonthlyRevenue = finObj.optString("expectedMonthlyRevenue", "N/A"),
                    expectedMonthlyNetProfit = finObj.optString("expectedMonthlyNetProfit", "N/A"),
                    breakevenTimeline = finObj.optString("breakevenTimeline", "N/A"),
                    roiPercentage = finObj.optString("roiPercentage", "N/A")
                )
            } else null

            val mappedCategory = if (input.customCategory.isNotBlank()) {
                BusinessCategory.fromCustomText(input.customCategory)
            } else {
                input.category
            }

            FeasibilityReport(
                category = mappedCategory,
                location = input.location.sanitized(input.language),
                budgetTier = input.budgetTier.name,
                marketReach = marketReach,
                opportunityAnalysis = opportunity,
                swotAnalysis = swotAnalysis,
                threatsIdentification = if (threatsList.isNotEmpty()) threatsList else listOf("Risk of supply fluctuation: Build local buffer"),
                competitorMapping = competitor,
                pricingStrategy = pricing,
                keyRecommendations = if (recommendationsList.isNotEmpty()) recommendationsList else listOf("Procure quality equipment", "Maintain transparent customer books"),
                isOfflineGenerated = false,
                customCategory = input.customCategory,
                businessDetails = input.businessDetails,
                executiveSummary = executiveSummary,
                financialViability = viability,
                isAiGenerated = true,
                aiModelName = "Gemini 1.5 Flash"
            )
        } catch (_: Throwable) {
            null
        }
    }

    private fun toStringList(array: JSONArray?): List<String> {
        if (array == null) return emptyList()
        val list = ArrayList<String>()
        for (i in 0 until array.length()) {
            val item = array.optString(i)
            if (item.isNotBlank()) {
                list.add(item)
            }
        }
        return list
    }
}
