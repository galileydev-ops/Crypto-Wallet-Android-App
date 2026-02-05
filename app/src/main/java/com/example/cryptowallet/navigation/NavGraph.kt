package com.example.cryptowallet.navigation

import VerifyEmailScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptowallet.screens.auth.AuthViewModel
import com.example.cryptowallet.screens.auth.LoginScreen
import com.example.cryptowallet.screens.transaction.SendTransactionScreen
import com.example.cryptowallet.screens.wallet.WalletDetailsScreen
import com.example.cryptowallet.screens.wallet.WalletViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {

        composable(Routes.LOGIN_SCREEN) {
            val viewModel: AuthViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,
                onVerifyEmail = {
                    val email = viewModel.state.value.email
                    navController.navigate("${Routes.VERIFY_EMAIL_SCREEN}/$email") {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = "${Routes.VERIFY_EMAIL_SCREEN}/{${Routes.VERIFY_EMAIL_SCREEN_EMAIL_ARG}}",
            arguments = listOf(navArgument(Routes.VERIFY_EMAIL_SCREEN_EMAIL_ARG) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(Routes.VERIFY_EMAIL_SCREEN_EMAIL_ARG)

            VerifyEmailScreen(
                viewModel = hiltViewModel(),
                email = email!!,
                onWalletDetails = {
                    navController.navigate(Routes.WALLET_DETAILS_SCREEN) {
                        popUpTo(Routes.VERIFY_EMAIL_SCREEN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(Routes.VERIFY_EMAIL_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.WALLET_DETAILS_SCREEN) {
            val walletModel: WalletViewModel = hiltViewModel()
            val authModel: AuthViewModel = hiltViewModel()

            WalletDetailsScreen(
                walletModel = walletModel,
                onLogout = {
                    authModel.logout()
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(Routes.WALLET_DETAILS_SCREEN) { inclusive = true }
                    }
                },
                onSendTransaction = {
                    val address = walletModel.state.value.address
                    navController.navigate("${Routes.SEND_TRANSACTION_SCREEN}/$address") {
                        popUpTo(Routes.WALLET_DETAILS_SCREEN) { inclusive = false }
                    }
                },
                onNavigateBack = { }
            )
        }

        composable(
            route = "${Routes.SEND_TRANSACTION_SCREEN}/{${Routes.SEND_TRANSACTION_SCREEN_ADDRESS_ARG}}",
            arguments = listOf(navArgument(Routes.SEND_TRANSACTION_SCREEN_ADDRESS_ARG) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val walletAddress = backStackEntry.arguments?.getString(Routes.SEND_TRANSACTION_SCREEN_ADDRESS_ARG)

            SendTransactionScreen(
                viewModel = hiltViewModel(),
                walletAddress = walletAddress!!,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}