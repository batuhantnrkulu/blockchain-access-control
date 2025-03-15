package com.blockchain.accesscontrol.access_control_system.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;

public abstract class BaseContractService<T>  
{
	protected final Web3j web3j;
	protected final TransactionManagerFactory transactionManagerFactory;
	protected final String contractAddress;
	
	public BaseContractService(Web3j web3j, TransactionManagerFactory transactionManagerFactory, String contractAddress)
	{
		this.web3j = web3j;
		this.transactionManagerFactory = transactionManagerFactory;
		this.contractAddress = contractAddress;
	}
	
	protected T loadContract(Class<T> contractClass, String contractAddress, String privateKey) 
	{
        TransactionManager txManager = transactionManagerFactory.createTransactionManager(privateKey);

        try {
            // Use contractClass.getMethod("load", ...) to dynamically invoke the load method
            Method loadMethod = contractClass.getMethod("load", String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);

            // Invoke the `load` method and cast it to T
            return contractClass.cast(
                loadMethod.invoke(null, contractAddress, web3j, txManager, new DefaultGasProvider())
            );

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error loading contract: " + e.getMessage(), e); // Handle exceptions appropriately
        }
    }
	
	protected T loadContract(Class<T> contractClass, String privateKey) 
	{
        TransactionManager txManager = transactionManagerFactory.createTransactionManager(privateKey);

        try {
            // Use contractClass.getMethod("load", ...) to dynamically invoke the load method
            Method loadMethod = contractClass.getMethod("load", String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);

            // Invoke the `load` method and cast it to T
            return contractClass.cast(
                loadMethod.invoke(null, contractAddress, web3j, txManager, new DefaultGasProvider())
            );

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error loading contract: " + e.getMessage(), e); // Handle exceptions appropriately
        }
    }
	
	protected T loadContract(Class<T> contractClass) {
        TransactionManager txManager = transactionManagerFactory.defaultTransactionManager();

        try {
            // Use contractClass.getMethod("load", ...) to dynamically invoke the load method
            Method loadMethod = contractClass.getMethod("load", String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);

            // Invoke the `load` method and cast it to T
            return contractClass.cast(
                loadMethod.invoke(null, contractAddress, web3j, txManager, new DefaultGasProvider())
            );

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error loading contract: " + e.getMessage(), e); // Handle exceptions appropriately
        }
    }
}
