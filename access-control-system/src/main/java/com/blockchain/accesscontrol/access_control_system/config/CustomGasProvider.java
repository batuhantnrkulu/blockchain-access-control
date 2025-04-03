package com.blockchain.accesscontrol.access_control_system.config;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.tx.gas.StaticGasProvider;

public class CustomGasProvider extends StaticGasProvider {
    // Match Ganache's default gas price (20 GWei)
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L); 
    
    // Set below Ganache's block gas limit (30M)
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000); 

    public CustomGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}