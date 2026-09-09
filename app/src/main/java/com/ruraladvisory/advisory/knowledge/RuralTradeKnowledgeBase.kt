package com.ruraladvisory.advisory.knowledge

import com.ruraladvisory.advisory.model.BudgetTier
import com.ruraladvisory.advisory.model.BusinessCategory
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.advisory.model.LocationInfo
import com.ruraladvisory.advisory.model.SwotAnalysis

/**
 * Rich domain knowledge heuristics for all 7 rural business categories
 * calibrated across 3 budget tiers (Micro, Small, Medium) in both English and Hindi.
 */
object RuralTradeKnowledgeBase {

    data class SwotPack(
        val strengths: List<String>,
        val weaknesses: List<String>,
        val opportunities: List<String>,
        val threats: List<String>
    )

    data class ThreatVectorItem(
        val vectorKey: String,
        val threatEn: String,
        val threatHi: String,
        val mitigationEn: String,
        val mitigationHi: String
    )

    data class CategoryHeuristic(
        val category: BusinessCategory,
        val radiusEn: String,
        val radiusHi: String,
        val primaryChannelsEn: List<String>,
        val primaryChannelsHi: List<String>,
        val consumerBaseByTierEn: Map<BudgetTier, String>,
        val consumerBaseByTierHi: Map<BudgetTier, String>,
        val opportunityNicheEn: String,
        val opportunityNicheHi: String,
        val unservedSegmentsEn: List<String>,
        val unservedSegmentsHi: List<String>,
        val swotByTierEn: Map<BudgetTier, SwotPack>,
        val swotByTierHi: Map<BudgetTier, SwotPack>,
        val threats: List<ThreatVectorItem>,
        val competitorDensityEn: String,
        val competitorDensityHi: String,
        val competitorProfileEn: String,
        val competitorProfileHi: String,
        val competitiveMoatEn: String,
        val competitiveMoatHi: String,
        val benchmarkPricingEn: List<String>,
        val benchmarkPricingHi: List<String>,
        val grossMarginRangeEn: String,
        val grossMarginRangeHi: String,
        val recommendationsByTierEn: Map<BudgetTier, List<String>>,
        val recommendationsByTierHi: Map<BudgetTier, List<String>>
    )

