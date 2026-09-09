package com.ruraladvisory.ui

import com.ruraladvisory.advisory.engine.GeminiConfig
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.BusinessScale
import com.ruraladvisory.advisory.model.GrowthObjective
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.UserProfile
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.SchemeCategory
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [AdvisoryViewModel] and UI Presentation state management.
 */
class AdvisoryViewModelTest {

    private lateinit var viewModel: AdvisoryViewModel

    @Before
    fun setUp() {
        viewModel = AdvisoryViewModel()
    }

    @Test
    fun initialState_hasCleanUnfilledFieldsForUserEntry() {
        val state = viewModel.uiState.value

        assertEquals("", state.village)
        assertEquals("", state.block)
        assertEquals("", state.district)
        assertEquals("", state.state)
        assertEquals("", state.activeProfile.name)
        assertEquals("", state.activeProfile.phoneNumber)
        assertEquals(0.0, state.marginCapital, 0.001)
        assertEquals("", state.marginInputText)
        assertEquals(BusinessCategory.DAIRY, state.selectedCategory)
        assertEquals(LanguageCode.EN, state.language)
        assertFalse(state.isAmortizationExpanded)
        assertFalse(state.isAudioPlaying)
        assertNull(state.errorMessage)
    }

    @Test
    fun selectProfile_populatesAllFieldsCorrectly() {
        viewModel.selectProfile(UserProfile.USER_A)
        val state = viewModel.uiState.value

        assertEquals("Nar", state.village)
        assertEquals("Petlad", state.block)
        assertEquals("Anand", state.district)
        assertEquals("Gujarat", state.state)
        assertEquals("Ramesh Patel", state.activeProfile.name)
        assertEquals("+91 98250 12345", state.activeProfile.phoneNumber)
        assertEquals(350000.0, state.marginCapital, 0.001)

        val success = state.successfulFinance
        assertNotNull(success)
        assertEquals(SchemeCategory.TERM_LOAN, success?.scheme)
    }

    @Test
    fun onMarginChanged_microFinanceBoundary_routesCorrectly() {
        // At margin 10,000 -> Project Cost 1,00,000 -> Micro Finance (uncapped)
        viewModel.onMarginChanged(10000.0)
        var state = viewModel.uiState.value
        var success = state.successfulFinance

        assertNotNull(success)
        assertEquals(SchemeCategory.MICRO_FINANCE, success?.scheme)
        assertEquals(100000.0, success?.structure?.totalProjectCost ?: 0.0, 0.01)
        assertEquals(90000.0, success?.structure?.actualLoanAmount ?: 0.0, 0.01)
        assertFalse(success?.structure?.isCapped ?: true)
        assertEquals(12, success?.schedule?.quarters?.size)

        // At margin 14,000 -> Project Cost 1,40,000 -> Micro Finance threshold (capped at 1.25L)
        viewModel.onMarginChanged(14000.0)
        state = viewModel.uiState.value
        success = state.successfulFinance

        assertNotNull(success)
        assertEquals(SchemeCategory.MICRO_FINANCE, success?.scheme)
        assertEquals(140000.0, success?.structure?.totalProjectCost ?: 0.0, 0.01)
        assertEquals(125000.0, success?.structure?.actualLoanAmount ?: 0.0, 0.01)
        assertTrue(success?.structure?.isCapped ?: false)
    }

