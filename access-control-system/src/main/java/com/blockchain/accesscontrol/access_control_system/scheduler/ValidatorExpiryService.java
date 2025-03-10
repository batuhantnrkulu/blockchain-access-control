package com.blockchain.accesscontrol.access_control_system.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.requests.ValidatorRequestDTO;
import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.mapper.UnjoinedPeerMapper;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.UnjoinedPeer;
import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;
import com.blockchain.accesscontrol.access_control_system.repository.UnjoinedPeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ValidatorListRepository;
import com.blockchain.accesscontrol.access_control_system.service.BehaviorHistoryService;
import com.blockchain.accesscontrol.access_control_system.service.NotificationService;
import com.blockchain.accesscontrol.access_control_system.service.RoleTokenService;

import jakarta.transaction.Transactional;

@Service
public class ValidatorExpiryService 
{
	private final ValidatorListRepository validatorListRepository;
	private final RoleTokenService roleTokenService;
	private final UnjoinedPeerRepository unjoinedPeerRepository;
	private final UnjoinedPeerMapper unjoinedPeerMapper;
	private final BehaviorHistoryService behaviorHistoryService;
	private final NotificationService notificationService;
	private final SimpMessagingTemplate messagingTemplate;
	
	public ValidatorExpiryService(ValidatorListRepository validatorListRepository, RoleTokenService roleTokenService,
			NotificationService notificationService, SimpMessagingTemplate messagingTemplate, UnjoinedPeerMapper unjoinedPeerMapper,
			BehaviorHistoryService behaviorHistoryService, UnjoinedPeerRepository unjoinedPeerRepository) 
	{
		this.validatorListRepository = validatorListRepository;
		this.roleTokenService = roleTokenService;
		this.behaviorHistoryService = behaviorHistoryService;
		this.notificationService = notificationService;
		this.messagingTemplate = messagingTemplate;
		this.unjoinedPeerMapper = unjoinedPeerMapper;
		this.unjoinedPeerRepository = unjoinedPeerRepository;
	}
	
