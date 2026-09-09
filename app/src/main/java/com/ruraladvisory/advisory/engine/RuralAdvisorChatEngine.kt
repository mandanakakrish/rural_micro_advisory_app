package com.ruraladvisory.advisory.engine

import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper

/**
 * Intelligent Chat Advisory Engine for rural micro-entrepreneurs,
 * modeled directly on the SCA Sahayak architecture in WEB/server.ts.
 */
object RuralAdvisorChatEngine {

    fun generateWelcomeMessage(
        businessCategory: String,
        location: String,
        schemeName: String,
        loanAmount: Double,
        language: LanguageCode
    ): String {
        val formattedLoan = TextToSpeechHelper.formatIndianCurrency(loanAmount)
        return if (language == LanguageCode.HI) {
            "नमस्ते! " + businessCategory + " (" + location + ") के लिए मैं आपका एआई ग्रामीण सहायक हूँ। चयनित योजना: " + schemeName + " (90% रियायती ऋण: ₹" + formattedLoan + ")। आप सरकारी योजनाओं, सब्सिडी, जनसमर्थ आवेदन, मशीनरी खरीद या किश्त प्रबंधन से संबंधित कोई भी सवाल पूछ सकते हैं।"
        } else {
            "Greetings! I am your AI Rural Sahayak for your " + businessCategory + " enterprise in " + location + ". Selected scheme: " + schemeName + " (90% Concessional Credit: ₹" + formattedLoan + "). Ask me anything about scheme eligibility, machinery sourcing, unit pricing, or repayment planning!"
        }
    }

    fun getSamplePrompts(language: LanguageCode): List<String> {
        return if (language == LanguageCode.HI) {
            listOf(
                "जनसमर्थ पोर्टल पर ऋण आवेदन कैसे करें?",
                "उत्पाद का सही विक्रय मूल्य (Pricing) क्या रखें?",
                "मशीनरी व उपकरण सस्ते में कहाँ से खरीदें?",
                "मोरेटोरियम अवधि में क्या सावधानियां बरतें?"
            )
        } else {
            listOf(
                "How do I apply on JanSamarth portal?",
                "How should I price my products for village markets?",
                "Where can I source reliable machinery?",
                "How does the loan moratorium period work?"
            )
        }
    }

