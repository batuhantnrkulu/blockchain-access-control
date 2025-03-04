package com.blockchain.accesscontrol.access_control_system.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.responses.MemberDTO;
import com.blockchain.accesscontrol.access_control_system.mapper.MemberMapper;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
	private final PeerRepository peerRepository;
    private final MemberMapper memberMapper;
    
    public AuthController(PeerRepository peerRepository, MemberMapper memberMapper) 
    {
        this.peerRepository = peerRepository;
        this.memberMapper = memberMapper;
    }
    
    /**
     * When accessed with proper Basic Auth credentials, this endpoint returns the current member information.
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        
        Optional<Peer> peerOpt = peerRepository.findByUsername(authentication.getName());
        
        if (!peerOpt.isPresent()) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Peer not found");
        }
        
        MemberDTO memberDTO = memberMapper.toMemberDTO(peerOpt.get());
        return ResponseEntity.ok(memberDTO);
    }
}
