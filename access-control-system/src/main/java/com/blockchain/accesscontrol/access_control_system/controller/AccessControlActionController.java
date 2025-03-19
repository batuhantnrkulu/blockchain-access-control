package com.blockchain.accesscontrol.access_control_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.AccessControlRequestDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.ResourceResponseDTO;
import com.blockchain.accesscontrol.access_control_system.exception.UnauthorizedAccessException;
import com.blockchain.accesscontrol.access_control_system.service.AccessControlActionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/access-control")
@RequiredArgsConstructor
public class AccessControlActionController 
{    
    private final AccessControlActionService accessControlActionService;

    @PostMapping("/check")
    public ResponseEntity<?> checkAccessControl(@RequestBody AccessControlRequestDTO request) 
    {
        try 
        {
            ResourceResponseDTO response = accessControlActionService.handleAccessControl(request);
            return ResponseEntity.ok(response);
        } 
        catch (UnauthorizedAccessException e) 
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Member is blocked or error processing request.");
        }
    }
}
