# Original User Request

## 2026-09-08T01:14:22+05:30

Build a native Kotlin Android application using Jetpack Compose that acts as an NLP-powered, multilingual AI Business Advisory and Smart Financial Structuring Assistant for rural and semi-urban micro-entrepreneurs.

Working directory: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app
Integrity mode: development

## Requirements

### R1. Hyper-Local Business Feasibility Report Engine (Module 1)
The engine must accept:
- Geographic Location: Village, Block, and District (e.g., in rural/semi-urban regions).
- Available Margin Capital: e.g., ₹1,00,000 (10% beneficiary contribution).
- Proposed Business Category: e.g., Dairy, Retail, Food Processing, Textiles, Poultry, Handicrafts, Agro-Services.

It must dynamically synthesize and present a comprehensive feasibility report covering:
1. **Market Reach**: Estimated consumer base within a 5–10 km radius and identification of primary local distribution channels (haats, village cooperatives, local retailers).
2. **Opportunity Analysis**: Identification of unserved or underserved niches within the chosen sector for that specific rural economy.
3. **SWOT Analysis**: Tailored breakdown of Strengths, Weaknesses, Opportunities, and Threats calibrated specifically to the entrepreneur's budget.
4. **Threats Identification**: Analysis of localized risks including seasonal demand fluctuations, supply chain bottlenecks, raw material volatility, and single-buyer dependency.
5. **Competitor Mapping**: Localized density estimation of existing competing businesses in the block.
6. **Product Market Value & Pricing Strategy**: Optimal unit pricing recommendations and expected product/service valuation aligned with local rural purchasing power.

The advisory system must feature a hybrid architecture: an intelligent on-device NLP/heuristic knowledge base that functions completely offline, with an optional Gemini/LLM integration for enhanced dynamic advisory when network access is available. It must also support multilingual display (English and Hindi).

### R2. Smart Financial Calculator & Scheme Router (Module 2)
An automated, rigorous financial engine that ingests the user's available margin capital and executes government concessional credit logic:
1. **Financial Structuring**:
   - Total Feasible Project Cost = Margin Capital / 10% = Margin Capital * 10.
   - Maximum Loan Amount = 90% of Project Cost (subject to scheme caps).
2. **Scheme Auto-Selection Logic**:
   - **Micro Finance Scheme**: Selected when Project Cost <= ₹1.40 Lakh.
     - Interest rate: 6.5% per annum.
     - Tenure: 3 years (12 quarters).
     - Moratorium: 3 months (1 quarter) grace period.
     - Maximum loan cap: ₹1.25 Lakh.
   - **Term Loan Scheme**: Selected when ₹1.40 Lakh < Project Cost <= ₹50.00 Lakh.
     - Interest rate: 8.0% per annum.
     - Tenure: 7 years (28 quarters).
     - Moratorium: 6 months (2 quarters) grace period.
     - Maximum loan cap: ₹45.00 Lakh.
   - Clear validation and messaging if project cost exceeds ₹50.00 Lakh or margin is below minimum thresholds.
3. **Repayment Schedule & Cash Flow Generator**:
   - Detailed quarterly repayment schedule calculating interest accrual, moratorium treatment, principal repayment, and total quarterly outflow.
   - Working capital requirements and estimated operational cost breakdown.

### R3. Modern Jetpack Compose UI with Rural-Friendly UX
1. Clean, intuitive Android interface with high-contrast elements, vernacular support (English / Hindi toggle), and audio/voice summary cues suitable for rural micro-entrepreneurs.
2. Form input screen with interactive sliders/number pickers for margin capital and category dropdowns.
3. Interactive visual dashboard displaying financial breakdown cards, loan qualification badges, SWOT grid, and quarterly amortization tables.
4. Export or share summary report feature.

### R4. Automated Verification & Test Suite
1. Automated unit test suite verifying the financial engine across edge cases:
   - Margin ₹10,000 -> Project Cost ₹1,00,000 -> Micro Finance Scheme (6.5%, 3-yr tenure, 3-mo moratorium).
   - Margin ₹14,000 -> Project Cost ₹1,40,000 -> Micro Finance Scheme threshold.
   - Margin ₹15,000 -> Project Cost ₹1,50,000 -> Term Loan Scheme (8.0%, 7-yr tenure, 6-mo moratorium).
   - Margin ₹5,00,000 -> Project Cost ₹50,00,000 -> Term Loan Scheme maximum cap.
   - Moratorium quarterly calculation accuracy.
2. Unit tests validating feasibility report synthesis and SWOT generation for multiple trade categories.
3. Successful compilation via Gradle.

## Acceptance Criteria

### Financial Calculations & Scheme Selection
- [ ] Financial calculator correctly routes Project Cost <= ₹1.40 Lakh to Micro Finance at 6.5% interest with 3-month moratorium.
- [ ] Financial calculator correctly routes ₹1.40 Lakh < Project Cost <= ₹50.00 Lakh to Term Loan at 8.0% interest with 6-month moratorium.
- [ ] Maximum loan cap restrictions are enforced (₹1.25 Lakh for Micro Finance, ₹45.00 Lakh for Term Loan).
- [ ] Quarterly repayment schedule matches expected interest and principal amortization formulas after the moratorium grace period.

### Advisory & Feasibility Engine
- [ ] Advisory generator outputs all 6 dimensions (Market Reach, Opportunity, SWOT, Threats, Competitor Mapping, Pricing) for selected business categories.
- [ ] Language switching between English and Hindi correctly localizes terminology and advisory output.
- [ ] Works seamlessly offline without crashing or stalling if network or API keys are unavailable.

### Code Quality & Build Verification
- [ ] Android project builds cleanly via Gradle.
- [ ] Unit test suite executes and achieves 100% pass rate on all financial routing, repayment math, and advisory data model tests.
