# Test Infrastructure Specification: 4-Tier E2E Testing Architecture

**Project**: Rural Micro-Advisory & Smart Financial Structuring App  
**Target Package**: `com.ruraladvisory.e2e`  
**Framework**: JUnit 4 / Kotlin 2.2 / Android Gradle Plugin 9.4  
**Philosophy**: Opaque-Box, Requirements-Driven, Deterministic Micro-Enterprise Validation  

---

## 1. Testing Philosophy & Principles

The E2E testing framework adheres to an **opaque-box, requirements-driven** philosophy. All test verifications are derived strictly from the mathematical domain rules, economic models, and behavioral requirements established in `ORIGINAL_REQUEST.md` and `PROJECT.md`.

### Core Axioms
1. **Opaque-Box Contract Verification**: Tests interact exclusively with public API boundaries (`FinancialCalculatorEngine`, `FeasibilityAdvisoryEngine`, `MultilingualDictionary`, and their data models). Internal private implementation details, reflection hacks, or mock facades are strictly prohibited.
2. **Zero-Facade Integrity**: Every test exercises genuine computational pipelines. Expected values are mathematically derived from first principles (e.g., quarterly simple interest $I_q = P \times \frac{r}{4}$, straight-line principal amortization $P_{inst} = \frac{L}{N_{repay}}$, penny balancing $\sum P_{repaid} \equiv L$, statutory scheme ceilings, and agro-economic heuristic vectors).
3. **100% Offline Determinism**: All test executions run with zero external network dependencies, ensuring lightning-fast execution (<50ms per test execution) and absolute repeatability across CI/CD and air-gapped deployment environments.
4. **Independent & Isolated State**: Each test is atomic, self-contained, and order-independent. No shared mutable state or inter-test coupling exists.

---

## 2. 4-Tier Testing Methodology

The test suite is structured into four complementary hierarchical tiers designed to progressively validate functional correctness, edge-case resilience, cross-module cohesion, and authentic end-user persona workflows.

```
+-------------------------------------------------------------------------+
|                  Tier 4: Real-World Application Scenarios               |
|         (End-to-End Enterprise Persona Journeys across India)           |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------+------------------------------------+
|               Tier 3: Cross-Feature Combinations                        |
|       (Pairwise Matrix: Category x Budget Tier x Scheme x Language)     |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------+------------------------------------+
|               Tier 2: Boundary & Corner Cases                           |
|       (BVA Limits, ₹14k/₹50L caps, Rejections, Whitespace, Unicode)      |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------+------------------------------------+
|               Tier 1: Feature Coverage                                  |
|       (10x Scaling, 90% Loan, Schemes, Amortization, 7 Sectors, 6 Dims)  |
+-------------------------------------------------------------------------+
```

### Tier 1: Feature Coverage (Category-Partition Testing)
- **Objective**: Verify that each atomic capability and domain rule functions correctly in isolation.
- **Coverage Criteria**: $\ge 5$ discrete test cases per core feature family; minimum $\ge 35$ total tests.
- **Feature Scope**:
  1. **Project Cost Scaling**: Verification of exact $10 \times \text{Margin}$ project cost scaling across diverse margin amounts.
  2. **Debt Structuring**: 90% loan calculation, uncapped loan determination, and debt-to-cost ratio.
  3. **Micro Finance Scheme Routing**: Project cost $\le \text{₹}1,40,000 \implies$ 6.5% interest, 12 quarters tenure, 1 quarter moratorium, ₹1,25,000 cap.
  4. **Term Loan Scheme Routing**: $\text{₹}1,40,000 < \text{Project Cost} \le \text{₹}50,00,000 \implies$ 8.0% interest, 28 quarters tenure, 2 quarters moratorium, ₹45,00,000 cap.
  5. **Moratorium Simple Interest**: Correct quarterly non-capitalized interest accrual during grace period (Q1 for Micro; Q1-Q2 for Term).
  6. **Straight-Line Quarterly Amortization**: Post-moratorium equal installments with penny balancing closing at exactly ₹0.00.
  7. **CAPEX & OPEX Cost Breakdown**: 70% CAPEX, 25% Working Capital, 5% Contingency, and sub-allocation splits (Raw Materials 55%, Labor 25%, Utilities 12%, Maintenance 8%).
  8. **All 7 Trade Sectors**: Autonomous synthesis for Dairy, Retail, Food Processing, Textiles, Poultry, Handicrafts, Agro-Services.
  9. **All 6 Feasibility Dimensions**: Dynamic synthesis of Market Reach, Opportunity Analysis, SWOT (4 quadrants), Threats (4 risk vectors + mitigations), Competitor Mapping, and Pricing Strategy.
  10. **Bilingual Parity**: 1:1 English and Hindi dictionary parity with authentic Devanagari script.

