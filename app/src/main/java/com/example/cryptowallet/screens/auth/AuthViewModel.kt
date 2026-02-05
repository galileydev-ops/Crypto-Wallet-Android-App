package com.example.cryptowallet.screens.auth

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

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun updateEmail(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun updateOtp(value: String) {
        _state.value = _state.value.copy(otp = value)
    }

    fun sendEmailOTP() {
        val emailTrimmed = _state.value.email.trim()
        if (emailTrimmed.isEmpty()) {
            _state.value = _state.value.copy(errorMessage = "Email cannot be empty")
            return
        }

        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching {
                DynamicSDK.getInstance().auth.email.sendOTP(emailTrimmed)
            }.onSuccess {
                _state.value = _state.value.copy(isLoading = false, otpSent = true)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to send email OTP")
            }
        }
    }

    fun verifyOTP() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching {
                DynamicSDK.getInstance().auth.email.verifyOTP(_state.value.otp)
            }.onSuccess {
                _state.value = _state.value.copy(isLoading = false, authSuccessful = true)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to verify OTP code")
            }
        }
    }

    fun resendEmailOTP() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching {
                DynamicSDK.getInstance().auth.email.resendOTP()
            }.onSuccess {
                _state.value = _state.value.copy(isLoading = false)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to resend OTP")
            }
        }
    }

    fun logout() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching {
                DynamicSDK.getInstance().auth.logout()
            }.onSuccess {
                _state.value = AuthState()
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to logout")
            }
        }
    }
}