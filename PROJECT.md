# Project: Rural Micro-Advisory & Smart Financial Structuring App

## Architecture
A native Android application built in Kotlin with Jetpack Compose, featuring an offline-first hybrid architecture:
- **Core Financial Engine** (`com.ruraladvisory.finance`): Pure Kotlin mathematical domain engine calculating project cost (10x margin), 90% loan structuring, scheme routing (Micro Finance vs. Term Loan), loan caps, moratorium simple interest, straight-line quarterly principal amortization, and CAPEX/Working Capital breakdowns.
- **Feasibility & Heuristic NLP Engine** (`com.ruraladvisory.advisory`): Domain-driven offline heuristic advisor providing synthesized reports across 6 dimensions (Market Reach, Opportunity, SWOT, Threats, Competitor Mapping, Pricing) for 7 rural trade sectors, with optional Gemini LLM fallback.
- **Localization Engine** (`com.ruraladvisory.localization`): Full bilingual support (English and Hindi) with in-app language toggle without device reboot.
- **Audio & TTS Engine** (`com.ruraladvisory.audio`): Android Text-to-Speech integration providing audio synthesis of feasibility and loan summaries for rural micro-entrepreneurs.
- **UI & Presentation Layer** (`com.ruraladvisory.ui`): Modern Jetpack Compose UI tailored for rural accessibility: high-contrast WCAG AAA color palette, large touch targets, interactive sliders, visual cards, qualification badges, 2x2 SWOT grid, amortization tables, and share functionality.

```
+-------------------------------------------------------------------------+
|                              Compose UI                                 |
|  (Input Form, Sliders, Language Toggle, Audio Cues, Dashboard, Reports) |
+------------------------------------+------------------------------------+
                                     |
                                     v
+------------------------------------+------------------------------------+
|                         Presentation / ViewModel                        |
+------------------+----------------------------------+-------------------+
                   |                                  |
                   v                                  v
+----------------------------------+  +----------------------------------+
|      Financial Engine (M1)       |  |     Feasibility Engine (M2)      |
| - Project Cost (10x Margin)      |  | - 7 Rural Business Categories    |
| - Scheme Router (Micro vs Term)  |  | - 6 Analytical Dimensions        |
| - Moratorium & Amortization      |  | - Offline Heuristic Knowledge    |
| - CAPEX / OPEX Breakdown         |  | - Optional LLM Enhancer          |
+----------------------------------+  +----------------------------------+
                   |                                  |
                   +-----------------+----------------+
                                     |
                                     v
                      +------------------------------+
                      | Localization & Audio Engines |
                      | - English / Hindi Taxonomy   |
                      | - Native TTS Audio Summaries |
                      +------------------------------+
```

## Feature Inventory
Every feature identified during specification mining and original requirements is cataloged below with its assigned milestone.

