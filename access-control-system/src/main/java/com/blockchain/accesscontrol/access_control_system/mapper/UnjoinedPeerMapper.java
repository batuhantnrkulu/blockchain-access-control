package com.blockchain.accesscontrol.access_control_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PeerRegistrationRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.UnjoinedPeerResponseDTO;
import com.blockchain.accesscontrol.access_control_system.model.UnjoinedPeer;

@Mapper(componentModel = "spring")
public interface UnjoinedPeerMapper 
{
    UnjoinedPeerResponseDTO toDto(UnjoinedPeer unjoinedPeer);
    
    // Map bcAddress into blockchainAddress.
    @Mapping(source = "bcAddress", target = "blockchainAddress")
    PeerRegistrationRequest toPeerRegistrationRequest(UnjoinedPeer unjoinedPeer);
}
