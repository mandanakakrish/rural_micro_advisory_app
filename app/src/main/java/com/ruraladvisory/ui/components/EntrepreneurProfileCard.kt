package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.UserProfile
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.OutlineWarm
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SchemeMicroGreenText
import com.ruraladvisory.ui.theme.SchemeTermTealBg
import com.ruraladvisory.ui.theme.SchemeTermTealText
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted

/**
 * Top Entrepreneur Profile & Pipeline Control Card (Section 3 & 5).
 * Represents User -> Auth -> Profile -> Business Data -> AI Advisory pipeline.
 * Offers quick switching between representative rural personas:
 * - User A: Ramesh Patel (Dairy, Small, Gujarat, 30 Cattle)
 * - User B: Priya Sharma (Handicrafts, Medium, Rajasthan, 1,200 Craft Units)
 * - User C: Mohan Lal (Pickle / Food Processing, Micro, MP, 500 Bottles)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntrepreneurProfileCard(
    activeProfile: UserProfile,
    isBackendOnline: Boolean,
    isSyncing: Boolean,
    language: LanguageCode,
    onSelectProfile: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(1.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Header: Icon, Title & Backend Connectivity Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SchemeMicroGreenBg)
                            .border(1.dp, ForestGreenLight.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (language == LanguageCode.HI) "उद्यमी प्रोफाइल" else "Enterprise Profile",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            maxLines = 1
                        )
                        Text(
                            text = if (language == LanguageCode.HI) "पंजीकृत ग्रामीण सूक्ष्म उद्यम" else "Registered Rural Enterprise",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Backend Connectivity Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isBackendOnline) SchemeMicroGreenBg else SchemeTermTealBg)
                        .border(
                            0.5.dp,
                            if (isBackendOnline) ForestGreenLight else AgriTealSecondary.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                strokeWidth = 2.dp,
                                color = ForestGreenPrimary
                            )
                            Text(
                                text = if (language == LanguageCode.HI) "सिंक हो रहा है..." else "Syncing...",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenPrimary,
                                maxLines = 1
                            )
                        } else if (isBackendOnline) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Online",
                                tint = SchemeMicroGreenText,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (language == LanguageCode.HI) "क्लाउड सिंक" else "Cloud Synced",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SchemeMicroGreenText,
                                maxLines = 1
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Offline Mode",
                                tint = SchemeTermTealText,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (language == LanguageCode.HI) "ऑफलाइन मोड" else "Offline Ready",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SchemeTermTealText,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = OutlineLight.copy(alpha = 0.6f), thickness = 0.5.dp)

            // 2. Active Profile Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(0.5.dp, OutlineWarm, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (activeProfile.name.isNotBlank()) activeProfile.name else (if (language == LanguageCode.HI) "नया उद्यम" else "New Enterprise"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextHighContrast
                        )
                        Text(
                            text = if (activeProfile.phoneNumber.isNotBlank()) activeProfile.phoneNumber else (if (language == LanguageCode.HI) "मोबाइल: दर्ज नहीं" else "Phone: Not set"),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    // Metadata Badges: Scale, Location, Capacity
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Category Chip
                        ProfileBadge(
                            text = activeProfile.category.displayName(language),
                            bgColor = SchemeMicroGreenBg,
                            textColor = SchemeMicroGreenText
                        )

                        // Scale Chip
                        ProfileBadge(
                            text = activeProfile.scale.displayName(language),
                            bgColor = SchemeTermTealBg,
                            textColor = SchemeTermTealText
                        )

                        // State & District Chip
                        val locParts = listOf(activeProfile.district, activeProfile.state).filter { it.isNotBlank() }
                        val locLabel = if (locParts.isNotEmpty()) locParts.joinToString(", ") else if (language == LanguageCode.HI) "स्थान: दर्ज नहीं" else "Location: Not set"
                        ProfileBadge(
                            text = locLabel,
                            bgColor = SurfaceCream,
                            textColor = TextMediumContrast
                        )

                        // Production Capacity Chip
                        if (activeProfile.productionCapacity.isNotBlank()) {
                            ProfileBadge(
                                text = "⚡ ${activeProfile.productionCapacity}",
                                bgColor = ForestGreenLight.copy(alpha = 0.4f),
                                textColor = ForestGreenDark
                            )
                        }
                    }
                }
            }

            // 3. Sample Enterprise Switcher Bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (language == LanguageCode.HI) "उद्यम प्रोफाइल विकल्प (Templates):" else "Enterprise Profile Options:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMediumContrast
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isCustom = activeProfile.id == "custom_user" || activeProfile.id == "empty_user"
                    FilterChip(
                        selected = isCustom,
                        onClick = { onSelectProfile(UserProfile.EMPTY) },
                        label = {
                            Text(
                                text = if (language == LanguageCode.HI) "खाली फॉर्म (Blank)" else "Blank Form",
                                fontWeight = if (isCustom) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceCard,
                            labelColor = TextHighContrast
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isCustom,
                            borderColor = OutlineLight,
                            selectedBorderColor = ForestGreenPrimary,
                            borderWidth = if (isCustom) 1.dp else 0.5.dp
                        )
                    )

                    UserProfile.PRESETS.forEach { preset ->
                        val isSelected = preset.id == activeProfile.id
                        val presetLabel = when (preset.id) {
                            UserProfile.USER_A.id -> if (language == LanguageCode.HI) "डेयरी फार्म (गुजरात)" else "Dairy Farm (GJ)"
                            UserProfile.USER_B.id -> if (language == LanguageCode.HI) "हस्तशिल्प (राजस्थान)" else "Handicrafts (RJ)"
                            UserProfile.USER_C.id -> if (language == LanguageCode.HI) "खाद्य प्रसंस्करण (म.प्र.)" else "Food Processing (MP)"
                            else -> preset.name
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectProfile(preset) },
                            label = {
                                Text(
                                    text = presetLabel,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreenPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceCard,
                                labelColor = TextHighContrast
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = OutlineLight,
                                selectedBorderColor = ForestGreenPrimary,
                                borderWidth = if (isSelected) 1.dp else 0.5.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileBadge(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
