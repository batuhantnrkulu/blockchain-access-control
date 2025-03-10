package com.blockchain.accesscontrol.access_control_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.AccessControlFactory;
import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.AccessRequestRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

@Service
public class AccessRequestService
{
	private final PeerRepository peerRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccessControlService accessControlService;
    private final NotificationService notificationService;
	
    public AccessRequestService(PeerRepository peerRepository, AccessRequestRepository accessRequestRepository,
            SimpMessagingTemplate messagingTemplate, AccessControlService accessControlService,
            NotificationService notificationService) 
    {
		this.peerRepository = peerRepository;
		this.accessRequestRepository = accessRequestRepository;
		this.messagingTemplate = messagingTemplate;
		this.accessControlService = accessControlService;
		this.notificationService = notificationService;
	}
    
    /**
     * Creates a new access request record for a subject peer requesting access
     * to an object peer’s resource. Also sends a notification (via WebSocket) to the object peer.
     */
    public AccessRequest createAccessRequest(Long objectPeerId, Long subjectPeerId) 
    {
        Peer objectPeer = peerRepository.findById(objectPeerId)
                .orElseThrow(() -> new RuntimeException("Object peer not found"));
        Peer subjectPeer = peerRepository.findById(subjectPeerId)
                .orElseThrow(() -> new RuntimeException("Subject peer not found"));
        
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setObjectPeer(objectPeer);
        accessRequest.setSubjectPeer(subjectPeer);
        accessRequest.setApproved(false);
        accessRequest.setAccAddress(null);
        accessRequestRepository.save(accessRequest);
        
        // create notification that shows subject peer's request on object peer.
        notificationService.createNotification(objectPeer, subjectPeer.getUsername() + " wants to create access mechanism with you", 
        		NotificationType.ACCESS_CONTROL);
        
        // Notify the object peer (using its bc_address as the channel)
        String objectNotificationChannel = "/topic/notifications/" + objectPeer.getBcAddress();
        String notificationMessage = "New access request from " + subjectPeer.getUsername() +
                " (Request ID: " + accessRequest.getId() + ")";
        messagingTemplate.convertAndSend(objectNotificationChannel, notificationMessage);
        
        return accessRequest;
    }
    
    /**
     * When the object peer approves the access request, deploy the ACC via AccessControlService,
     * update the record, and notify the subject peer.
     */
    public String approveAccessRequest(Long accessRequestId, String accType) 
    {
        AccessRequest accessRequest = accessRequestRepository.findById(accessRequestId)
                .orElseThrow(() -> new RuntimeException("Access request not found"));
        
        // Ensure that the caller is the object peer
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = auth.getName();
        
        if (!accessRequest.getObjectPeer().getUsername().equals(callerUsername)) 
        {
            throw new RuntimeException("You are not authorized to approve this access request");
        }
        
        if (accessRequest.getApproved()) 
        {
            throw new RuntimeException("Access request already approved");
        }
        
        Peer objectPeer = accessRequest.getObjectPeer();
        Peer subjectPeer = accessRequest.getSubjectPeer();
        
        // Deploy the ACC using the object peer's credentials.
        String accAddress = accessControlService.deployAccessControlContract(objectPeer.getId(), subjectPeer.getId(), accType);
        
        // Update the access request.
        accessRequest.setAccAddress(accAddress);
        accessRequest.setApproved(true);
        accessRequestRepository.save(accessRequest);
        
        // create notification that shows subject peer's request on object peer.
        notificationService.createNotification(subjectPeer, objectPeer.getUsername() + " accepted your request.", 
        		NotificationType.ACCESS_CONTROL);
        
        // Notify the subject peer that access is granted.
        String subjectNotificationChannel = "/topic/notifications/" + subjectPeer.getBcAddress();
        String notificationMessage = "Your access request (ID: " + accessRequest.getId() +
                ") has been approved. ACC deployed at: " + accAddress;
        messagingTemplate.convertAndSend(subjectNotificationChannel, notificationMessage);
        
        return accAddress;
    }
    
    public List<AccessRequest> getRequestsICreated(String username) {
        return accessRequestRepository.findBySubjectPeer_Username(username);
    }

    public List<AccessRequest> getRequestsToMe(String username) {
        return accessRequestRepository.findByObjectPeer_Username(username);
    }
	
}
