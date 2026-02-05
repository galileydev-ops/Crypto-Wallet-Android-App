package com.example.cryptowallet.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dynamic.sdk.android.Models.ChainEnum
import com.example.cryptowallet.R
import com.example.cryptowallet.enums.NetworkEnum

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailsScreen(
    walletModel: WalletViewModel,
    onLogout: () -> Unit,
    onSendTransaction: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by walletModel.state.collectAsState()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        walletModel.connectWallet(NetworkEnum.SEPOLIA, ChainEnum.EVM)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.wallet_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(state.chain, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.address_label), fontWeight = FontWeight.Medium)
                        Text(
                            state.address,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(stringResource(R.string.current_network_label), fontWeight = FontWeight.Medium)
                        Text(state.network)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(stringResource(R.string.balance_label), fontWeight = FontWeight.Medium)
                        Text(
                            "${state.balance} ETH",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { clipboardManager.setText(AnnotatedString(state.address)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.address.isNotEmpty()
                ) {
                    Text(stringResource(R.string.copy_address_button))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onSendTransaction() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.address.isNotEmpty()
                ) {
                    Text(stringResource(R.string.send_transaction_button))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { onLogout() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.logout_button))
                }

                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(error, color = Color.Red)
                }
            }
        }
    }
}