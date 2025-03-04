package com.blockchain.accesscontrol.access_control_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.MemberDTO;
import com.blockchain.accesscontrol.access_control_system.service.RoleTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role-token")
// @CrossOrigin(origins = "http://localhost:3000")  // Allow frontend access
public class RoleTokenController 
{
	private final RoleTokenService roleTokenService;

    public RoleTokenController(RoleTokenService roleTokenService) 
    {
        this.roleTokenService = roleTokenService;
    }

    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(@Valid @RequestBody PeerRegistrationRequest peerRegistrationRequest) 
    {
        try 
        {
            roleTokenService.assignRole(peerRegistrationRequest);
            return ResponseEntity.ok("Role assigned successfully");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to assign role: " + e.getMessage());
        }
    }
    
    @GetMapping("/get-member")
    public ResponseEntity<?> getMember(@RequestParam String memberAddress) 
    {
        try 
        {
            MemberDTO memberDTO = roleTokenService.getMember(memberAddress);
            return ResponseEntity.ok(memberDTO);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch member details: " + e.getMessage());
        }
    }
}
