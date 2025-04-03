package com.blockchain.accesscontrol.access_control_system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;

@Repository
public interface BehaviorHistoryRepository extends JpaRepository<BehaviorHistory, Long> {

    // Find all token history records for the given peer (ordered as needed)
    List<BehaviorHistory> findAllByPeerOrderByStatusUpdateDesc(Peer peer);

    List<BehaviorHistory> findByPeerOrderByStatusUpdateAsc(Peer peer);
    
    List<BehaviorHistory> findByPeerAndStatusUpdateAfter(Peer peer, LocalDateTime cutoff);
    
    Optional<BehaviorHistory> findFirstByPeerAndPositiveOrderByStatusUpdateDesc(Peer peer, boolean isPositive);
}