| # | Feature | Description | Milestone | Source |
|---|---------|-------------|-----------|--------|
| 1 | Android Project Scaffolding | Gradle 9.6/AGP 9.4/Kotlin 2.2 project setup, SDK configuration, and local.properties | M0 | Survey |
| 2 | Project Cost Scaling | Compute Feasible Project Cost = Margin Capital * 10 | M1 | ORIGINAL_REQUEST §R2.1 |
| 3 | Uncapped Loan Calculation | Compute 90% loan amount before applying scheme ceiling | M1 | ORIGINAL_REQUEST §R2.1 |
| 4 | Micro Finance Scheme Routing | Route Project Cost <= ₹1.40 Lakh to Micro Finance Scheme (6.5%, 3-yr tenure, 1-qtr moratorium, ₹1.25L cap) | M1 | ORIGINAL_REQUEST §R2.2 |
| 5 | Term Loan Scheme Routing | Route ₹1.40L < Project Cost <= ₹50.00L to Term Loan Scheme (8.0%, 7-yr tenure, 2-qtr moratorium, ₹45.0L cap) | M1 | ORIGINAL_REQUEST §R2.2 |
| 6 | Boundary Loan Cap Enforcement | Enforce ₹1.25L cap at Margin ₹14,000 (uncapped ₹1.26L) and ₹45.0L cap at Margin ₹5,00,000 | M1 | ORIGINAL_REQUEST §R2.2, R4.1 |
| 7 | Margin Threshold Validation | Reject margin < ₹1,000 (Cost < ₹10k) and margin > ₹5,00,000 (Cost > ₹50L) with clear error states | M1 | ORIGINAL_REQUEST §R2.2 |
| 8 | Moratorium Interest Accrual | Calculate quarterly simple interest during moratorium (Q1 for Micro; Q1-Q2 for Term) | M1 | ORIGINAL_REQUEST §R2.3 |
| 9 | Straight-Line Quarterly Amortization | Generate post-moratorium equal principal installments with penny-rounding balancing down to ₹0.00 closing balance | M1 | ORIGINAL_REQUEST §R2.3 |
| 10 | Cost & OPEX Breakdown | Generate 70% CAPEX, 25% Working Capital, 5% Contingency, and quarterly OPEX breakdown | M1 | ORIGINAL_REQUEST §R2.3 |
| 11 | 7 Trade Sector Heuristics | Sector-calibrated advisory models for Dairy, Retail, Food Processing, Textiles, Poultry, Handicrafts, Agro-Services | M2 | ORIGINAL_REQUEST §R1 |
| 12 | 6 Feasibility Dimensions | Market Reach (5-10km), Opportunity, SWOT (budget-calibrated), Threats, Competitor Mapping, and Pricing Strategy | M2 | ORIGINAL_REQUEST §R1 |
| 13 | Offline Heuristic NLP Engine | Fully offline deterministic advisory knowledge base executing in <50ms with zero network dependencies | M2 | ORIGINAL_REQUEST §R1 |
| 14 | Optional Gemini LLM Fallback | Hybrid integration: attempts LLM enrichment when online/configured, fails over silently to offline engine | M2 | ORIGINAL_REQUEST §R1 |
| 15 | Multilingual Taxonomy | Complete 1:1 English and Hindi translation dictionary for categories, SWOT, dimensions, and financial terms | M2 | ORIGINAL_REQUEST §R1 |
| 16 | Rural High-Contrast UI Theme | WCAG AAA compliant palette (`#1B5E20` forest green, `#FDFBF7` cream), large touch targets (>=56dp) | M3 | ORIGINAL_REQUEST §R3.1 |
| 17 | Dynamic Vernacular Toggle | Instant in-app English / Hindi switch affecting all labels, inputs, and synthesized advisory | M3 | ORIGINAL_REQUEST §R3.1 |
| 18 | Voice / Audio Summary Cues | Android TTS integration providing natural spoken summary of feasibility and loan eligibility in English and Hindi | M3 | ORIGINAL_REQUEST §R3.1 |
| 19 | Interactive Form Inputs | Slider and preset chips for Margin Capital, dropdown for Trade Category, and location fields | M3 | ORIGINAL_REQUEST §R3.2 |
| 20 | Visual Financial Dashboard | Scheme qualification badges, financial breakdown cards, and full quarterly amortization schedule table | M3 | ORIGINAL_REQUEST §R3.3 |
| 21 | SWOT & Advisory Visual Grid | 2x2 color-coded SWOT matrix and modular cards for all 6 feasibility dimensions | M3 | ORIGINAL_REQUEST §R3.3 |
| 22 | Share & Export Summary | Share plain-text / formatted advisory report via standard Android share sheet (WhatsApp, SMS) | M3 | ORIGINAL_REQUEST §R3.4 |
| 23 | E2E Testing Suite (Tiers 1-4) | Comprehensive opaque-box test suite covering feature coverage, boundaries, combinations, and real-world scenarios | E2E | ORIGINAL_REQUEST §R4 |
| 24 | Adversarial Hardening (Tier 5) | White-box stress-testing, boundary edge cases, and forensic integrity verification | M4 | Project Pattern |

## Milestones