### Tier 2: Boundary & Corner Cases (Boundary Value Analysis & Stress Testing)
- **Objective**: Expose anomalies, rounding drifts, threshold transitions, and input edge conditions.
- **Coverage Criteria**: $\ge 5$ discrete boundary tests per threshold boundary; minimum $\ge 20$ total tests.
- **Boundary Matrix**:
  1. **Minimum Threshold Boundary**:
     - Margin ₹1,000.00 (exact lower limit $\implies$ Project Cost ₹10,000, Micro Finance).
     - Margin ₹999.99 (immediately below limit $\implies$ `BelowMinimumThreshold` rejection).
     - Margin ₹500.00 (sub-minimum rejection).
  2. **Micro Finance / Term Loan Transition Boundary**:
     - Margin ₹14,000.00 (upper Micro Finance threshold $\implies$ Project Cost ₹1,40,000, strictly capped at ₹1,25,000).
     - Margin ₹14,000.01 (infinitesimal threshold crossover $\implies$ Project Cost ₹1,40,000.10, switches to Term Loan Scheme).
     - Margin ₹14,001.00 (whole rupee step above $\implies$ Term Loan Scheme).
     - Margin ₹15,000.00 (standard Term Loan entry point $\implies$ Project Cost ₹1,50,000).
  3. **Maximum Ceiling Boundary**:
     - Margin ₹5,00,000.00 (exact upper ceiling $\implies$ Project Cost ₹50,00,000, Term Loan ₹45,00,000 max cap).
     - Margin ₹5,00,000.01 / ₹5,00,001.00 (exceeds ceiling $\implies$ `ExceedsMaximumThreshold` rejection).
  4. **Invalid & Malformed Inputs**:
     - Zero margin (`0.0`), negative margins (`-5000.0`), `Double.NaN`, `Double.POSITIVE_INFINITY`, `Double.NEGATIVE_INFINITY` $\implies$ `InvalidInput` rejection.
  5. **Advisory String & Unicode Resilience**:
     - Blank, whitespace-only, tabbed strings in village, block, and district.
     - Fallback token verification in both English ("Local Village", etc.) and Hindi ("स्थानीय गाँव", etc.).
     - Non-ASCII, Unicode Devanagari script strings (`\u0900-\u097F`).
     - Special character handling in location fields (`&`, `-`, `/`, `,`).

### Tier 3: Cross-Feature Combinations (Pairwise Combinatorial Testing)
- **Objective**: Validate inter-feature compatibility and guarantee that changing one dimension (e.g., language or trade category) never corrupts financial math or advisory integrity.
- **Coverage Criteria**: Pairwise combinatorial coverage covering Category $\times$ Budget Tier $\times$ Loan Scheme $\times$ Language; minimum $\ge 15$ total tests.
- **Factor Space**:
  - Factor A: Business Category (7 levels: Dairy, Retail, Food Processing, Textiles, Poultry, Handicrafts, Agro-Services).
  - Factor B: Budget Tier (3 levels: Micro $< \text{₹}25\text{k}$, Small $\text{₹}25\text{k}..\text{₹}1.5\text{L}$, Medium $> \text{₹}1.5\text{L}$).
  - Factor C: Credit Scheme (2 levels: Micro Finance vs. Term Loan).
  - Factor D: Language (2 levels: English, Hindi).
  - Factor E: Financial Structuring (Capped vs. Uncapped, Moratorium Duration 1 vs. 2 quarters).

