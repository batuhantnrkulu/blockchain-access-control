package com.blockchain.accesscontrol.access_control_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.blockchain.accesscontrol.access_control_system.dto.responses.AccessRequestResponse;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;

@Mapper(componentModel = "spring")
public interface AccessRequestMapper 
{
	@Mapping(target = "objectPeerName", source = "objectPeer.username")
    @Mapping(target = "subjectPeerName", source = "subjectPeer.username")
    AccessRequestResponse toResponse(AccessRequest accessRequest);
}
