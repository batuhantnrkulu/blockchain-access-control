package com.blockchain.accesscontrol.access_control_system.config;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import com.blockchain.accesscontrol.access_control_system.utils.AtomicBigInteger;

@Component
public class TransactionManagerFactory 
{
	@Value("${admin.private-key}")
	private String privateKey;
	
	private final Web3j web3j;
	private final Map<String, AtomicBigInteger> nonceMap = new ConcurrentHashMap<>();
	
	public TransactionManagerFactory(Web3j web3j)
	{
		this.web3j = web3j;
	}
	
	public CustomRawTransactionManager createCustomTransactionManager(String userPrivateKey) {
	    Credentials credentials = Credentials.create(userPrivateKey);
	    long chainId = 1337; // Ganache
	    int attempts = 40;
	    int sleepDuration = 1500;

	    return new CustomRawTransactionManager(
	        web3j,
	        credentials,
	        chainId,
	        new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts),
	        this // to access nonceMap
	    );
	}
	
	public synchronized BigInteger getAndIncrementNonce(String address) throws Exception {
	    if (!nonceMap.containsKey(address)) {
	        BigInteger chainNonce = web3j.ethGetTransactionCount(
	            address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
	        nonceMap.put(address, new AtomicBigInteger(chainNonce));
	    }
	    return nonceMap.get(address).getAndIncrement();
	}
	
	public FastRawTransactionManager defaultTransactionManager() 
	{
		return createCustomTransactionManager(privateKey);
    }
}
