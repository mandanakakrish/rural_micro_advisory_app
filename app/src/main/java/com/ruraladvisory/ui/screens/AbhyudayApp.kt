package com.ruraladvisory.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.ui.navigation.AppTab
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel
import kotlinx.coroutines.launch

/**
 * Master Application Container implementing the 5-screen navigation architecture
 * directly referencing WEB/src/App.tsx.
 */
@Composable
fun AbhyudayApp(
    viewModel: AdvisoryViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val ttsHelper = remember { TextToSpeechHelper(context) }

    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    // Voice recognition launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                if (uiState.activeTab == AppTab.ADVISOR) {
                    viewModel.sendChatMessage(spoken)
                } else {
                    viewModel.onBusinessDetailsChanged(
                        if (uiState.businessDetails.isBlank()) spoken
                        else "${uiState.businessDetails} $spoken"
                    )
                }
                coroutineScope.launch {
                    val msg = if (uiState.language == LanguageCode.HI) {
                        "आवाज़ से दर्ज किया गया: $spoken"
                    } else {
                        "Voice recorded: $spoken"
                    }
                    snackbarHostState.showSnackbar(msg)
                }
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
                if (uiState.language == LanguageCode.HI) "अपने व्यापार के बारे में बोलें..." else "Speak your business details..."
            )
        }
        try {
            speechLauncher.launch(intent)
        } catch (_: Exception) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    if (uiState.language == LanguageCode.HI)
                        "इस डिवाइस पर वॉइस इनपुट उपलब्ध नहीं है।"
                    else
                        "Voice input is not available on this device."
                )
            }
        }
    }

    val shareSummary: () -> Unit = {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "ABHYUDAY Business Advisory & DPR")
            putExtra(Intent.EXTRA_TEXT, uiState.shareText)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share ABHYUDAY Summary"))
    }

    BackHandler(enabled = uiState.isUserDetailsVisible) {
        viewModel.closeUserDetails()
    }

    if (uiState.isUserDetailsVisible) {
        UserDetailsScreen(
            uiState = uiState,
            viewModel = viewModel
        )
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = WarmCreamBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AbhyudayTopBar(
                    language = uiState.language,
                    onLanguageToggle = { viewModel.onLanguageToggled() },
                    onOpenProfile = { viewModel.openUserDetails() }
                )
            },
            bottomBar = {
                AbhyudayBottomBar(
                    currentTab = uiState.activeTab,
                    language = uiState.language,
                    onTabSelect = { viewModel.selectTab(it) }
                )
            }
        ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.activeTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { tab ->
            when (tab) {
                AppTab.SETUP -> SetupScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onStartVoiceInput = startVoiceInput
                )
                AppTab.CALCULATOR -> FinancialCalculatorScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                AppTab.FEASIBILITY -> BusinessFeasibilityScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    ttsHelper = ttsHelper
                )
                AppTab.DPR -> DprDocumentScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onShareDpr = shareSummary
                )
                AppTab.ADVISOR -> AdvisorChatScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    ttsHelper = ttsHelper,
                    onStartVoiceInput = startVoiceInput
                )
            }
        }
    }
    }
}

@Composable
private fun AbhyudayTopBar(
    language: LanguageCode,
    onLanguageToggle: () -> Unit,
    onOpenProfile: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceCream)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Branding
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "ABHYUDAY",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = ForestGreenDark,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ForestGreenPrimary)
                        )
                    }
                    Text(
                        text = if (language == LanguageCode.HI) "ग्रामीण उद्यम एवं वित्तीय सलाहकार" else "Rural Enterprise Advisory Platform",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Profile & Scheme Details Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ForestGreenLight.copy(alpha = 0.5f))
                        .clickable { onOpenProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Beneficiary Scheme Profile",
                        tint = ForestGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Compact Single-Pill Language Toggle Button
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = ForestGreenPrimary.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.clickable { onLanguageToggle() }
                ) {
                    Text(
                        text = if (language == LanguageCode.EN) "हिंदी" else "English",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
        HorizontalDivider(color = OutlineLight, thickness = 0.5.dp)
    }
}

@Composable
private fun AbhyudayBottomBar(
    currentTab: AppTab,
    language: LanguageCode,
    onTabSelect: (AppTab) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = OutlineLight, thickness = 0.5.dp)
        NavigationBar(
            modifier = Modifier.navigationBarsPadding(),
            containerColor = SurfaceCream,
            tonalElevation = 0.dp
        ) {
            AppTab.entries.forEach { tab ->
                val selected = currentTab == tab
                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabSelect(tab) },
                    icon = {
                        Icon(
                            imageVector = tab.icon(),
                            contentDescription = tab.title(language),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = tab.title(language),
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestGreenPrimary,
                        selectedTextColor = ForestGreenDark,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = ForestGreenLight.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}
