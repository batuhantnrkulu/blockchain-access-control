package com.blockchain.accesscontrol.access_control_system.dto.responses;

import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;

import lombok.Data;

@Data
public class NotificationResponseDTO 
{
	private Long id;
	private String message;
    private NotificationType type;
    private Boolean read;
    private String createdAt;
}
