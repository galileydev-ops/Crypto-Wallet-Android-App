package com.example.cryptowallet.ui.screens

data class AuthScreenState(
    var otpSent: Boolean = false,
    val authSuccessful: Boolean = false
)