    @Test
    fun onMarginChanged_termLoanBoundary_routesCorrectly() {
        // At margin 15,000 -> Project Cost 1,50,000 -> Term Loan Scheme (28 quarters)
        viewModel.onMarginChanged(15000.0)
        var state = viewModel.uiState.value
        var success = state.successfulFinance

        assertNotNull(success)
        assertEquals(SchemeCategory.TERM_LOAN, success?.scheme)
        assertEquals(150000.0, success?.structure?.totalProjectCost ?: 0.0, 0.01)
        assertEquals(135000.0, success?.structure?.actualLoanAmount ?: 0.0, 0.01)
        assertFalse(success?.structure?.isCapped ?: true)
        assertEquals(28, success?.schedule?.quarters?.size)

        // At margin 500,000 -> Project Cost 50,00,000 -> Term Loan maximum cap (45.00 Lakh)
        viewModel.onMarginChanged(500000.0)
        state = viewModel.uiState.value
        success = state.successfulFinance

        assertNotNull(success)
        assertEquals(SchemeCategory.TERM_LOAN, success?.scheme)
        assertEquals(5000000.0, success?.structure?.totalProjectCost ?: 0.0, 0.01)
        assertEquals(4500000.0, success?.structure?.actualLoanAmount ?: 0.0, 0.01)
        assertFalse(success?.structure?.isCapped ?: true)
    }

    @Test
    fun onMarginInputTextChanged_validDigits_updatesMarginAndRecalculates() {
        viewModel.onMarginInputTextChanged("25000")
        val state = viewModel.uiState.value

        assertEquals(25000.0, state.marginCapital, 0.001)
        assertEquals("25000", state.marginInputText)
        assertNull(state.errorMessage)

        val success = state.successfulFinance
        assertNotNull(success)
        assertEquals(SchemeCategory.TERM_LOAN, success?.scheme)
        assertEquals(250000.0, success?.structure?.totalProjectCost ?: 0.0, 0.01)
    }

