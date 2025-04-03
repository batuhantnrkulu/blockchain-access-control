package com.blockchain.accesscontrol.access_control_system.dto.responses;

import java.time.LocalDateTime;

import com.blockchain.accesscontrol.access_control_system.enums.Role;

import lombok.Data;

@Data
public class MemberDTO 
{
	private Long id;
	private String memberAddress;
	private String name;
	private String memberType;
	private String status;
	private LocalDateTime lastStatusUpdate;
	private Role role;
	private String blockingEndTime;
	private Boolean isWebUser;
	private String ipAddress;
}
