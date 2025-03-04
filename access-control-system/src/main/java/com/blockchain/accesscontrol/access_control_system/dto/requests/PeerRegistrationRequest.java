package com.blockchain.accesscontrol.access_control_system.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PeerRegistrationRequest 
{
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
	private String ipAddress;
	private String blockchainAddress;
	private String privateKey;
	private String usagePurpose;
	private String group;
	private Boolean isWebUser;
}
