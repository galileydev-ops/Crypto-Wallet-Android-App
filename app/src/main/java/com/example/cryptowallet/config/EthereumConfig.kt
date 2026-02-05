package com.example.cryptowallet.config

import java.math.BigInteger

object EthereumConfig {
    val GAS_LIMIT: BigInteger = BigInteger.valueOf(21_000)
    val MAX_FEE_PER_GAS: BigInteger = BigInteger.valueOf(50_000_000_000)
    val MAX_PRIORITY_FEE_PER_GAS: BigInteger = BigInteger.valueOf(2_000_000_000)
    val URL_PREFIX = "https://goerli.etherscan.io/tx"
}