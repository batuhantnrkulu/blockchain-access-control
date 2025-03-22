package com.blockchain.accesscontrol.access_control_system.service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.RoleToken;
import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.MemberDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.UnjoinedPeerResponseDTO;
import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.mapper.MemberMapper;
import com.blockchain.accesscontrol.access_control_system.mapper.UnjoinedPeerMapper;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.UnjoinedPeer;
import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;
import com.blockchain.accesscontrol.access_control_system.repository.BehaviorHistoryRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.UnjoinedPeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ValidatorListRepository;
import com.blockchain.accesscontrol.access_control_system.utils.ConsensusUtil;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;
import com.blockchain.accesscontrol.access_control_system.utils.RoleUtils;

import jakarta.transaction.Transactional;

@Service
public class RoleTokenService extends BaseContractService<RoleToken>
{
	private final MemberMapper memberMapper;
	private final PeerRepository peerRepository;
	private final UnjoinedPeerRepository unjoinedPeerRepository;
    private final ValidatorListRepository validatorListRepository;
    private final EncryptionUtil encryptionUtil;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final UnjoinedPeerMapper unjoinedPeerMapper;
    private final BehaviorHistoryRepository behaviorHistoryRepository;
    private final ConsensusUtil consensusUtil;
    
    // Inject the WebSocket messaging template
    private final SimpMessagingTemplate messagingTemplate;
	
	public RoleTokenService(Web3j web3j, TransactionManagerFactory transactionManagerFactory,
			@Value("${contract.roleTokenAddress}") String contractAddress, MemberMapper memberMapper,
			PeerRepository peerRepository, ValidatorListRepository validatorListRepository,
			UnjoinedPeerRepository unjoinedPeerRepository, EncryptionUtil encryptionUtil, PasswordEncoder passwordEncoder,
			SimpMessagingTemplate messagingTemplate, NotificationService notificationService, UnjoinedPeerMapper unjoinedPeerMapper,
			BehaviorHistoryRepository behaviorHistoryRepository, ConsensusUtil consensusUtil) 
	{
		super(web3j, transactionManagerFactory, contractAddress);
		this.memberMapper = memberMapper;
		this.peerRepository = peerRepository;
		this.validatorListRepository = validatorListRepository;
		this.unjoinedPeerRepository = unjoinedPeerRepository;
		this.encryptionUtil = encryptionUtil;
		this.passwordEncoder = passwordEncoder;
		this.messagingTemplate = messagingTemplate;
		this.notificationService = notificationService;
		this.unjoinedPeerMapper = unjoinedPeerMapper;
		this.behaviorHistoryRepository = behaviorHistoryRepository;
		this.consensusUtil = consensusUtil;
	}
	
	@Transactional
    public String assignRole(PeerRegistrationRequest peerRegistrationRequest) 
	{
		long primaryHeadCount = peerRepository.countAllByRoleAndStatus(Role.PRIMARY_GROUP_HEAD, Status.BENIGN);
        int requiredValidators = calculateRequiredValidators(primaryHeadCount); // for skipping 0

        if (requiredValidators > 0) 
        {
            assignValidators(peerRegistrationRequest, requiredValidators);
            return "Request has been sent to Validators to vote!";
        } 
        else 
        {
            assignDirectly(peerRegistrationRequest);
            return "Role assigned successfully!";
        }
    }

	private int calculateRequiredValidators(long primaryHeadCount) 
	{
	    if (primaryHeadCount < 4) 
	    	return 0;
	    
	    // Inspired by OmniLedger's committee scaling (logarithmic) and 
	    // Algorand's committee size analysis (square root)
	    double logFactor = Math.log(primaryHeadCount + 1); // Natural logarithm
	    double sqrtFactor = Math.sqrt(primaryHeadCount);
	    
	    // Hybrid approach combining both log and sqrt terms
	    int validators = (int) Math.ceil(3.0 * logFactor + 0.5 * sqrtFactor);
	    
	    // Ensure minimum of 4 validators for BFT safety (Castro and Liskov, 2002)
	    validators = Math.max(validators, 4);
	    
	    return Math.min(validators, (int) primaryHeadCount);
	}

