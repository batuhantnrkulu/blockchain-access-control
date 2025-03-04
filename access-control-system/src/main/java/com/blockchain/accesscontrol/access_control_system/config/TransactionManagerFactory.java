package com.blockchain.accesscontrol.access_control_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;

@Component
public class TransactionManagerFactory 
{
	@Value("${admin.private-key}")
	private String privateKey;
	
	private final Web3j web3j;
	
	public TransactionManagerFactory(Web3j web3j)
	{
		this.web3j = web3j;
	}
	
	public RawTransactionManager createTransactionManager(String userPrivateKey)
	{
		Credentials credentials = Credentials.create(userPrivateKey);
		return new RawTransactionManager(web3j, credentials);
	}
	
	public RawTransactionManager defaultTransactionManager()
	{
		return createTransactionManager(privateKey);
	}
}
