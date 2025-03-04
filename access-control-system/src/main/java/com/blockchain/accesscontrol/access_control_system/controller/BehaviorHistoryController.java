package com.blockchain.accesscontrol.access_control_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.responses.BehaviorHistoryResponseDTO;
import com.blockchain.accesscontrol.access_control_system.mapper.BehaviorHistoryMapper;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.service.BehaviorHistoryService;

@RestController
@RequestMapping("/api/behavior-history")
public class BehaviorHistoryController 
{
	private final BehaviorHistoryService behaviorHistoryService;
    private final PeerRepository peerRepository;
    private final BehaviorHistoryMapper behaviorHistoryMapper;

    public BehaviorHistoryController(BehaviorHistoryService behaviorHistoryService,
    		PeerRepository peerRepository, BehaviorHistoryMapper behaviorHistoryMapper) 
    {
        this.behaviorHistoryService = behaviorHistoryService;
        this.peerRepository = peerRepository;
        this.behaviorHistoryMapper = behaviorHistoryMapper;
    }

    // Retrieve token history records for a given peer (by username)
    @GetMapping("/{username}")
    public ResponseEntity<List<BehaviorHistoryResponseDTO>> getHistory(@PathVariable String username) 
    {
        Peer peer = peerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Peer not found"));
        
        List<BehaviorHistory> histories = behaviorHistoryService.getHistoryByPeer(peer);
        List<BehaviorHistoryResponseDTO> dtoList = behaviorHistoryMapper.toDtoList(histories);
        return ResponseEntity.ok(dtoList);
    }
}
