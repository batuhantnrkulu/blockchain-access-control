package com.blockchain.accesscontrol.access_control_system.dto.requests;

import lombok.Data;

@Data
public class PenalizeRequest {
    private String memberAddress;
    private Long amount;
}
