package com.blockchain.accesscontrol.access_control_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.AccessControlFactory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

@Service
public class AccessControlService extends BaseContractService<AccessControlFactory>
{
	private final PeerRepository peerRepository;
    private final EncryptionUtil encryptionUtil;
	
	public AccessControlService(Web3j web3j, TransactionManagerFactory transactionManagerFactory,
			@Value("${contract.accessControlFactoryAddress}") String contractAddress, PeerRepository peerRepository, EncryptionUtil encryptionUtil) 
	{
		super(web3j, transactionManagerFactory, contractAddress);
		
		this.encryptionUtil = encryptionUtil;
		this.peerRepository = peerRepository;
	}
	
	public String deployAccessControlContract(Long objectPeerId, Long subjectPeerId, String accType) 
	{
        Peer objectPeer = peerRepository.findById(objectPeerId)
                .orElseThrow(() -> new RuntimeException("Object peer not found"));
        Peer subjectPeer = peerRepository.findById(subjectPeerId)
                .orElseThrow(() -> new RuntimeException("Subject peer not found"));
        
        String objectAddress = objectPeer.getBcAddress();
        String subjectAddress = subjectPeer.getBcAddress();
        String objectPrivateKey = objectPeer.getPrivateKey(encryptionUtil);
        
        // Load the AccessControlFactory contract using the object peer's credentials.
        AccessControlFactory accessControlFactory = loadContract(AccessControlFactory.class, objectPrivateKey);
        System.out.println("test: " + objectPrivateKey);
        
        try 
        {
        	EthGetBalance balance = web3j.ethGetBalance(objectAddress, DefaultBlockParameterName.LATEST).send();
        	System.out.println("Object balance: " + balance.getBalance());
        	
            // Deploy the ACC contract.
            TransactionReceipt receipt = accessControlFactory.deployAccessControlContract(objectAddress, subjectAddress, accType).send();
            
            // Check if the transaction reverted.
            if (!"0x1".equals(receipt.getStatus())) 
            {
                throw new RuntimeException("Transaction reverted with status: " + receipt.getStatus());
            }
            
            // Listen for the ACCDeployed event.
            List<AccessControlFactory.ACCDeployedEventResponse> events = accessControlFactory.getACCDeployedEvents(receipt);
            
            if (events == null || events.isEmpty()) 
            {
                throw new RuntimeException("No ACCDeployed event found; possibly the transaction reverted.");
            }
            
            return events.get(0).accAddress;
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Error deploying access control contract: " + e.getMessage(), e);
        }
    }
	
}
