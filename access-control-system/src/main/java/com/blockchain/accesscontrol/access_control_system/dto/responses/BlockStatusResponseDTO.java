package com.blockchain.accesscontrol.access_control_system.dto.responses;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlockStatusResponseDTO 
{
	private boolean blocked;
    private LocalDateTime blockingEndTime;
}
