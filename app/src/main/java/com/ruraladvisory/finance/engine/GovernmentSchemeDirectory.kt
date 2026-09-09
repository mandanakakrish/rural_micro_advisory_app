package com.ruraladvisory.finance.engine

import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.finance.model.EligibleScheme
import com.ruraladvisory.finance.model.SchemeCategory

/**
 * Directory and matching engine determining which statutory and sector-specific
 * government concessional schemes an entrepreneur is eligible for based on project cost and trade category.
 * Includes complete nodal agency, required documents checklist, and official application process information.
 */
object GovernmentSchemeDirectory {

    fun getEligibleSchemes(
        category: BusinessCategory,
        marginCapital: Double,
        projectCost: Double
    ): List<EligibleScheme> {
        val schemes = mutableListOf<EligibleScheme>()

        // 1. Primary SCA Concessional Scheme (Micro Finance Scheme vs Term Loan Scheme)
        if (projectCost <= SchemeCategory.MICRO_FINANCE.maxProjectCost) {
            schemes.add(
                EligibleScheme(
                    id = "SCA_MICRO_FINANCE",
                    nameEn = "SCA Micro Finance Scheme",
                    nameHi = "एससीए सूक्ष्म वित्त योजना (Micro Finance)",
                    authorityEn = "State Channelizing Agency (SCA / NBCFDC / NSFDC)",
                    authorityHi = "राज्य चैनलाइजिंग एजेंसी (SCA / निगम)",
                    interestRateDescriptionEn = "6.5% p.a. Concessional",
                    interestRateDescriptionHi = "6.5% वार्षिक रियायती ब्याज",
                    maxCeilingEn = "₹1,25,000 (90% Loan to Cost)",
                    maxCeilingHi = "₹1,25,000 (90% रियायती ऋण)",
                    subsidyBenefitEn = "10% Beneficiary Margin Only",
                    subsidyBenefitHi = "केवल 10% लाभार्थी मार्जिन अंशदान",
                    tenureMoratoriumEn = "3 Years (incl. 3-Month Moratorium)",
                    tenureMoratoriumHi = "3 वर्ष (3 महीने की छूट अवधि सहित)",
                    isPrimaryRouted = true,
                    isEligible = true,
                    eligibilityNoteEn = "Primary active scheme: Total project cost (₹) qualifies within the ₹1.40 Lakh threshold.",
                    eligibilityNoteHi = "प्राथमिक सक्रिय योजना: कुल परियोजना लागत (₹) ₹1.40 लाख की सीमा के अंतर्गत आती है।",
                    requiredDocumentsEn = listOf(
                        "Aadhaar Card & Passport Size Photos",
                        "Village Domicile / Residence Proof",
                        "Quotation for Raw Material / Tools",
                        "Bank Account Passbook (Active Savings Account)",
                        "Target Group / Category Certificate (if applicable)"
                    ),
                    requiredDocumentsHi = listOf(
                        "आधार कार्ड एवं पासपोर्ट साइज फोटो",
                        "गाँव का निवास प्रमाण पत्र / पहचान पत्र",
                        "कच्चे माल अथवा औजारों का विक्रेता कोटेशन",
                        "बैंक बचत खाता पासबुक",
                        "जाति / श्रेणी प्रमाण पत्र (यदि लागू हो)"
                    ),
                    applicationProcessEn = "Submit proposal to nearest District Channelizing Agency (SCA) office, District Industries Centre (DIC), or online via JanSamarth portal.",
                    applicationProcessHi = "निकटतम जिला चैनलाइजिंग एजेंसी (SCA) कार्यालय, जिला उद्योग केंद्र (DIC) अथवा जनसमर्थ पोर्टल के माध्यम से आवेदन करें।",
                    portalUrl = "https://www.jansamarth.in"
                )
            )
        } else {
            schemes.add(
                EligibleScheme(
                    id = "SCA_TERM_LOAN",
                    nameEn = "SCA Concessional Term Loan Scheme",
                    nameHi = "एससीए सावधि ऋण योजना (Term Loan)",
                    authorityEn = "State Channelizing Agency (SCA / NBCFDC / NSFDC)",
                    authorityHi = "राज्य चैनलाइजिंग एजेंसी (SCA / निगम)",
                    interestRateDescriptionEn = "8.0% p.a. Concessional",
                    interestRateDescriptionHi = "8.0% वार्षिक रियायती ब्याज",
                    maxCeilingEn = "₹45,00,000 (90% Loan to Cost)",
                    maxCeilingHi = "₹45,00,000 (90% रियायती ऋण)",
                    subsidyBenefitEn = "10% Beneficiary Margin Only",
                    subsidyBenefitHi = "केवल 10% लाभार्थी मार्जिन अंशदान",
                    tenureMoratoriumEn = "7 Years (incl. 6-Month Moratorium)",
                    tenureMoratoriumHi = "7 वर्ष (6 महीने की छूट अवधि सहित)",
                    isPrimaryRouted = true,
                    isEligible = projectCost <= SchemeCategory.TERM_LOAN.maxProjectCost,
                    eligibilityNoteEn = "Primary active scheme: Projects between ₹1.40 Lakh and ₹50.00 Lakh qualify for 7-year extended tenure.",
                    eligibilityNoteHi = "प्राथमिक सक्रिय योजना: ₹1.40 लाख से ₹50.00 लाख के मध्य की परियोजनाएं 7 वर्ष की लंबी अवधि हेतु पात्र हैं।",
                    requiredDocumentsEn = listOf(
                        "Aadhaar Card and PAN Card",
                        "Land Title / Rent Deed / Shed Possession Proof",
                        "Detailed Project Report (DPR) / Cost Estimates",
                        "Authorized Equipment Dealer Machinery Quotation",
                        "Last 6 Months Bank Statement & Credit Score"
                    ),
                    requiredDocumentsHi = listOf(
                        "आधार कार्ड एवं पैन कार्ड",
                        "भूमि स्वामित्व / किरायानामा / शेड पट्टा प्रमाण",
                        "विस्तृत प्रोजेक्ट रिपोर्ट (DPR) एवं लागत अनुमान",
                        "अधिकृत विक्रेता से मशीनरी का पक्का कोटेशन",
                        "पिछले 6 माह का बैंक स्टेटमेंट"
                    ),
                    applicationProcessEn = "Apply via JanSamarth National Portal choosing Term Loan under Backward Classes/Minority Welfare Corporation or through District Lead Bank.",
                    applicationProcessHi = "जनसमर्थ राष्ट्रीय पोर्टल पर सावधि ऋण हेतु ऑनलाइन आवेदन करें अथवा जिला अग्रणी बैंक व निगम कार्यालय में संपर्क करें।",
                    portalUrl = "https://www.jansamarth.in"
                )
            )
        }

        // 2. Sector-Specific Government Scheme Mappings
        when (category) {
            BusinessCategory.DAIRY -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_DAIRY_AHIDF",
                        nameEn = "AHIDF / Dairy Entrepreneurship Development",
                        nameHi = "पशुपालन अवसंरचना विकास निधि (AHIDF / डेयरी योजना)",
                        authorityEn = "Ministry of Fisheries, Animal Husbandry & Dairying / NABARD",
                        authorityHi = "मत्स्य पालन, पशुपालन व डेयरी मंत्रालय / नाबार्ड",
                        interestRateDescriptionEn = "3% Interest Subvention p.a.",
                        interestRateDescriptionHi = "3% ब्याज अनुदान (सबवेंशन) वार्षिक",
                        maxCeilingEn = "Up to 90% Project Cost",
                        maxCeilingHi = "परियोजना लागत का 90% तक",
                        subsidyBenefitEn = "25% (General) / 33.33% (SC/ST/Women) Capital Subsidy",
                        subsidyBenefitHi = "25% (सामान्य) / 33.33% (अनुसूचित जाति/जनजाति/महिला) पूंजीगत सब्सिडी",
                        tenureMoratoriumEn = "Up to 8 Years (incl. up to 2-yr Moratorium)",
                        tenureMoratoriumHi = "8 वर्ष तक (2 वर्ष तक की छूट अवधि सहित)",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Specially eligible for rural milch cattle, bulk milk coolers, and mini dairy production units.",
                        eligibilityNoteHi = "दुधारू पशु क्रय, दूध शीतलन केंद्र व लघु डेयरी स्थापना हेतु विशेष पात्र।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar & PAN of Beneficiary / SHG / FPO",
                            "Cattle Shed Land Ownership / Village Lease Agreement",
                            "Veterinary Doctor Health & Valuation Certificate",
                            "Milk Supply Tie-up / Cooperative Society Passbook",
                            "Detailed Cattle & Feed Cost Estimate"
                        ),
                        requiredDocumentsHi = listOf(
                            "लाभार्थी / समूह / एफपीओ का आधार व पैन कार्ड",
                            "पशु शेड हेतु भूमि खतौनी / ग्राम पंचायत पट्टा",
                            "पशु चिकित्सा अधिकारी द्वारा स्वास्थ्य व मूल्यांकन प्रमाण",
                            "दुग्ध समिति / डेयरी फेडरेशन आपूर्ति अनुबंध",
                            "पशु क्रय व आहार लागत प्राक्कलन"
                        ),
                        applicationProcessEn = "Apply online on AHIDF portal (ahidf.udyamimitra.in) or visit your District Veterinary Office / Lead Bank branch.",
                        applicationProcessHi = "AHIDF पोर्टल (ahidf.udyamimitra.in) पर ऑनलाइन आवेदन करें अथवा जिला पशुपालन अधिकारी से संपर्क करें।",
                        portalUrl = "https://ahidf.udyamimitra.in"
                    )
                )
            }
            BusinessCategory.FOOD_PROCESSING -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_FOOD_PMFME",
                        nameEn = "PM Formalisation of Micro Food Processing (PMFME)",
                        nameHi = "पीएम सूक्ष्म खाद्य प्रसंस्करण उद्यम योजना (PMFME)",
                        authorityEn = "Ministry of Food Processing Industries (MoFPI)",
                        authorityHi = "खाद्य प्रसंस्करण उद्योग मंत्रालय (भारत सरकार)",
                        interestRateDescriptionEn = "Priority Sector Concessional Bank Rates",
                        interestRateDescriptionHi = "प्राथमिकता क्षेत्र रियायती बैंक दर",
                        maxCeilingEn = "35% Credit-Linked Capital Subsidy up to ₹10 Lakh",
                        maxCeilingHi = "35% क्रेडिट लिंक्ड सब्सिडी (अधिकतम ₹10 लाख)",
                        subsidyBenefitEn = "35% Capital Subsidy + ₹40,000 Seed Capital per SHG Member",
                        subsidyBenefitHi = "35% पूंजीगत सब्सिडी + समूह सदस्यों हेतु ₹40,000 सीड कैपिटल",
                        tenureMoratoriumEn = "5 to 7 Years (Bank Aligned)",
                        tenureMoratoriumHi = "5 से 7 वर्ष की अवधि",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Ideal for spice grinding, oil expellers, flour/dal mills, pickle making, and agro processing.",
                        eligibilityNoteHi = "मसाला चक्की, तेल पेराई मिल, आटा/दाल मिल, अचार निर्माण व कृषि प्रसंस्करण हेतु अत्यंत लाभकारी।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar & PAN Card",
                            "Food Business Registration / FSSAI undertaking",
                            "Food Processing Machinery Supplier Quotation",
                            "Unit Location Electricity Bill / Rent Agreement",
                            "Bank Statement (Last 6 Months)"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड एवं पैन कार्ड",
                            "खाद्य व्यवसाय पंजीकरण / FSSAI शपथ-पत्र",
                            "खाद्य प्रसंस्करण मशीनरी आपूर्तिकर्ता का कोटेशन",
                            "इकाई स्थल का बिजली बिल / किराया अनुबंध",
                            "बैंक खाता विवरण (पिछले 6 माह)"
                        ),
                        applicationProcessEn = "Register on PMFME portal (pmfme.mofpi.gov.in) with free guidance from your local District Resource Person (DRP).",
                        applicationProcessHi = "PMFME पोर्टल (pmfme.mofpi.gov.in) पर जिला संसाधन व्यक्ति (DRP) के निःशुल्क सहयोग से ऑनलाइन आवेदन करें।",
                        portalUrl = "https://pmfme.mofpi.gov.in"
                    )
                )
            }
            BusinessCategory.TEXTILES -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_TEXTILE_VISHWAKARMA",
                        nameEn = "PM Vishwakarma Scheme (Tailors & Weavers)",
                        nameHi = "प्रधानमंत्री विश्वकर्मा योजना (दर्जी व बुनकर)",
                        authorityEn = "Ministry of Micro, Small & Medium Enterprises (MSME)",
                        authorityHi = "सूक्ष्म, लघु एवं मध्यम उद्यम मंत्रालय (MSME)",
                        interestRateDescriptionEn = "5.0% Subsidized Concessional Interest",
                        interestRateDescriptionHi = "5.0% रियायती ब्याज दर (भारत सरकार द्वारा सबवेंशन)",
                        maxCeilingEn = "Tranche 1: ₹1 Lakh, Tranche 2: ₹2 Lakh",
                        maxCeilingHi = "प्रथम चरण: ₹1 लाख, द्वितीय चरण: ₹2 लाख",
                        subsidyBenefitEn = "₹15,000 Free Toolkit Grant + ₹500/day Training Stipend",
                        subsidyBenefitHi = "₹15,000 निःशुल्क टूलकिट ग्रांट + ₹500/दिन प्रशिक्षण भत्ता",
                        tenureMoratoriumEn = "18 to 30 Months (Collateral-Free)",
                        tenureMoratoriumHi = "18 से 30 महीने (बिना किसी गारंटी/बंधक के)",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Covers tailoring, modern sewing machinery, handlooms, and rural garment production.",
                        eligibilityNoteHi = "सिलाई, आधुनिक सिलाई मशीन, हथकरघा व वस्त्र निर्माण कार्य हेतु पूर्णतः पात्र।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar Card with Mobile Linkage",
                            "Active Bank Account Passbook",
                            "Traditional Tailor / Weaver Trade Self-Declaration",
                            "Ration Card / Family ID"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड (मोबाइल नंबर से लिंक)",
                            "सक्रिय बैंक खाता पासबुक",
                            "दर्जी / बुनकर पारंपरिक कार्य का स्व-प्रमाणन",
                            "राशन कार्ड / परिवार पहचान पत्र"
                        ),
                        applicationProcessEn = "Enroll through your local Common Services Centre (CSC) or Village Gram Panchayat on pmvishwakarma.gov.in.",
                        applicationProcessHi = "ग्राम पंचायत अथवा सीएससी (CSC केंद्र) पर जाकर pmvishwakarma.gov.in पर निःशुल्क बायोमेट्रिक पंजीकरण कराएं।",
                        portalUrl = "https://pmvishwakarma.gov.in"
                    )
                )
            }
            BusinessCategory.POULTRY -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_POULTRY_NLM",
                        nameEn = "National Livestock Mission (NLM - Poultry)",
                        nameHi = "राष्ट्रीय पशुधन मिशन (NLM - कुक्कुट पालन)",
                        authorityEn = "Department of Animal Husbandry & Dairying (DAHD)",
                        authorityHi = "पशुपालन और डेयरी विभाग (भारत सरकार)",
                        interestRateDescriptionEn = "Priority Sector Lending Rates",
                        interestRateDescriptionHi = "प्राथमिकता प्राप्त क्षेत्र बैंक ब्याज दर",
                        maxCeilingEn = "Up to ₹25.00 Lakh Project Cost",
                        maxCeilingHi = "₹25.00 लाख तक की परियोजना लागत",
                        subsidyBenefitEn = "Up to 50% Capital Subsidy (Direct Benefit Transfer)",
                        subsidyBenefitHi = "50% तक पूंजीगत सब्सिडी (डीबीटी के माध्यम से)",
                        tenureMoratoriumEn = "5 to 7 Years (incl. 1-Year Moratorium)",
                        tenureMoratoriumHi = "5 से 7 वर्ष (1 वर्ष की छूट अवधि सहित)",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Available for rural backyard poultry, broiler farming, hatcheries, and feed processing.",
                        eligibilityNoteHi = "ग्रामीण कुक्कुट पालन, ब्रायलर फार्म, हैचरी व दाना निर्माण इकाई हेतु मान्य।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar & PAN Card",
                            "Poultry Shed Land Possession Document",
                            "Poultry Equipment & Day-Old Chicks Quotation",
                            "Training Certificate from KVK / Animal Husbandry Dept",
                            "Water & Bio-security Clearance Undertaking"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड एवं पैन कार्ड",
                            "मुर्गी फार्म शेड हेतु जमीन का दस्तावेज",
                            "उपकरण व चूजा (Day-Old Chicks) खरीद कोटेशन",
                            "कृषि विज्ञान केंद्र (KVK) अथवा पशुपालन विभाग से प्रशिक्षण प्रमाण",
                            "स्वच्छ जल व जैव-सुरक्षा शपथ पत्र"
                        ),
                        applicationProcessEn = "Submit application on NLM portal (nlm.udyamimitra.in) for state-level committee approval.",
                        applicationProcessHi = "NLM पोर्टल (nlm.udyamimitra.in) पर ऑनलाइन आवेदन कर जिला पशु चिकित्सालय से अग्रेषित कराएं।",
                        portalUrl = "https://nlm.udyamimitra.in"
                    )
                )
            }
            BusinessCategory.AGRO_SERVICES -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_AGRO_SMAM",
                        nameEn = "Sub-Mission on Agri Mechanization (SMAM / CHC)",
                        nameHi = "कृषि यंत्रीकरण उप-मिशन (SMAM - कस्टम हायरिंग केंद्र)",
                        authorityEn = "Ministry of Agriculture & Farmers Welfare",
                        authorityHi = "कृषि एवं किसान कल्याण मंत्रालय",
                        interestRateDescriptionEn = "7.0% - 8.5% Concessional Agriculture Loan",
                        interestRateDescriptionHi = "7.0% - 8.5% रियायती कृषि ऋण दर",
                        maxCeilingEn = "Up to ₹10.00 Lakh per Custom Hiring Centre",
                        maxCeilingHi = "कस्टम हायरिंग केंद्र हेतु ₹10.00 लाख तक",
                        subsidyBenefitEn = "40% to 50% Subsidy on Machinery & Equipment",
                        subsidyBenefitHi = "कृषि यंत्रों व उपकरणों पर 40% से 50% तक की सीधी सब्सिडी",
                        tenureMoratoriumEn = "5 to 7 Years Tenure",
                        tenureMoratoriumHi = "5 से 7 वर्ष की पुनर्भुगतान अवधि",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Covers tractor attachments, solar irrigation pumps, seed drills, and power tillers for hiring.",
                        eligibilityNoteHi = "किराये पर देने हेतु ट्रैक्टर उपकरण, सौर पंप, रोटावेटर व थ्रेशर खरीद के लिए अनुमन्य।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar Card and Farmer ID / Khasra Khatauni",
                            "Agricultural Machinery Registered Dealer Quotation",
                            "Bank Account Passbook (DBT-enabled)",
                            "Tractor Registration Certificate (RC) for mounted implements"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड एवं किसान पंजीकरण / खसरा-खतौनी",
                            "पंजीकृत कृषि यंत्र डीलर से बिल/कोटेशन",
                            "डीबीटी-सक्षम बैंक खाता पासबुक",
                            "ट्रैक्टर चालित यंत्रों हेतु ट्रैक्टर आरसी की प्रति"
                        ),
                        applicationProcessEn = "Register on Direct Benefit Transfer in Agriculture Mechanization portal (agrimachinery.nic.in).",
                        applicationProcessHi = "कृषि यंत्रीकरण डीबीटी पोर्टल (agrimachinery.nic.in) पर ऑनलाइन टोकन प्राप्त कर आवेदन करें।",
                        portalUrl = "https://agrimachinery.nic.in"
                    )
                )
            }
            BusinessCategory.HANDICRAFTS -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_CRAFT_VISHWAKARMA",
                        nameEn = "PM Vishwakarma / Ambedkar Hastshilp Yojana",
                        nameHi = "पीएम विश्वकर्मा व अंबेडकर हस्तशिल्प विकास योजना",
                        authorityEn = "Ministry of Textiles / Development Commissioner (Handicrafts)",
                        authorityHi = "वस्त्र मंत्रालय / विकास आयुक्त (हस्तशिल्प)",
                        interestRateDescriptionEn = "5.0% Subsidized Concessional Interest",
                        interestRateDescriptionHi = "5.0% रियायती ब्याज दर",
                        maxCeilingEn = "Up to ₹3.00 Lakh (Collateral-Free)",
                        maxCeilingHi = "₹3.00 लाख तक (बिना किसी गारंटी के)",
                        subsidyBenefitEn = "₹15,000 Digital Toolkit Grant + Marketing Support",
                        subsidyBenefitHi = "₹15,000 टूलकिट अनुदान + विपणन व मेला सहायता",
                        tenureMoratoriumEn = "3 to 5 Years",
                        tenureMoratoriumHi = "3 से 5 वर्ष की अवधि",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Tailored for clay pottery, terracotta, bamboo/cane products, metal crafts, and rural artisans.",
                        eligibilityNoteHi = "मिट्टी के बर्तन, टेराकोटा, बांस/बेंत शिल्प, मूर्तिकला व ग्रामीण दस्तकारों हेतु समर्पित।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar Card and Mobile Number",
                            "Pehchan Artisan Card / DC Handicrafts ID (if available)",
                            "Bank Account Passbook",
                            "Craft Sample Photographs and Workshop Details"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड एवं मोबाइल नंबर",
                            "पहचान दस्तकार कार्ड (यदि उपलब्ध हो)",
                            "बैंक खाता पासबुक",
                            "हस्तशिल्प उत्पाद के फोटो एवं कार्यशाला विवरण"
                        ),
                        applicationProcessEn = "Apply via PM Vishwakarma portal or DC Handicrafts Service Centre in your district.",
                        applicationProcessHi = "पीएम विश्वकर्मा पोर्टल अथवा जिला हस्तशिल्प सेवा केंद्र के माध्यम से आवेदन करें।",
                        portalUrl = "https://pmvishwakarma.gov.in"
                    )
                )
            }
            BusinessCategory.RETAIL -> {
                schemes.add(
                    EligibleScheme(
                        id = "SECTOR_RETAIL_MUDRA",
                        nameEn = "Pradhan Mantri MUDRA Yojana (PMMY)",
                        nameHi = "प्रधानमंत्री मुद्रा योजना (PMMY)",
                        authorityEn = "MUDRA / Department of Financial Services (DFS)",
                        authorityHi = "मुद्रा / वित्तीय सेवाएं विभाग (भारत सरकार)",
                        interestRateDescriptionEn = "8.0% - 9.5% Priority Sector Lending",
                        interestRateDescriptionHi = "8.0% - 9.5% प्राथमिकता प्राप्त क्षेत्र ब्याज दर",
                        maxCeilingEn = "Up to ₹10.00 Lakh (No Collateral Required)",
                        maxCeilingHi = "₹10.00 लाख तक (बिना किसी जमानत/बंधक के)",
                        subsidyBenefitEn = "Zero Collateral + MUDRA RuPay Card Working Capital",
                        subsidyBenefitHi = "शून्य बंधक आवश्यकता + मुद्रा रुपे कार्ड कार्यशील पूंजी",
                        tenureMoratoriumEn = "3 to 5 Years Flexible Repayment",
                        tenureMoratoriumHi = "3 से 5 वर्ष का सुविधाजनक पुनर्भुगतान",
                        isPrimaryRouted = false,
                        isEligible = true,
                        eligibilityNoteEn = "Ideal for village grocery stores, kirana shops, hardware, mobile recharge, and retail outlets.",
                        eligibilityNoteHi = "ग्रामीण किराना दुकान, जनरल स्टोर, खाद-बीज रिटेल व दैनिक उपभोग सामग्री विक्रय हेतु।",
                        requiredDocumentsEn = listOf(
                            "Aadhaar Card and PAN Card",
                            "Shop Establishment Certificate / Gram Panchayat NOC",
                            "Stock and Inventory Purchase Quotations",
                            "Last 6 Months Bank Statement"
                        ),
                        requiredDocumentsHi = listOf(
                            "आधार कार्ड एवं पैन कार्ड",
                            "दुकान स्थापना / ग्राम पंचायत अनापत्ति प्रमाण",
                            "दुकान सामग्री / स्टॉक खरीद कोटेशन",
                            "पिछले 6 माह का बैंक खाता विवरण"
                        ),
                        applicationProcessEn = "Apply directly at any commercial bank, regional rural bank (RRB), or via UdyamiMitra / JanSamarth portal.",
                        applicationProcessHi = "किसी भी ग्रामीण बैंक, कमर्शियल बैंक शाखा अथवा जनसमर्थ / उद्यमीमित्र पोर्टल पर सीधे आवेदन करें।",
                        portalUrl = "https://www.udyamimitra.in"
                    )
                )
            }
        }

        // 3. Flagship National Rural Entrepreneurship Scheme (PMEGP)
        schemes.add(
            EligibleScheme(
                id = "NATIONAL_PMEGP",
                nameEn = "Prime Minister's Employment Generation Programme (PMEGP)",
                nameHi = "प्रधानमंत्री रोजगार सृजन कार्यक्रम (PMEGP)",
                authorityEn = "Khadi and Village Industries Commission (KVIC) / MSME",
                authorityHi = "खादी एवं ग्रामोद्योग आयोग (KVIC) / एमएसएमई",
                interestRateDescriptionEn = "Normal Commercial Bank Lending Rate",
                interestRateDescriptionHi = "सामान्य बैंक ऋण दर",
                maxCeilingEn = "Manufacturing: ₹50 Lakh | Service: ₹20 Lakh",
                maxCeilingHi = "विनिर्माण: ₹50 लाख | सेवा क्षेत्र: ₹20 लाख",
                subsidyBenefitEn = "25% (General Rural) / 35% (SC/ST/OBC/Women Rural) Margin Subsidy",
                subsidyBenefitHi = "25% (सामान्य ग्रामीण) / 35% (आरक्षित/महिला ग्रामीण) मार्जिन सब्सिडी",
                tenureMoratoriumEn = "3 to 7 Years (Bank Aligned)",
                tenureMoratoriumHi = "3 से 7 वर्ष की अवधि",
                isPrimaryRouted = false,
                isEligible = projectCost <= 5000000.0,
                eligibilityNoteEn = "National flagship scheme providing heavy capital subsidies for rural non-farm enterprises.",
                eligibilityNoteHi = "ग्रामीण क्षेत्रों में गैर-कृषि उद्यम शुरू करने हेतु भारी पूंजीगत सब्सिडी वाली प्रमुख योजना।",
                requiredDocumentsEn = listOf(
                    "Aadhaar Card and PAN Card",
                    "Educational Qualification Certificate (8th pass for >₹10L)",
                    "Special Category / Rural Area Domicile Certificate",
                    "Detailed Project Report (DPR) with Machinery Quotation",
                    "EDP Training Completion Certificate (Post sanction)"
                ),
                requiredDocumentsHi = listOf(
                    "आधार कार्ड एवं पैन कार्ड",
                    "शैक्षणिक योग्यता प्रमाण पत्र (10 लाख से अधिक हेतु न्यूनतम 8वीं पास)",
                    "ग्रामीण क्षेत्र निवास व आरक्षण श्रेणी प्रमाण पत्र",
                    "विस्तृत प्रोजेक्ट रिपोर्ट (DPR) मय मशीनरी कोटेशन",
                    "ईडीपी उद्यमिता प्रशिक्षण प्रमाण पत्र (स्वीकृति उपरांत)"
                ),
                applicationProcessEn = "Apply online on KVIC e-portal (kviconline.gov.in) selecting District Industries Centre (DIC) as implementing agency.",
                applicationProcessHi = "केवीआईसी ई-पोर्टल (kviconline.gov.in) पर जिला उद्योग केंद्र (DIC) एजेंसी चुनकर ऑनलाइन आवेदन करें।",
                portalUrl = "https://www.kviconline.gov.in"
            )
        )

        return schemes
    }
}
