package com.example.cryptowallet.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.DynamicSDK
import com.example.cryptowallet.config.EthereumConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendTransactionViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SendTransactionState())
    val state: StateFlow<SendTransactionState> = _state.asStateFlow()

    fun updateRecipient(value: String) {
        _state.value = _state.value.copy(recipient = value)
    }

    fun updateAmount(value: String) {
        _state.value = _state.value.copy(amount = value)
    }

    fun sendTransaction(walletAddress: String) {
        val recipient = _state.value.recipient
        val amount = _state.value.amount.toDoubleOrNull()

        if (recipient.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Recipient address cannot be empty")
            return
        }

        if (amount == null || amount <= 0.0) {
            _state.value = _state.value.copy(errorMessage = "Amount must be greater than 0")
            return
        }

        val sdk = DynamicSDK.getInstance()

        val wallet = sdk.wallets.userWallets.firstOrNull {
            it.address.equals(walletAddress, ignoreCase = true)
        } ?: throw Exception("No wallet found")

        _state.value = _state.value.copy(isLoading = true, errorMessage = null, success = false)

        viewModelScope.launch {
            runCatching {
                val transaction = EthereumTransaction(
                    from = wallet.address,
                    to = recipient,
                    value = convertEthToWei(amount.toString()),
                    gas = EthereumConfig.GAS_LIMIT,
                    maxFeePerGas = EthereumConfig.MAX_FEE_PER_GAS,
                    maxPriorityFeePerGas = EthereumConfig.MAX_PRIORITY_FEE_PER_GAS
                )

                val txHash = sdk.evm.sendTransaction(transaction, wallet)
                _state.value = _state.value.copy(isLoading = false, success = true, txHash = txHash)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Transaction failed")
            }
        }
    }
}