	public ResponseEntity<?> submitResponse(ValidatorRequestDTO request)
	{
		Optional<ValidatorList> optionalValidatorList =
                validatorListRepository.findByUnjoinedPeerIdAndValidator_BcAddress(request.getUnjoinedPeerId(), request.getValidatorBcAddress());
        
        if (!optionalValidatorList.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        ValidatorList validatorEntry = optionalValidatorList.get();
        
        // If the request is already expired, do not accept a response.
        if (validatorEntry.getExpiryTime().isBefore(LocalDateTime.now())) {
        	return ResponseEntity.badRequest().body("Request is expired.");
        }
        
        // If the validator has already provided a response, reject further updates.
        if (validatorEntry.getResponded()) {
        	return ResponseEntity.badRequest().body("Request already submitted.");
        }
        
        // Update the record based on the validator's response.
        validatorEntry.setApproved(request.getApproved());
        validatorEntry.setResponded(true);
        validatorListRepository.save(validatorEntry);
        
        return ResponseEntity.ok("Request recorded successfully.");
	}
	
	@Scheduled(fixedDelay = 60000) // in every one minute
	@Transactional
	public void processExpiredValidatorRequests() 
	{
		LocalDateTime now = LocalDateTime.now();
	    List<ValidatorList> expiredRequests = validatorListRepository.findByApprovedFalseAndExpiryTimeBefore(now);

	    for (ValidatorList validatorEntry : expiredRequests) 
	    {
	    	// Skip entries that have been responded to.
	        if (validatorEntry.getResponded()) 
	        {
	        	Peer validator = validatorEntry.getValidator();
		        String validatorAddress = validator.getBcAddress();

		        // Reward validator and get the updated token balance
		        Long updatedTokenBalance = roleTokenService.rewardMemberAndUpdate(validatorAddress, 1000);

		        // If token balance is null, do not proceed
		        if (updatedTokenBalance == null) 
		        {
		            System.err.println("Error: Failed to fetch updated token balance for " + validatorAddress);
		            continue;
		        }

		        // Log to BehaviorHistory
		        BehaviorHistory history = new BehaviorHistory();
		        history.setPeer(validator);
		        history.setTokenAmount(updatedTokenBalance);  // Save the latest balance
		        history.setTokenAmountChange(1000L); // Save the deduction amount
		        history.setReason("Validator did not respond in time");
		        history.setStatusUpdate(now);
		        behaviorHistoryService.addHistory(history);

		        // Send notification
		        String message = "You did respond in time. 1000 tokens rewarded. Your current balance: " + updatedTokenBalance;
		        notificationService.createNotification(validator, message, NotificationType.VALIDATION);

		        // Send WebSocket notification to frontend (Optional)
		        String destination = "/topic/validator/" + validator.getBcAddress();
	            messagingTemplate.convertAndSend(destination, message);

		        // Logging
		        System.out.println("Processed reward for validator " + validatorAddress + ", new balance: " + updatedTokenBalance);
		        
		        // remove validator from the list
		        validatorListRepository.delete(validatorEntry);
	        	
	            continue;
	        }
	        else
	        {
	        	Peer validator = validatorEntry.getValidator();
		        String validatorAddress = validator.getBcAddress();

		        // Penalize validator and get the updated token balance
		        Long updatedTokenBalance = roleTokenService.penalizeMemberAndUpdate(validatorAddress, 1000);

		        // If token balance is null, do not proceed
		        if (updatedTokenBalance == null) 
		        {
		            System.err.println("Error: Failed to fetch updated token balance for " + validatorAddress);
		            continue;
		        }

		        // Log to BehaviorHistory
		        BehaviorHistory history = new BehaviorHistory();
		        history.setPeer(validator);
		        history.setTokenAmount(updatedTokenBalance);  // Save the latest balance
		        history.setTokenAmountChange(-1000L); // Save the deduction amount
		        history.setReason("Validator did not respond in time");
		        history.setStatusUpdate(now);
		        behaviorHistoryService.addHistory(history);

		        // Send notification
		        String message = "You did not respond in time. 1000 tokens deducted. Your current balance: " + updatedTokenBalance;
		        notificationService.createNotification(validator, message, NotificationType.VALIDATION);

		        // Send WebSocket notification to frontend (Optional)
		        String destination = "/topic/validator/" + validator.getBcAddress();
	            messagingTemplate.convertAndSend(destination, message);

		        // Logging
		        System.out.println("Processed expiry for validator " + validatorAddress + ", new balance: " + updatedTokenBalance);
		        
		        // remove validator from the list
		        validatorListRepository.delete(validatorEntry);
	        }
	        
	        // Group the expired validator assignments by their associated unjoined peer.
	        Map<UnjoinedPeer, List<ValidatorList>> assignmentsByUnjoinedPeer = expiredRequests.stream()
	                .collect(Collectors.groupingBy(ValidatorList::getUnjoinedPeer));

	        // For each unjoined peer, calculate consensus and take appropriate action.
	        for (Map.Entry<UnjoinedPeer, List<ValidatorList>> entry : assignmentsByUnjoinedPeer.entrySet()) 
	        {
	            UnjoinedPeer unjoinedPeer = entry.getKey();
	            List<ValidatorList> assignments = entry.getValue();

	            // Determine how many validator assignments approved the request.
	            long approvedCount = assignments.stream()
	                    .filter(vl -> vl.getApproved())
	                    .count();

	            // Use the stored validatorCount, which is the total expected number.
	            int totalValidators = unjoinedPeer.getValidatorCount();
	            double approvalRate = (double)approvedCount / totalValidators;

	            if (approvalRate >= 0.51) 
	            {
	                // Consensus achieved: onboard the unjoined peer into the system.
	            	PeerRegistrationRequest peerRegistrationRequest = unjoinedPeerMapper.toPeerRegistrationRequest(unjoinedPeer);
	                roleTokenService.assignDirectly(peerRegistrationRequest);
	                
	                // Optionally, notify the applicant about their successful registration.
	                System.out.println("Unjoined peer " + unjoinedPeer.getBcAddress() + " approved by consensus (" + (approvalRate * 100) + "%).");
	            } 
	            else 
	            {
	                // Consensus not met: the registration is rejected.
	                System.out.println("Unjoined peer " + unjoinedPeer.getBcAddress() + " rejected by consensus (" + (approvalRate * 100) + "%).");
	            }

	            // Remove the unjoined peer record from the repository.
	            unjoinedPeerRepository.delete(unjoinedPeer);

	            // Clean up all validator assignments for this unjoined peer.
	            validatorListRepository.deleteAll(assignments);
	        }
	    }
	}
}
