package com.blockchain.accesscontrol.access_control_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.responses.BlockStatusResponseDTO;
import com.blockchain.accesscontrol.access_control_system.service.PeerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/peers")
@RequiredArgsConstructor
public class PeerController {

    private final PeerService peerService;

    @GetMapping("/{bcAddress}/is-blocked")
    public ResponseEntity<?> isPeerBlocked(@PathVariable String bcAddress) 
    {
        try 
        {
        	BlockStatusResponseDTO blockStatusResponse = peerService.isPeerBlocked(bcAddress);
            return ResponseEntity.ok().body(blockStatusResponse);
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}