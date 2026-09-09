# Dispatch Log
Task: Mine detailed financial specifications, formulas, edge cases, moratorium rules, and quarterly schedule requirements.
Working Directory: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\spec_miner_finance
Parent: C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\orchestrator_1

## 2026-09-07T19:45:36Z
Read C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\ORIGINAL_REQUEST.md.
Mine all precise specifications, mathematical formulas, business rules, edge cases, and test requirements for the Smart Financial Calculator & Scheme Router (Module 2).
Include:
1. Exact calculation formulas:
   - Total Feasible Project Cost = Margin Capital * 10
   - Maximum Loan Amount = 90% of Project Cost (capped by scheme)
2. Scheme routing logic & boundaries:
   - Micro Finance Scheme: Project Cost <= ₹1.40 Lakh (Margin <= ₹14,000) -> 6.5% p.a., 3-year tenure (12 quarters), 3-month moratorium (1 quarter), max cap ₹1.25 Lakh
   - Term Loan Scheme: ₹1.40 Lakh < Project Cost <= ₹50.00 Lakh (₹14,000 < Margin <= ₹5,00,000) -> 8.0% p.a., 7-year tenure (28 quarters), 6-month moratorium (2 quarters), max cap ₹45.00 Lakh
   - Invalidation rules: Project Cost > ₹50.00 Lakh or below minimum threshold
3. Quarterly Repayment Amortization Schedule & Moratorium math:
   - Interest accrual during moratorium
   - Principal repayment over post-moratorium quarters
   - Total quarterly outflow breakdown (Interest + Principal)
   - Working capital & operating cost estimation breakdown
4. Test cases specified in R4.1 (Margin ₹10k, ₹14k, ₹15k, ₹5L, boundary tests).
Write a comprehensive specification document to:
C:\Users\LENOVO\.gemini\antigravity\worktrees\ABHYUDAY\rural_micro_advisory_app\.agents\spec_miner_finance\handoff.md
Send a message back to parent when complete.

