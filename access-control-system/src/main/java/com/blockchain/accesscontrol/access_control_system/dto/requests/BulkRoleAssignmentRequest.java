package com.blockchain.accesscontrol.access_control_system.dto.requests;

import java.util.List;

import lombok.Data;

@Data
public class BulkRoleAssignmentRequest 
{
	 private List<PeerRegistrationRequest> requests;
	 private boolean async; // Optional: for async processing
}
