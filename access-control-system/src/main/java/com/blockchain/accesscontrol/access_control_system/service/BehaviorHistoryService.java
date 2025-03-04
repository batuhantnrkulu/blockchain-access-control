package com.blockchain.accesscontrol.access_control_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.BehaviorHistoryRepository;

@Service
public class BehaviorHistoryService 
{
	private final BehaviorHistoryRepository tokenHistoryRepository;

    public BehaviorHistoryService(BehaviorHistoryRepository tokenHistoryRepository) {
        this.tokenHistoryRepository = tokenHistoryRepository;
    }

    public BehaviorHistory addHistory(BehaviorHistory history) 
    {
        // Retrieve the token history entries for this peer (oldest first)
        List<BehaviorHistory> allHistories = tokenHistoryRepository.findByPeerOrderByStatusUpdateAsc(history.getPeer());
        
        // Delete the oldest entry if there are already 10 records
        if (allHistories.size() >= 10) 
        {
            tokenHistoryRepository.delete(allHistories.get(0));
        }
        
        return tokenHistoryRepository.save(history);
    }

    public List<BehaviorHistory> getHistoryByPeer(Peer peer) 
    {
        return tokenHistoryRepository.findAllByPeerOrderByStatusUpdateDesc(peer);
    }
}