    private val heuristics: Map<BusinessCategory, CategoryHeuristic> = mapOf(
        BusinessCategory.DAIRY to CategoryHeuristic(
            category = BusinessCategory.DAIRY,
            radiusEn = "5 to 10 km radius covering dairy collection routes and local village clusters",
            radiusHi = "5 से 10 किमी का दायरा जिसमें दुग्ध संकलन मार्ग एवं आसपास के ग्रामीण मजरे शामिल हैं",
            primaryChannelsEn = listOf(
                "Village Dairy Cooperative Societies (DCS/Amul/Mother Dairy)",
                "Local village tea shops and halwais (sweet makers)",
                "Direct morning household delivery in village center",
                "Weekly regional livestock haats"
            ),
            primaryChannelsHi = listOf(
                "ग्राम दुग्ध सहकारी समितियां (DCS / अमूल / सुधा / मदर डेयरी)",
                "स्थानीय चाय की दुकानें एवं मिष्ठान्न भंडार (हलवाई)",
                "गाँव के केंद्र में सुबह-सुबह घर-घर सीधी दूध आपूर्ति",
                "साप्ताहिक क्षेत्रीय पशु हाट बाजार"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 2,500 to 4,000 residents across 2-3 adjoining hamlets",
                BudgetTier.SMALL to "Estimated 8,000 to 15,000 residents across 5-8 villages",
                BudgetTier.MEDIUM to "Estimated 25,000 to 45,000 residents across the entire block"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "2-3 नजदीकी मजरों में लगभग 2,500 से 4,000 ग्रामीण उपभोक्ता",
                BudgetTier.SMALL to "5-8 गांवों में लगभग 8,000 से 15,000 संभावित उपभोक्ता",
                BudgetTier.MEDIUM to "पूरे प्रखंड में लगभग 25,000 से 45,000 उपभोक्ताओं का विशाल बाजार"
            ),
            opportunityNicheEn = "Production of value-added Desi Ghee, Paneer, and spiced Buttermilk yielding 35-45% profit margins versus 12-15% on raw milk.",
            opportunityNicheHi = "कच्चे दूध की जगह शुद्ध देसी घी, ताजा पनीर एवं छाछ निर्माण, जिससे 12-15% के बजाय 35-45% तक शुद्ध लाभ प्राप्त होता है।",
            unservedSegmentsEn = listOf(
                "Premium A2 indigenous cow milk direct subscriptions",
                "Fresh daily paneer supply to village wedding caterers",
                "Hygienically packaged curd and buttermilk for weekly haats"
            ),
            unservedSegmentsHi = listOf(
                "शुद्ध देशी ए2 गाय के दूध की सीधी मासिक आपूर्ति",
                "ग्रामीण वैवाहिक आयोजनों हेतु ताजा पनीर का थोक ऑर्डर",
                "साप्ताहिक हाट बाजारों में स्वच्छ छाछ व दही की बिक्री"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Immediate daily morning cash inflow from milk sales.",
                        "Utilizes household labor and free farm crop residues as fodder."
                    ),
                    weaknesses = listOf(
                        "Absence of on-farm chilling creates evening milk spoilage risk.",
                        "Vulnerable to sudden cattle illness without immediate vet access."
                    ),
                    opportunities = listOf(
                        "Direct tie-up with local village sweet shops for higher per-liter realization.",
                        "Kisan Credit Card (KCC) animal husbandry subsidized working capital loans."
                    ),
                    threats = listOf(
                        "Severe green fodder shortage during dry summer months.",
                        "Unplanned cattle dry lactation period causing temporary income pause."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "Herd of 4 to 8 milch cattle provides consistent daily milk volume.",
                        "Mechanized chaff cutter reduces labor requirements and feed wastage."
                    ),
                    weaknesses = listOf(
                        "Higher initial working capital locked in cattle concentrates and minerals.",
                        "Dependency on continuous power for mechanized milking and chaffing."
                    ),
                    opportunities = listOf(
                        "On-site conversion of surplus milk into high-margin paneer and curd.",
                        "Biogas plant integration to offset household cooking fuel expenses."
                    ),
                    threats = listOf(
                        "Commercial cattle feed price spikes eroding monthly margins.",
                        "Local mastitis outbreak risk requiring strict teat-dip hygiene."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "15+ cattle commercial dairy with bulk milk cooler (BMC 500L).",
                        "Bulk bargaining power on concentrated feeds and veterinary medicines."
                    ),
                    weaknesses = listOf(
                        "Substantial debt servicing obligation requiring strict financial discipline.",
                        "Management complexity in supervising external farm labor."
                    ),
                    opportunities = listOf(
                        "Branded paneer and bottled pasteurized milk distribution in nearby town.",
                        "Organic vermicompost packaging from dairy manure for local farmers."
                    ),
                    threats = listOf(
                        "Price undercutting by unorganized milk vendors in urban fringes.",
                        "Stringent regulatory milk quality and fat testing penalties."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "दूध की बिक्री से प्रतिदिन सुबह नकद आय की प्राप्ति।",
                        "पारिवारिक श्रम एवं खेतों के अवशेषों से मुफ्त चारे की उपलब्धता।"
                    ),
                    weaknesses = listOf(
                        "शीतलन (चिलिंग) सुविधा न होने से शाम के दूध के खराब होने का जोखिम।",
                        "पशु चिकित्सक की तत्काल अनुपलब्धता में मवेशी की बीमारी का खतरा।"
                    ),
                    opportunities = listOf(
                        "स्थानीय हलवाई से सीधा अनुबंध कर प्रति लीटर अधिक मूल्य प्राप्त करना।",
                        "पशुपालन केसीसी (KCC) योजना के तहत रियायती ब्याज दर पर ऋण सुविधा।"
                    ),
                    threats = listOf(
                        "मई-जून की भीषण गर्मी में हरे चारे और सूखे भूसे की भारी कमी।",
                        "मवेशी के दूध न देने की अवधि (ड्राई पीरियड) में आय का अस्थायी रूप से रुकना।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "4 से 8 दुधारू पशुओं के समूह से पर्याप्त दैनिक दुग्ध उत्पादन।",
                        "मोटर चालित कुट्टी मशीन द्वारा चारे की बर्बादी और श्रम में कमी।"
                    ),
                    weaknesses = listOf(
                        "पशु आहार एवं खनिज मिश्रण की खरीद में अधिक कार्यशील पूंजी की आवश्यकता।",
                        "मशीनरी चलाने हेतु नियमित बिजली आपूर्ति पर निर्भरता।"
                    ),
                    opportunities = listOf(
                        "अतिरिक्त दूध से पनीर और खोया बनाकर दोगुना मुनाफा कमाने का अवसर।",
                        "गोबर गैस (बायोगैस) संयंत्र लगाकर घरेलू ईंधन खर्च में बचत।"
                    ),
                    threats = listOf(
                        "खली, चूरी और संतुलित पशुआहार की कीमतों में अचानक वृद्धि।",
                        "थनैला रोग (Mastitis) जैसी संक्रामक बीमारियों से उत्पादन घटने का डर।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "15+ पशुओं की आधुनिक डेयरी एवं बल्क मिल्क कूलर (BMC) से सुरक्षित भंडारण।",
                        "पशु आहार और दवाओं की थोक खरीद पर विशेष छूट का लाभ।"
                    ),
                    weaknesses = listOf(
                        "मासिक ऋण ईएमआई (EMI) का बड़ा दायित्व और समय पर भुगतान का दबाव।",
                        "बाहरी श्रमिकों के प्रबंधन और लगातार निगरानी की चुनौती।"
                    ),
                    opportunities = listOf(
                        "निकटवर्ती कस्बे में अपने ब्रांड का शुद्ध पनीर और घी की सीधी पैकिंग बिक्री।",
                        "डेयरी के गोबर से उच्च गुणवत्ता वाली केंचुआ खाद (वर्मीकम्पोस्ट) का उत्पादन।"
                    ),
                    threats = listOf(
                        "असंगठित दूधियों द्वारा मिलावटी दूध बेचकर मूल्य में अनुचित प्रतिस्पर्धा।",
                        "सहकारी समितियों में फैट/एसएनएफ परीक्षण में उतार-चढ़ाव से दर में कटौती।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Sharp milk production drop and fodder price escalation during peak summer months.",
                    threatHi = "भीषण गर्मी में पशुओं के दूध उत्पादन में गिरावट एवं चारे की कीमतों में तेजी।",
                    mitigationEn = "Prepare silage bags post-harvest and grow drought-resistant green fodder (hybrid napier).",
                    mitigationHi = "कटाई के बाद साइलेज (आचार घास) तैयार करें और नेपियर जैसी सूखा-प्रतिरोधी घास लगाएं।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Milk spoilage during transport over bad rural roads under hot ambient temperatures.",
                    threatHi = "खराब ग्रामीण सड़कों और तेज गर्मी में परिवहन के दौरान दूध के फटने का डर।",
                    mitigationEn = "Use stainless steel insulated transport cans and deliver within 90 minutes of milking.",
                    mitigationHi = "स्टेनलेस स्टील इंसुलेटेड मिल्क कैन का उपयोग करें और दोहन के 90 मिनट में डिलीवरी करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Sudden escalation in prices of mustard cake (khal), wheat bran, and cattle mineral mixture.",
                    threatHi = "सरसों की खली, चोकर और मिनरल मिक्सचर के दामों में अचानक भारी उछाल।",
                    mitigationEn = "Purchase 3 months of dry fodder during harvest directly from farmers; join a dairy FPO for feed discounts.",
                    mitigationHi = "कटाई के समय सीधे किसानों से 3 महीने का भूसा भंडारित करें और एफपीओ से थोक आहार खरीदें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Excessive reliance on a single village middleman (doodhiya) who delays payments and imposes arbitrary deductions.",
                    threatHi = "एक ही स्थानीय दूधिए पर निर्भरता, जो भुगतान में देरी और मनमानी कटौती करता है।",
                    mitigationEn = "Split milk distribution: 60% to organized dairy cooperative, 30% to retail halwais, 10% cash retail.",
                    mitigationHi = "दूध का बंटवारा करें: 60% सहकारी समिति को, 30% स्थानीय हलवाई को और 10% नकद खुदरा बिक्री।"
                )
            ),
            competitorDensityEn = "Moderate density across the block with scattered traditional household rearers.",
            competitorDensityHi = "प्रखंड स्तर पर मध्यम प्रतिस्पर्धा; अधिकांश ग्रामीण पारंपरिक घरेलू स्तर पर पशु पालते हैं।",
            competitorProfileEn = "Traditional smallholders with 1-2 non-descript cows selling raw unchilled milk to roving middlemen.",
            competitorProfileHi = "पारंपरिक पशुपालक जिनके पास 1-2 मवेशी हैं और वे बिना जांच के दूधियों को सस्ता दूध बेचते हैं।",
            competitiveMoatEn = "Transparent digital lactometer fat/SNF testing, certified disease-free herd, and strict morning delivery timing.",
            competitiveMoatHi = "पारदर्शी डिजिटल फैट/एसएनएफ परीक्षण, स्वच्छ पशु आवास एवं समय पर शुद्ध दूध पहुंचाने का अटूट भरोसा।",
            benchmarkPricingEn = listOf(
                "Fresh Cow Milk: ₹50 - ₹56 per liter (approx. 18-22% gross margin)",
                "Buffalo Milk (6%+ Fat): ₹64 - ₹72 per liter (approx. 22-26% gross margin)",
                "Fresh Country Paneer: ₹380 - ₹440 per kg (approx. 35-40% gross margin)",
                "Pure Desi Cow Ghee: ₹750 - ₹950 per kg (approx. 40-45% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "शुद्ध गाय का दूध: ₹50 - ₹56 प्रति लीटर (लगभग 18-22% सकल मुनाफा)",
                "भैंस का दूध (6%+ फैट): ₹64 - ₹72 प्रति लीटर (लगभग 22-26% सकल मुनाफा)",
                "ताजा देशी पनीर: ₹380 - ₹440 प्रति किलो (लगभग 35-40% सकल मुनाफा)",
                "शुद्ध देशी गाय का घी: ₹750 - ₹950 प्रति किलो (लगभग 40-45% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "18% to 45% depending on product mix (raw milk vs value-added dairy)",
            grossMarginRangeHi = "18% से 45% (कच्चे दूध की तुलना में मूल्य संवर्धित उत्पादों पर अधिक लाभ)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Begin with 2 high-yielding crossbred cows/Murrah buffaloes with verified lactation history.",
                    "Ensure comprehensive livestock insurance and enter the local Dairy Cooperative Society on day one.",
                    "Limit customer informal credit to under ₹1,000 total and settle on weekly Sunday cycles."
                ),
                BudgetTier.SMALL to listOf(
                    "Invest in a motorized chaff cutter and hygienic stainless steel milk cans.",
                    "Allocate 25% of daily milk to on-site paneer preparation for weekend village markets.",
                    "Secure a Kisan Credit Card to maintain a ₹30,000 cattle feed emergency buffer."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Install a 500-liter Bulk Milk Cooler (BMC) to eliminate milk curdling risks.",
                    "Form a direct supply contract with institutional sweet shops and tea chains in the nearby tehsil.",
                    "Establish a silage bunker to store 6 months of green fodder ahead of peak summer."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "प्रमाणित दुग्ध इतिहास वाली 2 उच्च नस्ल की गाय/मुर्रा भैंस से शुरुआत करें।",
                    "पहले ही दिन पशु बीमा करवाएं और स्थानीय दुग्ध सहकारी समिति के सदस्य बनें।",
                    "उधारी को सख्त रूप से ₹1,000 से कम रखें और प्रत्येक रविवार को भुगतान चुकता कराएं।"
                ),
                BudgetTier.SMALL to listOf(
                    "मोटर चालित कुट्टी मशीन और स्वच्छ स्टेनलेस स्टील के बर्तनों में निवेश करें।",
                    "सप्ताहांत के हाट बाजार हेतु दैनिक दूध के 25% भाग से ताजा पनीर बनाएं।",
                    "पशुआहार की अग्रिम खरीद हेतु केसीसी (KCC) सीमा का उपयोग करें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "दूध के फटने के नुकसान को शून्य करने हेतु 500 लीटर का बल्क मिल्क कूलर लगाएं।",
                    "निकटवर्ती कस्बे के प्रतिष्ठित हलवाइयों और मिष्ठान्न भंडारों से सीधा आपूर्ति अनुबंध करें।",
                    "गर्मी से पहले 6 महीने का हरा चारा संरक्षित रखने हेतु साइलेज बंकर का निर्माण करें।"
                )
            )
        ),

        BusinessCategory.RETAIL to CategoryHeuristic(
            category = BusinessCategory.RETAIL,
            radiusEn = "3 to 8 km footfall radius covering village center, chauraha, and adjoining agricultural hamlets",
            radiusHi = "3 से 8 किमी का दायरा जिसमें गाँव का मुख्य चौराहा एवं आसपास के खेत-मजरे शामिल हैं",
            primaryChannelsEn = listOf(
                "Front-counter storefront at village central junction (Chowk)",
                "Weekly rotating village haat stall",
                "Home delivery to elderly and harvesting farmers on tractor trails",
                "Self-Help Group (SHG) bulk grocery distribution"
            ),
            primaryChannelsHi = listOf(
                "गाँव के मुख्य चौराहे/चौक पर स्थायी खुदरा दुकान",
                "साप्ताहिक ग्रामीण हाट बाजार में स्टॉल",
                "बुजुर्गों एवं खेतों में काम कर रहे किसानों को होम डिलीवरी",
                "महिला स्वयं सहायता समूहों को थोक किराना आपूर्ति"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 1,800 to 3,500 daily residents within 2 km walking radius",
                BudgetTier.SMALL to "Estimated 6,000 to 12,000 consumers across 4-6 connected villages",
                BudgetTier.MEDIUM to "Estimated 18,000 to 35,000 consumers acting as a mini-stockist"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "पैदल दूरी (2 किमी) के भीतर लगभग 1,800 से 3,500 दैनिक निवासी",
                BudgetTier.SMALL to "4-6 जुड़े गांवों के लगभग 6,000 से 12,000 नियमित ग्राहक",
                BudgetTier.MEDIUM to "18,000 से 35,000 उपभोक्ताओं हेतु मिनी-थोक भंडार"
            ),
            opportunityNicheEn = "Micro-sachet inventory (₹5 & ₹10 SKUs) tailored for daily wage earners, combined with digital Aadhaar cash-out (AePS) footfall magnet.",
            opportunityNicheHi = "दैनिक मजदूरी वाले परिवारों हेतु ₹5 और ₹10 के छोटे पाउच का भरपूर स्टॉक, साथ ही आधार सक्षम नकद निकासी (AePS) की सुविधा।",
            unservedSegmentsEn = listOf(
                "High-margin packaged dry fruits and festive gift hampers during Diwali and Eid",
                "Cold beverages and ice creams using deep freezer backup",
                "Digital utility bill payment and mobile recharge desk"
            ),
            unservedSegmentsHi = listOf(
                "त्योहारों (दीवाली, ईद, शादी) पर ड्राई फ्रूट्स और आकर्षक गिफ्ट पैक",
                "डीप फ्रीजर बैकअप के साथ ठंडे पेय पदार्थ एवं आइसक्रीम",
                "बिजली बिल भुगतान एवं मोबाइल रिचार्ज की डिजिटल सुविधा"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Central village location commands captive footfall for daily emergency staples.",
                        "Rapid rotation of basic pantry goods (tea, sugar, salt, oil, pulses)."
                    ),
                    weaknesses = listOf(
                        "Intense pressure from villagers for informal credit (Udhar) locking working capital.",
                        "Constrained shelf space restricting product variety."
                    ),
                    opportunities = listOf(
                        "Stocking high-margin FMCG micro-packs (₹5-₹10) that sell out daily.",
                        "Accepting UPI QR payments to capture tech-savvy youth transactions."
                    ),
                    threats = listOf(
                        "Price undercutting on bulk staples from tehsil wholesale markets.",
                        "Rodent and moisture damage to open grain sacks during monsoon."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "200 sq.ft organized store with refrigeration for dairy and cold drinks.",
                        "Direct wholesale sourcing from tehsil mandi saving 5-8% on procurement."
                    ),
                    weaknesses = listOf(
                        "Higher utility and electricity generator expense during summer blackouts.",
                        "Risk of slow-moving inventory tied up in cosmetics and packaged snacks."
                    ),
                    opportunities = listOf(
                        "Setting up a micro-ATM / AePS terminal to draw daily banking cash-out traffic.",
                        "Offering bulk grain cleaning and delivery to local primary schools and Anganwadis."
                    ),
                    threats = listOf(
                        "New competing shops opening on the same village road.",
                        "FMCG distributor minimum order quantity thresholds causing overstocking."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "Substantial capital allowing wholesale stockist pricing to 10+ village kiosks.",
                        "Ability to purchase full vehicle loads of sugar, edible oil, and flour directly."
                    ),
                    weaknesses = listOf(
                        "Significant credit exposure to smaller hamlet kiosk owners.",
                        "Inventory tracking complexity requiring computerized barcode/POS discipline."
                    ),
                    opportunities = listOf(
                        "Exclusive regional distribution rights for popular regional snacks and beverages.",
                        "Establishing an attached agri-inputs and cattle-feed retail section."
                    ),
                    threats = listOf(
                        "Online rural quick-commerce or wholesale apps entering the block.",
                        "Bad debt default from unverified rural retail borrowers."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "गाँव के केंद्र में होने से दैनिक आवश्यक वस्तुओं हेतु नियमित ग्राहकों का आना।",
                        "चाय, चीनी, तेल और दालों जैसी रोजमर्रा की वस्तुओं की तेजी से बिक्री।"
                    ),
                    weaknesses = listOf(
                        "गाँव में उधारी का भारी दबाव, जिससे छोटी कार्यशील पूंजी फंसने का जोखिम।",
                        "दुकान में सीमित जगह होने से सामान की विविधता कम होना।"
                    ),
                    opportunities = listOf(
                        "₹5 और ₹10 वाले छोटे FMCG पैकेट रखकर अधिक मार्जिन कमाना।",
                        "यूपीआई क्यूआर (UPI QR) कोड लगाकर युवाओं से डिजिटल नकद भुगतान लेना।"
                    ),
                    threats = listOf(
                        "पास के बड़े कस्बे के थोक व्यापारियों द्वारा सस्ती दरों पर सामान बेचना।",
                        "बरसात के दिनों में सीलन और चूहों से खुले अनाज की बोरियों को नुकसान।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "200 वर्ग फुट की पक्की दुकान एवं ठंडे पेय हेतु रेफ्रिजरेटर की सुविधा।",
                        "तहसील मंडी से सीधे थोक खरीद से लागत में 5-8% की सीधी बचत।"
                    ),
                    weaknesses = listOf(
                        "गर्मियों में बिजली कटने पर इन्वर्टर/जनरेटर का अतिरिक्त खर्च।",
                        "सौंदर्य प्रसाधन व पैकेटबंद नमकीन में पूंजी के मंद गति से घूमने का डर।"
                    ),
                    opportunities = listOf(
                        "दुकान में आधार माइक्रो-एटीएम (AePS) लगाकर नकद निकासी से नए ग्राहक जोड़ना।",
                        "स्थानीय आंगनवाड़ी एवं स्कूलों को थोक राशन की समय पर आपूर्ति।"
                    ),
                    threats = listOf(
                        "गाँव की उसी सड़क पर नई प्रतिस्पर्धी किराना दुकानों का खुलना।",
                        "वितरकों द्वारा थोक माल उठाने के न्यूनतम लक्ष्य का दबाव।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "मजबूत पूंजी बल, जिससे 10+ छोटी ग्रामीण दुकानों को थोक माल देने की क्षमता।",
                        "चीनी, खाद्य तेल एवं आटे की सीधी गाड़ी मंगाकर भारी थोक मुनाफा।"
                    ),
                    weaknesses = listOf(
                        "छोटी दुकानों को उधारी माल देने से उधारी वसूली में जोखिम।",
                        "सैकड़ों सामानों के स्टॉक प्रबंधन हेतु कंप्यूटर/सॉफ्टवेयर की अनिवार्य आवश्यकता।"
                    ),
                    opportunities = listOf(
                        "लोकप्रिय नमकीन, बिस्कुट एवं पेय कंपनियों की क्षेत्रीय डीलरशिप लेना।",
                        "किराना दुकान के साथ बीज, कीटनाशक एवं पशुआहार का विशेष काउंटर जोड़ना।"
                    ),
                    threats = listOf(
                        "शहरी थोक कंपनियों या ऑनलाइन सप्लाई का ग्रामीण क्षेत्र में विस्तार।",
                        "बिना सत्यापन के उधार लेने वाले छोटे दुकानदारों से पैसे डूबने का खतरा।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Monsoon agricultural lull causing villagers to drastically cut discretionary retail purchases.",
                    threatHi = "बरसात के दिनों में खेती का काम थमने से ग्रामीणों द्वारा गैर-जरूरी खर्चों में भारी कटौती।",
                    mitigationEn = "Trim luxury inventory before rains; focus working capital strictly on basic staples and seeds.",
                    mitigationHi = "बरसात से पहले गैर-जरूरी सामान कम करें; केवल आवश्यक खाद्यान्न और बीजों पर पूंजी लगाएं।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Flooded or unpaved approach roads severing wholesale delivery trucks from the tehsil.",
                    threatHi = "कच्ची सड़कों पर पानी भरने से कस्बे से सामान लाने वाली गाड़ियों का आवागमन रुकना।",
                    mitigationEn = "Pre-stock 2 to 3 weeks of high-density essentials (salt, matches, oil, pulses) prior to heavy rains.",
                    mitigationHi = "भारी बारिश से पूर्व 2-3 हफ्तों का नमक, दाल, खाद्य तेल और माचिस का सुरक्षित स्टॉक रखें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Sharp wholesale price increases in edible oils and pulses squeezing retail profit margins.",
                    threatHi = "खाद्य तेलों एवं दालों के थोक भावों में अचानक उछाल से खुदरा लाभ मार्जिन का घटना।",
                    mitigationEn = "Form a local buyers club with 3 neighboring shopkeepers to procure directly from wholesale oil mills.",
                    mitigationHi = "3 पड़ौसी दुकानदारों के साथ मिलकर तेल मिलों और दाल मिलों से सीधे थोक खरीद करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Accumulated informal customer credit (Udhar khata) leading to defaults and liquidity freeze.",
                    threatHi = "खाता-बही में ग्राहकों की उधारी अत्यधिक बढ़ने से पूंजी का फंसना और नकदी संकट।",
                    mitigationEn = "Enforce a strict ceiling: max ₹500 credit per household; stop fresh credit if unpaid past 10 days.",
                    mitigationHi = "कड़ा नियम बनाएं: प्रति परिवार अधिकतम ₹500 उधारी; 10 दिन से पुराना बिल चुकाए बिना नया सामान न दें।"
                )
            ),
            competitorDensityEn = "High density in central village with 3-5 existing unorganized kirana kiosks.",
            competitorDensityHi = "गाँव के चौराहे पर अधिक प्रतिस्पर्धा; पहले से 3 से 5 छोटी असंगठित दुकानें मौजूद हैं।",
            competitorProfileEn = "Traditional cluttered family-run shops with dark interiors, manual calculation, and frequent stockouts.",
            competitorProfileHi = "पारंपरिक छोटी दुकानें जहां रोशनी की कमी, हाथ से हिसाब और अक्सर सामान खत्म रहने की समस्या होती है।",
            competitiveMoatEn = "Brightly lit clean store, digital weighing scale, printed/SMS receipts, and zero expired goods guarantee.",
            competitiveMoatHi = "उजालेदार साफ-सुथरी दुकान, डिजिटल इलेक्ट्रॉनिक कांटा, पारदर्शी मूल्य और एक्सपायरी मुक्त सामान की गारंटी।",
            benchmarkPricingEn = listOf(
                "Staple Grains & Pulses: 8% - 12% retail markup over wholesale mandi",
                "Packaged FMCG & Snacks: 15% - 20% margin on printed MRP",
                "Soaps, Detergents & Personal Care: 18% - 25% margin on printed MRP",
                "Seasonal & Festive Goods: 25% - 35% margin during festival weeks"
            ),
            benchmarkPricingHi = listOf(
                "अनाज एवं दालें: थोक मंडी भाव पर 8% - 12% खुदरा मार्जिन",
                "पैकेटबंद स्नैक्स एवं बिस्कुट: एमआरपी (MRP) पर 15% - 20% मार्जिन",
                "साबुन, डिटर्जेंट एवं प्रसाधन सामग्री: एमआरपी पर 18% - 25% मार्जिन",
                "त्योहारी एवं मौसमी सामान: त्योहारों के सप्ताह में 25% - 35% मार्जिन"
            ),
            grossMarginRangeEn = "10% to 22% overall weighted average gross margin",
            grossMarginRangeHi = "10% से 22% समग्र औसत सकल मुनाफा मार्जिन",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Focus 80% of budget on high-velocity items: tea, sugar, cooking oil, salt, and ₹5 biscuit packets.",
                    "Use a digital ledger app (like Khatabook) to track credit and send automated SMS reminders.",
                    "Display daily prices clearly on a small chalkboard to build village trust."
                ),
                BudgetTier.SMALL to listOf(
                    "Install a single-door commercial refrigerator for milk, curd, and cold beverages.",
                    "Set up an AePS micro-ATM device to earn ₹5-₹10 commission per cash withdrawal.",
                    "Negotiate bi-weekly delivery from FMCG distributors to minimize personal travel to tehsil."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Implement a barcode scanner and mini-thermal billing printer to prevent pilferage.",
                    "Supply 10 surrounding hamlet shops with wholesale confectionery and packaged snacks.",
                    "Stock agricultural inputs (fertilizer bags, hybrid vegetable seeds) during planting seasons."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "पूंजी का 80% हिस्सा तेज बिकने वाले सामान: चाय, चीनी, तेल, नमक एवं ₹5 के बिस्कुट में लगाएं।",
                    "उधारी के हिसाब हेतु डिजिटल ऐप का उपयोग करें ताकि ग्राहकों को स्वचालित एसएमएस जा सके।",
                    "ग्राहकों का विश्वास जीतने हेतु दुकान के बाहर ब्लैकबोर्ड पर दैनिक मूल्य स्पष्ट रूप से लिखें।"
                ),
                BudgetTier.SMALL to listOf(
                    "दूध, दही और ठंडे पेय सुरक्षित रखने हेतु एक सिंगल-डोर व्यावसायिक फ्रिज लगाएं।",
                    "दुकान में आधार माइक्रो-एटीएम लगाकर प्रति नकद निकासी ₹5-₹10 का कमीशन अर्जित करें।",
                    "कस्बे जाने का खर्च बचाने हेतु बड़ी कंपनियों के सेल्समैन से दुकान पर ही डिलीवरी तय करें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "सामान की चोरी रोकने हेतु बारकोड स्कैनर एवं थर्मल बिलिंग प्रिंटर स्थापित करें।",
                    "आसपास के मजरों की 10 छोटी दुकानों को थोक मूल्य पर बिस्कुट, नमकीन व तेल की आपूर्ति करें।",
                    "बुवाई के मौसम में दुकान में प्रमाणित सब्जी के बीज एवं उर्वरक का विशेष स्टॉक रखें।"
                )
            )
        ),

        BusinessCategory.FOOD_PROCESSING to CategoryHeuristic(
            category = BusinessCategory.FOOD_PROCESSING,
            radiusEn = "6 to 12 km rural radius encompassing agricultural production clusters and mandis",
            radiusHi = "6 से 12 किमी का ग्रामीण दायरा जिसमें अनाज उत्पादक क्षेत्र एवं स्थानीय मंडियां शामिल हैं",
            primaryChannelsEn = listOf(
                "Direct village customer service (job-work peesai/peraai)",
                "Supply of packaged flour and spices to local kirana stores",
                "Weekly rotating haat stall with freshly ground spices",
                "Farmer Producer Company (FPC) bulk milling tie-ups"
            ),
            primaryChannelsHi = listOf(
                "ग्रामीणों की सीधी सेवा (जॉब-वर्क पिसाई एवं तेल पेराई)",
                "स्थानीय किराना दुकानों को पैकेट आटा एवं पिसे मसालों की आपूर्ति",
                "साप्ताहिक हाट में ताजा पिसे शुद्ध मसालों का बिक्री काउंटर",
                "किसान उत्पादक कंपनी (FPC) के साथ थोक प्रसंस्करण अनुबंध"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 3,000 to 5,500 residents across 3 neighboring farming hamlets",
                BudgetTier.SMALL to "Estimated 10,000 to 18,000 residents across 8 to 12 villages",
                BudgetTier.MEDIUM to "Estimated 30,000 to 60,000 consumers across the agricultural block"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "3 पड़ोसी गांवों के लगभग 3,000 से 5,500 किसान एवं ग्रामीण",
                BudgetTier.SMALL to "8 से 12 गांवों के लगभग 10,000 से 18,000 नियमित उपभोक्ता",
                BudgetTier.MEDIUM to "पूरे कृषि प्रखंड के लगभग 30,000 से 60,000 उपभोक्ताओं का विशाल संकुल"
            ),
            opportunityNicheEn = "Stone-ground multi-grain and millet processing (Ragi, Bajra, Jowar) under government Shree Anna initiatives, plus cold-pressed pure mustard oil.",
            opportunityNicheHi = "श्री अन्न योजना के अंतर्गत रागी, बाजरा व ज्वार का पौष्टिक मल्टीग्रेन आटा पिसाई, एवं शुद्ध कच्ची घानी सरसों तेल का उत्पादन।",
            unservedSegmentsEn = listOf(
                "Unadulterated turmeric, coriander, and red chilli powder freshly ground on order",
                "Mini dal-mill pulse de-husking (arhar, moong, chana) at farm gate",
                "Ready-to-cook roasted sattu and multigrain porridge packaging"
            ),
            unservedSegmentsHi = listOf(
                "ऑर्डर पर तुरंत पिसे गए शुद्ध हल्दी, धनिया और लाल मिर्च मसाले",
                "गाँव स्तर पर ही अरहर, चना और मूंग की दाल बनाने की मिनी दाल मिल सेवा",
                "ताजा भुना हुआ चने का सत्तू एवं दलिया तैयार कर पैकेट में बेचना"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Steady year-round fee-based revenue; clients bring their own grain so zero raw material capital is tied up.",
                        "Extremely high gross margins (60%+) on custom milling services (peesai)."
                    ),
                    weaknesses = listOf(
                        "Single-phase electric motor limits processing speed and capacity.",
                        "Frequent electrical voltage fluctuations causing motor tripping."
                    ),
                    opportunities = listOf(
                        "Adding a small spice pulverizer attachment to grind chilli and turmeric.",
                        "Selling clean wheat bran (chokar) byproduct to local dairy farmers."
                    ),
                    threats = listOf(
                        "Prolonged rural power cuts during harvest seasons.",
                        "Wear-and-tear requiring stone re-dressing every 15-20 days."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "Integrated unit: Commercial Flour Mill + Mini Dal Mill + Mustard Expeller.",
                        "Ability to serve both service peesai and sell proprietary packaged products."
                    ),
                    weaknesses = listOf(
                        "Diesel generator running expenses significantly increase cost during power cuts.",
                        "Dust and noise pollution requiring proper shed ventilation."
                    ),
                    opportunities = listOf(
                        "Branding 5kg and 10kg pure mustard oil tins for local weddings and dhabas.",
                        "Institutional supply contracts with local mid-day meal school schemes."
                    ),
                    threats = listOf(
                        "Sudden electricity commercial tariff hikes.",
                        "Cheap adulterated cooking oils flooding weekly haat markets."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "Automated cleaning, destoning, grading, milling, and nitrogen packaging facility.",
                        "High throughput capacity exceeding 5 quintals per hour."
                    ),
                    weaknesses = listOf(
                        "Large capital required to procure raw grains directly at mandi harvest.",
                        "Compliance requirements (FSSAI registration, weights and measures certification)."
                    ),
                    opportunities = listOf(
                        "Supplying branded millet flours to urban health food distributors.",
                        "Securing PMFME (PM Formalisation of Micro Food Processing Enterprises) 35% subsidy."
                    ),
                    threats = listOf(
                        "Unseasonal rains spoiling raw grain stocks during monsoon storage.",
                        "Sharp harvest price crashes devaluing stored grain inventory."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "साल भर निरंतर नकद सेवा शुल्क; ग्राहक स्वयं अनाज लाता है अतः पूंजी फंसने का शून्य जोखिम।",
                        "पिसाई सेवा (जॉब-वर्क) पर 60% से अधिक का भारी सकल मुनाफा मार्जिन।"
                    ),
                    weaknesses = listOf(
                        "सिंगल-फेज मोटर होने से पिसाई की गति और दैनिक क्षमता सीमित होना।",
                        "ग्रामीण क्षेत्रों में वोल्टेज के उतार-चढ़ाव से मोटर जलने या ट्रिप होने का खतरा।"
                    ),
                    opportunities = listOf(
                        "चक्की के साथ मिनी मसाला पल्वराइजर जोड़कर हल्दी-मिर्च पिसाई शुरू करना।",
                        "गेहूं की छनाई से निकलने वाले चोकर को स्थानीय पशुपालकों को लाभदायक भाव पर बेचना।"
                    ),
                    threats = listOf(
                        "फसल कटाई के मुख्य सीजन में लगातार बिजली कटौती।",
                        "हर 15-20 दिन में चक्की के पत्थरों की घिसाई (टांका लगाने) की आवश्यकता।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "संयुक्त इकाई: आटा चक्की + मिनी दाल मिल + सरसों तेल एक्सपेलर।",
                        "पिसाई सेवा शुल्क के साथ-साथ खुद का पैकेटबंद तेल व आटा बेचने का दोहरा लाभ।"
                    ),
                    weaknesses = listOf(
                        "बिजली न होने पर डीजल जनरेटर चलाने से प्रति क्विंटल पिसाई लागत का बढ़ना।",
                        "मशीन के चलने से निकलने वाली धूल और आवाज हेतु उपयुक्त शेड की आवश्यकता।"
                    ),
                    opportunities = listOf(
                        "स्थानीय ढाबों और शादियों हेतु 5 लीटर व 15 लीटर सरसों तेल के डिब्बों की सीधी बिक्री।",
                        "स्थानीय प्राथमिक विद्यालयों के मध्याह्न भोजन योजना (Mid-Day Meal) हेतु अनाज पिसाई का ठेका।"
                    ),
                    threats = listOf(
                        "व्यावसायिक बिजली की दरों में अचानक वृद्धि।",
                        "हाट बाजारों में बिकने वाले सस्ते मिलावटी तेल से मूल्य प्रतिस्पर्धा।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "अनाज की स्वचालित सफाई, ग्रेडिंग, पिसाई एवं नाइट्रोजन फ्लश पैकिंग प्लांट।",
                        "5 क्विंटल प्रति घंटा से अधिक की तेज प्रसंस्करण क्षमता।"
                    ),
                    weaknesses = listOf(
                        "कटाई के समय किसानों से भारी मात्रा में सीधे अनाज खरीदने हेतु बड़ी पूंजी की जरूरत।",
                        "खाद्य सुरक्षा मानक (FSSAI) एवं नाप-तौल विभाग के कड़े नियमों का पालन।"
                    ),
                    opportunities = listOf(
                        "शहरी सुपरमार्केट और ऑर्गेनिक दुकानों को बाजरा व रागी का ब्रांडेड आटा आपूर्ति करना।",
                        "पीएमएफएमई (PMFME) योजना के तहत 35% सरकारी क्रेडिट-लिंक्ड सब्सिडी का लाभ उठाना।"
                    ),
                    threats = listOf(
                        "गोदाम में रखे अनाज में सीलन और कीट लगने से भारी नुकसान का जोखिम।",
                        "मंडी में अचानक फसल के भाव गिरने से भंडारित माल के मूल्य में कमी।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Sharp volume dips in grain milling during the 2 months immediately preceding new harvest.",
                    threatHi = "नई फसल आने से ठीक पहले के 2 महीनों में ग्रामीणों के पास अनाज घटने से पिसाई में मंदी।",
                    mitigationEn = "Pivot to mustard oil expelling and spice grinding during pre-harvest lean milling periods.",
                    mitigationHi = "अनाज पिसाई के मंदे महीनों में सरसों तेल पेराई और मसाला पिसाई के काम पर अधिक ध्यान दें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Burnout of electric motor bearings or broken drive belts causing days of mill shutdown.",
                    threatHi = "मोटर बेयरिंग के जाम होने या पट्टा टूटने से कई दिनों तक चक्की का बंद रहना।",
                    mitigationEn = "Maintain spare v-belts, capacitor sets, and stone dressing tools directly on-site.",
                    mitigationHi = "अतिरिक्त वी-बेल्ट, कैपेसिटर और पत्थर टांकने के औजार हमेशा चक्की पर तैयार रखें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Sudden spikes in whole mustard seed prices impacting proprietary oil extraction margins.",
                    threatHi = "साबुत सरसों के थोक भावों में उछाल से तेल पेराई के मुनाफे में कमी आना।",
                    mitigationEn = "Prioritize customer custom peraai (charging ₹12/kg fee) to insulate against commodity price swings.",
                    mitigationHi = "स्वयं माल खरीदने के बजाय किसानों की सरसों की नकद पेराई (₹12/किग्रा शुल्क) को प्राथमिकता दें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Credit trap caused by local dhabas delaying payment for bulk flour and mustard oil deliveries.",
                    threatHi = "स्थानीय ढाबों द्वारा आटे और तेल के थोक बिलों के भुगतान में महीनों की देरी करना।",
                    mitigationEn = "Establish a strict 50% cash deposit rule on commercial bulk supply orders.",
                    mitigationHi = "व्यावसायिक थोक ऑर्डरों पर 50% अग्रिम नकद भुगतान लेने का कड़ा नियम लागू करें।"
                )
            ),
            competitorDensityEn = "Low to moderate density; existing chakkis operate old low-efficiency stone mills.",
            competitorDensityHi = "प्रखंड में कम से मध्यम प्रतिस्पर्धा; पुरानी चक्कियां धीमी गति और अधिक बिजली खपत पर चलती हैं।",
            competitorProfileEn = "Traditional standalone chakkis with poorly ventilated single-cylinder engines causing flour overheating.",
            competitorProfileHi = "पारंपरिक अकेली आटा चक्कियां जहां अत्यधिक गर्मी से आटे की पौष्टिकता नष्ट हो जाती है।",
            competitiveMoatEn = "Cold stone-ground low-RPM technology preserving grain nutrients, dust-free enclosure, and zero waiting time.",
            competitiveMoatHi = "कम आरपीएम (RPM) पर ठंडी पिसाई जिससे आटे के पोषक तत्व नष्ट न हों, स्वच्छ परिसर एवं तुरंत सेवा।",
            benchmarkPricingEn = listOf(
                "Wheat Flour Custom Milling: ₹4.50 - ₹6.00 per kg (approx. 65% service gross margin)",
                "Pulse De-Husking (Dal Mill): ₹8.00 - ₹12.00 per kg (approx. 55% service gross margin)",
                "Mustard Oil Expelling: ₹12.00 - ₹16.00 per kg seed (approx. 50% service gross margin)",
                "Packaged Pure Spice Powders: ₹280 - ₹420 per kg (approx. 38% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "गेहूं पिसाई सेवा शुल्क: ₹4.50 - ₹6.00 प्रति किलो (लगभग 65% सेवा सकल मुनाफा)",
                "दाल प्रसंस्करण सेवा शुल्क: ₹8.00 - ₹12.00 प्रति किलो (लगभग 55% सेवा सकल मुनाफा)",
                "सरसों तेल पेराई शुल्क: ₹12.00 - ₹16.00 प्रति किलो बीज (लगभग 50% सेवा सकल मुनाफा)",
                "शुद्ध पैकेटबंद मसाला पाउडर: ₹280 - ₹420 प्रति किलो (लगभग 38% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "35% to 65% (higher on service job-work, stable on packaged products)",
            grossMarginRangeHi = "35% से 65% (जॉब-वर्क पिसाई पर अधिक, पैकेट उत्पादों पर स्थिर लाभ)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Start with a 10 HP motor-driven atta chakki and mini table-top spice pulverizer.",
                    "Charge competitive peesai rates (₹5/kg) and provide free sieving to win over village housewives.",
                    "Save wheat bran byproduct in clean sacks and sell weekly to local dairy farmers."
                ),
                BudgetTier.SMALL to listOf(
                    "Add a 6-bolt cold-press mustard oil expeller and mini pulse de-husker.",
                    "Apply for an FSSAI Basic Registration to legally package and label 1kg flour and spice packs.",
                    "Provide special custom processing hours for millet (Ragi and Bajra) to capture health demand."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Procure an automated cleaning and destoning line to ensure zero stone particles in flour.",
                    "Apply for PMFME scheme to receive up to ₹10 Lakh in capital subsidy and subsidized credit.",
                    "Package cold-pressed mustard oil in 1L PET bottles with local branding for tehsil retail distribution."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "10 एचपी मोटर चालित आधुनिक चक्की और छोटे मसाला पल्वराइजर से शुरुआत करें।",
                    "प्रतिस्पर्धी दर (₹5/किग्रा) रखें और महिलाओं को आकर्षित करने हेतु मुफ्त छनाई की सुविधा दें।",
                    "छनाई से निकला चोकर साफ बोरियों में भरकर स्थानीय पशुपालकों को नियमित बेचें।"
                ),
                BudgetTier.SMALL to listOf(
                    "चक्की के साथ 6-बोल्ट सरसों तेल एक्सपेलर और मिनी दाल मिल स्थापित करें।",
                    "1 किग्रा आटा व मसाले पैकेट में बेचने हेतु बुनियादी एफएसएसएआई (FSSAI) पंजीकरण लें।",
                    "मोटे अनाज (रागी व बाजरा) की पिसाई हेतु अलग दिन निर्धारित कर स्वास्थ्य-सचेत ग्राहकों को जोड़ें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "आटे में कंकड़-पत्थर पूरी तरह समाप्त करने हेतु स्वचालित क्लीनर व डी-स्टोनर मशीन लगाएं।",
                    "पीएमएफएमई (PMFME) योजना में आवेदन कर ₹10 लाख तक की पूंजीगत सब्सिडी प्राप्त करें।",
                    "कच्ची घानी सरसों के तेल को 1 लीटर की बोतलों में अपने ब्रांड के साथ पैक कर कस्बों में सप्लाई करें।"
                )
            )
        ),

        BusinessCategory.TEXTILES to CategoryHeuristic(
            category = BusinessCategory.TEXTILES,
            radiusEn = "5 to 10 km operational radius catering to women and rural households across 6 to 8 villages",
            radiusHi = "5 से 10 किमी का दायरा जिसमें 6 से 8 गांवों की महिलाएं एवं परिवार शामिल हैं",
            primaryChannelsEn = listOf(
                "Village tailoring workshop and women's boutique counter",
                "Government and private school uniform bulk tenders",
                "Stalls at festive Melas (Diwali, Eid, Chhath, Rakhi)",
                "Weekly rotating rural apparel haat markets"
            ),
            primaryChannelsHi = listOf(
                "गाँव में आधुनिक सिलाई केंद्र एवं महिला बुटीक काउंटर",
                "सरकारी एवं निजी स्कूलों के गणवेश (यूनिफॉर्म) के थोक ऑर्डर",
                "त्योहारी मेलों (दीवाली, ईद, छठ, रक्षाबंधन) में विशेष स्टॉल",
                "साप्ताहिक ग्रामीण कपड़ा हाट बाजारों में रेडीमेड वस्त्र बिक्री"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 1,500 to 3,000 women and school-going children in immediate cluster",
                BudgetTier.SMALL to "Estimated 5,000 to 10,000 consumers across 5 to 7 surrounding villages",
                BudgetTier.MEDIUM to "Estimated 15,000 to 30,000 consumers covering tehsil school networks"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "आसपास के मजरों की लगभग 1,500 से 3,000 महिलाएं एवं स्कूली बच्चे",
                BudgetTier.SMALL to "5 से 7 गांवों के लगभग 5,000 से 10,000 नियमित ग्राहक",
                BudgetTier.MEDIUM to "पूरे तहसील के स्कूल नेटवर्क को मिलाकर 15,000 से 30,000 संभावित ग्राहक"
            ),
            opportunityNicheEn = "Ready-to-wear tailored women's ethnic kurtis, designer blouses, and annual institutional school uniform bulk contracts.",
            opportunityNicheHi = "महिलाओं के रेडीमेड कुर्ती-सूट, आधुनिक डिजाइनर ब्लाउज एवं स्कूलों के यूनिफॉर्म सिलाई का वार्षिक थोक अनुबंध।",
            unservedSegmentsEn = listOf(
                "Custom bridal and festival lehenga alterations at 60% lower cost than town boutiques",
                "Children's readymade cotton clothing sets at ₹250 - ₹450 price points",
                "Self-Help Group uniform and hospital bedsheet stitching contracts"
            ),
            unservedSegmentsHi = listOf(
                "शहर के महंगे बुटीक के मुकाबले 60% कम खर्च में शादी-ब्याह के लहंगे व सूट की सिलाई",
                "बच्चों के सूती कपड़ों के आकर्षक रेडीमेड सेट (₹250 - ₹450 की किफायती रेंज में)",
                "आंगनवाड़ी, स्वयं सहायता समूह एवं स्थानीय अस्पतालों के वस्त्र निर्माण के ऑर्डर"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Very low initial machinery capex; can operate from home or small kiosk.",
                        "Strong word-of-mouth referral network among village women."
                    ),
                    weaknesses = listOf(
                        "Manual foot-treadle machines limit daily stitching output to 2-3 garments.",
                        "High physical fatigue without electric motorized attachments."
                    ),
                    opportunities = listOf(
                        "Surge demand during wedding and festival seasons (Diwali, Eid, weddings).",
                        "Offering alteration and zip/button repair services for daily cash."
                    ),
                    threats = listOf(
                        "Cheap synthetic readymade apparel brought from town wholesale markets.",
                        "Sudden price increases in sewing thread, canvas, and zipper notions."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "4 to 6 motorized high-speed sewing and overlock interlock machines.",
                        "Ability to deliver complete school uniform batches within strict deadlines."
                    ),
                    weaknesses = listOf(
                        "Reliance on semi-skilled sewing assistants who may migrate during harvest.",
                        "Electricity dependency for motorized machines and steam irons."
                    ),
                    opportunities = listOf(
                        "Stocking running fabric rolls (cotton, rayon) to offer design-plus-stitching package.",
                        "Bidding for Block Education Officer government school uniform tenders."
                    ),
                    threats = listOf(
                        "Delayed payment from school committees for bulk uniform orders.",
                        "Fabric inventory obsolescence if seasonal color trends change."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "Full-fledged mini garment unit (12-15 stations) with computerized embroidery.",
                        "Substantial volume capacity to supply tehsil wholesale clothing retailers."
                    ),
                    weaknesses = listOf(
                        "High monthly payroll overhead and operator turnover management.",
                        "Space requirement for dedicated cutting tables and finished goods storage."
                    ),
                    opportunities = listOf(
                        "Creating an independent rural apparel brand with regional e-commerce catalog.",
                        "Direct wholesale fabric procurement from Surat and Ahmedabad textile hubs."
                    ),
                    threats = listOf(
                        "Large fast-fashion retail chains opening branches in nearby district centers.",
                        "Wholesale client default on 60-day credit terms."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "शुरुआती मशीनों में बहुत कम खर्च; घर से या छोटी दुकान से आसानी से शुरुआत संभव।",
                        "गाँव की महिलाओं के आपसी विश्वास और प्रशंसा से तेजी से ग्राहक जुड़ना।"
                    ),
                    weaknesses = listOf(
                        "पैडल वाली हाथ मशीनों से दैनिक उत्पादन 2-3 कपड़ों तक ही सीमित रहना।",
                        "मोटर न होने के कारण लगातार सिलाई करने में अधिक शारीरिक थकान।"
                    ),
                    opportunities = listOf(
                        "त्योहारों (दीवाली, ईद, करवाचौथ) एवं शादियों के सीजन में काम की भारी मांग।",
                        "सिलाई के साथ कपड़ों की मरम्मत, अल्टरेशन व फाल-पीको से दैनिक नकद आमदनी।"
                    ),
                    threats = listOf(
                        "कस्बों से आने वाले सस्ते रेडीमेड पॉलिएस्टर कपड़ों से प्रतिस्पर्धा।",
                        "धागे, अस्तर, बकरम और जिपर की फुटकर कीमतों में अचानक बढ़ोतरी।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "4 से 6 आधुनिक मोटर चालित सिलाई व इंटरलॉक ओवरलॉक मशीनें।",
                        "निश्चित समय सीमा के भीतर स्कूलों के गणवेश (यूनिफॉर्म) तैयार करने की क्षमता।"
                    ),
                    weaknesses = listOf(
                        "सिलाई कारीगरों पर निर्भरता, जो फसल कटाई के समय काम छोड़ देते हैं।",
                        "मोटर मशीनों और स्टीम प्रेस चलाने हेतु निरंतर बिजली की आवश्यकता।"
                    ),
                    opportunities = listOf(
                        "सूट के थान का कपड़ा रखकर सिलाई सहित पैकेज बेचकर दोहरा मुनाफा कमाना।",
                        "सरकारी एवं निजी स्कूलों से संपर्क कर यूनिफॉर्म का थोक ठेका हासिल करना।"
                    ),
                    threats = listOf(
                        "स्कूल समितियों द्वारा यूनिफॉर्म के भुगतान में कई महीनों का विलंब।",
                        "फैशन बदलने पर बिना बिका कपड़ा दुकान में फंसे रहने का जोखिम।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "12 से 15 मशीनों की गारमेंट निर्माण इकाई एवं कंप्यूटराइज्ड कढ़ाई मशीन।",
                        "तहसील के थोक कपड़ा व्यापारियों को बड़ी संख्या में रेडीमेड माल देने की क्षमता।"
                    ),
                    weaknesses = listOf(
                        "कारीगरों के मासिक वेतन का बड़ा खर्च और श्रमिकों का बार-बार काम बदलना।",
                        "कपड़ा कटिंग टेबल और तैयार माल के सुरक्षित भंडारण हेतु बड़ी जगह की जरूरत।"
                    ),
                    opportunities = listOf(
                        "सस्ते व आकर्षक परिधानों का अपना स्वतंत्र क्षेत्रीय ग्रामीण ब्रांड बनाना।",
                        "सूरत एवं अहमदाबाद की कपड़ा मंडियों से सीधे थोक थान मंगाकर लागत घटाना।"
                    ),
                    threats = listOf(
                        "जिले के मुख्य शहरों में बड़े रेडीमेड शोरूम खुलने से ग्राहकों का आकर्षित होना।",
                        "थोक व्यापारियों द्वारा 60 दिन की उधारी पर माल लेकर समय पर भुगतान न करना।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Severe revenue plunge during agricultural sowing months when tailoring demand disappears.",
                    threatHi = "खेती की बुवाई के मौसम में सिलाई के काम में भारी मंदी और आमदनी का रुकना।",
                    mitigationEn = "Utilize lean months to pre-stitch standard school uniforms and readymade kurtis for upcoming festivals.",
                    mitigationHi = "मंद गति वाले महीनों में आने वाले त्योहारी सीजन हेतु रेडीमेड कुर्तियां और स्कूल ड्रेस पहले से सिलकर रखें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Frequent breakdown of electronic zig-zag and overlock motors halting customer deliveries.",
                    threatHi = "इंटरलॉक या पिको मशीन की मोटर खराब होने से ग्राहकों के कपड़ों की डिलीवरी अटकना।",
                    mitigationEn = "Maintain basic mechanical tuning skills and keep spare bobbin cases, needles, and belts.",
                    mitigationHi = "सिलाई मशीनों की बुनियादी मरम्मत खुद सीखें और अतिरिक्त सुई, बॉबिन केस व बेल्ट रखें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Price volatility in uniform fabric and lining materials eating into fixed-price tender bids.",
                    threatHi = "कपड़े और अस्तर के दामों में वृद्धि से तय किए गए यूनिफॉर्म ठेके के मुनाफे में कमी।",
                    mitigationEn = "Secure firm supplier price lock-ins and purchase complete cloth rolls upfront against customer advances.",
                    mitigationHi = "थोक व्यापारी से भाव पहले ही तय करें और ग्राहकों के अग्रिम भुगतान से पूरा थान एक साथ खरीदें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Financial exposure if a single private school delays payment for 200 uniform sets.",
                    threatHi = "किसी एक निजी स्कूल द्वारा 200 ड्रेस का भुगतान महीनों तक अटकाए रखना।",
                    mitigationEn = "Require a mandatory 40% advance before stitching begins, and 40% on mid-delivery.",
                    mitigationHi = "कपड़ा काटने से पूर्व 40% अग्रिम और आधी डिलीवरी पर 40% भुगतान लेने की अनिवार्य शर्त रखें।"
                )
            ),
            competitorDensityEn = "Moderate density; traditional local tailors operate with manual treadle machines.",
            competitorDensityHi = "मध्यम प्रतिस्पर्धा; स्थानीय दर्जी पुरानी हाथ-पैरों की मशीनों से साधारण सिलाई करते हैं।",
            competitorProfileEn = "Individual home-based tailors with frequent delivery delays and poor finishing on modern cuts.",
            competitorProfileHi = "घरेलू दर्जी जो डिलीवरी में बहुत देरी करते हैं और आधुनिक फिटिंग व फिनिशिंग देने में असमर्थ हैं।",
            competitiveMoatEn = "Guaranteed 48-hour delivery on urgent festive orders, modern neckline designs, and flawless overlock seam finish.",
            competitiveMoatHi = "48 घंटे में सिलाई की गारंटी, आधुनिक डिजाइनर गले व बाजू की कटिंग एवं मजबूत इंटरलॉक सिलाई।",
            benchmarkPricingEn = listOf(
                "Simple Blouse Stitching: ₹120 - ₹180 (approx. 70% service gross margin)",
                "Designer Blouse with Piping/Dori: ₹280 - ₹450 (approx. 75% service gross margin)",
                "Salwar Suit / Kurti Complete: ₹250 - ₹420 (approx. 68% service gross margin)",
                "School Uniform Pair (Shirt + Trousers/Skirt): ₹350 - ₹500 (approx. 40% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "साधारण ब्लाउज सिलाई: ₹120 - ₹180 (लगभग 70% सेवा सकल मुनाफा)",
                "डिजाइनर ब्लाउज (डोरी/पाइपिंग सहित): ₹280 - ₹450 (लगभग 75% सेवा सकल मुनाफा)",
                "सलवार सूट / कुर्ती सिलाई: ₹250 - ₹420 (लगभग 68% सेवा सकल मुनाफा)",
                "स्कूल यूनिफॉर्म सेट (शर्ट + पैंट/स्कर्ट): ₹350 - ₹500 (लगभग 40% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "40% to 75% across stitching labor and readymade apparel",
            grossMarginRangeHi = "40% से 75% (सिलाई श्रम पर अधिक, रेडीमेड कपड़ों पर 40%)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Acquire an electric motor attachment and umbrella sewing machine to triple daily output.",
                    "Offer free fitting alteration on any readymade garment brought by customers to attract footfall.",
                    "Collect 50% advance on all festive blouse and suit orders to fund lining and thread purchases."
                ),
                BudgetTier.SMALL to listOf(
                    "Invest in a 4-thread overlock machine to produce industrial-grade seam finishes.",
                    "Contact 3 nearby private schools by February to secure annual uniform stitching contracts.",
                    "Stock high-demand printed cotton and rayon suit fabrics to sell complete fabric-plus-tailoring packages."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Set up an automated cloth cutting table and computerized multi-needle embroidery machine.",
                    "Hire and train local women through Self-Help Groups (SHGs) under skill mission stipends.",
                    "Launch readymade ethnic collections at ₹350-₹500 targeted at weekly rural haats."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "सिलाई की गति तीन गुना करने हेतु अम्ब्रेला मशीन में इलेक्ट्रिक मोटर लगवाएं।",
                    "शुरुआत में नए ग्राहक जोड़ने हेतु बाहर से लाए गए रेडीमेड कपड़ों पर मुफ्त अल्टरेशन की सुविधा दें।",
                    "अस्तर व धागे की खरीद हेतु ग्राहकों से सिलाई का 50% अग्रिम भुगतान अवश्य लें।"
                ),
                BudgetTier.SMALL to listOf(
                    "कपड़ों में मजबूत फिनिशिंग देने हेतु 4-थ्रेड वाली ओवरलॉक इंटरलॉक मशीन में निवेश करें।",
                    "फरवरी-मार्च में ही 3 स्थानीय निजी स्कूलों से मिलकर स्कूल ड्रेस सिलाई का अनुबंध पक्का करें।",
                    "दुकान में आकर्षक सूती व रेयॉन कपड़े के थान रखें ताकि ग्राहक कपड़ा और सिलाई दोनों यहीं से लें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "कपड़ों की तेज कटिंग हेतु इलेक्ट्रिक कटर एवं कंप्यूटराइज्ड कढ़ाई मशीन स्थापित करें।",
                    "आजीविका मिशन के तहत गाँव की महिलाओं को प्रशिक्षित कर सिलाई इकाई में रोजगार दें।",
                    "ग्रामीण साप्ताहिक हाट बाजारों हेतु ₹350-₹500 की किफायती रेंज में रेडीमेड सूट तैयार करें।"
                )
            )
        ),

        BusinessCategory.POULTRY to CategoryHeuristic(
            category = BusinessCategory.POULTRY,
            radiusEn = "8 to 15 km distribution radius covering meat retailers, rural dhabas, and weekly livestock haats",
            radiusHi = "8 से 15 किमी का दायरा जिसमें मांस विक्रेता, ग्रामीण ढाबे एवं साप्ताहिक पशु हाट शामिल हैं",
            primaryChannelsEn = listOf(
                "Direct farm-gate wholesale lifting by local chicken retailers",
                "Supply contracts with highway dhabas and rural eateries",
                "Weekly livestock haats for live bird sales",
                "Direct sales of organic brown eggs to village grocers"
            ),
            primaryChannelsHi = listOf(
                "स्थानीय मुर्गा विक्रेताओं द्वारा फार्म-गेट से सीधी थोक खरीद",
                "हाईवे ढाबों और ग्रामीण होटलों के साथ साप्ताहिक आपूर्ति अनुबंध",
                "साप्ताहिक हाट बाजारों में जीवित देशी मुर्गों की नकद बिक्री",
                "किराना दुकानों को पौष्टिक देशी भूरे अंडों की सीधी नियमित आपूर्ति"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 4,000 to 7,000 consumers across 4-6 connected villages",
                BudgetTier.SMALL to "Estimated 15,000 to 25,000 consumers across tehsil meat markets",
                BudgetTier.MEDIUM to "Estimated 40,000 to 80,000 consumers catering to multi-block lifters"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "4-6 जुड़े गांवों के लगभग 4,000 से 7,000 मांसाहारी उपभोक्ता",
                BudgetTier.SMALL to "तहसील स्तर के मांस बाजारों के लगभग 15,000 से 25,000 नियमित उपभोक्ता",
                BudgetTier.MEDIUM to "कई प्रखंडों के थोक खरीदारों को मिलाकर 40,000 से 80,000 उपभोक्ताओं का बाजार"
            ),
            opportunityNicheEn = "Rearing native Desi birds (Kadaknath, Kuroiler) and organic brown eggs fetching 2x-2.5x price premium over commercial broiler chicken.",
            opportunityNicheHi = "देशी मुर्गी (कड़कनाथ, क्रोइलर) एवं देशी भूरे अंडों का उत्पादन, जिनकी कीमत सामान्य सफेद ब्रायलर से 2 से 2.5 गुना अधिक मिलती है।",
            unservedSegmentsEn = listOf(
                "Free-range desi chicken for festival feasts and traditional winter celebrations",
                "High-protein brown eggs sold directly to health-conscious rural households",
                "Composted dry poultry manure sold as high-nitrogen organic fertilizer to vegetable farmers"
            ),
            unservedSegmentsHi = listOf(
                "त्योहारों एवं सर्दियों में पारंपरिक स्वाद हेतु खुले में पली देशी मुर्गी की भारी मांग",
                "गर्भवती महिलाओं एवं बच्चों हेतु पौष्टिक देशी भूरे अंडों की सीधी आपूर्ति",
                "मुर्गी की बीट से तैयार सूखी खाद को सब्जी उत्पादक किसानों को जैविक खाद के रूप में बेचना"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Extremely fast 35-42 day broiler harvest cycle yields fast working capital recovery.",
                        "Growing protein consumption across rural hamlets guarantees daily demand."
                    ),
                    weaknesses = listOf(
                        "High mortality risk if brooding temperature and vaccination schedules are missed.",
                        "Heavy dependence on commercial feed bags (maize/soya)."
                    ),
                    opportunities = listOf(
                        "Rearing dual-purpose Desi birds (Kuroiler) requiring minimal shed investment.",
                        "Selling dry poultry manure to local vegetable growers for extra cash."
                    ),
                    threats = listOf(
                        "Sudden summer heatwave mortality spikes in uninsulated sheds.",
                        "Bird flu rumors causing temporary collapse in local retail meat prices."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "1,500 to 2,500 bird semi-automated shed with automatic bell drinkers and feeders.",
                        "Staggered batch rotation providing continuous cash inflow every 14 days."
                    ),
                    weaknesses = listOf(
                        "Substantial working capital locked in chick feed during the 5th and 6th weeks.",
                        "Odor and fly control complaints from neighbors if litter is wet."
                    ),
                    opportunities = listOf(
                        "Entering into contract farming agreements with major hatcheries (Suguna/Venky's) for guaranteed placement.",
                        "Direct retail counter at village junction capturing 20% higher dressed chicken realization."
                    ),
                    threats = listOf(
                        "Sharp feed price increases eroding batch profitability.",
                        "Viral disease outbreaks (Ranikhet / IBD) wiping out an entire batch."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "5,000+ bird climate-controlled environmentally controlled (EC) shed.",
                        "Bulk direct feed formulation unit blending local maize, reducing feed cost by ₹3/kg."
                    ),
                    weaknesses = listOf(
                        "Heavy electricity and generator backup expenses to run cooling pads and exhaust fans.",
                        "High initial capital capex requiring disciplined loan repayment."
                    ),
                    opportunities = listOf(
                        "Direct institutional contracts with highway restaurants and city hotel chains.",
                        "Setting up an on-farm cold room to hold dressed birds during price drops."
                    ),
                    threats = listOf(
                        "Overproduction gluts in the district depressing wholesale live bird rates.",
                        "Strict environmental pollution control board regulatory compliance."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "ब्रायलर का 35-42 दिन का तीव्र चक्र, जिससे पूंजी बहुत तेजी से वापस लौटती है।",
                        "ग्रामीण क्षेत्रों में मांसाहार और प्रोटीन की बढ़ती मांग से नियमित बिक्री की गारंटी।"
                    ),
                    weaknesses = listOf(
                        "ब्रूडिंग तापमान नियंत्रण और समय पर टीकाकरण न होने पर मृत्यु दर का भारी खतरा।",
                        "व्यावसायिक दाने (मक्का/सोया आहार) की बाजार कीमतों पर अत्यधिक निर्भरता।"
                    ),
                    opportunities = listOf(
                        "कम लागत में देशी नस्ल (क्रोइलर/कड़कनाथ) पालन कर अधिक मूल्य प्राप्त करना।",
                        "मुर्गी की बीट/खाद को स्थानीय सब्जी उत्पादकों को बेचकर अतिरिक्त आमदनी।"
                    ),
                    threats = listOf(
                        "मई-जून की भीषण लू और गर्मी में तापमान बढ़ने से मुर्गियों की आकस्मिक मौत।",
                        "बर्ड फ्लू की अफवाहों से स्थानीय बाजार में अचानक मांग और भाव का गिरना।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "1,500 से 2,500 मुर्गियों का शेड एवं स्वचालित पानी/दाने के बर्तनों की सुविधा।",
                        "बैच रोटेशन (हर 14 दिन पर नया बैच) से महीने भर निरंतर नकद आय का प्रवाह।"
                    ),
                    weaknesses = listOf(
                        "5वें और 6वें हफ्ते में मुर्गियों के अधिक दाना खाने से बड़ी कार्यशील पूंजी की जरूरत।",
                        "बिछावन (बुरादा/भूसा) गीला रहने पर बदबू और मक्खियों की समस्या।"
                    ),
                    opportunities = listOf(
                        "बड़ी पोल्ट्री कंपनियों (सुगुना/वेंकीज) से अनुबंध (कॉन्ट्रैक्ट फार्मिंग) कर जोखिम मुक्त कमाई।",
                        "गाँव के तिराहे पर अपना खुदरा चिकन सेंटर खोलकर प्रति किलो ₹20 अधिक कमाना।"
                    ),
                    threats = listOf(
                        "मुर्गी आहार के दामों में अचानक उछाल से प्रति किलो लागत का बढ़ जाना।",
                        "रानीखेत या गम्बोरो जैसी संक्रामक बीमारियों से शेड में नुकसान का डर।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "5,000+ मुर्गियों का आधुनिक पर्यावरण-नियंत्रित (EC) शेड।",
                        "अपना खुद का फीड मिक्सर प्लांट लगाकर स्थानीय मक्के से दाना बनाना (₹3/किग्रा बचत)।"
                    ),
                    weaknesses = listOf(
                        "कूलिंग पैड और एग्जॉस्ट फैन चलाने हेतु निर्बाध बिजली और डीजल जनरेटर का भारी खर्च।",
                        "आधुनिक शेड निर्माण में बड़ा पूंजीगत खर्च और समय पर ईएमआई चुकाने का दबाव।"
                    ),
                    opportunities = listOf(
                        "हाईवे के बड़े होटलों और ढाबों से सीधे थोक आपूर्ति का पक्का अनुबंध।",
                        "मंदी के समय मुर्गियों को प्रोसेस कर सुरक्षित रखने हेतु छोटा कोल्ड स्टोरेज बनाना।"
                    ),
                    threats = listOf(
                        "क्षेत्र में मुर्गियों का अधिक उत्पादन होने पर थोक दरों में भारी मंदी।",
                        "प्रदूषण नियंत्रण बोर्ड और स्थानीय निकायों के कड़े पर्यावरण नियम।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Extreme summer heatwave causing sudden bird heatstroke and mortality exceeding 15%.",
                    threatHi = "भीषण गर्मी और लू के थपेड़ों से मुर्गियों में हीटस्ट्रोक और मृत्यु दर 15% से ऊपर जाना।",
                    mitigationEn = "Install sprinkler misters on tin roofs, hang wet jute curtains, and add electrolytes to drinking water.",
                    mitigationHi = "शेड की छत पर पानी के फव्वारे लगाएं, गीली बोरियां लटकाएं और पीने के पानी में इलेक्ट्रोलाइट दें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Unreliable day-old-chick (DOC) delivery and poor chick quality with high first-week mortality.",
                    threatHi = "कमजोर चूजों की डिलीवरी और प्रथम सप्ताह में चूजों की अधिक मृत्यु दर।",
                    mitigationEn = "Pre-book vaccinated grade-A chicks strictly from certified ISO hatcheries.",
                    mitigationHi = "हमेशा प्रमाणित हैचरी से टीकाकरण युक्त ए-ग्रेड के स्वस्थ चूजे ही अग्रिम बुक करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Sharp escalation in soybean meal and yellow maize feed costs eroding farm profit margins.",
                    threatHi = "सोयाबीन खली और पीले मक्के की कीमतों में उछाल से दाने की लागत अत्यधिक बढ़ना।",
                    mitigationEn = "Incorporate up to 15% local broken rice and wheat bran into starter and finisher rations.",
                    mitigationHi = "दाने में स्थानीय स्तर पर उपलब्ध टूटे चावल (कनकी) और गेहूं के चोकर का 15% तक मिश्रण करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Roving poultry wholesale lifters forming cartels to depress purchase prices at the 40-day harvest mark.",
                    threatHi = "40 दिन की तैयार मुर्गियों के समय थोक खरीदारों द्वारा सिंडिकेट बनाकर मनमाने सस्ते भाव लगाना।",
                    mitigationEn = "Cultivate 4 independent village meat vendors and supply directly to avoid single-lifter blackmail.",
                    mitigationHi = "4 स्वतंत्र स्थानीय मांस विक्रेताओं से सीधा संपर्क रखें ताकि किसी एक व्यापारी की मनमानी न चले।"
                )
            ),
            competitorDensityEn = "Low to moderate density in village interior; higher density along highway corridors.",
            competitorDensityHi = "गाँव के भीतर कम प्रतिस्पर्धा; हाईवे और मुख्य संपर्क मार्गों पर मध्यम प्रतिस्पर्धा।",
            competitorProfileEn = "Small unscientific open-sided sheds vulnerable to disease and seasonal mortality spikes.",
            competitorProfileHi = "पारंपरिक खुले शेड जहां वैज्ञानिक ब्रूडिंग और जैव-सुरक्षा नियमों का अभाव रहता है।",
            competitiveMoatEn = "Strict biosecurity foot-dips, zero antibiotic residue, consistent 2.0 kg bird weight at 38 days, and fair digital weighing.",
            competitiveMoatHi = "कड़े जैव-सुरक्षा नियम, एंटीबायोटिक मुक्त स्वस्थ पक्षी, 38 दिन में 2 किग्रा वजन एवं पारदर्शी इलेक्ट्रॉनिक तौल।",
            benchmarkPricingEn = listOf(
                "Live Broiler Chicken: ₹135 - ₹160 per kg live weight (approx. 18-22% gross margin)",
                "Dressed Fresh Broiler Meat: ₹210 - ₹250 per kg (approx. 22-26% gross margin)",
                "Live Desi / Native Bird (Kuroiler): ₹280 - ₹360 per kg (approx. 40-45% gross margin)",
                "Desi Brown Free-Range Eggs: ₹12 - ₹15 per piece (approx. 45-50% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "जीवित ब्रायलर मुर्गा: ₹135 - ₹160 प्रति किलो (लगभग 18-22% सकल मुनाफा)",
                "ताजा कटा ब्रायलर चिकन: ₹210 - ₹250 प्रति किलो (लगभग 22-26% सकल मुनाफा)",
                "जीवित देशी मुर्गा (क्रोइलर/कड़कनाथ): ₹280 - ₹360 प्रति किलो (लगभग 40-45% सकल मुनाफा)",
                "देशी भूरे अंडे: ₹12 - ₹15 प्रति अंडा (लगभग 45-50% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "18% to 45% (commercial broiler ~20%, native desi ~42%)",
            grossMarginRangeHi = "18% से 45% (ब्रायलर पर ~20%, देशी मुर्गी पर ~42%)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Construct a simple 500-bird deep-litter shed using locally available bamboo and thatch.",
                    "Ensure strict Ranikhet (Lasota) day-7 and Gumboro day-14 eye-drop vaccination.",
                    "Sell birds directly to local dhabas and butcher shops at 1.8 to 2.0 kg weight to avoid holding costs."
                ),
                BudgetTier.SMALL to listOf(
                    "Set up two 1,000-bird sheds to run staggered batches, selling every 3 weeks for steady cash flow.",
                    "Install roof misting nozzles and high-speed exhaust fans to protect birds from summer heatwaves.",
                    "Sell bagged poultry manure at ₹80/bag to nearby sugarcane and vegetable growers."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Construct an insulated shed with cooling pads to achieve a feed conversion ratio (FCR) below 1.55.",
                    "Install a small on-farm feed mill to mix yellow maize and soya meal, saving 10% on feed expenses.",
                    "Open a company-owned retail chicken shop at the tehsil junction to capture retail margins."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "स्थानीय बांस और छप्पर का उपयोग कर 500 मुर्गियों का कम लागत वाला डीप-लीटर शेड बनाएं।",
                    "7वें दिन रानीखेत (लासोटा) और 14वें दिन गम्बोरो का आंख-ड्रॉप टीकाकरण सख्ती से करें।",
                    "दाना खर्च बचाने हेतु मुर्गियों का वजन 1.8 से 2.0 किग्रा होते ही तुरंत स्थानीय ढाबों को बेचें।"
                ),
                BudgetTier.SMALL to listOf(
                    "दो अलग 1,000 पक्षियों के शेड बनाएं ताकि हर 3 हफ्ते पर माल तैयार हो और नकदी प्रवाह बना रहे।",
                    "गर्मी में लू से बचाव हेतु शेड पर पानी के फव्वारे और तेज एग्जॉस्ट पंखे अवश्य लगाएं।",
                    "मुर्गी की खाद को बोरियों में भरकर ₹80/बोरी की दर से सब्जी उत्पादक किसानों को बेचें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "कूलिंग पैड युक्त आधुनिक शेड बनाएं ताकि फीड कन्वर्जन रेश्यो (FCR) 1.55 से नीचे रहे।",
                    "मक्के और सोया से दाना तैयार करने हेतु अपना फीड प्लांट लगाएं और 10% आहार खर्च बचाएं।",
                    "तहसील के चौराहे पर अपना खुदरा चिकन सेंटर खोलकर बिचौलियों का मुनाफा सीधे हासिल करें।"
                )
            )
        ),

        BusinessCategory.HANDICRAFTS to CategoryHeuristic(
            category = BusinessCategory.HANDICRAFTS,
            radiusEn = "10 to 25 km outreach radius covering village feasts, temple pilgrimage centers, and melas",
            radiusHi = "10 से 25 किमी का दायरा जिसमें ग्रामीण वैवाहिक समारोह, तीर्थ स्थल एवं मेले शामिल हैं",
            primaryChannelsEn = listOf(
                "Village wedding caterers and community feast (Bhandara) committees",
                "Pilgrimage temple complexes and religious tourism stalls",
                "State Rural Livelihood Mission (SRLM) and Saras Melas",
                "Khadi Gramodyog and eco-friendly packaging distributors"
            ),
            primaryChannelsHi = listOf(
                "ग्रामीण हलवाई, कैटरर्स एवं सामुदायिक भंडारा समितियां",
                "तीर्थ स्थल, मंदिर ट्रस्ट एवं धार्मिक पर्यटन बिक्री केंद्र",
                "राज्य ग्रामीण आजीविका मिशन (SRLM) एवं सरस मेले",
                "खादी ग्रामोद्योग एवं पर्यावरण-अनुकूल पैकेजिंग वितरक"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 3,500 to 6,000 residents across festive village events",
                BudgetTier.SMALL to "Estimated 12,000 to 25,000 event attendees and temple visitors",
                BudgetTier.MEDIUM to "Estimated 40,000 to 100,000 consumers across regional eco-catering networks"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "ग्रामीण वैवाहिक आयोजनों के लगभग 3,500 से 6,000 उपभोक्ता",
                BudgetTier.SMALL to "धार्मिक आयोजनों एवं मेलों के लगभग 12,000 से 25,000 आगंतुक",
                BudgetTier.MEDIUM to "क्षेत्रीय कैटरिंग नेटवर्क से जुड़े 40,000 से 1,00,000 उपभोक्ताओं का विशाल क्षेत्र"
            ),
            opportunityNicheEn = "Biodegradable areca nut and sal leaf tableware (pattal-dona) directly substituting single-use banned plastic plates at village weddings and community feasts.",
            opportunityNicheHi = "शादियों एवं भंडारों में प्रतिबंधित प्लास्टिक व थर्माकोल की जगह प्राकृतिक पत्तल-दोना का निर्माण एवं आपूर्ति।",
            unservedSegmentsEn = listOf(
                "Leak-proof hydraulic pressed leaf bowls (dona) for liquid curries and dal",
                "Handcrafted bamboo fruit baskets and decorative gift packaging for urban Diwali hampers",
                "Terracotta kulhads and curd pots supplied to tea stalls and sweet shops"
            ),
            unservedSegmentsHi = listOf(
                "दाल और रसेदार सब्जी हेतु मजबूत एवं लीक-प्रूफ हाइड्रोलिक प्रेस पत्तल-दोने",
                "त्योहारों पर उपहार पैकिंग हेतु मजबूत व कलात्मक बांस की टोकरियां",
                "चाय की दुकानों एवं मिष्ठान्न भंडारों हेतु मिट्टी के कुल्हड़ व दही की मटकियां"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Zero raw material import dependency; utilizes locally harvested fallen leaves and bamboo.",
                        "Negligible electric power requirement; sustainable low-capex village craft."
                    ),
                    weaknesses = listOf(
                        "Manual hand-shaping is time-intensive, capping daily output at 300-500 pieces.",
                        "Lack of direct access to high-paying urban buyers."
                    ),
                    opportunities = listOf(
                        "Government bans on single-use plastic plates driving massive local demand.",
                        "Securing stalls at district Saras and Gramshree fairs supported by NABARD."
                    ),
                    threats = listOf(
                        "Illegal smuggling of cheap non-biodegradable thermocol plates by local wholesalers.",
                        "Moisture fungus damage to raw leaves during prolonged monsoon rains."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "Double-die hydraulic leaf plate pressing machine yields 2,000+ plates daily.",
                        "Uniform heat and pressure produce rigid, leak-proof, odor-free tableware."
                    ),
                    weaknesses = listOf(
                        "Working capital locked in purchasing bulk dry leaves ahead of monsoon.",
                        "Heating element maintenance and periodic die cleaning requirements."
                    ),
                    opportunities = listOf(
                        "Annual supply contracts with prominent temple trust dining halls and pilgrim eateries.",
                        "Supplying branded terracotta kulhads to tea vendors along national highways."
                    ),
                    threats = listOf(
                        "Sudden spikes in raw areca/sal leaf transport freight from coastal/forest belts.",
                        "Unstable single-phase electricity in remote rural industrial sheds."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "Multi-machine cooperative cluster (5+ pressing units) with shrink-wrapping line.",
                        "Direct bulk supply contracts with district catering associations and hotel chains."
                    ),
                    weaknesses = listOf(
                        "High warehouse storage footprint needed for bulky finished paper/leaf tableware.",
                        "Management of seasonal raw leaf collectors across tribal forest fringes."
                    ),
                    opportunities = listOf(
                        "Exporting artisanal eco-friendly tableware to green catering chains in metropolitan cities.",
                        "PMEGP (Prime Minister's Employment Generation Programme) 35% rural subsidy."
                    ),
                    threats = listOf(
                        "Large urban industrial paper cup and plate manufacturers flooding rural mandis.",
                        "Regulatory freight transport inspections delaying inter-state leaf deliveries."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "कच्चे माल हेतु बाहर पर निर्भरता नहीं; स्थानीय स्तर पर पत्ते, मिट्टी व बांस उपलब्ध।",
                        "बिजली का नाममात्र खर्च; पर्यावरण-अनुकूल टिकाऊ ग्रामीण कुटीर व्यवसाय।"
                    ),
                    weaknesses = listOf(
                        "हाथ से बनाने में समय अधिक लगना, जिससे दैनिक उत्पादन 300-500 पीस तक सीमित रहना।",
                        "शहरों के बड़े व अच्छे मूल्य देने वाले खरीदारों से सीधा संपर्क न होना।"
                    ),
                    opportunities = listOf(
                        "सिंगल-यूज प्लास्टिक पर सरकारी प्रतिबंध से पत्तल-दोने की भारी स्थानीय मांग।",
                        "नाबार्ड एवं आजीविका मिशन द्वारा प्रायोजित सरस मेलों में मुफ्त बिक्री स्टॉल मिलना।"
                    ),
                    threats = listOf(
                        "कस्बों के व्यापारियों द्वारा सस्ते थर्माकोल के बर्तनों की अवैध बिक्री।",
                        "बरसात के मौसम में सीलन से सूखे पत्तों में फफूंद लगने का खतरा।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "डबल-डाई हाइड्रोलिक पत्तल मशीन से प्रतिदिन 2,000+ पत्तल बनाने की क्षमता।",
                        "तापमान और दबाव से एकदम मजबूत, लीक-प्रूफ और साफ-सुथरे बर्तनों का निर्माण।"
                    ),
                    weaknesses = listOf(
                        "बरसात से पहले सूखे पत्तों के बड़े स्टॉक हेतु अग्रिम पूंजी की आवश्यकता।",
                        "हीटिंग एलिमेंट बदलने एवं डाई की नियमित सफाई का रख-रखाव खर्च।"
                    ),
                    opportunities = listOf(
                        "प्रसिद्ध मंदिर ट्रस्टों एवं तीर्थ स्थलों के भंडारों हेतु नियमित वार्षिक अनुबंध।",
                        "राष्ट्रीय राजमार्ग के चाय ढाबों को शुद्ध मिट्टी के कुल्हड़ आपूर्ति करना।"
                    ),
                    threats = listOf(
                        "जंगल या तटीय क्षेत्रों से पत्तों के परिवहन भाड़े में अप्रत्याशित वृद्धि।",
                        "ग्रामीण क्षेत्रों में बिजली के कम वोल्टेज से मशीन के हीटर का धीमा चलना।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "5+ हाइड्रोलिक मशीनों की क्लस्टर इकाई एवं नमी-रोधी श्रिंक-पैकिंग लाइन।",
                        "जिला कैटरिंग एसोसिएशन और बड़े होटलों से सीधे थोक आपूर्ति के पक्के अनुबंध।"
                    ),
                    weaknesses = listOf(
                        "पत्तल-दोने हल्के लेकिन बड़े होने के कारण विशाल गोदाम की आवश्यकता।",
                        "जंगल से पत्ते बीनने वाले वनवासी समूहों के साथ मौसमी समन्वय की चुनौती।"
                    ),
                    opportunities = listOf(
                        "महानगरों की पर्यावरण-प्रेमी कैटरिंग कंपनियों को सीधे इको-फ्रेंडली उत्पाद भेजना।",
                        "पीएमईजीपी (PMEGP) योजना के तहत 35% ग्रामीण मार्जिन मनी सब्सिडी का लाभ उठाना।"
                    ),
                    threats = listOf(
                        "शहरों की बड़ी पेपर प्लेट फैक्ट्रियों द्वारा सस्ते कागजी उत्पादों की प्रतिस्पर्धा।",
                        "राज्यों के बीच माल परिवहन में चेकिंग से डिलीवरी में अनावश्यक विलंब।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Sharp collapse in leaf tableware demand during non-auspicious monsoon months with zero weddings.",
                    threatHi = "बरसात के चतुर्मास में शादियां न होने से पत्तल-दोने की मांग में भारी गिरावट।",
                    mitigationEn = "Focus on non-seasonal temple pilgrimage centers and manufacture bamboo gift baskets during the monsoon.",
                    mitigationHi = "बरसात में नियमित धार्मिक तीर्थ स्थलों पर ध्यान दें और त्योहारी बांस की टोकरियां बनाएं।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Fungus decay spoiling stacked raw sal and areca leaves during extended rainy periods.",
                    threatHi = "लगातार बारिश के दौरान रखे हुए सूखे पत्तों में सीलन व फफूंद से सड़न पैदा होना।",
                    mitigationEn = "Store leaves in elevated wooden pallets inside a well-ventilated dry storage room with silica gel packs.",
                    mitigationHi = "पत्तों को जमीन पर रखने के बजाय लकड़ी के तख्तों पर रखें और हवादार सूखा कमरा उपयोग करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Shortage of premium uncut raw areca sheath or bamboo culms driving up procurement prices.",
                    threatHi = "अच्छे बड़े सुपारी पत्तों या मजबूत बांस की कमी से कच्चे माल की कीमतों में उछाल।",
                    mitigationEn = "Establish long-term supply ties with tribal self-help groups for sustainable volume harvesting.",
                    mitigationHi = "स्थानीय स्वयं सहायता समूहों से दीर्घकालिक अनुबंध कर उचित मूल्य पर कच्चा माल सुरक्षित करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Unpaid receivables when a single wedding caterer fails to settle payment after a multi-day event.",
                    threatHi = "शादी के बड़े आयोजन के बाद कैटरर द्वारा कई महीनों तक भुगतान न करना।",
                    mitigationEn = "Require full 100% payment upon delivery of tableware cartons to the event venue.",
                    mitigationHi = "समारोह स्थल पर माल उतारते समय ही 100% भुगतान लेने का सख्त नियम बनाएं।"
                )
            ),
            competitorDensityEn = "Low density across the block; most village feasts still use poorly pressed manual leaves.",
            competitorDensityHi = "प्रखंड में कम प्रतिस्पर्धा; अधिकांश आयोजनों में आज भी हाथ से सिली साधारण पत्तल उपयोग होती है।",
            competitorProfileEn = "Traditional artisans stitching uneven leaf plates with wooden splints that leak liquids.",
            competitorProfileHi = "पारंपरिक कारीगर जो तीलियों से पत्ते सिलते हैं, जिनसे दाल और रसा नीचे टपकता है।",
            competitiveMoatEn = "Hydraulic pressed high-rigidity tableware that is 100% leak-proof, sterilized, and shrink-wrapped in 100-piece packs.",
            competitiveMoatHi = "हाइड्रोलिक प्रेस से निर्मित मजबूत पत्तल-दोना जो कभी लीक नहीं करता और 100-100 पीस के पैकेट में पैक रहता है।",
            benchmarkPricingEn = listOf(
                "Areca / Sal Leaf Plates (12-inch): ₹1.60 - ₹2.40 per piece (approx. 45-50% gross margin)",
                "Pressed Leaf Bowls / Dona (6-inch): ₹0.75 - ₹1.20 per piece (approx. 50-55% gross margin)",
                "Handwoven Bamboo Baskets: ₹120 - ₹220 per piece (approx. 55-60% gross margin)",
                "Terracotta Tea Kulhads (Pack of 100): ₹90 - ₹140 (approx. 60-65% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "पत्तल प्लेट (12 इंच साइज): ₹1.60 - ₹2.40 प्रति पीस (लगभग 45-50% सकल मुनाफा)",
                "प्रेस किया हुआ दोना (6 इंच साइज): ₹0.75 - ₹1.20 प्रति पीस (लगभग 50-55% सकल मुनाफा)",
                "हस्तनिर्मित बांस की टोकरी: ₹120 - ₹220 प्रति पीस (लगभग 55-60% सकल मुनाफा)",
                "मिट्टी के कुल्हड़ (100 पीस का बंडल): ₹90 - ₹140 (लगभग 60-65% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "45% to 65% across artisanal and pressed eco-products",
            grossMarginRangeHi = "45% से 65% (पारंपरिक कला एवं मशीनी पत्तल उत्पादों पर उत्कृष्ट लाभ)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Start with manual foot-operated leaf cutting and join the local Village Livelihood Mission (SHG).",
                    "Target upcoming local village wedding seasons by taking 5,000-plate orders with 50% advance.",
                    "Supply terracotta tea kulhads to tea stalls at the village junction for daily cash flow."
                ),
                BudgetTier.SMALL to listOf(
                    "Purchase a dual-die hydraulic hot-press leaf plate machine with interchangeable bowl dies.",
                    "Register on the government GeM portal and apply for stalls at regional Saras Melas.",
                    "Stockpile 30,000 dry leaves prior to the monsoon season in a moisture-free raised shed."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Install a 4-station hydraulic press line with automatic temperature controllers and shrink packaging.",
                    "Form supply agreements with prominent catering associations in the neighboring district.",
                    "Apply for PMEGP 35% subsidy to finance warehouse infrastructure and bulk raw leaf procurement."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "शुरुआत में हाथ से कटिंग कर स्थानीय महिला आजीविका समूह (SHG) के साथ काम शुरू करें।",
                    "गाँव की आगामी शादियों में हलवाइयों से मिलकर 5,000 पत्तल का ऑर्डर 50% अग्रिम लेकर लें।",
                    "दैनिक नकद आय हेतु गाँव के चौराहे की चाय दुकानों को नियमित कुल्हड़ सप्लाई करें।"
                ),
                BudgetTier.SMALL to listOf(
                    "डबल-डाई हाइड्रोलिक हॉट-प्रेस पत्तल मशीन खरीदें जिसमें पत्तल और दोना दोनों बन सकें।",
                    "सरकारी जेम (GeM) पोर्टल पर पंजीकरण कराएं और क्षेत्रीय सरस मेलों में भाग लें।",
                    "बरसात से पहले 30,000 सूखे पत्तों का अग्रिम स्टॉक सुरक्षित सूखे शेड में रखें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "स्वचालित तापमान नियंत्रण एवं श्रिंक पैकिंग युक्त 4 मशीनों की आधुनिक लाइन लगाएं।",
                    "पड़ोसी जिले के बड़े कैटरिंग एसोसिएशनों से साल भर की आपूर्ति का सीधा अनुबंध करें।",
                    "गोदाम निर्माण और थोक पत्ते खरीदने हेतु पीएमईजीपी (PMEGP) 35% सब्सिडी का लाभ लें।"
                )
            )
        ),

        BusinessCategory.AGRO_SERVICES to CategoryHeuristic(
            category = BusinessCategory.AGRO_SERVICES,
            radiusEn = "5 to 15 km command area radius serving small and marginal farmers across 6 to 10 villages",
            radiusHi = "5 से 15 किमी का कमांड क्षेत्र जिसमें 6 से 10 गांवों के छोटे व सीमांत किसान शामिल हैं",
            primaryChannelsEn = listOf(
                "Farm-gate on-call equipment hiring via mobile phone booking",
                "Kisan Seva Kendra and Village Panchayat custom hiring register",
                "Farmer Producer Organization (FPO) shared machinery pool",
                "Village WhatsApp farmer groups and seasonal agri-clinics"
            ),
            primaryChannelsHi = listOf(
                "किसान के खेत पर मोबाइल फोन द्वारा बुकिंग आधारित उपकरण सेवा",
                "किसान सेवा केंद्र एवं ग्राम पंचायत कस्टम हायरिंग सेवा रजिस्टर",
                "किसान उत्पादक संगठन (FPO) का साझा मशीनरी केंद्र",
                "गाँव के किसान व्हाट्सएप ग्रुप एवं मौसमी कृषि सूचना केंद्र"
            ),
            consumerBaseByTierEn = mapOf(
                BudgetTier.MICRO to "Estimated 80 to 150 smallholder farming families (< 2 acres each)",
                BudgetTier.SMALL to "Estimated 300 to 600 farming households across 5 to 7 villages",
                BudgetTier.MEDIUM to "Estimated 1,000 to 2,500 farmers covering the entire block command area"
            ),
            consumerBaseByTierHi = mapOf(
                BudgetTier.MICRO to "लगभग 80 से 150 छोटे व सीमांत किसान परिवार (2 एकड़ से कम भूमि वाले)",
                BudgetTier.SMALL to "5 से 7 गांवों के लगभग 300 से 600 कृषक परिवार",
                BudgetTier.MEDIUM to "पूरे प्रखंड के लगभग 1,000 से 2,500 किसान परिवारों का विस्तृत नेटवर्क"
            ),
            opportunityNicheEn = "Farm-gate custom hiring of power tillers, mechanized weeders, and battery sprayers during acute agricultural labor shortages at peak sowing and weeding windows.",
            opportunityNicheHi = "बुवाई और निराई के समय मजदूरों की भारी कमी के दौरान पावर टिलर, वीडर और बैटरी स्प्रेयर से खेत पर जाकर किफायती सेवा देना।",
            unservedSegmentsEn = listOf(
                "High-speed battery knapsack spraying for nano-urea and bio-pesticides (charging per tank)",
                "Inter-cultivation weeding in narrow row crops (sugarcane, cotton, vegetables) where tractors cannot enter",
                "Portable mini-irrigation diesel pump sets rented by the hour for tail-end fields"
            ),
            unservedSegmentsHi = listOf(
                "नैनो यूरिया और कीटनाशक छिड़काव हेतु बैटरी स्प्रेयर सेवा (प्रति टंकी शुल्क आधारित)",
                "गन्ना, कपास व सब्जी की तंग कतारों में पावर वीडर से निराई-गुड़ाई, जहां ट्रैक्टर नहीं जा सकता",
                "नहर के अंतिम छोर वाले खेतों हेतु पोर्टेबल डीजल पंप-सेट का प्रति घंटा किराया"
            ),
            swotByTierEn = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "Very low initial investment: 2 battery sprayers + manual seed drill + brush cutter.",
                        "Zero diesel fuel expense; immediate cash collection per acre or per tank."
                    ),
                    weaknesses = listOf(
                        "Equipment range is limited to crop care and cannot handle heavy plowing.",
                        "Seasonal peak labor concentration in just 3-4 months per year."
                    ),
                    opportunities = listOf(
                        "Adding drone spraying tie-ups or high-pressure sprayers for fruit orchards.",
                        "Tie-up with local pesticide dealer to earn referral commission."
                    ),
                    threats = listOf(
                        "Farmer demand entirely dependent on timely monsoon rains.",
                        "Battery degradation requiring replacement after 2 years."
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "7 to 9 HP diesel Power Tiller with rotary cultivator, trailer, and pump attachment.",
                        "Highly agile machine that easily operates in small fragmented fields and wet paddy puddling."
                    ),
                    weaknesses = listOf(
                        "Mechanical breakdown during the 20-day peak sowing window can cause lost season revenue.",
                        "Physical fatigue for the operator navigating muddy paddy soil."
                    ),
                    opportunities = listOf(
                        "Offering multi-crop mini thresher services right at the farm threshing floor.",
                        "Using the power tiller trailer for off-season village brick and sand transport."
                    ),
                    threats = listOf(
                        "Diesel price inflation eroding hourly rental margins.",
                        "Farmers demanding harvest-time post-payment leading to working capital delays."
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "Full Custom Hiring Centre (CHC) with 45-50 HP Tractor, Rotavator, Laser Leveler, and Seed Drill.",
                        "Massive capacity covering 15-20 acres per day during peak land preparation."
                    ),
                    weaknesses = listOf(
                        "Heavy bank loan EMI burden requiring minimum 800 hours of annual tractor utilization.",
                        "Driver wages and operator retention during intense harvesting seasons."
                    ),
                    opportunities = listOf(
                        "Sub-Mission on Agricultural Mechanization (SMAM) 40% to 50% government subsidy.",
                        "Providing precision laser land leveling which saves farmers 25% on irrigation water."
                    ),
                    threats = listOf(
                        "Deficient monsoon leading to drastically reduced sown area in the block.",
                        "Severe price undercutting by competing tractor owners during late sowing."
                    )
                )
            ),
            swotByTierHi = mapOf(
                BudgetTier.MICRO to SwotPack(
                    strengths = listOf(
                        "बहुत कम प्रारंभिक पूंजी: 2 बैटरी स्प्रेयर + सीड ड्रिल + ब्रश कटर से शुरुआत।",
                        "डीजल का कोई खर्च नहीं; प्रति टंकी अथवा प्रति एकड़ तत्काल नकद आय की प्राप्ति।"
                    ),
                    weaknesses = listOf(
                        "उपकरण केवल कीटनाशक छिड़काव और घास कटाई तक सीमित; भारी जुताई नहीं कर सकते।",
                        "वर्ष में केवल 3-4 महीने ही मुख्य सीजनल काम मिलना।"
                    ),
                    opportunities = listOf(
                        "बागवानी वाले क्षेत्रों में बड़े प्रेशर स्प्रेयर अथवा ड्रोन छिड़काव सेवा जोड़ना।",
                        "स्थानीय खाद-बीज विक्रेता से अनुबंध कर दवा बिक्री पर कमीशन अर्जित करना।"
                    ),
                    threats = listOf(
                        "किसानों की मांग पूरी तरह समय पर मानसून और बारिश पर निर्भर।",
                        "2 साल बाद बैटरी के कमजोर होने से नई बैटरी बदलने का खर्च।"
                    )
                ),
                BudgetTier.SMALL to SwotPack(
                    strengths = listOf(
                        "7 से 9 एचपी का डीजल पावर टिलर, रोटावेटर, ट्रॉली एवं पानी पंप अटैचमेंट।",
                        "छोटे टुकड़ों वाले खेतों और धान के गीले कीचड़ (लेह) में आसानी से जुताई करने में सक्षम।"
                    ),
                    weaknesses = listOf(
                        "बुवाई के 20 दिनों के मुख्य समय में मशीन खराब होने पर सीजनल आमदनी का नुकसान।",
                        "कीचड़ में लगातार टिलर के पीछे चलने में ऑपरेटर की शारीरिक थकान।"
                    ),
                    opportunities = listOf(
                        "फसल कटाई के समय किसान के खलिहान पर जाकर मिनी थ्रेशर से मड़ाई सेवा देना।",
                        "खाली समय में टिलर ट्रॉली का उपयोग गाँव में ईंट, बालू और अनाज ढुलाई में करना।"
                    ),
                    threats = listOf(
                        "डीजल की कीमतों में वृद्धि से प्रति घंटे के लाभ मार्जिन में कमी।",
                        "किसानों द्वारा फसल कटाई तक उधारी मांगने से कार्यशील पूंजी का अटकना।"
                    )
                ),
                BudgetTier.MEDIUM to SwotPack(
                    strengths = listOf(
                        "कस्टम हायरिंग केंद्र (CHC): 45-50 एचपी ट्रैक्टर, रोटावेटर, लेजर लेवलर एवं सीड ड्रिल।",
                        "पीक सीजन में प्रतिदिन 15 से 20 एकड़ खेत तैयार करने की विशाल क्षमता।"
                    ),
                    weaknesses = listOf(
                        "बैंक ऋण की बड़ी मासिक ईएमआई, जिसे चुकाने हेतु साल में न्यूनतम 800 घंटे काम जरूरी।",
                        "कुशल ड्राइवर का वेतन एवं पीक सीजन में ऑपरेटर को रोके रखने की चुनौती।"
                    ),
                    opportunities = listOf(
                        "कृषि यंत्रीकरण उप-मिशन (SMAM) के तहत 40% से 50% तक सरकारी सब्सिडी प्राप्त करना।",
                        "लेजर लैंड लेवलर द्वारा खेत समतलीकरण, जिससे किसान का 25% सिंचाई पानी बचता है।"
                    ),
                    threats = listOf(
                        "कमजोर मानसून या सूखे के कारण ब्लॉक में फसलों के रकबे में भारी गिरावट।",
                        "सीजन के अंत में अन्य ट्रैक्टर मालिकों द्वारा किराए की दरों में भारी कटौती।"
                    )
                )
            ),
            threats = listOf(
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SEASONAL,
                    threatEn = "Zero farm machinery hiring demand during the 4 idle months between crop cycles.",
                    threatHi = "फसल चक्रों के बीच 4 खाली महीनों में कृषि यंत्रों की मांग बिल्कुल शून्य हो जाना।",
                    mitigationEn = "Equip tractors/tillers with transport trolleys and water tankers for village infrastructure and construction work.",
                    mitigationHi = "खाली महीनों में ट्रॉली और पानी टैंकर जोड़कर निर्माण सामग्री ढुलाई का काम करें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SUPPLY,
                    threatEn = "Catastrophic gearbox or hydraulic pump failure during peak 15-day paddy preparation.",
                    threatHi = "धान की बुवाई के मुख्य 15 दिनों में गियरबॉक्स या हाइड्रोलिक पंप का अचानक खराब होना।",
                    mitigationEn = "Perform complete pre-season servicing and keep spare rotavator blades, v-belts, and oil filters.",
                    mitigationHi = "सीजन शुरू होने से 20 दिन पहले पूरी सर्विसिंग कराएं और अतिरिक्त ब्लेड व बेल्ट पास रखें।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_RAW_MATERIAL,
                    threatEn = "Diesel price inflation eroding tractor operational margins when hourly rates are fixed.",
                    threatHi = "डीजल के दाम बढ़ने पर तय किए गए प्रति घंटे के किराए में से शुद्ध मुनाफे का घटना।",
                    mitigationEn = "Adopt a transparent fuel-indexed hourly rate (charging fixed service charge + actual fuel consumption).",
                    mitigationHi = "पारदर्शी दर लागू करें: या तो निश्चित सेवा शुल्क + किसान का डीजल, या ईंधन आधारित दर।"
                ),
                ThreatVectorItem(
                    vectorKey = MultilingualDictionary.VECTOR_SINGLE_BUYER,
                    threatEn = "Large local farmers insisting on deferred payment until harvest, locking critical cash for fuel.",
                    threatHi = "बड़े किसानों द्वारा फसल कटने के बाद भुगतान की शर्त रखकर नकदी फंसा देना।",
                    mitigationEn = "Require mandatory fuel cost payment upfront prior to plowing; offer 5% discount for instant cash.",
                    mitigationHi = "खेत में मशीन उतारने से पहले डीजल का खर्च नकद लेने का नियम बनाएं; तुरंत भुगतान पर 5% छूट दें।"
                )
            ),
            competitorDensityEn = "Moderate density; conventional large tractors are available but cannot access narrow village plots.",
            competitorDensityHi = "मध्यम प्रतिस्पर्धा; बड़े ट्रैक्टर उपलब्ध हैं लेकिन वे संकरे रास्तों और छोटे खेतों में नहीं जा सकते।",
            competitorProfileEn = "Local tractor owners offering generic plowing without specialized implements like rotavators or levelers.",
            competitorProfileHi = "पारंपरिक ट्रैक्टर मालिक जो केवल साधारण कल्टीवेटर चलाते हैं और आधुनिक यंत्रों का अभाव है।",
            competitiveMoatEn = "Agile compact machinery tailored for small 0.5-2 acre plots, timely arrival, and fuel-efficient modern rotavators.",
            competitiveMoatHi = "छोटे खेतों के लिए उपयुक्त कॉम्पैक्ट मशीनें, समय की पाबंदी एवं उच्च गुणवत्ता की बारीक जुताई।",
            benchmarkPricingEn = listOf(
                "Power Tiller Rotary Tilling: ₹380 - ₹500 per hour (approx. 45-50% gross margin)",
                "Tractor Rotavator Plowing: ₹850 - ₹1,100 per hour (approx. 38-42% gross margin)",
                "Battery Backpack Spraying: ₹75 - ₹100 per tank / ₹250 per acre (approx. 60-65% gross margin)",
                "Portable Irrigation Pump Hiring: ₹120 - ₹160 per hour (approx. 40-45% gross margin)"
            ),
            benchmarkPricingHi = listOf(
                "पावर टिलर रोटावेटर जुताई: ₹380 - ₹500 प्रति घंटा (लगभग 45-50% सकल मुनाफा)",
                "ट्रैक्टर रोटावेटर जुताई: ₹850 - ₹1,100 प्रति घंटा (लगभग 38-42% सकल मुनाफा)",
                "बैटरी स्प्रेयर छिड़काव सेवा: ₹75 - ₹100 प्रति टंकी / ₹250 प्रति एकड़ (लगभग 60-65% सकल मुनाफा)",
                "पोर्टेबल सिंचाई पंप किराया: ₹120 - ₹160 प्रति घंटा (लगभग 40-45% सकल मुनाफा)"
            ),
            grossMarginRangeEn = "38% to 65% across specialized farm machinery rental",
            grossMarginRangeHi = "38% से 65% (स्प्रेयर पर 60%+, भारी ट्रैक्टर पर ~40%)",
            recommendationsByTierEn = mapOf(
                BudgetTier.MICRO to listOf(
                    "Start with 2 commercial battery sprayers and an 8-tooth brush cutter for farm weeding.",
                    "Charge ₹80 per spray tank and market directly through local village pesticide retailers.",
                    "Collect cash on completion of each farm plot to maintain daily working capital."
                ),
                BudgetTier.SMALL to listOf(
                    "Procure an 8 HP diesel Power Tiller with rotavator and 1.5-ton trailer attachments.",
                    "Focus on wet-paddy puddling in small fragmented plots where large tractors get bogged down.",
                    "Use the power tiller trailer for village transport and logistics during off-season months."
                ),
                BudgetTier.MEDIUM to listOf(
                    "Establish a formal Custom Hiring Centre (CHC) with a 45 HP tractor and laser land leveler.",
                    "Apply for the SMAM scheme 40% government subsidy through the District Agriculture Office.",
                    "Create a farmer booking WhatsApp group with scheduled 24-hour time slots to maximize daily runtime."
                )
            ),
            recommendationsByTierHi = mapOf(
                BudgetTier.MICRO to listOf(
                    "2 व्यावसायिक बैटरी स्प्रेयर और खरपतवार हटाने हेतु ब्रश कटर से तुरंत काम शुरू करें।",
                    "प्रति टंकी ₹80 का पारदर्शी शुल्क रखें और स्थानीय कीटनाशक दुकानदारों से संपर्क करें।",
                    "प्रत्येक खेत का काम पूरा होते ही नकद भुगतान लें ताकि दैनिक खर्च बना रहे।"
                ),
                BudgetTier.SMALL to listOf(
                    "8 एचपी का डीजल पावर टिलर, रोटावेटर एवं 1.5 टन की छोटी ट्रॉली खरीदें।",
                    "धान की रोपाई में लेह (Puddling) करने पर ध्यान दें जहां भारी ट्रैक्टर कीचड़ में फंस जाते हैं।",
                    "खाली समय में गाँव के निर्माण कार्यों में सामान ढुलाई हेतु ट्रॉली का उपयोग करें।"
                ),
                BudgetTier.MEDIUM to listOf(
                    "45 एचपी ट्रैक्टर, रोटावेटर और लेजर लेवलर के साथ पूर्ण कस्टम हायरिंग सेंटर स्थापित करें।",
                    "जिला कृषि कार्यालय के माध्यम से SMAM योजना में 40% सब्सिडी हेतु आवेदन करें।",
                    "किसानों का व्हाट्सएप ग्रुप बनाएं और 24 घंटे पहले स्लॉट बुक कर प्रतिदिन 12 घंटे का उपयोग सुनिश्चित करें।"
                )
            )
        )
    )

    fun getHeuristic(category: BusinessCategory): CategoryHeuristic {
        return heuristics[category] ?: heuristics[BusinessCategory.DAIRY]!!
    }

    fun getAllHeuristics(): Map<BusinessCategory, CategoryHeuristic> = heuristics

    fun getSwotAnalysis(
        category: BusinessCategory,
        budgetTier: BudgetTier,
        language: LanguageCode
    ): SwotAnalysis {
        val h = getHeuristic(category)
        val pack = if (language == LanguageCode.HI) {
            h.swotByTierHi[budgetTier] ?: h.swotByTierHi[BudgetTier.MICRO]!!
        } else {
            h.swotByTierEn[budgetTier] ?: h.swotByTierEn[BudgetTier.MICRO]!!
        }
        return SwotAnalysis(
            strengths = pack.strengths,
            weaknesses = pack.weaknesses,
            opportunities = pack.opportunities,
            threats = pack.threats
        )
    }

    fun getThreatsList(
        category: BusinessCategory,
        language: LanguageCode
    ): List<String> {
        val h = getHeuristic(category)
        return h.threats.map { item ->
            val vectorName = MultilingualDictionary.getThreatVectorName(item.vectorKey, language)
            if (language == LanguageCode.HI) {
                "• [$vectorName]: ${item.threatHi} — समाधान: ${item.mitigationHi}"
            } else {
                "• [$vectorName]: ${item.threatEn} — Mitigation: ${item.mitigationEn}"
            }
        }
    }

    fun getRecommendations(
        category: BusinessCategory,
        budgetTier: BudgetTier,
        language: LanguageCode
    ): List<String> {
        val h = getHeuristic(category)
        return if (language == LanguageCode.HI) {
            h.recommendationsByTierHi[budgetTier] ?: h.recommendationsByTierHi[BudgetTier.MICRO]!!
        } else {
            h.recommendationsByTierEn[budgetTier] ?: h.recommendationsByTierEn[BudgetTier.MICRO]!!
        }
    }

    fun buildMarketReachNarrative(
        category: BusinessCategory,
        budgetTier: BudgetTier,
        location: LocationInfo,
        language: LanguageCode
    ): String {
        val h = getHeuristic(category)
        val loc = location.sanitized(language)
        val radius = if (language == LanguageCode.HI) h.radiusHi else h.radiusEn
        val consumerBase = if (language == LanguageCode.HI) {
            h.consumerBaseByTierHi[budgetTier] ?: ""
        } else {
            h.consumerBaseByTierEn[budgetTier] ?: ""
        }
        val channels = if (language == LanguageCode.HI) h.primaryChannelsHi else h.primaryChannelsEn
        val channelStr = channels.joinToString("; ")

        return if (language == LanguageCode.HI) {
            "${loc.village} (${loc.block}, ${loc.district}) के लिए लक्षित बाजार: $radius। " +
                    "उपभोक्ता आधार: $consumerBase। " +
                    "मुख्य वितरण माध्यम: $channelStr।"
        } else {
            "Targeted market reach for ${loc.village} (${loc.block}, ${loc.district}): $radius. " +
                    "Consumer Base: $consumerBase. " +
                    "Primary Distribution Channels: $channelStr."
        }
    }

    fun buildOpportunityNarrative(
        category: BusinessCategory,
        budgetTier: BudgetTier,
        location: LocationInfo,
        language: LanguageCode
    ): String {
        val h = getHeuristic(category)
        val loc = location.sanitized(language)
        val niche = if (language == LanguageCode.HI) h.opportunityNicheHi else h.opportunityNicheEn
        val unserved = if (language == LanguageCode.HI) h.unservedSegmentsHi else h.unservedSegmentsEn
        val unservedStr = unserved.joinToString("; ")

        return if (language == LanguageCode.HI) {
            "${loc.block} प्रखंड में प्राथमिक व्यापारिक अवसर: $niche। " +
                    "अछूते स्थानीय क्षेत्र एवं विशिष्ट मांग: $unservedStr।"
        } else {
            "Primary market opportunity in ${loc.block} block: $niche. " +
                    "Unserved local niches and high-margin segments: $unservedStr."
        }
    }

    fun buildCompetitorMappingNarrative(
        category: BusinessCategory,
        location: LocationInfo,
        language: LanguageCode
    ): String {
        val h = getHeuristic(category)
        val loc = location.sanitized(language)
        val density = if (language == LanguageCode.HI) h.competitorDensityHi else h.competitorDensityEn
        val profile = if (language == LanguageCode.HI) h.competitorProfileHi else h.competitorProfileEn
        val moat = if (language == LanguageCode.HI) h.competitiveMoatHi else h.competitiveMoatEn

        return if (language == LanguageCode.HI) {
            "${loc.block} प्रखंड में प्रतिस्पर्धा स्तर: $density। " +
                    "मौजूदा प्रतियोगी स्वरूप: $profile। " +
                    "प्रतिस्पर्धी बढ़त एवं सुरक्षात्मक रणनीति: $moat।"
        } else {
            "Competitor Density in ${loc.block} block: $density. " +
                    "Existing Competitor Profile: $profile. " +
                    "Competitive Moat & Differentiation: $moat."
        }
    }

    fun buildPricingStrategyNarrative(
        category: BusinessCategory,
        language: LanguageCode
    ): String {
        val h = getHeuristic(category)
        val margins = if (language == LanguageCode.HI) h.grossMarginRangeHi else h.grossMarginRangeEn
        val prices = if (language == LanguageCode.HI) h.benchmarkPricingHi else h.benchmarkPricingEn
        val priceListStr = prices.joinToString("; ")
        val creditPolicy = MultilingualDictionary.getCreditPolicyAdvice(language)

        return if (language == LanguageCode.HI) {
            "अनुशंसित इकाई मूल्य एवं बेंचमार्क: $priceListStr। " +
                    "अपेक्षित सकल मुनाफा: $margins। " +
                    creditPolicy
        } else {
            "Recommended Unit Pricing Benchmarks: $priceListStr. " +
                    "Expected Gross Profit Margin: $margins. " +
                    creditPolicy
        }
    }
}
