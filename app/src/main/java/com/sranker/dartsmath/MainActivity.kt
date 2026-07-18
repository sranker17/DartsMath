package com.sranker.dartsmath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sranker.dartsmath.ui.GameScreen
import com.sranker.dartsmath.ui.theme.DartsMathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DartsMathTheme {
                GameScreen()
            }
        }
    }
}
