package com.ruraladvisory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.R
import com.ruraladvisory.advisory.model.ActionPlanStep
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.GrowthObjective
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.AmberAccent
import com.ruraladvisory.ui.theme.AmberLight
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import com.ruraladvisory.ui.theme.SchemeMicroGreenBg
import com.ruraladvisory.ui.theme.SchemeMicroGreenText
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground

/**
 * Action Plan Card: "What Should I Do Next? / अब मुझे क्या करना चाहिए?"
 * Provides a sequential, prioritized 5-step operational roadmap answering
 * the rural entrepreneur's immediate execution questions (Section 9 & 10).
 */
@Composable
fun ActionPlanRoadmapCard(
    category: BusinessCategory,
    objective: GrowthObjective,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    val steps = remember(category, objective) {
        generateActionPlan(category, objective)
    }

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SchemeMicroGreenBg)
                        .border(1.dp, ForestGreenLight.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "Action Plan",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.label_action_plan_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                    Text(
                        text = stringResource(R.string.label_action_plan_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumContrast
                    )
                }
            }

            HorizontalDivider(color = OutlineLight.copy(alpha = 0.6f))

            // Step items
            steps.forEachIndexed { index, step ->
                ActionStepItem(
                    step = step,
                    isLast = index == steps.size - 1,
                    language = language
                )
            }
        }
    }
}

@Composable
private fun ActionStepItem(
    step: ActionPlanStep,
    isLast: Boolean,
    language: LanguageCode,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Step number indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(ForestGreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(OutlineLight)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = step.title(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHighContrast,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmberLight)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = step.priority(language),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }

            Text(
                text = step.description(language),
                style = MaterialTheme.typography.bodySmall,
                color = TextMediumContrast,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = step.timeline(language),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

private fun generateActionPlan(
    category: BusinessCategory,
    objective: GrowthObjective
): List<ActionPlanStep> {
    return listOf(
        ActionPlanStep(
            stepNumber = 1,
            titleEn = "JanSamarth & Concessional Scheme Filing",
            titleHi = "जनसमर्थ पोर्टल व योजना आवेदन",
            descriptionEn = "Compile beneficiary margin (10%), quotations, and file application on JanSamarth / DIC portal for concessional credit.",
            descriptionHi = "10% मार्जिन पूंजी तैयार रखें, मशीनरी कोटेशन लें और जनसमर्थ पोर्टल अथवा जिला उद्योग केंद्र में आवेदन करें।",
            timelineEn = "Week 1 - 2",
            timelineHi = "सप्ताह 1 - 2",
            priorityEn = "HIGH PRIORITY",
            priorityHi = "अति आवश्यक"
        ),
        ActionPlanStep(
            stepNumber = 2,
            titleEn = "Machinery & Workspace Setup",
            titleHi = "मशीनरी व कार्यस्थल स्थापना",
            descriptionEn = "Procure certified machinery under dealer warranty and prepare rural sheds with stable power/water connections.",
            descriptionHi = "वारंटी युक्त गुणवत्तापूर्ण उपकरण खरीदें और कार्यस्थल पर बिजली-पानी की सुचारू व्यवस्था सुनिश्चित करें।",
            timelineEn = "Week 3 - 4",
            timelineHi = "सप्ताह 3 - 4",
            priorityEn = "ESSENTIAL",
            priorityHi = "महत्वपूर्ण"
        ),
        ActionPlanStep(
            stepNumber = 3,
            titleEn = "Local Packaging & Unit Pricing",
            titleHi = "पैकेजिंग एवं उचित मूल्य-निर्धारण",
            descriptionEn = "Set customer-friendly unit sizes for local weekly haats and village clusters, maintaining at least 20% gross spread.",
            descriptionHi = "ग्रामीण हाट और पड़ोस के गांवों हेतु किफायती साइज में पैकिंग करें तथा कम से कम 20% का मार्जिन बनाए रखें।",
            timelineEn = "Week 5 - 6",
            timelineHi = "सप्ताह 5 - 6",
            priorityEn = "STRATEGIC",
            priorityHi = "रणनीतिक"
        ),
        ActionPlanStep(
            stepNumber = 4,
            titleEn = "Distribution & Direct-to-Consumer Channels",
            titleHi = "वितरण व स्थानीय खुदरा बिक्री",
            descriptionEn = "Establish distribution with 5–8 village retailers and local consumer WhatsApp ordering for fresh daily supplies.",
            descriptionHi = "5 से 8 स्थानीय किराना दुकानों से आपूर्ति अनुबंध करें और सीधे ग्राहकों से व्हाट्सएप्प पर ऑर्डर लें।",
            timelineEn = "Week 7 - 8",
            timelineHi = "सप्ताह 7 - 8",
            priorityEn = "EXPANSION",
            priorityHi = "विस्तार"
        ),
        ActionPlanStep(
            stepNumber = 5,
            titleEn = "Quarterly Repayment & Cash Buffer Tracking",
            titleHi = "तिमाही ऋण किश्त व बचत प्रबंधन",
            descriptionEn = "Deposit loan installment into designated account monthly and maintain a 45-day operational cash reserve.",
            descriptionHi = "तिमाही किश्त हेतु प्रतिमाह खाते में पैसा अलग रखें और 45 दिनों के खर्च के बराबर नकदी सुरक्षित रखें।",
            timelineEn = "Ongoing",
            timelineHi = "निरंतर",
            priorityEn = "MONITORING",
            priorityHi = "वित्तीय अनुशासन"
        )
    )
}
