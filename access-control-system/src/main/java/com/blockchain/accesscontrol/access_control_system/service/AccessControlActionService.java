package com.blockchain.accesscontrol.access_control_system.service;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.AccessControlContract;
import com.blockchain.accesscontrol.access_control_system.dto.requests.AccessControlRequestDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.ResourceResponseDTO;
import com.blockchain.accesscontrol.access_control_system.exception.UnauthorizedAccessException;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.Resource;
import com.blockchain.accesscontrol.access_control_system.repository.AccessRequestRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ResourceRepository;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

@Service
public class AccessControlActionService extends BaseContractService<AccessControlContract> 
{
	private final AccessRequestRepository accessRequestRepository;
    private final ResourceRepository resourceRepository;
    private final PeerRepository peerRepository;
    private final PeerService peerService;
    private final EncryptionUtil encryptionUtil;
	
	public AccessControlActionService(Web3j web3j, TransactionManagerFactory transactionManagerFactory, 
			AccessRequestRepository accessRequestRepository, ResourceRepository resourceRepository, 
			PeerRepository peerRepository, PeerService peerService, EncryptionUtil encryptionUtil) 
	{
		super(web3j, transactionManagerFactory, "0x0");
		
		this.accessRequestRepository = accessRequestRepository;
		this.resourceRepository = resourceRepository;
		this.peerRepository = peerRepository;
		this.peerService = peerService;
		this.encryptionUtil = encryptionUtil;
	}

	public ResourceResponseDTO handleAccessControl(AccessControlRequestDTO request) throws Exception 
	{
        AccessRequest accessRequest = accessRequestRepository.findByObjectPeerIdAndSubjectPeerId(
                request.getOwnerPeerId(), request.getUserId())
                .orElseThrow(() -> new RuntimeException("No ACC found between peers"));

        String accAddress = accessRequest.getAccAddress();
        String subjectPeerPrivateKey = accessRequest.getSubjectPeer().getPrivateKey(encryptionUtil);
        AccessControlContract contract = loadContract(AccessControlContract.class, accAddress, subjectPeerPrivateKey);

        TransactionReceipt receipt = contract.accessControl(request.getResourceName(), request.getAction()).send();

        List<AccessControlContract.AccessControlCheckedEventResponse> accessEvents = contract.getAccessControlCheckedEvents(receipt);
        List<AccessControlContract.MaliciousActivityReportedEventResponse> maliciousEvents = contract.getMaliciousActivityReportedEvents(receipt);
        List<AccessControlContract.NonPenalizeMisbehaviorReportedEventResponse> nonPenalizeEvents = contract.getNonPenalizeMisbehaviorReportedEvents(receipt);

        boolean isAllowed = accessEvents.stream().anyMatch(event -> event.allowed);

        if (!maliciousEvents.isEmpty()) {
            for (AccessControlContract.MaliciousActivityReportedEventResponse event : maliciousEvents) {
                handleMaliciousActivity(event);
            }
            throw new UnauthorizedAccessException("Access denied due to malicious activity.");
        }

        if (!nonPenalizeEvents.isEmpty()) {
            for (AccessControlContract.NonPenalizeMisbehaviorReportedEventResponse event : nonPenalizeEvents) {
                handleNonPenalizeMisbehavior(event);
            }
        }
        
        if (!isAllowed) {
            throw new UnauthorizedAccessException("Access denied for action: " + request.getAction());
        }

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        Peer ownerPeer = peerRepository.findById(request.getOwnerPeerId())
                .orElseThrow(() -> new RuntimeException("Owner peer not found"));

        return new ResourceResponseDTO(
                resource.getId(),
                resource.getResourceName(),
                ownerPeer.getId(),
                ownerPeer.getUsername(),
                resource.getJpgResource(),
                resource.getDataResource()
        );
    }
	
	private void handleMaliciousActivity(AccessControlContract.MaliciousActivityReportedEventResponse event) 
	{
        String subjectPeer = event.subject;
        BigInteger penaltyAmount = event.penaltyAmount;
        String reason = event.reason;
        LocalDateTime blockingEndTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(event.blockingEndTime.longValue()), ZoneId.systemDefault());
        String newStatus = event.newStatus;

        peerService.updateMaliciousPeerInfo(subjectPeer, penaltyAmount, reason, blockingEndTime, newStatus);
    }

    private void handleNonPenalizeMisbehavior(AccessControlContract.NonPenalizeMisbehaviorReportedEventResponse event) {
        String peerBcAddress = event.subject;
        BigInteger rewardAmount = event.rewardAmount;
        String newStatus = event.newStatus;

        if (!newStatus.equals("not changed"))
        {
        	peerService.updateNonMaliciousPeerInfo(peerBcAddress, rewardAmount, "Good Behavior", newStatus);
        }
    }
}
