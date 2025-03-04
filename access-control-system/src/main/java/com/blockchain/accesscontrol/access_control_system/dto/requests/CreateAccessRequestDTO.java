package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class CreateAccessRequestDTO 
{
    private Long objectPeerId;
    private Long subjectPeerId;
}