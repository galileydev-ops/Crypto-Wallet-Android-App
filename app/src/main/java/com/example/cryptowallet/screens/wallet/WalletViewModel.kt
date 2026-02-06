package com.example.cryptowallet.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.ChainEnum
import com.dynamic.sdk.android.Models.Network
import com.example.cryptowallet.enums.NetworkEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(WalletScreenState())
    val state: StateFlow<WalletScreenState> = _state.asStateFlow()

    fun connectSepoliaWallet() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        val network = NetworkEnum.SEPOLIA
        viewModelScope.launch {
            val sdk = DynamicSDK.getInstance()
            runCatching {
                val wallet = sdk.wallets.userWallets.firstOrNull {
                    it.chain.equals(ChainEnum.EVM.value, ignoreCase = true)
                } ?: throw Exception("No wallet found")

                sdk.wallets.switchNetwork(wallet, Network.evm(network.chainId))
                val balance = sdk.wallets.getBalance(wallet)

                _state.value = WalletScreenState(
                    chain = wallet.chain,
                    address = wallet.address,
                    network = "${NetworkEnum.getName(network.chainId)}-${network.chainId}",
                    balance = balance
                )
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, errorMessage = it.message)
            }
        }
    }
}