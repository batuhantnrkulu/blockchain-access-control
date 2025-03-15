package com.blockchain.accesscontrol.access_control_system.dto.responses;

import lombok.Data;

@Data
public class PolicyStatusResponse 
{
	private String view;
    private String edit;
    private String delete;
}
