package com.example.cryptowallet.navigation

import VerifyEmailScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptowallet.screens.auth.LoginScreen
import com.example.cryptowallet.screens.wallet.WalletDetailsScreen

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

            VerifyEmailScreen(
                viewModel = hiltViewModel(parentEntry),
                onNavigateNext = {
                    navController.navigate(Routes.WALLET_DETAILS_SCREEN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.WALLET_DETAILS_SCREEN) {
            WalletDetailsScreen(
                walletModel = hiltViewModel(),
                authModel = hiltViewModel(),
                onLogout = {
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = { })
        }
    }
}