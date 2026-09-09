package com.ruraladvisory.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruraladvisory.advisory.engine.FeasibilityAdvisoryEngine
import com.ruraladvisory.advisory.engine.GeminiAdvisoryService
import com.ruraladvisory.advisory.engine.GeminiConfig
import com.ruraladvisory.advisory.engine.OfflineHeuristicAdvisoryEngine
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.AdvisoryInput
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.engine.DefaultFinancialCalculatorEngine
import com.ruraladvisory.finance.engine.FinancialCalculatorEngine
import com.ruraladvisory.advisory.model.BusinessScale
import com.ruraladvisory.advisory.model.GrowthObjective
import com.ruraladvisory.advisory.model.UserProfile
import com.ruraladvisory.finance.engine.BusinessHealthEngine
import com.ruraladvisory.finance.model.BusinessHealthReport
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.finance.model.FinancialStructure
import com.ruraladvisory.advisory.engine.RuralAdvisorChatEngine
import com.ruraladvisory.advisory.model.ChatMessage
import com.ruraladvisory.advisory.model.ChatSender
import com.ruraladvisory.finance.model.SchemeCategory
import com.ruraladvisory.network.AbhyudayApiClient
import com.ruraladvisory.ui.navigation.AppTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Immutable UI State for the Rural Micro-Advisory application.
 */
data class AdvisoryUiState(
    val activeTab: AppTab = AppTab.SETUP,
    val village: String = "",
    val block: String = "",
    val district: String = "",
    val state: String = "",
    val marginCapital: Double = 0.0,
    val marginInputText: String = "",
    val selectedCategory: BusinessCategory = BusinessCategory.DAIRY,
    val customCategory: String = "",
    val businessSubtype: String = "",
    val promoterCategory: String = "",
    val experienceYears: Int = 0,
    val growthObjective: GrowthObjective = GrowthObjective.NEW_VENTURE,
    val scale: BusinessScale = BusinessScale.MICRO,
    val productionCapacity: String = "",
    val businessDetails: String = "",
    val monthlyRevenue: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val revenueDropPercent: Int = 0,
    val costInflationPercent: Int = 0,
    val isUserDetailsVisible: Boolean = false,
    val beneficiaryEmail: String = "",
    val beneficiaryGender: String = "Male",
    val beneficiaryAge: Int = 0,
    val beneficiarySpecialCategory: String = "None",
    val beneficiaryEducation: String = "",
    val beneficiaryHasEdpTraining: Boolean = false,
    val beneficiaryPincode: String = "",
    val beneficiaryAreaType: String = "Rural",
    val activeProfile: UserProfile = UserProfile(
        id = "custom_user",
        name = "",
        phoneNumber = "",
        village = "",
        block = "",
        district = "",
        state = "",
        pincode = "",
        category = BusinessCategory.DAIRY,
        scale = BusinessScale.MICRO,
        marginCapital = 0.0
    ),
    val isBackendOnline: Boolean = false,
    val isProfileSyncing: Boolean = false,
    val profileSyncSuccess: Boolean? = null,
    val businessHealthReport: BusinessHealthReport? = null,
    val apiKey: String = GeminiConfig.GEMINI_API_KEY,
    val isAiLoading: Boolean = false,
    val aiErrorMessage: String? = null,
    val language: LanguageCode = LanguageCode.EN,
    val financialResult: FinancialCalculationResult? = null,
    val feasibilityReport: FeasibilityReport? = null,
    val errorMessage: String? = null,
    val isAmortizationExpanded: Boolean = false,
    val isAudioPlaying: Boolean = false,
    val shareText: String = "",
    val ttsScript: String = "",
    val chatMessages: List<ChatMessage> = emptyList(),
    val isChatSending: Boolean = false
) {
    val successfulFinance: FinancialCalculationResult.Success?
        get() = financialResult as? FinancialCalculationResult.Success
}

/**
 * Core ViewModel bridging financial computation, heuristic feasibility,
 * and real-time Gemini AI engines to the Jetpack Compose presentation layer.
 */
