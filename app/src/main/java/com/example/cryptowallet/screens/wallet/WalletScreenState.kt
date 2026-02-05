package com.example.cryptowallet.screens.wallet

data class WalletScreenState(
    val chain: String = "",
    val address: String = "",
    val network: String = "",
    val balance: String = "0.0",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
