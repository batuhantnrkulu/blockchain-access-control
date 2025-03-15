package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class PermissionRequestDTO {
	private Long objectPeerId;
	private Long subjectPeerId;
    private Long resourceId;
    private String view;
    private String edit;
    private String delete;
}
