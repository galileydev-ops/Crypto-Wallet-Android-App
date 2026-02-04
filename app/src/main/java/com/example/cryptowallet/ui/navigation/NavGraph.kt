package com.example.cryptowallet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptowallet.ui.screens.LoginScreen
import com.example.cryptowallet.ui.screens.VerifyEmailScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onNavigateNext = {
                    navController.navigate(Routes.VERIFY_EMAIL_SCREEN) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.VERIFY_EMAIL_SCREEN) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Routes.LOGIN_SCREEN)
            }

            VerifyEmailScreen(hiltViewModel(parentEntry))
        }
    }
}