package com.blockchain.accesscontrol.access_control_system.dto.responses;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BehaviorHistoryResponseDTO 
{
	private Long id;
    private int tokenAmount;
    private String reason;
    private String statusUpdateFormatted;
    private String peerUsername;
}
