package com.blockchain.accesscontrol.access_control_system.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;
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
	private final BehaviorHistoryService behaviorHistoryService;
	private final NotificationService notificationService;
	private final SimpMessagingTemplate messagingTemplate;
	
	public ValidatorExpiryService(ValidatorListRepository validatorListRepository, RoleTokenService roleTokenService,
			NotificationService notificationService, SimpMessagingTemplate messagingTemplate, BehaviorHistoryService behaviorHistoryService) 
	{
		this.validatorListRepository = validatorListRepository;
		this.roleTokenService = roleTokenService;
		this.behaviorHistoryService = behaviorHistoryService;
		this.notificationService = notificationService;
		this.messagingTemplate = messagingTemplate;
	}
	
	@Scheduled(fixedDelay = 60000) // in every one minute
	@Transactional
	public void processExpiredValidatorRequests() 
	{
		LocalDateTime now = LocalDateTime.now();
	    List<ValidatorList> expiredRequests = validatorListRepository.findByApprovedFalseAndExpiryTimeBefore(now);

	    for (ValidatorList validatorEntry : expiredRequests) 
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
	}
}
