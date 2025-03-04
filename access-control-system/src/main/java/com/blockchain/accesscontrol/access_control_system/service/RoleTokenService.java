package com.blockchain.accesscontrol.access_control_system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.RoleToken;
import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.MemberDTO;
import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.mapper.MemberMapper;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.UnjoinedPeer;
import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;
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
    
    // Inject the WebSocket messaging template
    private final SimpMessagingTemplate messagingTemplate;
	
	public RoleTokenService(Web3j web3j, TransactionManagerFactory transactionManagerFactory,
			@Value("${contract.roleTokenAddress}") String contractAddress, MemberMapper memberMapper,
			PeerRepository peerRepository, ValidatorListRepository validatorListRepository,
			UnjoinedPeerRepository unjoinedPeerRepository, EncryptionUtil encryptionUtil, PasswordEncoder passwordEncoder,
			SimpMessagingTemplate messagingTemplate) 
	{
		super(web3j, transactionManagerFactory, contractAddress);
		this.memberMapper = memberMapper;
		this.peerRepository = peerRepository;
		this.validatorListRepository = validatorListRepository;
		this.unjoinedPeerRepository = unjoinedPeerRepository;
		this.encryptionUtil = encryptionUtil;
		this.passwordEncoder = passwordEncoder;
		this.messagingTemplate = messagingTemplate;
	}
	
	@Transactional
    public void assignRole(PeerRegistrationRequest peerRegistrationRequest) 
	{
		long primaryHeadCount = peerRepository.countAllByRoleAndStatus(Role.PRIMARY_GROUP_HEAD, Status.BENIGN);
        int requiredValidators = calculateRequiredValidators(primaryHeadCount);

        if (requiredValidators > 0) 
        {
            assignValidators(peerRegistrationRequest, requiredValidators);
        } 
        else 
        {
            assignDirectly(peerRegistrationRequest);
        }
    }

    private int calculateRequiredValidators(long primaryHeadCount) 
    {
        if (primaryHeadCount >= 3 && primaryHeadCount < 5) 
        {
            return 1;
        } 
        else if (primaryHeadCount >= 5 && primaryHeadCount <= 10) 
        {
            return 3;
        } 
        else if (primaryHeadCount > 10) 
        {
            return 7;
        }
        
        return 0;
    }

    private void assignDirectly(PeerRegistrationRequest peerRegistrationRequest) 
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
	                System.out.println("Role Type: " + event.memberType);

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

        UnjoinedPeer unjoinedPeer = unjoinedPeerRepository.findByBcAddress(request.getBlockchainAddress())
                .orElseGet(() -> {
                	UnjoinedPeer newPeer = new UnjoinedPeer();
                    newPeer.setUsername(request.getUsername());
                    newPeer.setPassword(request.getPassword());
                    newPeer.setIpAddress(request.getIpAddress());
                    newPeer.setBcAddress(request.getBlockchainAddress()); 
                    newPeer.setUsagePurpose(request.getUsagePurpose());
                    newPeer.setGroup(request.getGroup());
                    newPeer.setIsWebUser(request.getIsWebUser());

                    return unjoinedPeerRepository.save(newPeer);
                });

        List<Peer> selectedValidators = ConsensusUtil.selectWeightedRandom(primaryHeads, count);

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
            String message = "Validation request for new peer: " + unjoinedPeer.getUsername();

            // Send the notification over a dedicated destination.
            // For example, each validator's client can subscribe to "/topic/validator/{blockchainAddress}"
            String destination = "/topic/validator/" + validator.getBcAddress();
            messagingTemplate.convertAndSend(destination, message);

            // Optionally log or further process.
            System.out.println("Sent notification to validator " + validator.getBcAddress());
        }

        System.out.println("Assigned " + selectedValidators.size() + " validators.");
    }
	
	public MemberDTO getMember(String memberAddress) 
	{
        RoleToken contract = loadContract(RoleToken.class);

        try {
            RoleToken.Member memberStruct = contract.getMember(memberAddress).send();
            return memberMapper.toMemberDTO(memberStruct);  // Use MapStruct for mapping
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch member details.", e);
        }
    }
}