class AdvisoryViewModel(
    private val financialEngine: FinancialCalculatorEngine = DefaultFinancialCalculatorEngine(),
    private val advisoryEngine: FeasibilityAdvisoryEngine = OfflineHeuristicAdvisoryEngine(),
    private val geminiService: GeminiAdvisoryService = GeminiAdvisoryService(),
    private val apiClient: AbhyudayApiClient = AbhyudayApiClient.INSTANCE
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdvisoryUiState())
    val uiState: StateFlow<AdvisoryUiState> = _uiState.asStateFlow()

    init {
        // Automatically calculate baseline advisory for immediate out-of-the-box guidance
        recalculate()
        resetOrInitChat()
        checkBackendHealth()
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun openUserDetails() {
        _uiState.update { it.copy(isUserDetailsVisible = true) }
    }

    fun closeUserDetails() {
        _uiState.update { it.copy(isUserDetailsVisible = false) }
    }

    fun updateUserDetails(
        name: String,
        phone: String,
        email: String,
        gender: String,
        age: Int,
        socialCategory: String,
        specialCategory: String,
        education: String,
        hasEdp: Boolean,
        village: String,
        block: String,
        district: String,
        state: String,
        pincode: String,
        areaType: String
    ) {
        _uiState.update { current ->
            val updatedProfile = current.activeProfile.copy(
                name = name,
                phoneNumber = phone,
                email = email,
                gender = gender,
                age = age,
                promoterCategory = socialCategory,
                specialCategory = specialCategory,
                educationQualification = education,
                hasEdpTraining = hasEdp,
                village = village,
                block = block,
                district = district,
                state = state,
                pincode = pincode,
                areaType = areaType
            )
            current.copy(
                isUserDetailsVisible = false,
                village = village,
                block = block,
                district = district,
                state = state,
                promoterCategory = socialCategory,
                beneficiaryEmail = email,
                beneficiaryGender = gender,
                beneficiaryAge = age,
                beneficiarySpecialCategory = specialCategory,
                beneficiaryEducation = education,
                beneficiaryHasEdpTraining = hasEdp,
                beneficiaryPincode = pincode,
                beneficiaryAreaType = areaType,
                activeProfile = updatedProfile
            )
        }
        recalculate()
        syncProfileWithBackend()
    }

    fun onPromoterCategoryChanged(newCategory: String) {
        _uiState.update { it.copy(promoterCategory = newCategory) }
        recalculate()
    }

    fun onBusinessSubtypeChanged(newSubtype: String) {
        _uiState.update { it.copy(businessSubtype = newSubtype) }
        recalculate()
    }

    fun onExperienceYearsChanged(years: Int) {
        _uiState.update { it.copy(experienceYears = years) }
        recalculate()
    }

    fun onRevenueDropChanged(percent: Int) {
        _uiState.update { it.copy(revenueDropPercent = percent) }
    }

    fun onCostInflationChanged(percent: Int) {
        _uiState.update { it.copy(costInflationPercent = percent) }
    }

    fun onVillageChanged(newVillage: String) {
        _uiState.update { it.copy(village = newVillage) }
        recalculate()
    }

    fun onBlockChanged(newBlock: String) {
        _uiState.update { it.copy(block = newBlock) }
        recalculate()
    }

    fun onDistrictChanged(newDistrict: String) {
        _uiState.update { it.copy(district = newDistrict) }
        recalculate()
    }

    fun onStateChanged(newState: String) {
        _uiState.update { it.copy(state = newState) }
        recalculate()
    }

    fun onScaleSelected(newScale: BusinessScale) {
        _uiState.update { it.copy(scale = newScale) }
        recalculate()
    }

    fun onProductionCapacityChanged(newCapacity: String) {
        _uiState.update { it.copy(productionCapacity = newCapacity) }
        recalculate()
    }

    fun selectProfile(profile: UserProfile) {
        _uiState.update { current ->
            current.copy(
                activeProfile = profile,
                village = profile.village,
                block = profile.block,
                district = profile.district,
                state = profile.state,
                selectedCategory = profile.category,
                customCategory = MultilingualDictionary.getCategoryTitle(profile.category, current.language),
                businessSubtype = profile.businessSubtype.ifBlank { current.businessSubtype },
                promoterCategory = profile.promoterCategory,
                experienceYears = profile.experienceYears,
                scale = profile.scale,
                productionCapacity = profile.productionCapacity,
                marginCapital = profile.marginCapital,
                marginInputText = profile.marginCapital.toLong().toString(),
                monthlyRevenue = profile.monthlyRevenue,
                monthlyExpenses = profile.monthlyExpenses,
                growthObjective = profile.growthObjective,
                businessDetails = profile.businessDetails,
                beneficiaryEmail = profile.email,
                beneficiaryGender = profile.gender,
                beneficiaryAge = profile.age,
                beneficiarySpecialCategory = profile.specialCategory,
                beneficiaryEducation = profile.educationQualification,
                beneficiaryHasEdpTraining = profile.hasEdpTraining,
                beneficiaryPincode = profile.pincode,
                beneficiaryAreaType = profile.areaType
            )
        }
        recalculate()
        resetOrInitChat()
        syncProfileWithBackend()
    }

    fun syncProfileWithBackend() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProfileSyncing = true) }
            val isHealthy = apiClient.checkHealth()
            _uiState.update { it.copy(isBackendOnline = isHealthy) }
            if (isHealthy) {
                val synced = apiClient.syncProfile(_uiState.value.activeProfile)
                _uiState.update { it.copy(isProfileSyncing = false, profileSyncSuccess = synced) }
            } else {
                _uiState.update { it.copy(isProfileSyncing = false, profileSyncSuccess = null) }
            }
        }
    }

    fun checkBackendHealth() {
        viewModelScope.launch {
            val isHealthy = apiClient.checkHealth()
            _uiState.update { it.copy(isBackendOnline = isHealthy) }
        }
    }

    fun onMarginChanged(newMargin: Double) {
        val clampedMargin = newMargin.coerceIn(1000.0, 500000.0)
        _uiState.update {
            it.copy(
                marginCapital = clampedMargin,
                marginInputText = clampedMargin.toLong().toString()
            )
        }
        recalculate()
    }

    fun onMarginInputTextChanged(newText: String) {
        val filtered = newText.filter { it.isDigit() }
        val parsed = filtered.toDoubleOrNull()

        _uiState.update {
            it.copy(
                marginInputText = filtered,
                marginCapital = parsed ?: it.marginCapital
            )
        }

        if (parsed != null) {
            recalculate()
        } else if (filtered.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = if (it.language == LanguageCode.HI) {
                        "कृपया एक वैध मार्जिन राशि दर्ज करें।"
                    } else {
                        "Please enter a valid margin capital amount."
                    }
                )
            }
        }
    }

    fun onPresetMarginSelected(presetMargin: Double) {
        onMarginChanged(presetMargin)
    }

    fun onCategorySelected(category: BusinessCategory) {
        val title = MultilingualDictionary.getCategoryTitle(category, _uiState.value.language)
        _uiState.update {
            it.copy(
                selectedCategory = category,
                customCategory = title
            )
        }
        recalculate()
    }

    fun onCustomCategoryChanged(newCategory: String) {
        val mappedCategory = BusinessCategory.fromCustomText(newCategory)
        _uiState.update {
            it.copy(
                customCategory = newCategory,
                selectedCategory = mappedCategory
            )
        }
        recalculate()
    }

    fun onBusinessDetailsChanged(newDetails: String) {
        _uiState.update { it.copy(businessDetails = newDetails) }
        recalculate()
    }

    fun onAppendSpokenText(spokenText: String) {
        if (spokenText.isBlank()) return
        _uiState.update { current ->
            val updatedDetails = if (current.businessDetails.isBlank()) {
                spokenText.trim()
            } else {
                "${current.businessDetails.trim()} ${spokenText.trim()}"
            }
            current.copy(businessDetails = updatedDetails)
        }
        recalculate()
    }

    fun onApiKeyChanged(newKey: String) {
        _uiState.update { it.copy(apiKey = newKey.trim()) }
    }

    fun triggerGeminiAnalysis() {
        val state = _uiState.value
        val effectiveApiKey = if (state.apiKey.isNotBlank()) state.apiKey.trim() else GeminiConfig.GEMINI_API_KEY.trim()

        if (effectiveApiKey.isBlank()) {
            _uiState.update {
                it.copy(
                    aiErrorMessage = if (it.language == LanguageCode.HI) {
                        "Gemini API कुंजी सेट नहीं है। कृपया GeminiConfig.kt में अपनी API कुंजी जोड़ें।"
                    } else {
                        "Gemini API key is not set. Please add your key in GeminiConfig.kt to use AI features."
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAiLoading = true, aiErrorMessage = null) }

            val success = state.successfulFinance
            val struct = success?.structure
            val scheme = success?.scheme

            val projectCost = struct?.totalProjectCost ?: (state.marginCapital * 10.0)
            val loanAmount = struct?.actualLoanAmount ?: (state.marginCapital * 9.0)
            val schemeName = if (scheme == SchemeCategory.MICRO_FINANCE) "Micro Finance Scheme" else "Term Loan Scheme"
            val promoterEquity = struct?.effectivePromoterEquity ?: state.marginCapital
            val rate = scheme?.annualInterestRate ?: 0.065
            val tenure = scheme?.tenureYears ?: 3
            val moratorium = scheme?.moratoriumMonths ?: 3

            val location = LocationInfo(state.village, state.block, state.district, state.state)
            val input = AdvisoryInput(
                location = location,
                marginCapital = state.marginCapital,
                category = state.selectedCategory,
                customCategory = state.customCategory,
                businessDetails = state.businessDetails,
                growthObjective = state.growthObjective,
                scale = state.scale,
                productionCapacity = state.productionCapacity,
                language = state.language
            )

            val aiReport = geminiService.generateAdvisory(
                input = input,
                projectCost = projectCost,
                loanAmount = loanAmount,
                schemeName = schemeName,
                promoterEquity = promoterEquity,
                annualInterestRate = rate,
                tenureYears = tenure,
                moratoriumMonths = moratorium,
                apiKey = effectiveApiKey
            )

            if (aiReport != null) {
                val ttsScript = TextToSpeechHelper.buildSpokenScript(aiReport, struct, scheme, state.language)
                val shareText = if (state.financialResult != null) {
                    buildFormattedShareText(aiReport, state.financialResult, state.language)
                } else ""

                _uiState.update {
                    it.copy(
                        feasibilityReport = aiReport,
                        isAiLoading = false,
                        ttsScript = ttsScript,
                        shareText = shareText
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isAiLoading = false,
                        aiErrorMessage = if (it.language == LanguageCode.HI) {
                            "Gemini AI से कनेक्ट नहीं हो सका। ऑफलाइन सलाह सक्रिय है।"
                        } else {
                            "Unable to connect to Gemini AI. Reverted to offline heuristics."
                        }
                    )
                }
            }
        }
    }

    fun onLanguageToggled() {
        val currentLang = _uiState.value.language
        val nextLang = if (currentLang == LanguageCode.EN) LanguageCode.HI else LanguageCode.EN
        setLanguage(nextLang)
    }

    fun setLanguage(language: LanguageCode) {
        _uiState.update { it.copy(language = language) }
        recalculate()
        resetOrInitChat()
    }

    fun resetOrInitChat() {
        val state = _uiState.value
        val schemeName = state.successfulFinance?.scheme?.let {
            if (it == SchemeCategory.MICRO_FINANCE) "Micro Finance Scheme" else "Term Loan Scheme"
        } ?: "SCA Concessional Scheme"
        val loanAmount = state.successfulFinance?.structure?.actualLoanAmount ?: (state.marginCapital * 9.0)
        val locList = listOf(state.village, state.district).filter { it.isNotBlank() }
        val loc = if (locList.isNotEmpty()) locList.joinToString(", ") else if (state.language == LanguageCode.HI) "ग्रामीण क्षेत्र" else "Rural Enterprise"
        val categoryName = state.customCategory.ifBlank {
            MultilingualDictionary.getCategoryTitle(state.selectedCategory, state.language)
        }
        val welcome = RuralAdvisorChatEngine.generateWelcomeMessage(
            businessCategory = categoryName,
            location = loc,
            schemeName = schemeName,
            loanAmount = loanAmount,
            language = state.language
        )
        val welcomeMsg = ChatMessage(
            id = "welcome-1",
            sender = ChatSender.ASSISTANT,
            text = welcome,
            timestamp = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
        )
        _uiState.update { it.copy(chatMessages = listOf(welcomeMsg)) }
    }

    fun sendChatMessage(query: String) {
        val clean = query.trim()
        if (clean.isBlank()) return
        val time = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
        val userMsg = ChatMessage(
            id = "user-${System.currentTimeMillis()}",
            sender = ChatSender.USER,
            text = clean,
            timestamp = time
        )

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isChatSending = true
            )
        }

        viewModelScope.launch {
            val state = _uiState.value
            val schemeName = state.successfulFinance?.scheme?.let {
                if (it == SchemeCategory.MICRO_FINANCE) "Micro Finance Scheme" else "Term Loan Scheme"
            } ?: "SCA Concessional Scheme"
            val projectCost = state.successfulFinance?.structure?.totalProjectCost ?: (state.marginCapital * 10.0)
            val moratorium = state.successfulFinance?.scheme?.moratoriumMonths ?: 3

            val replyText = RuralAdvisorChatEngine.generateFallbackReply(
                message = clean,
                category = state.customCategory,
                location = "${state.village}, ${state.district}, ${state.state}",
                marginCapital = state.marginCapital,
                projectCost = projectCost,
                schemeName = schemeName,
                moratoriumMonths = moratorium,
                language = state.language
            )

            val botMsg = ChatMessage(
                id = "assistant-${System.currentTimeMillis()}",
                sender = ChatSender.ASSISTANT,
                text = replyText,
                timestamp = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
            )

            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + botMsg,
                    isChatSending = false
                )
            }
        }
    }

    fun toggleAmortizationExpanded() {
        _uiState.update { it.copy(isAmortizationExpanded = !it.isAmortizationExpanded) }
    }

    fun setAudioPlaying(isPlaying: Boolean) {
        _uiState.update { it.copy(isAudioPlaying = isPlaying) }
    }

    fun onGrowthObjectiveSelected(objective: GrowthObjective) {
        _uiState.update { it.copy(growthObjective = objective) }
        recalculate()
    }

    fun onMonthlyRevenueChanged(revenue: Double) {
        _uiState.update { it.copy(monthlyRevenue = revenue) }
        recalculate()
    }

    fun onMonthlyExpensesChanged(expenses: Double) {
        _uiState.update { it.copy(monthlyExpenses = expenses) }
        recalculate()
    }

    /**
     * Executes both the financial calculation and advisory synthesis synchronously.
     */
    fun recalculate() {
        val state = _uiState.value
        val margin = state.marginCapital
        val lang = state.language

        // 1. Execute Financial Engine
        val financeRes = financialEngine.calculate(margin)

        // 2. Execute Feasibility Engine
        val location = LocationInfo(
            village = state.village,
            block = state.block,
            district = state.district,
            state = state.state
        )
        val advisoryInput = AdvisoryInput(
            location = location,
            marginCapital = margin,
            category = state.selectedCategory,
            customCategory = state.customCategory,
            businessDetails = state.businessDetails,
            growthObjective = state.growthObjective,
            scale = state.scale,
            productionCapacity = state.productionCapacity,
            language = lang
        )
        val report = advisoryEngine.generateReport(advisoryInput)

        // 3. Determine Error Banner (if any)
        val errorMsg = if (margin <= 0.0) {
            null
        } else when (financeRes) {
            is FinancialCalculationResult.BelowMinimumThreshold -> {
                if (lang == LanguageCode.HI) {
                    "न्यूनतम मार्जिन पूंजी ₹1,000 (परियोजना लागत ₹10,000) होना अनिवार्य है।"
                } else {
                    "Minimum margin capital is ₹1,000 (project cost ₹10,000) to qualify for concessional credit."
                }
            }
            is FinancialCalculationResult.ExceedsMaximumThreshold -> {
                if (lang == LanguageCode.HI) {
                    "मार्जिन पूंजी ₹5,00,000 की अधिकतम सीमा (₹50.00 लाख परियोजना लागत) से अधिक है।"
                } else {
                    "Margin capital exceeds ₹5,00,000 limit (₹50.00 Lakh project cost ceiling)."
                }
            }
            is FinancialCalculationResult.InvalidInput -> financeRes.message
            is FinancialCalculationResult.Success -> null
        }

        // 4. Generate TTS and Share texts
        val structure = (financeRes as? FinancialCalculationResult.Success)?.structure
        val scheme = (financeRes as? FinancialCalculationResult.Success)?.scheme
        val ttsScript = TextToSpeechHelper.buildSpokenScript(report, structure, scheme, lang)
        val shareText = buildFormattedShareText(report, financeRes, lang)

        // 5. Compute Business Intelligence & Cash Flow Diagnostic (Section 8)
        val projectCost = structure?.totalProjectCost ?: (margin * 10.0)
        val quarterlyPayment = (financeRes as? FinancialCalculationResult.Success)
            ?.schedule?.quarters?.firstOrNull { it.isMoratorium.not() }?.totalQuarterlyOutflow
        val healthReport = BusinessHealthEngine.assess(
            inputRevenue = if (state.monthlyRevenue > 0) state.monthlyRevenue else null,
            inputOperatingExpenses = if (state.monthlyExpenses > 0) state.monthlyExpenses else null,
            projectCost = projectCost,
            quarterlyLoanPayment = quarterlyPayment
        )

        _uiState.update {
            it.copy(
                financialResult = financeRes,
                feasibilityReport = report,
                businessHealthReport = healthReport,
                errorMessage = errorMsg,
                shareText = shareText,
                ttsScript = ttsScript
            )
        }
    }

    /**
     * Builds a comprehensive formatted plain-text advisory summary for sharing
     * via WhatsApp, SMS, or export.
     */
    fun buildFormattedShareText(
        report: FeasibilityReport,
        financialResult: FinancialCalculationResult,
        language: LanguageCode
    ): String {
        val catTitle = if (report.customCategory.isNotBlank()) report.customCategory else MultilingualDictionary.getCategoryTitle(report.category, language)
        val loc = report.location.formatted(language)

        return buildString {
            if (language == LanguageCode.HI) {
                appendLine("🌾 *ग्रामीण सूक्ष्म-सलाहकार रिपोर्ट* 🌾")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("📍 *स्थान:* $loc")
                appendLine("📂 *व्यवसाय श्रेणी:* $catTitle")
                appendLine("🏷️ *बजट श्रेणी:* ${report.budgetTier}")
                if (report.isAiGenerated) {
                    appendLine("✨ *Gemini AI रियल-टाइम विश्लेषण सक्रिय*")
                }
                if (report.executiveSummary.isNotBlank()) {
                    appendLine("🎯 *AI त्वरित निष्कर्ष:* ${report.executiveSummary}")
                }
                report.financialViability?.let {
                    appendLine("📈 *AI वित्तीय अनुमान:* मासिक बिक्री: ${it.expectedMonthlyRevenue} | मुनाफा: ${it.expectedMonthlyNetProfit} | ब्रेक-इवन: ${it.breakevenTimeline} | ROI: ${it.roiPercentage}")
                }
                appendLine()

                if (financialResult is FinancialCalculationResult.Success) {
                    val struct = financialResult.structure
                    val scheme = financialResult.scheme
                    val schemeName = if (scheme == SchemeCategory.MICRO_FINANCE) "सूक्ष्म वित्त योजना" else "सावधि ऋण योजना"

                    appendLine("💰 *वित्तीय संरचना एवं योजना पात्रता:*")
                    appendLine("• योजना: $schemeName")
                    appendLine("• कुल परियोजना लागत: ₹${TextToSpeechHelper.formatIndianCurrency(struct.totalProjectCost)}")
                    appendLine("• पात्र रियायती ऋण: ₹${TextToSpeechHelper.formatIndianCurrency(struct.actualLoanAmount)}")
                    appendLine("• लाभार्थी मार्जिन (10%): ₹${TextToSpeechHelper.formatIndianCurrency(struct.marginCapital)}")
                    appendLine("• प्रवर्तक की कुल पूंजी: ₹${TextToSpeechHelper.formatIndianCurrency(struct.effectivePromoterEquity)}")
                    appendLine("• वार्षिक ब्याज दर: ${String.format(java.util.Locale.US, "%.1f", scheme.annualInterestRate * 100.0)}%")
                    appendLine("• ऋण अवधि: ${scheme.tenureYears} वर्ष (${scheme.tenureQuarters} तिमाही)")
                    appendLine("• मोराटोरियम (छूट अवधि): ${scheme.moratoriumMonths} माह")
                    if (struct.isCapped) {
                        appendLine("⚠️ *नोट:* वैधानिक अधिकतम ऋण सीमा लागू!")
                    }
                    appendLine()
                    appendLine("📊 *लागत एवं पूंजी आवंटन:*")
                    appendLine("• अचल संपत्ति एवं मशीनरी (CAPEX 70%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.fixedAssetsCapex)}")
                    appendLine("• कार्यशील पूंजी (25%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.workingCapital)}")
                    appendLine("• आकस्मिक आरक्षित निधि (5%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.contingencyBuffer)}")
                    appendLine("• अनुमानित मासिक परिचालन खर्च: ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.estimatedMonthlyOpex)}")
                    appendLine()
                }

                appendLine("🎯 *व्यवहार्यता विश्लेषण (6 आयाम):*")
                appendLine("1. बाजार पहुंच: ${report.marketReach}")
                appendLine("2. व्यापारिक अवसर: ${report.opportunityAnalysis}")
                appendLine("3. प्रतियोगी विश्लेषण: ${report.competitorMapping}")
                appendLine("4. उत्पाद मूल्य एवं रणनीति: ${report.pricingStrategy}")
                appendLine()

                appendLine("🛡️ *स्वॉट (SWOT) विश्लेषण:*")
                appendLine("• ताकत:")
                report.swotAnalysis.strengths.forEach { appendLine("   + $it") }
                appendLine("• कमजोरियां:")
                report.swotAnalysis.weaknesses.forEach { appendLine("   - $it") }
                appendLine("• अवसर:")
                report.swotAnalysis.opportunities.forEach { appendLine("   * $it") }
                appendLine("• जोखिम:")
                report.swotAnalysis.threats.forEach { appendLine("   ! $it") }
                appendLine()

                appendLine("⚠️ *जोखिम पहचान एवं सुरक्षा के उपाय:*")
                report.threatsIdentification.forEach { appendLine("• $it") }
                appendLine()

                appendLine("💡 *प्रमुख रणनीतिक सुझाव:*")
                report.keyRecommendations.forEach { appendLine("• $it") }
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("📲 ग्रामीण सूक्ष्म-सलाहकार ऐप द्वारा जनरेट किया गया।")
            } else {
                appendLine("🌾 *RURAL MICRO-ADVISORY REPORT* 🌾")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("📍 *Location:* $loc")
                appendLine("📂 *Trade Category:* $catTitle")
                appendLine("🏷️ *Budget Tier:* ${report.budgetTier}")
                if (report.isAiGenerated) {
                    appendLine("✨ *Gemini AI Real-time Analysis Enabled*")
                }
                if (report.executiveSummary.isNotBlank()) {
                    appendLine("🎯 *AI Executive Verdict:* ${report.executiveSummary}")
                }
                report.financialViability?.let {
                    appendLine("📈 *AI Financial Projections:* Revenue: ${it.expectedMonthlyRevenue} | Profit: ${it.expectedMonthlyNetProfit} | Breakeven: ${it.breakevenTimeline} | ROI: ${it.roiPercentage}")
                }
                appendLine()

                if (financialResult is FinancialCalculationResult.Success) {
                    val struct = financialResult.structure
                    val scheme = financialResult.scheme
                    val schemeName = if (scheme == SchemeCategory.MICRO_FINANCE) "Micro Finance Scheme" else "Term Loan Scheme"

                    appendLine("💰 *Financial Structuring & Scheme Qualification:*")
                    appendLine("• Scheme: $schemeName")
                    appendLine("• Total Project Cost: ₹${TextToSpeechHelper.formatIndianCurrency(struct.totalProjectCost)}")
                    appendLine("• Eligible Concessional Loan: ₹${TextToSpeechHelper.formatIndianCurrency(struct.actualLoanAmount)}")
                    appendLine("• Beneficiary Margin (10%): ₹${TextToSpeechHelper.formatIndianCurrency(struct.marginCapital)}")
                    appendLine("• Promoter Total Equity: ₹${TextToSpeechHelper.formatIndianCurrency(struct.effectivePromoterEquity)}")
                    appendLine("• Annual Interest Rate: ${String.format(java.util.Locale.US, "%.1f", scheme.annualInterestRate * 100.0)}%")
                    appendLine("• Repayment Tenure: ${scheme.tenureYears} Years (${scheme.tenureQuarters} Quarters)")
                    appendLine("• Moratorium Grace Period: ${scheme.moratoriumMonths} Months")
                    if (struct.isCapped) {
                        appendLine("⚠️ *Notice:* Loan capped at statutory scheme maximum limit!")
                    }
                    appendLine()
                    appendLine("📊 *Cost & Working Capital Breakdown:*")
                    appendLine("• Fixed Assets & Machinery (CAPEX 70%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.fixedAssetsCapex)}")
                    appendLine("• Working Capital (25%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.workingCapital)}")
                    appendLine("• Contingency Buffer (5%): ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.contingencyBuffer)}")
                    appendLine("• Estimated Monthly OPEX: ₹${TextToSpeechHelper.formatIndianCurrency(financialResult.costBreakdown.estimatedMonthlyOpex)}")
                    appendLine()
                }

                appendLine("🎯 *Feasibility Dimensions:*")
                appendLine("1. Market Reach: ${report.marketReach}")
                appendLine("2. Opportunity: ${report.opportunityAnalysis}")
                appendLine("3. Competitor Mapping: ${report.competitorMapping}")
                appendLine("4. Pricing Strategy: ${report.pricingStrategy}")
                appendLine()

                appendLine("🛡️ *Budget-Calibrated SWOT Analysis:*")
                appendLine("• Strengths:")
                report.swotAnalysis.strengths.forEach { appendLine("   + $it") }
                appendLine("• Weaknesses:")
                report.swotAnalysis.weaknesses.forEach { appendLine("   - $it") }
                appendLine("• Opportunities:")
                report.swotAnalysis.opportunities.forEach { appendLine("   * $it") }
                appendLine("• Threats:")
                report.swotAnalysis.threats.forEach { appendLine("   ! $it") }
                appendLine()

                appendLine("⚠️ *Threat Vectors & Mitigation:*")
                report.threatsIdentification.forEach { appendLine("• $it") }
                appendLine()

                appendLine("💡 *Key Strategic Recommendations:*")
                report.keyRecommendations.forEach { appendLine("• $it") }
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("📲 Generated offline by Rural Micro-Advisory App.")
            }
        }
    }
}