    fun generateFallbackReply(
        message: String,
        category: String,
        location: String,
        marginCapital: Double,
        projectCost: Double,
        schemeName: String,
        moratoriumMonths: Int,
        language: LanguageCode
    ): String {
        val lower = message.lowercase()

        if (language == LanguageCode.HI) {
            return when {
                lower.contains("jansamarth") || lower.contains("आवेदन") || lower.contains("portal") || lower.contains("apply") -> {
                    "जनसमर्थ (jansamarth.in) पर आवेदन करने के लिए: 1) आधार व पैन कार्ड, 2) बैंक पासबुक (पिछले 6 माह), 3) मशीनरी/कच्चे माल का GST कोटेशन, 4) ग्राम प्रधान/नगर पालिका से व्यापार एनओसी। पोर्टल पर 'Livelihood Loan' चुनकर स्टेट चैनलाइजिंग एजेंसी (SCA) या PMEGP का चयन करें और डिजिटल अप्रूवल प्राप्त करें।"
                }
                lower.contains("moratorium") || lower.contains("मोरेटोरियम") || lower.contains("किश्त") || lower.contains("emi") || lower.contains("repay") -> {
                    "मोरेटोरियम अवधि (" + moratoriumMonths + " माह) एक रियायती सुरक्षा कवच है जिसमें आपको मूलधन (Principal) नहीं चुकाना होता। इस दौरान अपनी मशीनरी स्थापित करें और कम से कम 20 नियमित ग्राहकों से संबंध बनाएं। त्रैमासिक किश्त हेतु प्रति माह 1/3 राशि अलग बैंक खाते में जमा करने का नियम बनाएं ताकि किश्त चूक न हो।"
                }
                lower.contains("price") || lower.contains("मूल्य") || lower.contains("रेट") || lower.contains("margin") || lower.contains("उधार") || lower.contains("udhar") -> {
                    "ग्रामीण बाजार में मूल्य निर्धारण का नियम: कुल लागत (कच्चा माल + श्रम + बिजली + भाड़ा) में 20% से 30% ग्रॉस मार्जिन जोड़ें। सबसे महत्वपूर्ण: गांव में अनियंत्रित उधार (Credit) न दें। उधार कुल मासिक बिक्री के 10-15% से अधिक कभी न रखें और 7 से 10 दिन में वसूली सुनिश्चित करें।"
                }
                lower.contains("मशीनरी") || lower.contains("machinery") || lower.contains("उपकरण") || lower.contains("vendor") || lower.contains("supplier") -> {
                    "मशीनरी बिचौलियों से न खरीदें। जिला उद्योग केंद्र (DIC) या नजदीकी उप-जिला थोक मंडी के प्रमाणित निर्माताओं से 3 अलग-अलग GST कोटेशन लें। वारंटी, इंस्टॉलेशन और स्थानीय स्पेयर पार्ट्स की उपलब्धता लिखित में सुनिश्चित करें। SCA ऋण वितरण सीधे मशीनरी विक्रेता के खाते में RTGS से होता है।"
                }
                lower.contains("subsidy") || lower.contains("सब्सिडी") || lower.contains("छूट") || lower.contains("grant") -> {
                    "SCA रियायती ढांचे में आपको केवल 10% प्रमोटर मार्जिन देना होता है, और 90% रियायती ऋण (6.5% - 8.0%) मिलता है। इसके अतिरिक्त, यदि आप PMEGP या PMFME के अंतर्गत पात्र हैं, तो ग्रामीण क्षेत्र में 25% से 35% तक पूंजीगत सब्सिडी (Capital Subsidy) बैंक खाते में 3 वर्ष के लॉक-इन के बाद क्रेडिट की जाती है।"
                }
                else -> {
                    "आपके " + category + " उद्यम (" + location + ") के लिए मुख्य परामर्श: अपने ₹" + TextToSpeechHelper.formatIndianCurrency(marginCapital) + " मार्जिन को सुरक्षित रखें। परियोजना लागत ₹" + TextToSpeechHelper.formatIndianCurrency(projectCost) + " के तहत 60% मशीनरी और 25% कार्यशील पूंजी में लगाएं। नकदी प्रवाह को मजबूत रखने हेतु कम से कम 45 दिनों का परिचालन कैश रिज़र्व बैंक में बनाए रखें।"
                }
            }
        } else {
            return when {
                lower.contains("jansamarth") || lower.contains("apply") || lower.contains("portal") || lower.contains("document") -> {
                    "To apply on JanSamarth (jansamarth.in): 1) Aadhaar & PAN, 2) 6-month bank statement, 3) Vendor machinery quotation with GST, 4) Gram Panchayat trade NOC. Select 'Livelihood Loan', pick State Channelizing Agency (SCA) or PMEGP, and receive instant digital in-principle sanction."
                }
                lower.contains("moratorium") || lower.contains("grace") || lower.contains("emi") || lower.contains("installment") || lower.contains("repay") -> {
                    "The " + moratoriumMonths + "-month moratorium is a grace window where you pay zero principal while your equipment and workspace are commissioned. Use this grace period to build cash reserves. Always deposit 1/3rd of your quarterly installment into a dedicated savings account every month to prevent default."
                }
                lower.contains("price") || lower.contains("pricing") || lower.contains("rate") || lower.contains("udhar") || lower.contains("credit") -> {
                    "Rural pricing formula: Direct Cost (raw stock + labor + power + transport) + 20% to 30% gross markup. Crucial rule: Never sell on unchecked credit (Udhaar). Restrict customer credit to under 15% of monthly sales, and mandate a 7-day collection cycle before extending fresh stock."
                }
                lower.contains("machinery") || lower.contains("equipment") || lower.contains("vendor") || lower.contains("supplier") -> {
                    "Never buy machinery through middlemen. Obtain 3 competing GST quotations directly from certified manufacturers in your nearest industrial hub. Verify on-site warranty, installation support, and local spare parts availability. SCA loan proceeds are disbursed directly to verified vendor accounts via RTGS."
                }
                lower.contains("subsidy") || lower.contains("grant") || lower.contains("scheme") -> {
                    "Under the SCA scheme, you contribute 10% margin equity, and 90% is financed at concessional 6.5% to 8.0% rates. In addition, rural non-farm units applying under PMEGP can receive 25% (General) to 35% (Special/Women/SC/ST/OBC) margin money capital subsidy credited directly to a bank TDR."
                }
                else -> {
                    "Expert guidance for your " + category + " unit in " + location + ": Protect your ₹" + TextToSpeechHelper.formatIndianCurrency(marginCapital) + " margin capital. Allocate 60% of total ₹" + TextToSpeechHelper.formatIndianCurrency(projectCost) + " project cost to reliable assets and 25% to raw material buffer. Always preserve a 45-day operational cash cushion to navigate seasonal demand fluctuations."
                }
            }
        }
    }
}