### Tier 4: Real-World Application Scenarios (Persona & User Workload Workflows)
- **Objective**: Validate complete end-to-end user journeys modeling actual rural Indian micro-entrepreneurs.
- **Coverage Criteria**: 5 exhaustive real-world scenarios representing distinct geographies, enterprise domains, budget scales, and loan schemes.
- **Scenarios**:
  1. **Scenario 1: Smallholder Dairy Farmer in Anand, Gujarat**
     - Geography: Anand, Anand Block, Anand District, Gujarat.
     - Capital: Margin ₹12,000 (Beneficiary equity).
     - Scheme: Micro Finance (Project Cost ₹1,20,000, 90% Loan ₹1,08,000 uncapped, 6.5% interest, 3-yr tenure, 1-qtr moratorium).
     - Feasibility: Dairy sector in Anand milk belt, cooperative procurement (Amul model), local chilling infrastructure, fodder risk mitigation.
  2. **Scenario 2: Handloom Weaver in Pochampally, Telangana**
     - Geography: Bhoodan Pochampally, Pochampally Block, Yadadri Bhuvanagiri District, Telangana.
     - Capital: Margin ₹14,000 (Boundary condition).
     - Scheme: Micro Finance Capped (Project Cost ₹1,40,000, Uncapped ₹1,26,000, Statutory Cap ₹1,25,000, Promoter Equity ₹15,000).
     - Feasibility: Traditional Ikat textiles, master weaver cooperative links, yarn price volatility mitigation, GI-tag value pricing.
  3. **Scenario 3: Spice & Pickle Processing SHG in Coorg, Karnataka**
     - Geography: Madikeri, Madikeri Block, Kodagu District, Karnataka.
     - Capital: Margin ₹1,50,000 (Small/Medium boundary).
     - Scheme: Term Loan (Project Cost ₹15,00,000, Loan ₹13,50,000, 8.0% interest, 7-yr tenure, 2-qtr moratorium).
     - Feasibility: Food Processing, spice value addition, FSSAI compliance, homestay tourist distribution, monsoon moisture spoilage mitigation.
  4. **Scenario 4: Rural Poultry Broiler Unit in Namakkal, Tamil Nadu**
     - Geography: Paramathi, Paramathi-Velur Block, Namakkal District, Tamil Nadu.
     - Capital: Margin ₹35,000 (Small enterprise).
     - Scheme: Term Loan (Project Cost ₹3,50,000, Loan ₹3,15,000, 8.0% interest, 28 quarters, 2-qtr moratorium).
     - Feasibility: Commercial poultry broiler contract integration, Feed Conversion Ratio (FCR), summer heatwave biosecurity, disease vaccination protocol.
  5. **Scenario 5: Custom Hiring Agro-Services Center in Bhatinda, Punjab**
     - Geography: Talwandi Sabo, Talwandi Sabo Block, Bhatinda District, Punjab.
     - Capital: Margin ₹5,00,000 (Maximum statutory ceiling).
     - Scheme: Term Loan Maximum Cap (Project Cost ₹50,00,000, Loan ₹45,00,000, 8.0% interest, 7-yr tenure, 2-qtr moratorium).
     - Feasibility: Farm mechanization (combine harvesters, laser levelers, happy seeders), seasonal rabi/kharif cashflow surges, high diesel/spares OPEX management.

---

## 3. Directory Layout & Test Class Architecture

```
app/src/test/java/com/ruraladvisory/e2e/
├── E2ETier1FeatureCoverageTest.kt   # >= 35 tests covering all features in isolation
├── E2ETier2BoundaryCornerTest.kt    # >= 20 tests covering boundary edge cases
├── E2ETier3CrossFeatureTest.kt      # >= 15 tests covering combinatorial pairs
└── E2ETier4RealWorldScenarioTest.kt # 5 real-world end-to-end persona scenarios
```

---

## 4. Minimum Quality & Verification Thresholds

| Metric | Threshold | Verification Method |
|---|---|---|
| **E2E Test Suites** | 4 distinct tier classes | Files present in `app/src/test/java/com/ruraladvisory/e2e/` |
| **Total E2E Test Cases** | $\ge 75$ tests | Automated JUnit test runner |
| **Tier 1 Tests** | $\ge 35$ tests | `E2ETier1FeatureCoverageTest` |
| **Tier 2 Tests** | $\ge 20$ tests | `E2ETier2BoundaryCornerTest` |
| **Tier 3 Tests** | $\ge 15$ tests | `E2ETier3CrossFeatureTest` |
| **Tier 4 Tests** | $\ge 5$ detailed scenarios | `E2ETier4RealWorldScenarioTest` |
| **Test Pass Rate** | 100% (0 failures, 0 errors, 0 skipped) | `gradlew.bat testDebugUnitTest` |
| **Execution Performance** | $\le 10$ seconds total E2E execution | Gradle execution timer |
| **Network Dependence** | 0 external network calls | 100% offline heuristic execution |
