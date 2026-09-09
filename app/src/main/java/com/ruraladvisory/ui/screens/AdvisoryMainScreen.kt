package com.ruraladvisory.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.finance.model.FinancialCalculationResult
import com.ruraladvisory.ui.components.ActionPlanRoadmapCard
import com.ruraladvisory.ui.components.AiAdvisoryStatusCard
import com.ruraladvisory.ui.components.AmortizationScheduleTable
import com.ruraladvisory.ui.components.AudioSummaryCard
import com.ruraladvisory.ui.components.BusinessHealthCard
import com.ruraladvisory.ui.components.DimensionCards
import com.ruraladvisory.ui.components.EligibleSchemesCard
import com.ruraladvisory.ui.components.EntrepreneurProfileCard
import com.ruraladvisory.ui.components.FinancialDashboardCard
import com.ruraladvisory.ui.components.InputFormCard
import com.ruraladvisory.ui.components.LanguageToggleButton
import com.ruraladvisory.ui.components.ShareActionBar
import com.ruraladvisory.ui.components.SwotMatrixGrid
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.WarmCreamBackground
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel
import kotlinx.coroutines.launch

/**
 * Main Advisory Screen uniting form inputs, financial calculation dashboard,
 * audio voice cues, SWOT matrix, feasibility cards, and share sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisoryMainScreen(
    viewModel: AdvisoryViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.onAppendSpokenText(spokenText)
            }
        }
    }

    val startVoiceInput: () -> Unit = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                if (uiState.language == LanguageCode.HI) "hi-IN" else "en-IN"
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.voice_input_prompt)
            )
        }
        try {
            speechRecognizerLauncher.launch(intent)
        } catch (_: ActivityNotFoundException) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.voice_error_not_available))
            }
        } catch (_: Throwable) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.voice_error_not_available))
            }
        }
    }

    LaunchedEffect(uiState.aiErrorMessage) {
        uiState.aiErrorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Android TextToSpeech Helper with lifecycle cleanup
    val ttsHelper = remember { TextToSpeechHelper(context) }

    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Column {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                color = ForestGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.app_tagline),
                                style = MaterialTheme.typography.bodySmall,
                                color = ForestGreenPrimary
                            )
                        }
                    }
                },
                actions = {
                    LanguageToggleButton(
                        currentLanguage = uiState.language,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmCreamBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = WarmCreamBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 0. Entrepreneur Profile & Pipeline Control (Section 3 & 5 Pipeline)
            EntrepreneurProfileCard(
                activeProfile = uiState.activeProfile,
                isBackendOnline = uiState.isBackendOnline,
                isSyncing = uiState.isProfileSyncing,
                language = uiState.language,
                onSelectProfile = { viewModel.selectProfile(it) }
            )

            // 1. Interactive Input Form Card with Location, Scale, Capacity, Margin & Details
            InputFormCard(
                village = uiState.village,
                block = uiState.block,
                district = uiState.district,
                state = uiState.state,
                onStateChange = { viewModel.onStateChanged(it) },
                selectedScale = uiState.scale,
                onScaleSelect = { viewModel.onScaleSelected(it) },
                productionCapacity = uiState.productionCapacity,
                onProductionCapacityChange = { viewModel.onProductionCapacityChanged(it) },
                marginCapital = uiState.marginCapital,
                marginInputText = uiState.marginInputText,
                customCategory = uiState.customCategory,
                businessDetails = uiState.businessDetails,
                language = uiState.language,
                onVillageChange = { viewModel.onVillageChanged(it) },
                onBlockChange = { viewModel.onBlockChanged(it) },
                onDistrictChange = { viewModel.onDistrictChanged(it) },
                onMarginChange = { viewModel.onMarginChanged(it) },
                onMarginInputTextChange = { viewModel.onMarginInputTextChanged(it) },
                onCustomCategoryChange = { viewModel.onCustomCategoryChanged(it) },
                onBusinessDetailsChange = { viewModel.onBusinessDetailsChanged(it) },
                onStartVoiceInput = startVoiceInput,
                selectedCategory = uiState.selectedCategory,
                onCategorySelect = { viewModel.onCategorySelected(it) },
                selectedObjective = uiState.growthObjective,
                onObjectiveSelect = { viewModel.onGrowthObjectiveSelected(it) }
            )

            // 2. Gemini AI Real-Time Advisory & Financial Projections
            AiAdvisoryStatusCard(
                report = uiState.feasibilityReport,
                location = LocationInfo(uiState.village, uiState.block, uiState.district, uiState.state),
                marginCapital = uiState.marginCapital,
                isAiLoading = uiState.isAiLoading,
                apiKey = uiState.apiKey,
                language = uiState.language,
                onGenerateAiClick = { viewModel.triggerGeminiAnalysis() }
            )

            // 3. Government Scheme Router & Smart Financial Structuring Dashboard (Restored to 3rd place)
            FinancialDashboardCard(
                financialResult = uiState.financialResult,
                language = uiState.language
            )

            // 4. Business Intelligence & Cash-Flow Diagnostic (Section 8: Revenue -> OpEx -> Loan EMI -> Net Profit -> Cash Flow)
            uiState.businessHealthReport?.let { healthReport ->
                BusinessHealthCard(
                    report = healthReport,
                    language = uiState.language,
                    monthlyRevenue = uiState.monthlyRevenue,
                    monthlyExpenses = uiState.monthlyExpenses,
                    onRevenueChange = { viewModel.onMonthlyRevenueChanged(it) },
                    onExpensesChange = { viewModel.onMonthlyExpensesChanged(it) }
                )
            }

            // 5. Audio Voice Summary Player Bar
            AudioSummaryCard(
                isPlaying = uiState.isAudioPlaying,
                language = uiState.language,
                onPlayAudio = {
                    viewModel.setAudioPlaying(true)
                    val script = uiState.ttsScript
                    val success = ttsHelper.speak(script, uiState.language)
                    if (!success) {
                        viewModel.setAudioPlaying(false)
                        coroutineScope.launch {
                            val msg = ttsHelper.lastMessage.value
                                ?: if (uiState.language == LanguageCode.HI) {
                                    "ऑडियो शुरू करने में समस्या हुई।"
                                } else {
                                    "Failed to start audio playback."
                                }
                            snackbarHostState.showSnackbar(msg)
                        }
                    }
                },
                onStopAudio = {
                    ttsHelper.stop()
                    viewModel.setAudioPlaying(false)
                }
            )

            // 6. 2x2 SWOT Matrix Grid
            SwotMatrixGrid(
                swotAnalysis = uiState.feasibilityReport?.swotAnalysis,
                language = uiState.language
            )

            // 7. 6-Dimension Feasibility Cards & Strategic Recommendations
            DimensionCards(
                report = uiState.feasibilityReport,
                language = uiState.language
            )

            // 8. Action Plan: What Should I Do Next? Roadmap (Section 9 & 10)
            ActionPlanRoadmapCard(
                category = uiState.selectedCategory,
                objective = uiState.growthObjective,
                language = uiState.language
            )

            // 9. Eligible Government Schemes Router Card (Immediately before Amortization Table)
            val projectCost = (uiState.financialResult as? FinancialCalculationResult.Success)
                ?.structure?.totalProjectCost
                ?: (if (uiState.marginCapital > 0.0) uiState.marginCapital / 0.10 else 100000.0)

            EligibleSchemesCard(
                category = uiState.selectedCategory,
                marginCapital = uiState.marginCapital,
                projectCost = projectCost,
                language = uiState.language
            )

            // 10. Quarterly Amortization Schedule Table
            val schedule = (uiState.financialResult as? FinancialCalculationResult.Success)?.schedule
            AmortizationScheduleTable(
                schedule = schedule,
                isExpanded = uiState.isAmortizationExpanded,
                language = uiState.language,
                onToggleExpand = { viewModel.toggleAmortizationExpanded() }
            )

            // 11. Share & Export Summary Sheet
            ShareActionBar(
                onShareClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "ABHYUDAY - AI Rural Business Advisory Report")
                        putExtra(Intent.EXTRA_TEXT, uiState.shareText)
                    }
                    val chooser = Intent.createChooser(
                        shareIntent,
                        if (uiState.language == LanguageCode.HI) "सलाहकार रिपोर्ट साझा करें" else "Share Advisory Report"
                    )
                    context.startActivity(chooser)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
