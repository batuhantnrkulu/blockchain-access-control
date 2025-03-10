package com.blockchain.accesscontrol.access_control_system.dto.responses;

import lombok.Data;

@Data
public class UnjoinedPeerResponseDTO 
{
	private String username;
    private String bcAddress;
    private String usagePurpose;
    private String group;
}
