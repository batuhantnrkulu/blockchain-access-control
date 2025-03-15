package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class PolicyBulkUpdateRequest 
{
	private Long accessRequestId;
    private Long resourceId;
    /**
     * Expected values: "allowed" or "disallowed"
     */
    private String view;
    private String edit;
    private String delete;
}
