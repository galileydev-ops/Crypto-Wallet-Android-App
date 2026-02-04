package com.example.cryptowallet.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AuthScreenState())
    val state: StateFlow<AuthScreenState> = _state

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _otpCode = MutableStateFlow("")
    val otpCode: StateFlow<String> = _otpCode.asStateFlow()

    fun updateEmail(value: String) { _email.value = value }
    fun updateOtp(value: String) { _otpCode.value = value }

    fun sendEmailOTP() {
        val trimmed = email.value.trim()
        if (trimmed.isEmpty()) return

        _errorMessage.value = null

        viewModelScope.launch {
            try {
                DynamicSDK.getInstance().auth.email.sendOTP(trimmed)
                _state.value = AuthScreenState(otpSent = true)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to send email OTP"
            }
        }
    }

    fun resendEmailOTP() {
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                DynamicSDK.getInstance().auth.email.resendOTP()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to resend email OTP"
            }
        }
    }

    fun verifyOTP() {
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                DynamicSDK.getInstance().auth.email.verifyOTP(otpCode.value)
                _state.value = AuthScreenState(authSuccessful = true)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to verify OTP code"
            }
        }
    }
}