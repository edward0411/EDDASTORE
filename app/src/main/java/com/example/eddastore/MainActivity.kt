package com.example.eddastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eddastore.ui.theme.EDDASTORETheme
import com.example.eddastore.ui.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EDDASTORETheme {

                AppNavHost()
            }
        }
    }
}
