package com.blockchain.accesscontrol.access_control_system.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.blockchain.accesscontrol.access_control_system.dto.responses.BehaviorHistoryResponseDTO;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;

@Mapper(componentModel = "spring")
public interface BehaviorHistoryMapper 
{
	@Mapping(source = "peer.username", target = "peerUsername")
    @Mapping(source = "statusUpdate", target = "statusUpdateFormatted", qualifiedByName = "formatDateTime")
    BehaviorHistoryResponseDTO toDto(BehaviorHistory tokenHistory);

    List<BehaviorHistoryResponseDTO> toDtoList(List<BehaviorHistory> tokenHistories);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}