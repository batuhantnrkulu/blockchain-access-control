package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class AccessControlRequestDTO 
{
    private Long userId;
    private String bcAddress;
    private Long resourceId;
    private Long ownerPeerId;
    private String resourceName;
    private String action;
}