    public void assignDirectly(PeerRegistrationRequest peerRegistrationRequest) 
    {
    	RoleToken contract = loadContract(RoleToken.class);
		
		try 
		{
			TransactionReceipt receipt = contract.assignRole(peerRegistrationRequest.getBlockchainAddress(), 
	                peerRegistrationRequest.getUsername(), peerRegistrationRequest.getGroup()
	        ).send();

	        // Process the events from the receipt
	        List<RoleToken.RoleAssignedEventResponse> events = contract.getRoleAssignedEvents(receipt);
	        
	        if (!events.isEmpty()) 
	        {
	            for (RoleToken.RoleAssignedEventResponse event : events) 
	            {
	            	System.out.println("Role assigned:");
	                System.out.println("Member Address: " + event.memberAddress);
	                System.out.println("Group: " + event.memberType);

	                // Convert event.memberType (String) to Role Enum
	                Role assignedRole = RoleUtils.mapRole(event.role);

	                // Determine ERC-20 token amount
	                long tokenAmount = RoleUtils.getTokenAmount(assignedRole);

	                // Save peer in the database
	                Peer newPeer = new Peer();
	                newPeer.setBcAddress(event.memberAddress);
	                // use aes to encrypt private key
	                newPeer.setPrivateKey(peerRegistrationRequest.getPrivateKey(), encryptionUtil);
	                newPeer.setUsername(peerRegistrationRequest.getUsername());
	                // Hash the password before storage
	                newPeer.setPassword(passwordEncoder.encode(peerRegistrationRequest.getPassword()));
	                newPeer.setIpAddress(peerRegistrationRequest.getIpAddress());
	                newPeer.setRole(assignedRole);
	                newPeer.setUsagePurpose(peerRegistrationRequest.getUsagePurpose());
	                newPeer.setGroup(peerRegistrationRequest.getGroup());
	                newPeer.setIsWebUser(peerRegistrationRequest.getIsWebUser());
	                newPeer.setBcStatus(Status.BENIGN);
	                
	                if (!peerRegistrationRequest.getIsWebUser())
	                	newPeer.setIpAddress(peerRegistrationRequest.getIpAddress());
	                
	                newPeer.setErc20TokenAmount(tokenAmount);

	                peerRepository.save(newPeer);
	            }
	            
	        } else {
	            System.out.println("No events found in this transaction.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

    @Transactional
    public void assignValidators(PeerRegistrationRequest request, int count) 
    {
        List<Peer> primaryHeads = peerRepository.findAllByRoleAndStatus(Role.PRIMARY_GROUP_HEAD, Status.BENIGN);

        if (primaryHeads.isEmpty()) {
            System.out.println("No primary heads available. Cannot assign validators.");
            return;
        }

        // Check if the peer has already requested to join
        Optional<UnjoinedPeer> existingUnjoinedPeer = unjoinedPeerRepository.findByBcAddress(request.getBlockchainAddress());
        
        if (existingUnjoinedPeer.isPresent()) 
        {
            System.out.println("Peer already requested to join. Cannot assign validators again.");
            return;
        }
        
        UnjoinedPeer unjoinedPeer = new UnjoinedPeer();
        unjoinedPeer.setUsername(request.getUsername());
        unjoinedPeer.setPassword(request.getPassword());
        unjoinedPeer.setIpAddress(request.getIpAddress());
        unjoinedPeer.setBcAddress(request.getBlockchainAddress());
        unjoinedPeer.setPrivateKey(request.getPrivateKey(), encryptionUtil);
        unjoinedPeer.setUsagePurpose(request.getUsagePurpose());
        unjoinedPeer.setGroup(request.getGroup());
        unjoinedPeer.setIsWebUser(request.getIsWebUser());
        unjoinedPeer.setValidatorCount(count);
        unjoinedPeer = unjoinedPeerRepository.save(unjoinedPeer);

        List<Peer> selectedValidators = consensusUtil.selectWeightedRandom(primaryHeads, count, behaviorHistoryRepository);

        if (selectedValidators.isEmpty()) {
            System.out.println("No validators selected.");
            return;
        }

        for (Peer validator : selectedValidators) 
        {
            ValidatorList validatorEntry = new ValidatorList();
            validatorEntry.setUnjoinedPeer(unjoinedPeer);
            validatorEntry.setValidator(validator);
            validatorEntry.setApproved(false);
            validatorEntry.setExpiryTime(LocalDateTime.now().plusMinutes(30));

            validatorListRepository.save(validatorEntry);

            // Prepare notification message.
            String message = "Validation request for new peer: " + unjoinedPeer.getUsername() + "(" + unjoinedPeer.getId() + ")";

            // Create and persist the notification for the validator.
            notificationService.createNotification(validator, message, NotificationType.VALIDATION_REQUEST);
            
            // Send the notification over a dedicated destination.
            // For example, each validator's client can subscribe to "/topic/validator/{blockchainAddress}"
            String destination = "/topic/validator/" + validator.getBcAddress();
            messagingTemplate.convertAndSend(destination, message);

            // Optionally log or further process.
            System.out.println("Sent notification to validator " + validator.getBcAddress());
        }

        System.out.println("Assigned " + selectedValidators.size() + " validators.");
    }
    
    /**
     * Deducts the specified penalty amount from the given member both on-chain via the contract,
     * and off-chain by updating the peer record in the database.
     * Also sends a notification alerting the member of the penalty.
     *
     * @param memberAddress The blockchain address of the member.
     * @param penaltyAmount The penalty amount to deduct.
     */
    @Transactional
    public Long penalizeMemberAndUpdate(String memberAddress, long penaltyAmount) 
    {
    	RoleToken contract = loadContract(RoleToken.class);
    	
        try 
        {
            BigInteger penalty = BigInteger.valueOf(penaltyAmount);
            
            // Call contract function to penalize the member.
            TransactionReceipt receipt = contract.penalizeMember(memberAddress, penalty).send();
            System.out.println("Penalty Transaction Receipt: " + receipt.getTransactionHash());

            // Update the member's balance in the local database.
            Optional<Peer> peerOpt = peerRepository.findByBcAddress(memberAddress);
            
            if (peerOpt.isPresent()) 
            {
            	Peer peer = peerOpt.get();
                long currentBalance = peer.getErc20TokenAmount();
                long newBalance = currentBalance - penaltyAmount;
                peer.setErc20TokenAmount(newBalance);
                peerRepository.save(peer);
                
                return newBalance;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Rewards the member with the specified amount on-chain and off-chain.
     *
     * @param memberAddress The blockchain address of the member.
     * @param rewardAmount  The reward amount to add.
     */
    @Transactional
    public Long rewardMemberAndUpdate(String memberAddress, long rewardAmount) 
    {
    	RoleToken contract = loadContract(RoleToken.class);
    	
        try 
        {
            BigInteger reward = BigInteger.valueOf(rewardAmount);
            
            // Call contract function to add tokens to the member.
            TransactionReceipt receipt = contract.rewardMember(memberAddress, reward).send();
            System.out.println("Reward Transaction Receipt: " + receipt.getTransactionHash());

            // Update the member's token balance in the database.
            Optional<Peer> peerOpt = peerRepository.findByBcAddress(memberAddress);
            
            if (peerOpt.isPresent()) 
            {
            	Peer peer = peerOpt.get();
            	
                long currentBalance = peer.getErc20TokenAmount();
                long newBalance = currentBalance + rewardAmount;
                peer.setErc20TokenAmount(newBalance);
                peerRepository.save(peer);
                
                return newBalance;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return null;
    }
	
    /**
     * Retrieves and converts an UnjoinedPeer entity into a DTO
     * containing only the relevant fields: username, bcAddress, usagePurpose, and group.
     *
     * @param id the id of the unjoined peer.
     * @return UnjoinedPeerResponseDTO with selected details.
     */
    public UnjoinedPeerResponseDTO getUnjoinedPeerDetails(Long id) 
    {
        Optional<UnjoinedPeer> optionalPeer = unjoinedPeerRepository.findById(id);
        
        if (!optionalPeer.isPresent()) 
        {
            throw new RuntimeException("Unjoined Peer with id: " + id + " not found");
        }
        
        return unjoinedPeerMapper.toDto(optionalPeer.get());
    }
    
    public void assignAdminRole(String contractAddress) 
    {
        RoleToken contract = loadContract(RoleToken.class);
        
        try 
        {
            TransactionReceipt receipt = contract.assignAdminRole(contractAddress).send();
        
            if (!receipt.isStatusOK()) 
            {
                throw new RuntimeException("Failed to assign admin role to contract: " + contractAddress);
            }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Error assigning admin role to contract: " + contractAddress, e);
        }
    }
    
    @Transactional
    public void swapRoles(String account1, String account2) 
    {
        RoleToken contract = loadContract(RoleToken.class);
        
        try 
        {
            // Send the swap transaction
            TransactionReceipt receipt = contract.swapRole(account1, account2).send();
            
            // Check transaction status
            if (!receipt.isStatusOK()) 
            {
                throw new RuntimeException("Swap role transaction reverted");
            }
            
            // Verify the swap occurred by checking events
            List<RoleToken.RoleAssignedEventResponse> events = contract.getRoleAssignedEvents(receipt);
            
            if (events.size() != 2) {
                throw new RuntimeException("Invalid number of RoleAssigned events emitted");
            }

            // Update database
            updateLocalRoles(account1, account2);
            
        } 
        catch (Exception e) 
        {
            handleSwapException(e, account1, account2);
        }
    }

    private void updateLocalRoles(String account1, String account2) 
    {
        Peer peer1 = peerRepository.findByBcAddress(account1)
            .orElseThrow(() -> new RuntimeException("Peer not found: " + account1));
        
        Peer peer2 = peerRepository.findByBcAddress(account2)
            .orElseThrow(() -> new RuntimeException("Peer not found: " + account2));

        // Swap roles
        Role temp = peer1.getRole();
        peer1.setRole(peer2.getRole());
        peer2.setRole(temp);

        peerRepository.saveAll(List.of(peer1, peer2));
    }

    private void handleSwapException(Exception e, String account1, String account2) 
    {
        String errorMessage = "Role swap failed between " + account1 + " and " + account2 + ": ";
        
        if (e instanceof TransactionException) 
        {
            errorMessage += "Blockchain transaction error - " + e.getMessage();
        } 
        else if (e instanceof IOException) 
        {
            errorMessage += "Network communication error";
        } 
        else 
        {
            errorMessage += "Unexpected error - " + e.getMessage();
        }

        // Log detailed error for debugging
        System.err.println(errorMessage);
        e.printStackTrace();

        throw new RuntimeException(errorMessage, e);
    }
}
