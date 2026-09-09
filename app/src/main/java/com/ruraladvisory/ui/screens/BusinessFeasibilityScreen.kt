package com.ruraladvisory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.ui.components.ActionPlanRoadmapCard
import com.ruraladvisory.ui.components.AiAdvisoryStatusCard
import com.ruraladvisory.ui.components.AudioSummaryCard
import com.ruraladvisory.ui.components.DimensionCards
import com.ruraladvisory.ui.components.EligibleSchemesCard
import com.ruraladvisory.ui.components.SwotMatrixGrid
import com.ruraladvisory.ui.navigation.AppTab
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

/**
 * Screen 3: Business Feasibility Intelligence & 6-Pillar Study
 * Reference: WEB/src/components/BusinessFeasibilityView.tsx
 */
@Composable
fun BusinessFeasibilityScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    ttsHelper: TextToSpeechHelper,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isHindi = uiState.language == LanguageCode.HI

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header: Screen Context
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isHindi) "उद्यम व्यवहार्यता एवं 6-स्तंभ विश्लेषण" else "Enterprise Feasibility & Viability Audit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Text(
                    text = if (isHindi) "बाजार पहुंच, SWOT मैट्रिक्स, जोखिम निवारण व कार्ययोजना" else "Market reach, SWOT matrix, risk mitigation & action roadmap",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // 1. AI Real-Time Advisory & Executive Summary
        AiAdvisoryStatusCard(
            report = uiState.feasibilityReport,
            location = LocationInfo(uiState.village, uiState.block, uiState.district, uiState.state),
            marginCapital = uiState.marginCapital,
            isAiLoading = uiState.isAiLoading,
            apiKey = uiState.apiKey,
            language = uiState.language,
            onGenerateAiClick = { viewModel.triggerGeminiAnalysis() }
        )

        // 2. Audio Voice Summary Player Bar
        AudioSummaryCard(
            isPlaying = uiState.isAudioPlaying,
            language = uiState.language,
            onPlayAudio = {
                viewModel.setAudioPlaying(true)
                val script = uiState.ttsScript
                val success = ttsHelper.speak(script, uiState.language)
                if (!success) {
                    viewModel.setAudioPlaying(false)
                }
            },
            onStopAudio = {
                ttsHelper.stop()
                viewModel.setAudioPlaying(false)
            }
        )

        // 3. Budget-Calibrated SWOT Analysis Matrix
        uiState.feasibilityReport?.swotAnalysis?.let { swot ->
            SwotMatrixGrid(
                swotAnalysis = swot,
                language = uiState.language
            )
        }

        // 4. 6-Dimensional Feasibility Insights
        uiState.feasibilityReport?.let { report ->
            DimensionCards(
                report = report,
                language = uiState.language
            )
        }

        // 5. Action Plan Roadmap: "What Should I Do Next?"
        ActionPlanRoadmapCard(
            category = uiState.selectedCategory,
            objective = uiState.growthObjective,
            language = uiState.language
        )

        // 6. Eligible Government Schemes & Direct Portals
        EligibleSchemesCard(
            category = uiState.selectedCategory,
            marginCapital = uiState.marginCapital,
            projectCost = uiState.successfulFinance?.structure?.totalProjectCost ?: (uiState.marginCapital * 10.0),
            language = uiState.language
        )

        // 7. Primary Forward Action Button
        Button(
            onClick = { viewModel.selectTab(AppTab.DPR) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreenPrimary,
                contentColor = SurfaceCream
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isHindi) "विस्तृत परियोजना रिपोर्ट (DPR) देखें" else "Generate Project Appraisal Report (DPR)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
