package com.example.cryptowallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import com.example.cryptowallet.navigation.NavGraph
import com.example.cryptowallet.ui.theme.CryptoWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDynamicSDK()
        setContent {
            CryptoWalletTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                    DynamicUI()
                }
            }
        }
    }

    private fun initDynamicSDK() {
        val props = ClientProps(
            environmentId = BuildConfig.ENVIRONMENT_ID,
            appName = "Crypto Wallet Test",
            redirectUrl = "${BuildConfig.APP_SCHEME}://",
            appOrigin = "https://test.app",
            logLevel = LoggerLevel.DEBUG
        )

        DynamicSDK.initialize(props, applicationContext, this)
    }
}