| # | Name | Scope | Dependencies | Status |
|---|------|-------|-------------|--------|
| M0 | Scaffolding & Toolchain | Initialize Android project at workspace root with Gradle 9.6, AGP 9.4, Kotlin 2.2, Compose, JUnit | none | DONE |
| E2E | E2E Testing Suite | Requirements-driven opaque-box test suite (Tiers 1-4) published via TEST_READY.md | M0 | DONE |
| M1 | Financial Engine & Router | Module 2: Financial calculator, scheme routing, loan capping, amortization schedule, cost breakdown | M0 | DONE |
| M2 | Feasibility & NLP Engine | Module 1: Heuristic advisory engine for 7 categories, 6 dimensions, offline NLP, English/Hindi dictionary | M0 | DONE |
| M3 | Compose UI & Rural UX | Module 3: High-contrast Compose theme, vernacular toggle, TTS audio cues, input form, dashboard, share | M1, M2 | DONE |
| M4 | Final Integration & Hardening | Phase 1: 100% E2E test pass (Tiers 1-4). Phase 2: Adversarial hardening & forensic audit | E2E, M3 | DONE |

## Interface Contracts

### Financial Engine ↔ UI / Application
```kotlin
package com.ruraladvisory.finance.model

enum class SchemeCategory { MICRO_FINANCE, TERM_LOAN }

data class FinancialStructure(
    val marginCapital: Double,
    val totalProjectCost: Double,
    val uncappedLoanAmount: Double,
    val actualLoanAmount: Double,
    val schemeMaxCap: Double,
    val isCapped: Boolean,
    val effectivePromoterEquity: Double,
    val loanToCostRatio: Double
)

data class RepaymentQuarter(
    val quarterIndex: Int,
    val isMoratorium: Boolean,
    val openingPrincipal: Double,
    val principalRepayment: Double,
    val interestAccrual: Double,
    val totalQuarterlyOutflow: Double,
    val closingPrincipal: Double
)

data class RepaymentSchedule(
    val quarters: List<RepaymentQuarter>,
    val totalPrincipalPaid: Double,
    val totalInterestPaid: Double,
    val totalOutflow: Double,
    val totalMoratoriumInterest: Double,
    val moratoriumQuartersCount: Int,
    val repaymentQuartersCount: Int
)

data class CostBreakdown(
    val totalProjectCost: Double,
    val fixedAssetsCapex: Double,
    val workingCapital: Double,
    val contingencyBuffer: Double,
    val estimatedMonthlyOpex: Double,
    val estimatedQuarterlyOpex: Double,
    val rawMaterialsOpex: Double,
    val laborWagesOpex: Double,
    val utilitiesLogisticsOpex: Double,
    val maintenanceSundryOpex: Double
)

sealed class FinancialCalculationResult {
    data class Success(
        val scheme: SchemeCategory,
        val structure: FinancialStructure,
        val schedule: RepaymentSchedule,
        val costBreakdown: CostBreakdown
    ) : FinancialCalculationResult()

    data class BelowMinimumThreshold(
        val enteredMargin: Double,
        val minimumAllowedMargin: Double = 1000.0,
        val minimumAllowedProjectCost: Double = 10000.0
    ) : FinancialCalculationResult()

    data class ExceedsMaximumThreshold(
        val enteredMargin: Double,
        val maximumAllowedMargin: Double = 500000.0,
        val maximumAllowedProjectCost: Double = 5000000.0
    ) : FinancialCalculationResult()

    data class InvalidInput(val message: String) : FinancialCalculationResult()
}

interface FinancialCalculatorEngine {
    fun calculate(marginCapital: Double): FinancialCalculationResult
}
```

