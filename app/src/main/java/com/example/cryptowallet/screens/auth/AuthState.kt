package com.example.cryptowallet.screens.auth

data class AuthState(
    val email: String = "",
    val otp: String = "",
    val otpSent: Boolean = false,
    val authSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)