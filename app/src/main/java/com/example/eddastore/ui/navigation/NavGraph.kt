package com.example.eddastore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eddastore.Routes
import com.example.eddastore.ui.screens.*
import com.example.eddastore.ui.product.ProductFormScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Routes.Splash, // ← aquí
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash → luego pasa a la tienda
        composable(Routes.Splash) {
            SplashScreen(
                onFinished = {
                    nav.navigate(Routes.PRODUCT_LIST) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }

        // Tienda visible para invitados
        composable(Routes.PRODUCT_LIST) {
            ProductListScreen(
                onEdit = { id -> nav.navigate("${Routes.PRODUCT_FORM}?id=$id") },
                onAddCart = { _ -> nav.navigate(Routes.CART) },
                onGoLogin = { nav.navigate(Routes.Login) },
                onGoCart = { nav.navigate(Routes.CART) },
                onGoProfile = { nav.navigate(Routes.Profile) },
                onGoNewProduct = { nav.navigate(Routes.PRODUCT_FORM) }
            )
        }

        // Auth
        composable(Routes.Login) {
            LoginScreen(
                onLoginOk = {
                    nav.navigate(Routes.PRODUCT_LIST) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onGoRegister = { prefill ->
                    nav.navigate("${Routes.Register}?email=$prefill")  // ← sin named param
                },
                onBack = { nav.navigateUp() }
            )
        }
        composable("${Routes.Register}?email={email}") { back ->
            val prefill = back.arguments?.getString("email")?.takeIf { it != "null" }
            RegisterScreen(
                prefillEmail = prefill,
                onRegisterOk = {
                    nav.navigate(Routes.PRODUCT_LIST) {
                        popUpTo(Routes.Register) { inclusive = true }
                    }
                },
                onBack = { nav.navigateUp() }
            )
        }
        composable(Routes.Profile) {
            ProfileScreen(
                onLogout = {
                    nav.navigate(Routes.PRODUCT_LIST) { popUpTo(Routes.Profile) { inclusive = true } }
                },
                onBack = { nav.navigateUp() }
            )
        }

        composable("${Routes.PRODUCT_FORM}?id={id}") { back ->
            val id = back.arguments?.getString("id")?.toLongOrNull()
            ProductFormScreen(
                productId = id,
                onSaved = { nav.popBackStack() },
                onCancel = { nav.popBackStack() }
            )
        }
        // TODO

        composable(Routes.CART) { /* ... */ }
        composable(Routes.AUTH) { /* ... */ }
        composable(Routes.Welcome) { WelcomeScreen(onLogin = { nav.navigate(Routes.Login) }) }
    }
}
