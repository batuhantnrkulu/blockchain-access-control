package com.blockchain.accesscontrol.access_control_system.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceResponseDTO 
{
	private Long id;
    private String resourceName;
    private Long ownerPeerId;
    private String peerUsername;
    private String jpgResource;
    private String dataResource;
}
