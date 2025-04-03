package com.blockchain.accesscontrol.access_control_system.config;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.response.TransactionReceiptProcessor;

public class CustomRawTransactionManager extends FastRawTransactionManager {

    private final TransactionManagerFactory nonceManager;
    private final String address;

    public CustomRawTransactionManager(Web3j web3j, Credentials credentials, long chainId,
                                       TransactionReceiptProcessor transactionReceiptProcessor,
                                       TransactionManagerFactory nonceManager) {
        super(web3j, credentials, chainId, transactionReceiptProcessor);
        this.nonceManager = nonceManager;
        this.address = credentials.getAddress();
    }

    @Override
    protected BigInteger getNonce() 
    {
        try {
			return nonceManager.getAndIncrementNonce(address);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}