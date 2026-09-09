package com.ruraladvisory

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScaffoldVerificationTest {
    @Test
    fun testScaffoldInitialized() {
        val appName = "Rural Micro-Advisory"
        assertTrue(appName.isNotBlank())
        assertEquals("Rural Micro-Advisory", appName)
    }

    @Test
    fun testMathematicalCalculationsWork() {
        val margin = 10000.0
        val projectCost = margin * 10.0
        val maxLoan = projectCost * 0.90
        assertEquals(100000.0, projectCost, 0.001)
        assertEquals(90000.0, maxLoan, 0.001)
    }
}
