package com.ruraladvisory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ruraladvisory.ui.screens.AbhyudayApp
import com.ruraladvisory.ui.theme.RuralAdvisoryTheme
import com.ruraladvisory.ui.theme.WarmCreamBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            RuralAdvisoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmCreamBackground
                ) {
                    AbhyudayApp()
                }
            }
        }
    }
}

