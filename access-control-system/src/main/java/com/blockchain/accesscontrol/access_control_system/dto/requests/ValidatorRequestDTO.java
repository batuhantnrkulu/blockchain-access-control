package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class ValidatorRequestDTO 
{
	private Long unjoinedPeerId;
    private String validatorBcAddress;
    private Boolean approved; // true if validator approved the validation, false if rejected
}
