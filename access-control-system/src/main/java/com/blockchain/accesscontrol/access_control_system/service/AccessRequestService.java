package com.blockchain.accesscontrol.access_control_system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.aot.generate.AccessControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PermissionRequestDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.OtherPeerResourceResponseDTO;
import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.enums.Permission;
import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.exception.ResourceNotFoundException;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.Policy;
import com.blockchain.accesscontrol.access_control_system.model.Resource;
import com.blockchain.accesscontrol.access_control_system.repository.AccessRequestRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PolicyRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ResourceRepository;

import jakarta.transaction.Transactional;

@Service
public class AccessRequestService
{
	private final PeerRepository peerRepository;
	private final ResourceRepository resourceRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final PolicyRepository policyRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccessControlService accessControlService;
    private final NotificationService notificationService;
    private final RoleTokenService roleTokenService;
	
    public AccessRequestService(PeerRepository peerRepository, AccessRequestRepository accessRequestRepository,
            SimpMessagingTemplate messagingTemplate, AccessControlService accessControlService,
            NotificationService notificationService, ResourceRepository resourceRepository, PolicyRepository policyRepository,
            RoleTokenService roleTokenService) 
    {
		this.peerRepository = peerRepository;
		this.accessRequestRepository = accessRequestRepository;
		this.messagingTemplate = messagingTemplate;
		this.accessControlService = accessControlService;
		this.resourceRepository = resourceRepository;
		this.notificationService = notificationService;
		this.policyRepository = policyRepository;
		this.roleTokenService = roleTokenService;
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
        
        roleTokenService.assignAdminRole(accAddress);
        
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
    
    /**
     * Retrieves a paginated list of "Other Peers' Resources" for the current peer.
     * The filtering is based on the current peer's role and group.
     *
     * @param peerId   the current peer's ID
     * @param search   an optional search parameter (not implemented in detail here)
     * @param pageable pagination details
     * @return a page of OtherPeerResourceDTO objects
     */
    public Page<OtherPeerResourceResponseDTO> getOtherPeersResources(Long peerId, String search, Pageable pageable) 
    {
        // Look up the current peer based on peerId.
        Peer currentPeer = peerRepository.findById(peerId)
                .orElseThrow(() -> new ResourceNotFoundException("Peer not found with id: " + peerId));

        // Query resources based on the current peer's role.
        Page<Resource> resourcesPage;
        
        if (currentPeer.getRole().toString().equals("PRIMARY_GROUP_HEAD")) 
        {
            resourcesPage = (search != null && !search.isEmpty()) 
                ? resourceRepository.findForPrimaryGroupHeadWithSearch(peerId, currentPeer.getGroup(), search, pageable) 
                : resourceRepository.findForPrimaryGroupHead(peerId, currentPeer.getGroup(), pageable);
        } 
        else 
        {
            resourcesPage = (search != null && !search.isEmpty()) 
                ? resourceRepository.findByGroupWithSearchExcludingPeer(peerId, currentPeer.getGroup(), search, pageable) 
                : resourceRepository.findByGroupExcludingPeer(peerId, currentPeer.getGroup(), pageable);
        }

        // Map each resource into a DTO.
        return resourcesPage.map(resource -> {
            OtherPeerResourceResponseDTO dto = new OtherPeerResourceResponseDTO();
            dto.setResourceId(resource.getId());
            dto.setResourceName(resource.getResourceName());
            dto.setOwnerPeerName(resource.getPeer().getUsername());
            dto.setOwnerPeerId(resource.getPeer().getId());
            
            // Check if an access request exists between the resource owner and the current peer.
            AccessRequest accessRequest = accessRequestRepository.findByObjectPeerAndSubjectPeer(resource.getPeer(), currentPeer);
            
            if (accessRequest == null) 
            {
                dto.setAccessControlStatus("Not Created");
                dto.setActionsPermissions(null);
            } 
            else if (!accessRequest.getApproved()) 
            {
                dto.setAccessControlStatus("Pending");
                dto.setActionsPermissions(null);
            }
            else 
            {
                dto.setAccessControlStatus("Approved");
                
            	// Retrieve the policies defined for this access request and resource.
                List<Policy> policyList = policyRepository.findByAccessRequestAndResource(accessRequest, resource);
                Map<String, String> actionsMap = new HashMap<>();
                
                // Initialize default states.
                actionsMap.put("view", "invalid");
                actionsMap.put("edit", "invalid");
                actionsMap.put("delete", "invalid");

                for (Policy policy : policyList) {
                    String key = policy.getAction().toString().toLowerCase(Locale.ENGLISH);
                    if (policy.getPermission() == Permission.ALLOWED) {
                        actionsMap.put(key, "valid");
                    }
                }
                dto.setActionsPermissions(actionsMap);
            }
            return dto;
        });
    }

    public List<AccessRequest> getRequestsToMe(String username) {
        return accessRequestRepository.findByObjectPeer_Username(username);
    }
    
    @Transactional
    public void requestPermissions(PermissionRequestDTO dto) 
    {
    	AccessRequest accessRequest = accessRequestRepository
                .findByObjectPeerIdAndSubjectPeerId(dto.getObjectPeerId(), dto.getSubjectPeerId())
                .orElseThrow(() -> new RuntimeException("AccessRequest not found for given peers"));
    	
    	Resource resource = resourceRepository.findById(dto.getResourceId())
    			.orElseThrow(() -> new RuntimeException("Resource not found for given peers"));

        String notificationMessage = "For ACC(" + accessRequest.getId() + ") Permission Request for Resource(" + resource.getResourceName() + "):" +
                " | View: " + dto.getView() +
                ", Edit: " + dto.getEdit() +
                ", Delete: " + dto.getDelete();

        String objectPeerBcAddress = accessRequest.getObjectPeer().getBcAddress();
        
        notificationService.createNotification(accessRequest.getObjectPeer(), notificationMessage, NotificationType.POLICY_REQUEST);
        
        // Notify the subject peer that access is granted.
        String subjectNotificationChannel = "/topic/notifications/" + objectPeerBcAddress;
        messagingTemplate.convertAndSend(subjectNotificationChannel, notificationMessage);
    }
	
}