    @Test
    fun onMarginInputTextChanged_emptyString_displaysValidationError() {
        viewModel.onMarginInputTextChanged("")
        val state = viewModel.uiState.value

        assertEquals("", state.marginInputText)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun onCategorySelected_allSevenCategories_generateValidFeasibilityReports() {
        val categories = BusinessCategory.values()
        assertEquals(7, categories.size)

        categories.forEach { category ->
            viewModel.onCategorySelected(category)
            val state = viewModel.uiState.value

            assertEquals(category, state.selectedCategory)
            val report = state.feasibilityReport
            assertNotNull(report)
            assertEquals(category, report?.category)
            assertTrue(report?.marketReach?.isNotBlank() == true)
            assertTrue(report?.opportunityAnalysis?.isNotBlank() == true)
            assertTrue(report?.competitorMapping?.isNotBlank() == true)
            assertTrue(report?.pricingStrategy?.isNotBlank() == true)
            assertTrue(report?.swotAnalysis?.strengths?.isNotEmpty() == true)
            assertTrue(report?.threatsIdentification?.isNotEmpty() == true)
            assertTrue(report?.keyRecommendations?.isNotEmpty() == true)
        }
    }

    @Test
    fun onLanguageToggled_switchesToHindiAndBackToEnglish() {
        // Initially English
        assertEquals(LanguageCode.EN, viewModel.uiState.value.language)
        assertTrue(viewModel.uiState.value.shareText.contains("RURAL MICRO-ADVISORY REPORT"))

        // Toggle to Hindi
        viewModel.onLanguageToggled()
        val hindiState = viewModel.uiState.value
        assertEquals(LanguageCode.HI, hindiState.language)
        assertTrue(hindiState.shareText.contains("ग्रामीण सूक्ष्म-सलाहकार रिपोर्ट"))
        assertTrue(hindiState.shareText.contains("स्थान"))
        assertTrue(hindiState.ttsScript.contains("सलाह"))

        // Toggle back to English
        viewModel.onLanguageToggled()
        val enState = viewModel.uiState.value
        assertEquals(LanguageCode.EN, enState.language)
        assertTrue(enState.shareText.contains("RURAL MICRO-ADVISORY REPORT"))
    }

    @Test
    fun onLocationChanged_updatesGeographicTargeting() {
        viewModel.onVillageChanged("Shivrajpur")
        viewModel.onBlockChanged("Saraswati")
        viewModel.onDistrictChanged("Prayagraj")

        val state = viewModel.uiState.value
        assertEquals("Shivrajpur", state.village)
        assertEquals("Saraswati", state.block)
        assertEquals("Prayagraj", state.district)

        val report = state.feasibilityReport
        assertNotNull(report)
        assertEquals("Shivrajpur, Saraswati, Prayagraj", report?.location?.formatted(LanguageCode.EN))
        assertTrue(state.shareText.contains("Shivrajpur, Saraswati, Prayagraj"))
    }

    @Test
    fun toggleAmortizationExpanded_togglesBooleanFlag() {
        assertFalse(viewModel.uiState.value.isAmortizationExpanded)

        viewModel.toggleAmortizationExpanded()
        assertTrue(viewModel.uiState.value.isAmortizationExpanded)

        viewModel.toggleAmortizationExpanded()
        assertFalse(viewModel.uiState.value.isAmortizationExpanded)
    }

    @Test
    fun setAudioPlaying_updatesState() {
        assertFalse(viewModel.uiState.value.isAudioPlaying)

        viewModel.setAudioPlaying(true)
        assertTrue(viewModel.uiState.value.isAudioPlaying)

        viewModel.setAudioPlaying(false)
        assertFalse(viewModel.uiState.value.isAudioPlaying)
    }

    @Test
    fun textToSpeechHelper_buildSpokenScript_generatesNaturalAudioInBothLanguages() {
        viewModel.onMarginChanged(14000.0)
        val state = viewModel.uiState.value
        val report = state.feasibilityReport!!
        val structure = state.successfulFinance?.structure
        val scheme = state.successfulFinance?.scheme

        val enScript = TextToSpeechHelper.buildSpokenScript(report, structure, scheme, LanguageCode.EN)
        assertTrue(enScript.contains("Advisory summary for"))
        assertTrue(enScript.contains("Under the Micro Finance Scheme"))
        assertTrue(enScript.contains("₹1,40,000"))
        assertTrue(enScript.contains("Thank you."))

        val hiScript = TextToSpeechHelper.buildSpokenScript(report, structure, scheme, LanguageCode.HI)
        assertTrue(hiScript.contains("के लिए सलाह"))
        assertTrue(hiScript.contains("सूक्ष्म वित्त योजना"))
        assertTrue(hiScript.contains("₹1,40,000"))
        assertTrue(hiScript.contains("धन्यवाद।"))
    }

    @Test
    fun buildFormattedShareText_containsAllCoreSections() {
        viewModel.onMarginChanged(14000.0)
        val state = viewModel.uiState.value
        val report = state.feasibilityReport!!
        val financeRes = state.financialResult!!

        val enShare = viewModel.buildFormattedShareText(report, financeRes, LanguageCode.EN)
        assertTrue(enShare.contains("RURAL MICRO-ADVISORY REPORT"))
        assertTrue(enShare.contains("Financial Structuring & Scheme Qualification:"))
        assertTrue(enShare.contains("Cost & Working Capital Breakdown:"))
        assertTrue(enShare.contains("Feasibility Dimensions:"))
        assertTrue(enShare.contains("Budget-Calibrated SWOT Analysis:"))
        assertTrue(enShare.contains("Threat Vectors & Mitigation:"))
        assertTrue(enShare.contains("Key Strategic Recommendations:"))

        val hiShare = viewModel.buildFormattedShareText(report, financeRes, LanguageCode.HI)
        assertTrue(hiShare.contains("ग्रामीण सूक्ष्म-सलाहकार रिपोर्ट"))
        assertTrue(hiShare.contains("वित्तीय संरचना एवं योजना पात्रता:"))
        assertTrue(hiShare.contains("लागत एवं पूंजी आवंटन:"))
        assertTrue(hiShare.contains("व्यवहार्यता विश्लेषण (6 आयाम):"))
        assertTrue(hiShare.contains("स्वॉट (SWOT) विश्लेषण:"))
        assertTrue(hiShare.contains("जोखिम पहचान एवं सुरक्षा के उपाय:"))
        assertTrue(hiShare.contains("प्रमुख रणनीतिक सुझाव:"))
    }

    @Test
    fun onCustomCategoryChanged_updatesCategoryAndSmartMapsEnum() {
        viewModel.onCustomCategoryChanged("Mustard Oil Expeller Mill")
        val state = viewModel.uiState.value

        assertEquals("Mustard Oil Expeller Mill", state.customCategory)
        assertEquals(BusinessCategory.FOOD_PROCESSING, state.selectedCategory)
        assertEquals("Mustard Oil Expeller Mill", state.feasibilityReport?.customCategory)
        assertTrue(state.shareText.contains("Mustard Oil Expeller Mill"))

        viewModel.onCustomCategoryChanged("Tailoring and Garments Boutique")
        assertEquals(BusinessCategory.TEXTILES, viewModel.uiState.value.selectedCategory)

        viewModel.onCustomCategoryChanged("Country Poultry Farm")
        assertEquals(BusinessCategory.POULTRY, viewModel.uiState.value.selectedCategory)
    }

    @Test
    fun onBusinessDetailsChanged_updatesStateAndReport() {
        val details = "Setup electric oil press to serve 200 farmer families in Manjhanpur"
        viewModel.onBusinessDetailsChanged(details)
        val state = viewModel.uiState.value

        assertEquals(details, state.businessDetails)
        assertEquals(details, state.feasibilityReport?.businessDetails)
    }

    @Test
    fun onAppendSpokenText_appendsSpokenVoiceTextCorrectly() {
        viewModel.onBusinessDetailsChanged("Need 5HP motor.")
        viewModel.onAppendSpokenText("Also will purchase seed filter machine.")

        val state = viewModel.uiState.value
        assertEquals("Need 5HP motor. Also will purchase seed filter machine.", state.businessDetails)
    }

    @Test
    fun onApiKeyChanged_updatesApiKeyInState() {
        viewModel.onApiKeyChanged("AIzaSyFakeTestGeminiKey123")
        assertEquals("AIzaSyFakeTestGeminiKey123", viewModel.uiState.value.apiKey)
    }

    @Test
    fun triggerGeminiAnalysis_withoutApiKey_setsErrorMessage() {
        val originalKey = GeminiConfig.GEMINI_API_KEY
        try {
            GeminiConfig.GEMINI_API_KEY = ""
            viewModel.onApiKeyChanged("")
            viewModel.triggerGeminiAnalysis()

            assertNotNull(viewModel.uiState.value.aiErrorMessage)
            assertTrue(viewModel.uiState.value.aiErrorMessage!!.contains("Gemini API key"))
        } finally {
            GeminiConfig.GEMINI_API_KEY = originalKey
        }
    }

    @Test
    fun onGrowthObjectiveSelected_updatesObjectiveAndRecalculates() {
        viewModel.onGrowthObjectiveSelected(GrowthObjective.MODERNIZATION)
        assertEquals(GrowthObjective.MODERNIZATION, viewModel.uiState.value.growthObjective)
    }

    @Test
    fun businessHealthReport_isGeneratedOnStartupAndUpdatesWithRevenue() {
        val initialHealth = viewModel.uiState.value.businessHealthReport
        assertNotNull("Business health report should be present on init", initialHealth)
        assertTrue(initialHealth!!.healthScore in 10..100)

        // Update custom revenue and expenses
        viewModel.onMonthlyRevenueChanged(80000.0)
        viewModel.onMonthlyExpensesChanged(40000.0)

        val updatedHealth = viewModel.uiState.value.businessHealthReport
        assertNotNull(updatedHealth)
        assertEquals(80000.0, updatedHealth!!.estimatedMonthlyRevenue, 0.01)
        assertEquals(40000.0, updatedHealth.estimatedMonthlyOperatingExpenses, 0.01)
        assertTrue(updatedHealth.netMonthlyProfit > 0)
    }

    @Test
    fun selectProfile_loadsUserAProfileAccurately() {
        viewModel.selectProfile(UserProfile.USER_A)
        val state = viewModel.uiState.value

        assertEquals("user_a_dairy", state.activeProfile.id)
        assertEquals("Nar", state.village)
        assertEquals("Petlad", state.block)
        assertEquals("Anand", state.district)
        assertEquals("Gujarat", state.state)
        assertEquals(BusinessCategory.DAIRY, state.selectedCategory)
        assertEquals(BusinessScale.SMALL, state.scale)
        assertEquals("30 Cattle (400 L/Day)", state.productionCapacity)
        assertEquals(350000.0, state.marginCapital, 0.01)
        assertEquals(180000.0, state.monthlyRevenue, 0.01)
        assertEquals(95000.0, state.monthlyExpenses, 0.01)
        assertEquals(GrowthObjective.EXPANSION, state.growthObjective)

        // Financial and health calculations updated
        val finance = state.successfulFinance
        assertNotNull(finance)
        assertEquals(SchemeCategory.TERM_LOAN, finance?.scheme)
        assertEquals(3500000.0, finance?.structure?.totalProjectCost ?: 0.0, 0.01)

        val health = state.businessHealthReport
        assertNotNull(health)
        assertEquals(180000.0, health!!.estimatedMonthlyRevenue, 0.01)
    }

    @Test
    fun selectProfile_loadsUserBProfileAccurately() {
        viewModel.selectProfile(UserProfile.USER_B)
        val state = viewModel.uiState.value

        assertEquals("user_b_handicrafts", state.activeProfile.id)
        assertEquals("Bagru", state.village)
        assertEquals("Jaipur", state.district)
        assertEquals("Rajasthan", state.state)
        assertEquals(BusinessCategory.HANDICRAFTS, state.selectedCategory)
        assertEquals(BusinessScale.MEDIUM, state.scale)
        assertEquals("1,200 Craft Units/Mo", state.productionCapacity)
        assertEquals(650000.0, state.marginCapital, 0.01)
        assertEquals(GrowthObjective.MARKET_REACH, state.growthObjective)
    }

    @Test
    fun selectProfile_loadsUserCProfileAccurately() {
        viewModel.selectProfile(UserProfile.USER_C)
        val state = viewModel.uiState.value

        assertEquals("user_c_food_processing", state.activeProfile.id)
        assertEquals("Kalyanpura", state.village)
        assertEquals("Ujjain", state.district)
        assertEquals("Madhya Pradesh", state.state)
        assertEquals(BusinessCategory.FOOD_PROCESSING, state.selectedCategory)
        assertEquals(BusinessScale.MICRO, state.scale)
        assertEquals("500 Pickle Bottles/Mo", state.productionCapacity)
        assertEquals(75000.0, state.marginCapital, 0.01)
        assertEquals(GrowthObjective.MODERNIZATION, state.growthObjective)

        // Micro finance scheme for ₹75k margin (project cost ₹7.5 Lakhs)
        val finance = state.successfulFinance
        assertNotNull(finance)
        assertEquals(750000.0, finance?.structure?.totalProjectCost ?: 0.0, 0.01)
    }

    @Test
    fun onStateChanged_updatesStateAndRecalculates() {
        viewModel.onStateChanged("Gujarat")
        assertEquals("Gujarat", viewModel.uiState.value.state)
    }

    @Test
    fun onScaleSelected_updatesScaleAndRecalculates() {
        viewModel.onScaleSelected(BusinessScale.SMALL)
        assertEquals(BusinessScale.SMALL, viewModel.uiState.value.scale)
    }

    @Test
    fun onProductionCapacityChanged_updatesCapacity() {
        viewModel.onProductionCapacityChanged("200 kg/day")
        assertEquals("200 kg/day", viewModel.uiState.value.productionCapacity)
    }
}
