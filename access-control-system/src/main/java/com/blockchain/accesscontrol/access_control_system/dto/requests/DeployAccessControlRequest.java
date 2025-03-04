package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class DeployAccessControlRequest 
{
	private Long objectPeerId;
	private Long subjectPeerId;
	private String accType;
}
