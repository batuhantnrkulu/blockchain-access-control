package com.blockchain.accesscontrol.access_control_system.dto.responses;

import lombok.Data;

@Data
public class ResourceResponseDTO 
{
	private Long id;
    private String resourceName;
    private String peerUsername;
    private String jpgResource;
    private String dataResource;
}
