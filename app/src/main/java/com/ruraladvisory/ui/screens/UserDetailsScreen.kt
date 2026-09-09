package com.ruraladvisory.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.OutlineWarm
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotOpportunityBlueBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserDetailsScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.language == LanguageCode.HI

    // Local mutable draft state initialized from current active profile
    var name by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.name) }
    var phone by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.phoneNumber) }
    var email by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.email) }
    var gender by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.gender) }
    var age by remember(uiState.activeProfile) { mutableIntStateOf(uiState.activeProfile.age) }
    var socialCategory by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.promoterCategory) }
    var specialCategory by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.specialCategory) }
    var education by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.educationQualification) }
    var hasEdp by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.hasEdpTraining) }

    var village by remember(uiState.activeProfile) { mutableStateOf(uiState.village) }
    var block by remember(uiState.activeProfile) { mutableStateOf(uiState.block) }
    var district by remember(uiState.activeProfile) { mutableStateOf(uiState.district) }
    var state by remember(uiState.activeProfile) { mutableStateOf(uiState.state) }
    var pincode by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.pincode) }
    var areaType by remember(uiState.activeProfile) { mutableStateOf(uiState.activeProfile.areaType) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        // Top App Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCream)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.closeUserDetails() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ForestGreenDark
                        )
                    }
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = if (isHindi) "उद्यमी योजना विवरण" else "Entrepreneur Scheme Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isHindi) "जनसमर्थ व सरकारी योजना अनिवार्य डेटा" else "JanSamarth & SCA Statutory Requisites",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bilingual Toggle Switch Pill
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = ForestGreenPrimary.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.clickable { viewModel.onLanguageToggled() }
                ) {
                    Text(
                        text = if (isHindi) "English" else "हिंदी",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
            HorizontalDivider(color = OutlineLight, thickness = 0.5.dp)
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Statutory Notice Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SwotOpportunityBlueBg.copy(alpha = 0.4f))
                    .border(1.dp, ForestGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isHindi)
                            "यह विवरण जनसमर्थ (JanSamarth), पीएमईजीपी एवं राज्य चैनलाइजिंग एजेंसी (SCA) के तहत 90% ऋण और 35% तक सरकारी सब्सिडी के लिए सीधे उपयोग किया जाता है।"
                        else
                            "These details directly calibrate your eligibility for 90% concessional credit, PMEGP subsidies (up to 35%), and bankable DPR sanctioning.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreenDark,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // 1. Identity & Contact Information
            SectionCard(
                icon = Icons.Default.Badge,
                title = if (isHindi) "1. आवेदक की पहचान एवं संपर्क" else "1. Applicant Identity & Contact",
                subtitle = if (isHindi) "वैधानिक पहचान व संपर्क विवरण" else "Legal Identity & Contact"
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isHindi) "उद्यमी का पूरा नाम *" else "Full Name of Entrepreneur *") },
                    placeholder = { Text(if (isHindi) "उदा. रमेश कुमार" else "e.g. Ramesh Kumar") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreenPrimary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedFieldColors()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(if (isHindi) "मोबाइल नंबर *" else "Mobile Number *") },
                        placeholder = { Text(if (isHindi) "उदा. 9876543210" else "e.g. 9876543210") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreenPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedFieldColors()
                    )

                    OutlinedTextField(
                        value = if (age > 0) age.toString() else "",
                        onValueChange = { age = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0 },
                        label = { Text(if (isHindi) "आयु (वर्ष) *" else "Age (Yrs) *") },
                        placeholder = { Text(if (isHindi) "उदा. 32" else "e.g. 32") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(100.dp),
                        colors = outlinedFieldColors()
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(if (isHindi) "ईमेल पता (वैकल्पिक)" else "Email Address (Optional)") },
                    placeholder = { Text("e.g. name@example.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreenPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedFieldColors()
                )

                // Gender Selection Chips
                Text(
                    text = if (isHindi) "लिंग (Gender) *" else "Gender *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Male", "Female", "Other").forEach { g ->
                        val label = when (g) {
                            "Male" -> if (isHindi) "पुरुष" else "Male"
                            "Female" -> if (isHindi) "महिला" else "Female"
                            else -> if (isHindi) "अन्य" else "Other"
                        }
                        val selected = gender == g
                        FilterChip(
                            selected = selected,
                            onClick = { gender = g },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = filterChipColors(selected)
                        )
                    }
                }
                if (gender == "Female") {
                    Text(
                        text = if (isHindi) "★ विशेष लाभ: महिला उद्यमियों को ग्रामीण क्षेत्रों में 35% तक अधिकतम सरकारी सब्सिडी प्राप्त होती है।"
                               else "★ Special Advantage: Women entrepreneurs qualify for up to 35% subsidy under PMEGP & Special SCA Category.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreenDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 2. Social Category & Statutory Eligibility
            SectionCard(
                icon = Icons.Default.School,
                title = if (isHindi) "2. सामाजिक श्रेणी व पात्रता" else "2. Social Category & Statutory Eligibility",
                subtitle = if (isHindi) "सब्सिडी प्रतिशत व रियायती ऋण कोटा" else "Determines subsidy % & concessional debt"
            ) {
                Text(
                    text = if (isHindi) "सामाजिक श्रेणी (Social Category) *" else "Social Category *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("OBC", "SC", "ST", "Minority", "General").forEach { cat ->
                        val selected = socialCategory == cat
                        FilterChip(
                            selected = selected,
                            onClick = { socialCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = filterChipColors(selected)
                        )
                    }
                }

                Text(
                    text = if (isHindi) "विशेष लाभार्थी श्रेणी" else "Special Beneficiary Category",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("None", "Women Entrepreneur", "Specially Abled", "Ex-Servicemen").forEach { spec ->
                        val label = when (spec) {
                            "None" -> if (isHindi) "कोई नहीं" else "None"
                            "Specially Abled" -> if (isHindi) "दिव्यांगजन" else "Divyangjan"
                            "Ex-Servicemen" -> if (isHindi) "भूतपूर्व सैनिक" else "Ex-Servicemen"
                            else -> spec
                        }
                        val selected = specialCategory == spec
                        FilterChip(
                            selected = selected,
                            onClick = { specialCategory = spec },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = filterChipColors(selected)
                        )
                    }
                }

                Text(
                    text = if (isHindi) "शैक्षणिक योग्यता *" else "Educational Qualification *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Under 8th", "8th Pass", "10th Pass", "12th Pass", "Graduate").forEach { edu ->
                        val label = when (edu) {
                            "Under 8th" -> if (isHindi) "8वीं से कम" else "Under 8th"
                            "8th Pass" -> if (isHindi) "8वीं उत्तीर्ण" else "8th Pass"
                            "10th Pass" -> if (isHindi) "10वीं / मैट्रिक" else "10th Pass"
                            "12th Pass" -> if (isHindi) "12वीं / इंटर" else "12th Pass"
                            "Graduate" -> if (isHindi) "स्नातक / डिप्लोमा" else "Graduate"
                            else -> edu
                        }
                        val selected = education == edu
                        FilterChip(
                            selected = selected,
                            onClick = { education = edu },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = filterChipColors(selected)
                        )
                    }
                }

                // EDP Training Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "उद्यमिता विकास प्रशिक्षण (EDP)" else "EDP Entrepreneurship Training",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextHighContrast
                        )
                        Text(
                            text = if (isHindi) "क्या सरकार-मान्यता प्राप्त EDP प्रशिक्षण लिया है?" else "Mandatory for loan release under PMEGP",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = hasEdp,
                        onCheckedChange = { hasEdp = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceCream,
                            checkedTrackColor = ForestGreenPrimary
                        )
                    )
                }
            }

            // 3. Resident & Enterprise Location
            SectionCard(
                icon = Icons.Default.LocationOn,
                title = if (isHindi) "3. निवास एवं इकाई का स्थान" else "3. Resident & Unit Location",
                subtitle = if (isHindi) "ग्रामीण क्षेत्र सब्सिडी पात्रता" else "Rural domicile unlocks higher subsidy"
            ) {
                // Area Type Chips
                Text(
                    text = if (isHindi) "क्षेत्र का प्रकार (Area Type) *" else "Area Type *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Rural", "Urban").forEach { a ->
                        val label = if (a == "Rural") (if (isHindi) "ग्रामीण (Rural - 35% Subsidy)" else "Rural (Up to 35% Subsidy)")
                                    else (if (isHindi) "शहरी (Urban - 25% Subsidy)" else "Urban (Up to 25% Subsidy)")
                        val selected = areaType == a
                        FilterChip(
                            selected = selected,
                            onClick = { areaType = a },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = filterChipColors(selected)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text(if (isHindi) "गाँव / ग्राम पंचायत *" else "Village / GP *") },
                        placeholder = { Text(if (isHindi) "उदा. रामपुर" else "e.g. Rampur") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedFieldColors()
                    )
                    OutlinedTextField(
                        value = block,
                        onValueChange = { block = it },
                        label = { Text(if (isHindi) "ब्लॉक / तहसील *" else "Block / Tehsil *") },
                        placeholder = { Text(if (isHindi) "उदा. मंझनपुर" else "e.g. Manjhanpur") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedFieldColors()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text(if (isHindi) "ज़िला *" else "District *") },
                        placeholder = { Text(if (isHindi) "उदा. कौशाम्बी" else "e.g. Kaushambi") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedFieldColors()
                    )
                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { pincode = it },
                        label = { Text(if (isHindi) "पिन कोड *" else "PIN Code *") },
                        placeholder = { Text("e.g. 212207") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedFieldColors()
                    )
                }

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text(if (isHindi) "राज्य *" else "State *") },
                    placeholder = { Text(if (isHindi) "उदा. उत्तर प्रदेश" else "e.g. Uttar Pradesh") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedFieldColors()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Bottom Action Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.updateUserDetails(
                            name = name,
                            phone = phone,
                            email = email,
                            gender = gender,
                            age = age,
                            socialCategory = socialCategory,
                            specialCategory = specialCategory,
                            education = education,
                            hasEdp = hasEdp,
                            village = village,
                            block = block,
                            district = district,
                            state = state,
                            pincode = pincode,
                            areaType = areaType
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreenPrimary,
                        contentColor = SurfaceCream
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "योजना प्रोफाइल सहेजें व जारी रखें" else "Save Scheme Profile & Continue",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ForestGreenLight.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            HorizontalDivider(color = OutlineLight, thickness = 1.dp)

            content()
        }
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ForestGreenPrimary,
    unfocusedBorderColor = OutlineWarm,
    focusedLabelColor = ForestGreenPrimary,
    unfocusedLabelColor = TextMuted,
    cursorColor = ForestGreenPrimary
)

@Composable
private fun filterChipColors(selected: Boolean) = FilterChipDefaults.filterChipColors(
    selectedContainerColor = ForestGreenPrimary,
    selectedLabelColor = SurfaceCream,
    containerColor = SurfaceCard,
    labelColor = TextHighContrast
)
