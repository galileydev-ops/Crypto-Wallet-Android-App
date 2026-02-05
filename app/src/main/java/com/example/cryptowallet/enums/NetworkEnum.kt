package com.example.cryptowallet.enums

enum class NetworkEnum(val chainId: Int, val displayName: String) {
    ETHEREUM_MAINNET(1, "Ethereum"),
    SEPOLIA(11155111, "Sepolia"),
    POLYGON_MAINNET(137, "Polygon"),
    OPTIMISM_MAINNET(10, "Optimism"),
    ARBITRUM_MAINNET(42161, "Arbitrum");

    companion object {
        fun getName(chainId: Int): String {
            return entries.firstOrNull { it.chainId == chainId }?.displayName ?: "Unknown"
        }
    }
}