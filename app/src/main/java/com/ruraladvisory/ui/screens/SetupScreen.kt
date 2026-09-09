package com.ruraladvisory.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.components.EntrepreneurProfileCard
import com.ruraladvisory.ui.components.InputFormCard
import com.ruraladvisory.ui.navigation.AppTab
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

/**
 * Screen 1: Entrepreneur Profile & Enterprise Setup
 * Reference: WEB/src/components/InputForm.tsx
 */
@Composable
fun SetupScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    onStartVoiceInput: () -> Unit,
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
                    text = if (isHindi) "उद्यम प्रोफाइल एवं सेटअप" else "Enterprise Setup & Profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Text(
                    text = if (isHindi) "स्थान, मार्जिन पूंजी एवं व्यवसाय विवरण" else "Location, margin capital & enterprise scope",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // 0. Scheme Registration Summary Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
            border = BorderStroke(0.5.dp, OutlineLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ForestGreenLight.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isHindi) "सरकारी योजना पंजीकरण विवरण" else "Beneficiary Scheme Profile",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                            Text(
                                text = if (isHindi) "जनसमर्थ, PMEGP व SCA सब्सिडी डेटा" else "JanSamarth, PMEGP & SCA Subsidy Requisites",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.openUserDetails() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                        border = BorderStroke(1.dp, ForestGreenPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        val btnText = if (uiState.activeProfile.name.isBlank()) {
                            if (isHindi) "विवरण भरें" else "Enter Details"
                        } else {
                            if (isHindi) "विवरण बदलें" else "Edit Details"
                        }
                        Text(btnText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = OutlineLight, thickness = 1.dp)

                if (uiState.activeProfile.name.isBlank()) {
                    Text(
                        text = if (isHindi)
                            "कोई आवेदक विवरण दर्ज नहीं है। जनसमर्थ, PMEGP एवं सरकारी सब्सिडी पात्रता के लिए 'विवरण भरें' पर टैप करें।"
                        else
                            "No beneficiary details entered yet. Tap 'Enter Details' to add your name, contact & rural location for scheme subsidies.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumContrast,
                        fontSize = 11.sp
                    )
                } else {
                    val phonePart = if (uiState.activeProfile.phoneNumber.isNotBlank()) " (${uiState.activeProfile.phoneNumber})" else ""
                    val catPart = if (uiState.promoterCategory.isNotBlank()) " • ${uiState.promoterCategory}" else ""
                    Text(
                        text = "${uiState.activeProfile.name}$phonePart$catPart • ${uiState.activeProfile.areaType}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextHighContrast,
                        fontSize = 11.sp
                    )
                    val locItems = listOf(uiState.village, uiState.block).filter { it.isNotBlank() }
                    val locStr = if (locItems.isNotEmpty()) locItems.joinToString(", ") else if (isHindi) "स्थान अनिर्दिष्ट" else "Location not set"
                    val pinStr = if (uiState.activeProfile.pincode.isNotBlank()) " (${uiState.activeProfile.pincode})" else ""
                    val eduStr = if (uiState.activeProfile.educationQualification.isNotBlank()) " • ${uiState.activeProfile.educationQualification}" else ""
                    Text(
                        text = "${if (isHindi) "स्थान" else "Location"}: $locStr$pinStr$eduStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumContrast,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 1. Entrepreneur Profile & Persona Switcher
        EntrepreneurProfileCard(
            activeProfile = uiState.activeProfile,
            isBackendOnline = uiState.isBackendOnline,
            isSyncing = uiState.isProfileSyncing,
            language = uiState.language,
            onSelectProfile = { viewModel.selectProfile(it) }
        )

        // 2. Input Form (Location, MSME Scale, Capacity, Margin Slider, Category, Goal)
        InputFormCard(
            village = uiState.village,
            onVillageChange = { viewModel.onVillageChanged(it) },
            block = uiState.block,
            onBlockChange = { viewModel.onBlockChanged(it) },
            district = uiState.district,
            onDistrictChange = { viewModel.onDistrictChanged(it) },
            state = uiState.state,
            onStateChange = { viewModel.onStateChanged(it) },
            selectedScale = uiState.scale,
            onScaleSelect = { viewModel.onScaleSelected(it) },
            productionCapacity = uiState.productionCapacity,
            onProductionCapacityChange = { viewModel.onProductionCapacityChanged(it) },
            marginCapital = uiState.marginCapital,
            marginInputText = uiState.marginInputText,
            onMarginChange = { viewModel.onMarginChanged(it) },
            onMarginInputTextChange = { viewModel.onMarginInputTextChanged(it) },
            customCategory = uiState.customCategory,
            onCustomCategoryChange = { viewModel.onCustomCategoryChanged(it) },
            businessDetails = uiState.businessDetails,
            onBusinessDetailsChange = { viewModel.onBusinessDetailsChanged(it) },
            language = uiState.language,
            onStartVoiceInput = onStartVoiceInput,
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = { viewModel.onCategorySelected(it) },
            selectedObjective = uiState.growthObjective,
            onObjectiveSelect = { viewModel.onGrowthObjectiveSelected(it) }
        )

        // 3. Primary Forward Action Button
        Button(
            onClick = { viewModel.selectTab(AppTab.CALCULATOR) },
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
                    text = if (isHindi) "वित्तीय योजना एवं ऋण संरचना देखें" else "Proceed to Financial Structuring",
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
