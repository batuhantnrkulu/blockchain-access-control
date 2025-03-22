package com.blockchain.accesscontrol.access_control_system.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.requests.PenalizeRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.UnjoinedPeerResponseDTO;
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
            String message = roleTokenService.assignRole(peerRegistrationRequest);
            return ResponseEntity.ok(message);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to assign role: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves details of an unjoined peer.
     *
     * @param id The id of the unjoined peer.
     * @return UnjoinedPeerDTO containing username, bcAddress, usagePurpose, and group.
     */
    @GetMapping("/unjoined-peer/{id}")
    public ResponseEntity<UnjoinedPeerResponseDTO> getUnjoinedPeer(@PathVariable Long id) 
    {
        UnjoinedPeerResponseDTO dto = roleTokenService.getUnjoinedPeerDetails(id);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping("/penalize")
    public ResponseEntity<?> penalizeMember(@Valid @RequestBody PenalizeRequest request) {
        try {
            Long newBalance = roleTokenService.penalizeMemberAndUpdate(
                request.getMemberAddress(), 
                request.getAmount()
            );
            
            if (newBalance != null) {
                return ResponseEntity.ok(Map.of(
                    "message", "Penalty applied successfully",
                    "newBalance", newBalance
                ));
            }
            return ResponseEntity.status(500).body("Penalty operation failed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error applying penalty: " + e.getMessage());
        }
    }
}
