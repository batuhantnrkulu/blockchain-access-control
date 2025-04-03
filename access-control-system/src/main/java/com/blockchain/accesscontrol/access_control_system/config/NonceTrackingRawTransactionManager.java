package com.blockchain.accesscontrol.access_control_system.config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;

//Add NonceTrackingRawTransactionManager.java
public class NonceTrackingRawTransactionManager extends RawTransactionManager {
 private final AtomicReference<BigInteger> currentNonce = new AtomicReference<>();
 
 public NonceTrackingRawTransactionManager(Web3j web3j, Credentials credentials, long chainId) {
     super(web3j, credentials, chainId);
 }

 @Override
 public BigInteger getNonce() throws IOException {
     if(currentNonce.get() == null) {
         currentNonce.set(super.getNonce());
     }
     return currentNonce.getAndUpdate(n -> n.add(BigInteger.ONE));
 }
}