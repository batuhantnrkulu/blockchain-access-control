package com.blockchain.accesscontrol.access_control_system.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.dto.responses.BlockStatusResponseDTO;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

@Service
public class PeerService 
{
	private final PeerRepository peerRepository;
	private final BehaviorHistoryService behaviorHistoryService;

    public PeerService(PeerRepository peerRepository, BehaviorHistoryService behaviorHistoryService) {
        this.peerRepository = peerRepository;
        this.behaviorHistoryService = behaviorHistoryService;
    }

    public void updateMaliciousPeerInfo(String bcAddress, BigInteger penaltyAmount, String reason, LocalDateTime blockingEndTime, String newStatus) {
        Peer peer = peerRepository.findByBcAddress(bcAddress)
                .orElseThrow(() -> new RuntimeException("Peer not found"));

        peer.setBlockingEndTime(blockingEndTime);

        long currentBalance = peer.getErc20TokenAmount();
        long newBalance = currentBalance - penaltyAmount.longValue();
        peer.setErc20TokenAmount(newBalance);
        
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + newStatus);
        }
        
        peer.setBcStatus(statusEnum);
        peerRepository.save(peer);

        BehaviorHistory history = new BehaviorHistory();
        history.setPeer(peer);
        history.setTokenAmount(newBalance);
        history.setTokenAmountChange(-penaltyAmount.longValue()); // Negative value for deduction
        history.setReason(reason);
        history.setStatusUpdate(LocalDateTime.now());

        behaviorHistoryService.addHistory(history);
    }
    
    public void updateNonMaliciousPeerInfo(String bcAddress, BigInteger rewardAmount, String reason, String newStatus) {
        Peer peer = peerRepository.findByBcAddress(bcAddress)
                .orElseThrow(() -> new RuntimeException("Peer not found"));

        long currentBalance = peer.getErc20TokenAmount();
        long newBalance = currentBalance + rewardAmount.longValue();
        peer.setErc20TokenAmount(newBalance);

        if (!newStatus.equals("not changed"))
        {
        	Status statusEnum;
            try {
                statusEnum = Status.valueOf(newStatus);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + newStatus);
            }
            
            peer.setBcStatus(statusEnum);	
        }
        
        peerRepository.save(peer);

        BehaviorHistory history = new BehaviorHistory();
        history.setPeer(peer);
        history.setTokenAmount(newBalance);
        history.setTokenAmountChange(rewardAmount.longValue());
        history.setReason(reason);
        history.setStatusUpdate(LocalDateTime.now());

        behaviorHistoryService.addHistory(history);
    }
    
    public BlockStatusResponseDTO isPeerBlocked(String bcAddress) 
    {
        Peer peer = peerRepository.findByBcAddress(bcAddress)
                        .orElseThrow(() -> new RuntimeException("Peer not found"));
        
    	BlockStatusResponseDTO blockStatusResponse = new BlockStatusResponseDTO();
    	blockStatusResponse.setBlocked(peer.getBlockingEndTime() != null && LocalDateTime.now().isBefore(peer.getBlockingEndTime()) ? true : false);
    	blockStatusResponse.setBlockingEndTime(peer.getBlockingEndTime());
        	
    	return blockStatusResponse;
    }
}