### Advisory Engine ↔ UI / Application
```kotlin
package com.ruraladvisory.advisory.model

enum class BusinessCategory {
    DAIRY, RETAIL, FOOD_PROCESSING, TEXTILES, POULTRY, HANDICRAFTS, AGRO_SERVICES
}

enum class LanguageCode { EN, HI }

data class LocationInfo(
    val village: String,
    val block: String,
    val district: String
)

data class AdvisoryInput(
    val location: LocationInfo,
    val marginCapital: Double,
    val category: BusinessCategory,
    val language: LanguageCode = LanguageCode.EN
)

data class SwotAnalysis(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val opportunities: List<String>,
    val threats: List<String>
)

data class FeasibilityReport(
    val category: BusinessCategory,
    val location: LocationInfo,
    val budgetTier: String,
    val marketReach: String,
    val opportunityAnalysis: String,
    val swotAnalysis: SwotAnalysis,
    val threatsIdentification: List<String>,
    val competitorMapping: String,
    val pricingStrategy: String,
    val keyRecommendations: List<String>,
    val isOfflineGenerated: Boolean
)

interface FeasibilityAdvisoryEngine {
    fun generateReport(input: AdvisoryInput): FeasibilityReport
}
```

## Code Layout
```
rural_micro_advisory_app/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/ruraladvisory/
│       │   │   ├── MainActivity.kt
│       │   │   ├── RuralAdvisoryApp.kt
│       │   │   ├── finance/
│       │   │   │   ├── model/
│       │   │   │   │   ├── SchemeCategory.kt
│       │   │   │   │   ├── FinancialStructure.kt
│       │   │   │   │   ├── RepaymentSchedule.kt
│       │   │   │   │   └── FinancialCalculationResult.kt
│       │   │   │   ├── engine/
│       │   │   │   │   ├── FinancialCalculatorEngine.kt
│       │   │   │   │   └── DefaultFinancialCalculatorEngine.kt
│       │   │   ├── advisory/
│       │   │   │   ├── model/
│       │   │   │   │   ├── BusinessCategory.kt
│       │   │   │   │   ├── FeasibilityReport.kt
│       │   │   │   │   └── SwotAnalysis.kt
│       │   │   │   ├── knowledge/
│       │   │   │   │   ├── RuralTradeKnowledgeBase.kt
│       │   │   │   │   └── MultilingualDictionary.kt
│       │   │   │   ├── engine/
│       │   │   │   │   ├── FeasibilityAdvisoryEngine.kt
│       │   │   │   │   └── OfflineHeuristicAdvisoryEngine.kt
│       │   │   ├── audio/
│       │   │   │   └── TextToSpeechHelper.kt
│       │   │   ├── ui/
│       │   │   │   ├── theme/
│       │   │   │   │   ├── Color.kt
│       │   │   │   │   ├── Theme.kt
│       │   │   │   │   └── Type.kt
│       │   │   │   ├── components/
│       │   │   │   │   ├── InputFormCard.kt
│       │   │   │   │   ├── FinancialDashboardCard.kt
│       │   │   │   │   ├── SwotMatrixGrid.kt
│       │   │   │   │   ├── AmortizationScheduleTable.kt
│       │   │   │   │   └── LanguageToggleButton.kt
│       │   │   │   ├── screens/
│       │   │   │   │   └── AdvisoryMainScreen.kt
│       │   │   │   └── viewmodel/
│       │   │   │       └── AdvisoryViewModel.kt
│       │   └── res/
│       │       ├── values/
│       │       │   ├── strings.xml
│       │       │   └── colors.xml
│       │       └── values-hi/
│       │           └── strings.xml
│       └── test/java/com/ruraladvisory/
│           ├── finance/
│           │   ├── FinancialEngineTest.kt
│           │   └── MoratoriumAmortizationTest.kt
│           ├── advisory/
│           │   ├── FeasibilityEngineTest.kt
│           │   └── MultilingualDictionaryTest.kt
│           └── e2e/
│               ├── E2ETier1FeatureCoverageTest.kt
│               ├── E2ETier2BoundaryCornerTest.kt
│               ├── E2ETier3CrossFeatureTest.kt
│               └── E2ETier4RealWorldScenarioTest.kt
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.properties
│       └── gradle-wrapper.jar
├── gradlew
├── gradlew.bat
└── local.properties
```
