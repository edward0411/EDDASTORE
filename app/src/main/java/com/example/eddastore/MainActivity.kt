package com.example.eddastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eddastore.ui.screens.LoginScreen
import com.example.eddastore.ui.screens.ProfileScreen
import com.example.eddastore.ui.screens.RegisterScreen
import com.example.eddastore.ui.screens.WelcomeScreen
import com.example.eddastore.ui.theme.EDDATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EDDATheme {
                val nav = rememberNavController()
                AppNavHost(nav)
            }
        }
    }
}

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.Welcome) {
        composable(Routes.Welcome) {
            WelcomeScreen(
                onLogin = { nav.navigate(Routes.Login) },
                onRegister = { nav.navigate(Routes.Register) }
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                onLoginOk = { nav.navigate(Routes.Profile) { popUpTo(Routes.Welcome) { inclusive = false } } },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                onRegisterOk = { nav.navigate(Routes.Profile) { popUpTo(Routes.Welcome) { inclusive = false } } },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.Profile) {
            ProfileScreen(onLogout = {
                nav.navigate(Routes.Welcome) { popUpTo(0) { inclusive = true } }
            })
        }
    }
}
