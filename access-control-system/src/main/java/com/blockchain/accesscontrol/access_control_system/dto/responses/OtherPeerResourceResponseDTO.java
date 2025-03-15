package com.blockchain.accesscontrol.access_control_system.dto.responses;

import java.util.Map;

import lombok.Data;

@Data
public class OtherPeerResourceResponseDTO 
{
	private Long resourceId;
    private Long ownerPeerId;
    private String resourceName;
    private String ownerPeerName;
    
    /**
     * For example:
     *   "Not Created" – no access control record exists,
     *   "Pending" – access control was sent but not yet approved,
     *   "Approved" – access control exists and is approved.
     */
    private String accessControlStatus;
    
    /**
     * A mapping of actions to their permission state.
     * For instance: { "view": "valid", "edit": "invalid", "delete": "invalid" }.
     */
    private Map<String, String> actionsPermissions;
}
