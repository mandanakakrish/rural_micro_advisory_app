package com.ruraladvisory.network

import com.ruraladvisory.advisory.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Lightweight HTTP client connecting the ABHYUDAY Android app to the FastAPI backend.
 * Provides real-time synchronization, financial structuring, and graceful offline fallback.
 * Corresponds to Phase 9 Full-Stack Integration.
 */
class AbhyudayApiClient(
    var baseUrl: String = DEFAULT_BASE_URL
) {
    companion object {
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8000"
        val INSTANCE: AbhyudayApiClient by lazy { AbhyudayApiClient() }
    }

    /**
     * Checks if the ABHYUDAY FastAPI backend is reachable.
     */
    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/health")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 2000
                readTimeout = 2000
            }
            conn.responseCode == 200
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Synchronizes entrepreneur profile with the FastAPI backend.
     */
    suspend fun syncProfile(profile: UserProfile): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/api/profile")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
                connectTimeout = 2500
                readTimeout = 2500
            }

            val json = JSONObject().apply {
                put("user_id", profile.id)
                put("business_name", "${profile.name}'s Enterprise")
                put("category", profile.category.name)
                put("village", profile.village.ifBlank { "Local Village" })
                put("block", profile.block.ifBlank { "Local Block" })
                put("district", profile.district.ifBlank { "Local District" })
                put("state", profile.state)
                put("margin_capital", profile.marginCapital)
                put("growth_objective", profile.growthObjective.name)
                put("business_scale", profile.scale.name)
                put("monthly_capacity", profile.productionCapacity)
                put("estimated_monthly_revenue", profile.monthlyRevenue)
                put("estimated_monthly_expenses", profile.monthlyExpenses)
            }

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(json.toString())
                writer.flush()
            }

            conn.responseCode in 200..299
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Fetches smart financial structure from the backend, or null if offline.
     */
    suspend fun fetchFinancialStructure(marginCapital: Double): BackendFinanceResult? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/api/finance/structure")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
                connectTimeout = 2500
                readTimeout = 2500
            }

            val json = JSONObject().apply {
                put("margin_capital", marginCapital)
            }

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(json.toString())
                writer.flush()
            }

            if (conn.responseCode in 200..299) {
                val responseText = BufferedReader(InputStreamReader(conn.inputStream)).use { it.readText() }
                val resp = JSONObject(responseText)
                BackendFinanceResult(
                    schemeName = resp.optString("scheme_name", ""),
                    totalProjectCost = resp.optDouble("total_project_cost", 0.0),
                    actualLoanAmount = resp.optDouble("actual_loan_amount", 0.0),
                    promoterEquity = resp.optDouble("promoter_equity", 0.0),
                    annualInterestRate = resp.optDouble("annual_interest_rate", 0.0),
                    tenureYears = resp.optInt("tenure_years", 0),
                    moratoriumMonths = resp.optInt("moratorium_months", 0),
                    isCapped = resp.optBoolean("is_capped", false)
                )
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

data class BackendFinanceResult(
    val schemeName: String,
    val totalProjectCost: Double,
    val actualLoanAmount: Double,
    val promoterEquity: Double,
    val annualInterestRate: Double,
    val tenureYears: Int,
    val moratoriumMonths: Int,
    val isCapped: Boolean
)
