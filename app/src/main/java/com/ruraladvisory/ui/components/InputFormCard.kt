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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.ruraladvisory.R
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.BusinessScale
import com.ruraladvisory.advisory.model.GrowthObjective
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.OutlineWarm
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted

/**
 * Main interactive input form card capturing geographic location,
 * available promoter margin capital (with slider and preset chips),
 * and rural business trade category.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputFormCard(
    village: String,
    block: String,
    district: String,
    marginCapital: Double,
    marginInputText: String,
    customCategory: String = "",
    businessDetails: String = "",
    language: LanguageCode,
    onVillageChange: (String) -> Unit,
    onBlockChange: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onMarginChange: (Double) -> Unit,
    onMarginInputTextChange: (String) -> Unit,
    state: String = "",
    onStateChange: (String) -> Unit = {},
    selectedScale: BusinessScale = BusinessScale.MICRO,
    onScaleSelect: ((BusinessScale) -> Unit)? = null,
    productionCapacity: String = "",
    onProductionCapacityChange: (String) -> Unit = {},
    onCustomCategoryChange: (String) -> Unit = {},
    onBusinessDetailsChange: (String) -> Unit = {},
    onStartVoiceInput: () -> Unit = {},
    selectedCategory: BusinessCategory = BusinessCategory.DAIRY,
    onCategorySelect: ((BusinessCategory) -> Unit)? = null,
    selectedObjective: GrowthObjective = GrowthObjective.EXPANSION,
    onObjectiveSelect: ((GrowthObjective) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(0.5.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Geographic Location Section
            LocationSection(
                village = village,
                block = block,
                district = district,
                state = state,
                language = language,
                onVillageChange = onVillageChange,
                onBlockChange = onBlockChange,
                onDistrictChange = onDistrictChange,
                onStateChange = onStateChange
            )

            // 2. Business Scale & Production Capacity Section
            BusinessScaleAndCapacitySection(
                selectedScale = selectedScale,
                productionCapacity = productionCapacity,
                language = language,
                onScaleSelect = onScaleSelect,
                onProductionCapacityChange = onProductionCapacityChange
            )

            // 3. Margin Capital Slider & Chips
            MarginCapitalSection(
                marginCapital = marginCapital,
                marginInputText = marginInputText,
                language = language,
                onMarginChange = onMarginChange,
                onMarginInputTextChange = onMarginInputTextChange
            )

            // 4. Custom Business Category & Details Section with Mic Button
            CustomCategoryAndDetailsSection(
                customCategory = customCategory,
                businessDetails = businessDetails,
                language = language,
                onCustomCategoryChange = {
                    onCustomCategoryChange(it)
                    onCategorySelect?.invoke(BusinessCategory.fromCustomText(it))
                },
                onBusinessDetailsChange = onBusinessDetailsChange,
                onStartVoiceInput = onStartVoiceInput
            )

            // 5. Growth Objective Selector (Section 5)
            GrowthObjectiveSection(
                selectedObjective = selectedObjective,
                language = language,
                onObjectiveSelect = onObjectiveSelect
            )
        }
    }
}

@Composable
private fun LocationSection(
    village: String,
    block: String,
    district: String,
    state: String,
    language: LanguageCode,
    onVillageChange: (String) -> Unit,
    onBlockChange: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onStateChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(R.string.label_location),
                style = MaterialTheme.typography.titleMedium,
                color = TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = village,
                onValueChange = onVillageChange,
                label = { Text(stringResource(R.string.label_village)) },
                placeholder = { Text(stringResource(R.string.hint_village)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = textFieldColors()
            )

            OutlinedTextField(
                value = block,
                onValueChange = onBlockChange,
                label = { Text(stringResource(R.string.label_block)) },
                placeholder = { Text(stringResource(R.string.hint_block)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = textFieldColors()
            )
        }

        OutlinedTextField(
            value = district,
            onValueChange = onDistrictChange,
            label = { Text(stringResource(R.string.label_district)) },
            placeholder = { Text(stringResource(R.string.hint_district)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = ForestGreenPrimary
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = state,
            onValueChange = onStateChange,
            label = { Text(if (language == LanguageCode.HI) "राज्य (State)" else "State") },
            placeholder = { Text("e.g. Gujarat") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        // Quick State Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val statePresets = listOf("Uttar Pradesh", "Gujarat", "Rajasthan", "Madhya Pradesh", "Maharashtra")
            statePresets.forEach { presetState ->
                val isSelected = state.equals(presetState, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onStateChange(presetState) },
                    label = { Text(presetState, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreenPrimary.copy(alpha = 0.15f),
                        selectedLabelColor = ForestGreenDark,
                        containerColor = SurfaceCard,
                        labelColor = TextMediumContrast
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = OutlineLight,
                        selectedBorderColor = ForestGreenPrimary,
                        borderWidth = if (isSelected) 1.5.dp else 1.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun BusinessScaleAndCapacitySection(
    selectedScale: BusinessScale,
    productionCapacity: String,
    language: LanguageCode,
    onScaleSelect: ((BusinessScale) -> Unit)?,
    onProductionCapacityChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = if (language == LanguageCode.HI) "उद्यम पैमाना एवं उत्पादन क्षमता" else "Business Scale & Capacity",
                style = MaterialTheme.typography.titleMedium,
                color = TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }

        // Scale chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BusinessScale.entries.forEach { scale ->
                val isSelected = scale == selectedScale
                FilterChip(
                    selected = isSelected,
                    onClick = { onScaleSelect?.invoke(scale) },
                    label = {
                        Text(
                            text = scale.displayName(language),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
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
                        selectedBorderColor = ForestGreenDark,
                        borderWidth = if (isSelected) 1.5.dp else 1.dp
                    )
                )
            }
        }

        // Production Capacity OutlinedTextField
        OutlinedTextField(
            value = productionCapacity,
            onValueChange = onProductionCapacityChange,
            label = {
                Text(if (language == LanguageCode.HI) "उत्पादन क्षमता (Production Capacity)" else "Production / Operational Capacity")
            },
            placeholder = {
                Text(if (language == LanguageCode.HI) "उदा. 500 बोतल/माह या 30 मवेशी (400 लीटर/दिन)" else "e.g. 500 bottles/month or 30 cattle (400 L/day)")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )
    }
}

@Composable
private fun MarginCapitalSection(
    marginCapital: Double,
    marginInputText: String,
    language: LanguageCode,
    onMarginChange: (Double) -> Unit,
    onMarginInputTextChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.label_margin_capital),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.desc_margin_capital),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            // High-visibility currency value box
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SwotStrengthGreenBg)
                    .border(1.dp, ForestGreenPrimary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "₹${TextToSpeechHelper.formatIndianCurrency(marginCapital)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = ForestGreenDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Direct numeric input box
        OutlinedTextField(
            value = marginInputText,
            onValueChange = onMarginInputTextChange,
            label = { Text(stringResource(R.string.label_margin_capital)) },
            placeholder = { Text(if (language == LanguageCode.HI) "उदा. 14000" else "e.g. 14000") },
            prefix = { Text("₹ ", fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        // Interactive Slider (₹1,000 to ₹5,00,000)
        Slider(
            value = marginCapital.toFloat().coerceIn(1000f, 500000f),
            onValueChange = { onMarginChange(it.toDouble()) },
            valueRange = 1000f..500000f,
            steps = 499,
            colors = SliderDefaults.colors(
                thumbColor = ForestGreenPrimary,
                activeTrackColor = ForestGreenPrimary,
                inactiveTrackColor = OutlineLight
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("₹1,000", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Text("₹14,000 (Micro Max)", style = MaterialTheme.typography.bodySmall, color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
            Text("₹5,00,000 (Term Max)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }

        // Preset Chips Row (Scrollable)
        Text(
            text = stringResource(R.string.label_quick_presets),
            style = MaterialTheme.typography.labelMedium,
            color = TextMediumContrast,
            fontWeight = FontWeight.SemiBold
        )

        val presetList = listOf(
            10000.0 to stringResource(R.string.chip_10k),
            14000.0 to stringResource(R.string.chip_14k),
            25000.0 to stringResource(R.string.chip_25k),
            50000.0 to stringResource(R.string.chip_50k),
            100000.0 to stringResource(R.string.chip_1l),
            200000.0 to stringResource(R.string.chip_2l),
            500000.0 to stringResource(R.string.chip_5l)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetList.forEach { (presetAmount, presetLabel) ->
                val isSelected = kotlin.math.abs(marginCapital - presetAmount) < 1.0
                FilterChip(
                    selected = isSelected,
                    onClick = { onMarginChange(presetAmount) },
                    label = {
                        Text(
                            text = presetLabel,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreenPrimary,
                        selectedLabelColor = SurfaceCream,
                        containerColor = SurfaceCard,
                        labelColor = TextHighContrast
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) ForestGreenPrimary else OutlineLight,
                        selectedBorderColor = ForestGreenPrimary,
                        borderWidth = if (isSelected) 2.dp else 1.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun CustomCategoryAndDetailsSection(
    customCategory: String,
    businessDetails: String,
    language: LanguageCode,
    onCustomCategoryChange: (String) -> Unit,
    onBusinessDetailsChange: (String) -> Unit,
    onStartVoiceInput: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = stringResource(R.string.label_category),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.desc_category),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // Custom Category Textbox
        OutlinedTextField(
            value = customCategory,
            onValueChange = onCustomCategoryChange,
            label = { Text(stringResource(R.string.label_category)) },
            placeholder = { Text(stringResource(R.string.hint_custom_category)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = ForestGreenPrimary
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        // Quick Category Suggestions Chips
        val categorySuggestions = listOf(
            stringResource(R.string.chip_dairy),
            stringResource(R.string.chip_retail),
            stringResource(R.string.chip_food),
            stringResource(R.string.chip_textiles),
            stringResource(R.string.chip_poultry),
            stringResource(R.string.chip_agro),
            stringResource(R.string.chip_handicrafts)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categorySuggestions.forEach { suggestion ->
                val isSelected = customCategory.equals(suggestion, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onCustomCategoryChange(suggestion) },
                    label = {
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreenPrimary,
                        selectedLabelColor = SurfaceCream,
                        containerColor = SurfaceCard,
                        labelColor = TextHighContrast
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) ForestGreenPrimary else OutlineLight,
                        selectedBorderColor = ForestGreenPrimary,
                        borderWidth = if (isSelected) 2.dp else 1.dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Business Details Header with Mic Action Pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.label_business_details),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.desc_business_details),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            // Prominent Voice-to-Text Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SwotStrengthGreenBg)
                    .border(1.dp, ForestGreenPrimary, RoundedCornerShape(20.dp))
                    .clickable(role = Role.Button, onClick = onStartVoiceInput)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = ForestGreenDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.btn_voice_input),
                        style = MaterialTheme.typography.labelMedium,
                        color = ForestGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Multiline Business Details OutlinedTextField
        OutlinedTextField(
            value = businessDetails,
            onValueChange = onBusinessDetailsChange,
            label = { Text(stringResource(R.string.label_business_details)) },
            placeholder = { Text(stringResource(R.string.hint_business_details)) },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(),
            trailingIcon = {
                IconButton(onClick = onStartVoiceInput) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Tap to speak",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ForestGreenPrimary,
    unfocusedBorderColor = OutlineLight,
    focusedLabelColor = ForestGreenPrimary,
    unfocusedLabelColor = TextMediumContrast,
    focusedTextColor = TextHighContrast,
    unfocusedTextColor = TextHighContrast,
    cursorColor = ForestGreenPrimary
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GrowthObjectiveSection(
    selectedObjective: GrowthObjective,
    language: LanguageCode,
    onObjectiveSelect: ((GrowthObjective) -> Unit)?
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.label_growth_objective),
                style = MaterialTheme.typography.titleSmall,
                color = TextHighContrast,
                fontWeight = FontWeight.Bold
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            GrowthObjective.entries.forEach { objective ->
                val isSelected = objective == selectedObjective
                FilterChip(
                    selected = isSelected,
                    onClick = { onObjectiveSelect?.invoke(objective) },
                    label = {
                        Text(
                            text = objective.displayName(language),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreenPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceCard,
                        labelColor = TextHighContrast
                    ),
                    border = BorderStroke(1.dp, if (isSelected) ForestGreenPrimary else OutlineLight)
                )
            }
        }
    }
}
