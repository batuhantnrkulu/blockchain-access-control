package com.blockchain.accesscontrol.access_control_system.dto.responses;

import lombok.Data;

@Data
public class AccessRequestResponse 
{
	private Long id;
    private String objectPeerName;
    private String subjectPeerName;
    private Boolean approved;
    private String accAddress;
}
