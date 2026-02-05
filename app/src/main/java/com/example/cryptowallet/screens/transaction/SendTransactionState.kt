package com.example.cryptowallet.screens.transaction

data class SendTransactionState(
    val recipient: String = "",
    val amount: String = "",
    val txHash: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)