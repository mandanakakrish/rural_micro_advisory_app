package com.ruraladvisory.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.ui.graphics.vector.ImageVector
import com.ruraladvisory.advisory.model.LanguageCode

/**
 * Navigation tabs corresponding to the 5 core modules of the ABHYUDAY platform,
 * mirroring the WEB reference architecture (WEB/src/types.ts):
 * 1. SETUP (उद्यमी प्रोफाइल व सेटअप)
 * 2. CALCULATOR (वित्तीय ढांचा व सिमुलेटर)
 * 3. FEASIBILITY (व्यावसायिक व्यवहार्यता)
 * 4. DPR (बैंक-तैयार डीपीआर रिपोर्ट)
 * 5. ADVISOR (एआई ग्रामीण सहायक)
 */
enum class AppTab(val route: String) {
    SETUP("setup"),
    CALCULATOR("calculator"),
    FEASIBILITY("feasibility"),
    DPR("dpr"),
    ADVISOR("advisor");

    fun title(language: LanguageCode): String = when (this) {
        SETUP -> if (language == LanguageCode.HI) "सेटअप" else "Setup"
        CALCULATOR -> if (language == LanguageCode.HI) "वित्त" else "Finance"
        FEASIBILITY -> if (language == LanguageCode.HI) "व्यवहार्यता" else "Feasibility"
        DPR -> if (language == LanguageCode.HI) "डीपीआर" else "DPR"
        ADVISOR -> if (language == LanguageCode.HI) "एआई सहायक" else "AI Sahayak"
    }

    fun stepTitle(language: LanguageCode): String = when (this) {
        SETUP -> if (language == LanguageCode.HI) "उद्यमी प्रोफाइल व सेटअप" else "Enterprise Setup & Profile"
        CALCULATOR -> if (language == LanguageCode.HI) "वित्तीय संरचना व ऋण मूल्यांकन" else "Credit Appraisal & Financials"
        FEASIBILITY -> if (language == LanguageCode.HI) "व्यावसायिक व्यवहार्यता अध्ययन" else "Enterprise Feasibility Study"
        DPR -> if (language == LanguageCode.HI) "बैंक-योग्य विस्तृत परियोजना रिपोर्ट" else "Bankable Project Report (DPR)"
        ADVISOR -> if (language == LanguageCode.HI) "उद्यम सलाहकार (सहायक)" else "Enterprise Advisory (Sahayak)"
    }

    fun icon(): ImageVector = when (this) {
        SETUP -> Icons.Default.Person
        CALCULATOR -> Icons.Default.AccountBalance
        FEASIBILITY -> Icons.Default.Explore
        DPR -> Icons.Default.Description
        ADVISOR -> Icons.Default.SupportAgent
    }
}
