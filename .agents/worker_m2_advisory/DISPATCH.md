# Dispatch Log — Worker M2 (Advisory & Feasibility Engine)

## 2026-09-08T01:36:00Z
**Parent**: orchestrator_1 (a44bd3ad-fb91-4245-92f7-145540a937a8)
**Task**: Implement Milestone 2: Hyper-Local Feasibility Engine, Offline Heuristic NLP, and Multilingual Support
**Working Directory**: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\worker_m2_advisory
**Workspace**: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app
**References**:
- PROJECT.md: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\PROJECT.md
- ORIGINAL_REQUEST.md: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\ORIGINAL_REQUEST.md
- Authoritative Advisory Spec: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\spec_miner_advisory\handoff.md

### Exclusive Write Ownership:
- `app/src/main/java/com/ruraladvisory/advisory/**`
- `app/src/test/java/com/ruraladvisory/advisory/**`
DO NOT edit files outside this boundary.

### Mandatory Integrity Warning:
DO NOT CHEAT. All implementations must be genuine. DO NOT hardcode test results, create dummy/facade implementations, or circumvent the intended task. A teamwork_preview_auditor will independently verify your work. Integrity violations WILL be detected and your work WILL be rejected.

### Concrete Assignment:
1. Implement the data models in `com.ruraladvisory.advisory.model`:
   - `BusinessCategory.kt`: DAIRY, RETAIL, FOOD_PROCESSING, TEXTILES, POULTRY, HANDICRAFTS, AGRO_SERVICES.
   - `LanguageCode.kt`: EN, HI.
   - `LocationInfo.kt`: village, block, district.
   - `AdvisoryInput.kt`: location, marginCapital, category, language.
   - `SwotAnalysis.kt`: strengths, weaknesses, opportunities, threats.
   - `FeasibilityReport.kt`: 6 dimensions:
     1. Market Reach (5-10 km radius, haats, cooperatives, retailers)
     2. Opportunity Analysis (underserved niches in rural economy)
     3. SWOT Analysis (budget-calibrated: Micro <₹25k margin, Small ₹25k-₹1.5L margin, Medium >₹1.5L margin)
     4. Threats Identification (seasonal fluctuations, supply bottlenecks, raw material volatility, single-buyer dependency)
     5. Competitor Mapping (density estimation in the block)
     6. Product Market Value & Pricing Strategy (optimal unit pricing and purchasing power alignment)
     + keyRecommendations and isOfflineGenerated.
2. Implement the knowledge base in `com.ruraladvisory.advisory.knowledge`:
   - `RuralTradeKnowledgeBase.kt`: Heuristic knowledge dictionaries for all 7 trade categories and 3 budget tiers.
   - `MultilingualDictionary.kt`: Complete English and Hindi 1:1 translations for all category names, report labels, SWOT entries, threat vectors, mitigations, and advice strings.
3. Implement the engine in `com.ruraladvisory.advisory.engine`:
   - `FeasibilityAdvisoryEngine.kt` interface.
   - `OfflineHeuristicAdvisoryEngine.kt`: Fully offline, robust, executes in <50ms without crashing or network stalling.
   - `HybridAdvisoryEngine.kt`: Attempts LLM/Gemini enhancement if available/configured, with immediate silent fallback to `OfflineHeuristicAdvisoryEngine` if offline, timed out, or unconfigured.
4. Write thorough unit tests in `app/src/test/java/com/ruraladvisory/advisory/`:
   - `FeasibilityEngineTest.kt`: Tests all 7 categories produce complete 6-dimension reports, budget calibration (Micro/Small/Medium), offline execution speed, non-null outputs.
   - `MultilingualDictionaryTest.kt`: Validates English and Hindi parity across all categories, dimensions, and SWOT keys.
5. Execute `cmd.exe /c "gradlew.bat testDebugUnitTest"` and verify 100% tests pass.
6. Write complete `handoff.md` and message parent.
