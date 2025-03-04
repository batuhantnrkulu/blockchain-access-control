package com.blockchain.accesscontrol.access_control_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.blockchain.accesscontrol.access_control_system.dto.requests.ResourceRequestDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.ResourceResponseDTO;
import com.blockchain.accesscontrol.access_control_system.model.Resource;

@Mapper(componentModel = "spring")
public interface ResourceMapper 
{
	// Maps the Resource entity to the ResourceResponseDTO.
    // The peer field is mapped to peerUsername from peer.username.
    @Mapping(source = "peer.username", target = "peerUsername")
    ResourceResponseDTO resourceToResourceResponseDTO(Resource resource);

    // Converts a ResourceRequestDTO to a Resource entity.
    // Ignore id and peer (which will be set later in the service) during creation.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peer", ignore = true)
    Resource resourceRequestDTOToResource(ResourceRequestDTO dto);
}
